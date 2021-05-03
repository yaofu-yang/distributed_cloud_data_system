import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class ThreadHandler {
  private static final String EXCHANGE_NAME = "supermarket_logs";
  private static final String SEVERITY = "store";
  private static final String QUEUE_NAME = "purchases";

  public static Thread[] createThreads(int maxThreads, CountDownLatch completed,
      Connection connection, ItemsByStore itemsByStore) {
    Thread[] threads = new Thread[maxThreads];
    for (int i = 0; i < maxThreads; i++) {
      int threadNumber = i;
      Runnable thread = () -> {
        try {
          insertIntoStore(connection, threadNumber, itemsByStore);
        } catch (IOException e) {
          System.out.println("Could not run insertIntoStore method");
        }
        completed.countDown();
      };
      threads[i] = new Thread(thread);
    }
    return threads;
  }

  public static void runThreads(Thread[] threads, int maxThreads) {
    for (int i = 0; i < maxThreads; i++) {
      new Thread(threads[i]).start();
    }
  }

  private static void insertIntoStore(Connection connection, int threadNumber,
      ItemsByStore itemsByStore) throws IOException {
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
//    String queueName = channel.queueDeclare().getQueue();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, SEVERITY);

    // 246:::246000858:::20210101:::{"items":[{"ItemID":"61167","numberOfItems":1},{"ItemID":"16808","numberOfItems":1}]}
//    System.out.println("Thread Number: " + threadNumber + " Waiting for messages.");
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
//      System.out.println(message);
      // Todo: Parse message and insert into store synchronously
      String[] purchaseInfo = message.split(":::");  // StoreId, custId, date, items
      int storeId = Integer.parseInt(purchaseInfo[0]);

      String[] items = purchaseInfo[3].split("},\\{");
      String[] itemsInfo;
      int itemId, numItems;
      for (int i = 0; i < items.length - 1; i++) {
        itemsInfo = items[i].split("\\\",\\\"");
        itemId = getEndValue(itemsInfo[0], 1);
        numItems = getEndValue(itemsInfo[1], 1);

        itemsByStore.incrementItemCount(storeId, itemId, numItems);
//        System.out.println("Inserted: " + storeId + ", " + itemId + ", " + numItems);
      }

      // Edge Case
      itemsInfo = items[items.length - 1].split("\\\",\\\"");
      itemId = getEndValue(itemsInfo[0], 1);
      numItems = getEndValue(itemsInfo[1], 4);
      itemsByStore.incrementItemCount(storeId, itemId, numItems);
//      System.out.println("Inserted: " + storeId + ", " + itemId + ", " + numItems);
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
    });

  }

  private static int getEndValue(String segment, int offset) {
    StringBuilder sb = new StringBuilder();
    for (int i = segment.length() - offset; i >= 0; i--) {
      int difference = segment.charAt(i) - '0';
      if (difference >= 0 && difference <= 9) {
        sb.append(segment.charAt(i));
      } else {
        break;
      }
    }
    return Integer.parseInt(sb.reverse().toString());
  }
}
