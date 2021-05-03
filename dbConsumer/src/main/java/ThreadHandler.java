import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

public class ThreadHandler {
  private static final String EXCHANGE_NAME = "supermarket_logs";
  private static final String SEVERITY = "db";
  private static final String QUEUE_NAME = "purchases";

  public static Thread[] createThreads(int maxThreads, CountDownLatch completed,
      Connection connection) {
    Thread[] threads = new Thread[maxThreads];
    for (int i = 0; i < maxThreads; i++) {
      int threadNumber = i;
      Runnable thread = () -> {
        try {
          insertIntoDatabase(connection, threadNumber);
        } catch (IOException e) {
          e.printStackTrace();
        }
        completed.countDown();
      };
      threads[i] = new Thread(thread);
    }

    return threads;
  }

  /**
   *     String queueName = channel.queueDeclare().getQueue();
   *     channel.queueBind(queueName, EXCHANGE_NAME, SEVERITY);
   *     System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
   *     PurchaseDao purchaseDao = new PurchaseDao();
   *     CountDownLatch completed = new CountDownLatch(MAX_THREADS);  // Threads
   *     Thread[] threads = ThreadHandler.createThreads();
   */

  public static void runThreads(Thread[] threads, int maxThreads) {
    for (int i = 0; i < maxThreads; i++) {
      new Thread(threads[i]).start();
    }
  }

  private static void insertIntoDatabase(Connection connection, int threadNumber) throws IOException {

    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
//    String queueName = channel.queueDeclare().getQueue();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, SEVERITY);

    System.out.println("Thread Number: " + threadNumber + " Waiting for messages.");
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      String[] purchase = message.split(":::");  // Might change delimiter
      try {
        PurchaseDao.createPurchase(new Purchase(purchase[0], purchase[1], purchase[2], purchase[3]));
//        System.out.println("Thread" + threadNumber + " created purchase");
      } catch (SQLException e) {
        e.printStackTrace();
      }
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
    });
  }
}

