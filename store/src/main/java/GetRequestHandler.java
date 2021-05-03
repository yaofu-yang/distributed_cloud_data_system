import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.PriorityQueue;

public final class GetRequestHandler {
  private static final String QUEUE_NAME = "get_request";
  private static final int MAX_ITEMS = 10;
  private static final int MAX_STORES = 10;

  public static void processRequest(Connection connection, ItemsByStore itemsByStore)
      throws IOException {

    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    channel.queuePurge(QUEUE_NAME);

    channel.basicQos(1);
    System.out.println("Awaiting RPC GET Requests...");

    Object monitor = new Object();
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      AMQP.BasicProperties replyProps = new AMQP.BasicProperties
          .Builder()
          .correlationId(delivery.getProperties().getCorrelationId())
          .build();

      String response = "";

      try {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
//        System.out.println(message);
        String[] query = message.split(":::");
        response = query[0].equals("store") ? getTopTenItems(query, itemsByStore)
            : getTopTenStores(query, itemsByStore);
      } catch (RuntimeException e) {
        System.out.println(" [.] " + e.toString());
      } finally {
        channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps,
            response.getBytes(StandardCharsets.UTF_8));
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        System.out.println("Response published: ");
        System.out.println(response);
        synchronized (monitor) {
          monitor.notify();
        }
      }
    };

    channel.basicConsume(QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));
    // Wait and be prepared to consume the message from RPC client.
    while (true) {
      synchronized (monitor) {
        try {
          monitor.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static String getTopTenItems(String[] query, ItemsByStore itemsByStore) {
    StringBuilder sb = new StringBuilder();
    sb.append("Top 10 Items in store ").append(query[1]).append("\n");
    int storeId = Integer.parseInt(query[1]);
    PriorityQueue<Item> pq = new PriorityQueue<>(Comparator.comparingInt(Item::getItemCount));  // min heap
    int[] storeItems = itemsByStore.getItemsByStore()[storeId];

    // Iterate through items and add them to priority queue, maintaining max size of 10.
    for (int i = 0; i < storeItems.length; i++) {
      if (storeItems[i] == 0) {  // No items
        continue;
      }
      pq.offer(new Item(i, storeItems[i]));
      if (pq.size() > MAX_ITEMS) {
        pq.poll();
      }
    }

    while (!pq.isEmpty()) {
      sb.append(pq.poll().toString());
    }
    return sb.toString();
  }

  private static String getTopTenStores(String[] query, ItemsByStore itemsByStore) {
    StringBuilder sb = new StringBuilder();
    sb.append("Finding Top 10 Stores with item: ").append(query[1]).append("\n");
    int itemId = Integer.parseInt(query[1]);
    PriorityQueue<Store> pq = new PriorityQueue<>(Comparator.comparingInt(Store::getItemCount));  // min heap
    int[][] store = itemsByStore.getItemsByStore();

    // Iterate through items and add them to priority queue, maintaining max size of 10.
    for (int i = 0; i < store.length; i++) {
      if (store[i][itemId] == 0) {
        continue;  // No items
      }
      pq.offer(new Store(i, store[i][itemId]));
      if (pq.size() > MAX_STORES) {
        pq.poll();
      }
    }

    while (!pq.isEmpty()) {
      sb.append(pq.poll().toString());
    }
    return sb.toString();
  }
}
