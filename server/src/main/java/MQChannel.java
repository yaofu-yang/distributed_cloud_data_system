import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MQChannel {
  private static final String EXCHANGE_NAME = "supermarket_logs";
  private static MQChannel instance;
  protected Channel channel;

  /**
   * Instance that returns a RabbitMQ channel
   */
  private MQChannel() {
    ConnectionFactory factory = new ConnectionFactory();
//    factory.setHost("localhost");  // For local testing
    factory.setUsername("yang");
    factory.setPassword("testYang");
    factory.setHost("100.26.236.221");  // Ec2 Instance

    try {
      System.out.println("Attempting to connect to factory");
      Connection connection = factory.newConnection();
      this.channel = connection.createChannel();
      this.channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
    } catch (TimeoutException | IOException e) {
      System.out.println("Failed try block.");
    }
  }

  /**
   * Lazy allocation to retrieve singleton MQChannel
   * @return MQChannel instance
   */
  public static synchronized MQChannel getInstance() {
    if (instance == null) {
      instance = new MQChannel();
    }
    return instance;
  }

  public Channel getChannel() {
    return channel;
  }

}
