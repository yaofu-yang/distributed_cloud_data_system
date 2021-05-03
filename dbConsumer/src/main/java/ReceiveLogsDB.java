import com.rabbitmq.client.*;
import java.util.concurrent.CountDownLatch;

public class ReceiveLogsDB {
  private static final int MAX_THREADS = 500;

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
//    factory.setHost("localhost");  // local testing
    factory.setUsername("yang");
    factory.setPassword("testYang");
    factory.setHost("100.26.236.221");  // Ec2 Instance

    Connection connection = factory.newConnection();

    CountDownLatch completed = new CountDownLatch(MAX_THREADS);  // Threads
    Thread[] threads = ThreadHandler.createThreads(MAX_THREADS, completed, connection);
    ThreadHandler.runThreads(threads, MAX_THREADS);
    completed.await();
  }
}
