import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.concurrent.CountDownLatch;

public class ReceiveItems {
  private static final int MAX_THREADS = 50;

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
//    factory.setHost("localhost");  // local testing
    factory.setUsername("yang");
    factory.setPassword("testYang");
    factory.setHost("100.26.236.221");  // Ec2 Instance

    Connection connection = factory.newConnection();
    CountDownLatch completed = new CountDownLatch(MAX_THREADS);
    ItemsByStore itemsByStore = new ItemsByStore();
    Thread[] threads = ThreadHandler.createThreads(MAX_THREADS, completed, connection, itemsByStore);
    ThreadHandler.runThreads(threads, MAX_THREADS);
    completed.await();

    GetRequestHandler.processRequest(connection, itemsByStore);
  }
}
