import com.rabbitmq.client.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/*")
public class SupermarketServlet extends HttpServlet {
  private static final String EXCHANGE_NAME = "supermarket_logs";
  private static final String DB_CHANNEL_KEY = "db";
  private static final String STORE_CHANNEL_KEY = "store";
  private static final String GET_CHANNEL_KEY = "get_request";

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    urlValidation(request, response);
    response.setStatus(HttpServletResponse.SC_OK);  // 200

    // Todo: Modified version to send to Exchanger
    Channel channel = MQChannel.getInstance().getChannel();

    String[] urlParams = request.getPathInfo().split("/");
    String storeId = urlParams[2], customerId = urlParams[4], date = urlParams[6];
    BufferedReader bodyBuffer = request.getReader();
    String items, message;
    while ((items = bodyBuffer.readLine()) != null) {
      message = joinString(storeId, customerId, date, items);
      channel.basicPublish(EXCHANGE_NAME, DB_CHANNEL_KEY, null, message.getBytes(
          StandardCharsets.UTF_8));
      channel.basicPublish(EXCHANGE_NAME, STORE_CHANNEL_KEY, null, message.getBytes(
          StandardCharsets.UTF_8));
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.getWriter().write("Valid endpoint:" + request.getPathInfo() + "\n");

    // /items/store/{storeID}
    // /items/top10/{itemID}

    String[] urlParams = request.getPathInfo().split("/");
    String getRequest = urlParams[2].equals("store") ? "store:::" : "top10:::";
    String getResponse = "No responses received.";
    try {
      getResponse = this.call(getRequest + urlParams[3]);
//      System.out.println(getRequest + urlParams[3]);
    } catch (InterruptedException | TimeoutException e) {
      System.out.println("Not a successful GET RPC request.");
      response.getWriter().write("Not a successful GET RPC request.");
    }

    response.getWriter().write(getResponse);
  }

  private String call(String message) throws IOException, InterruptedException, TimeoutException {
    final String corrId = UUID.randomUUID().toString();
    // Initial setup for GET requests
    ConnectionFactory factory = new ConnectionFactory();
//    factory.setHost("localhost");  // local testing
    factory.setUsername("yang");
    factory.setPassword("testYang");
    factory.setHost("18.234.106.224");  // Ec2 Instance
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    String replyQueueName = channel.queueDeclare().getQueue();
    AMQP.BasicProperties props = new AMQP.BasicProperties
        .Builder()
        .correlationId(corrId)
        .replyTo(replyQueueName)
        .build();

    channel.basicPublish("", GET_CHANNEL_KEY, props, message.getBytes(StandardCharsets.UTF_8));

    final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

    String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
      if (delivery.getProperties().getCorrelationId().equals(corrId)) {
        response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
      }
    }, consumerTag -> {
    });

    String result = response.take();
    channel.basicCancel(ctag);
    return result;
  }

  private void urlValidation(HttpServletRequest request, HttpServletResponse response)
      throws IOException  {
    response.setContentType("text/plain");
    String urlPath = request.getPathInfo();

    // Check we have a URL
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);  // 404
      response.getWriter().write("Missing parameters");
      return;
    }

    // Check url validity
    String[] urlParts = urlPath.split("/");
    if (!isPostUrlValid(urlParts) && isGetUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);  // 404
      response.getWriter().write("Could not retrieve data from url: " + urlPath);
      response.getWriter().write("Url length: " + urlParts.length);
    } else {
      response.setStatus(HttpServletResponse.SC_OK);  // 200
      response.getWriter().write("It works!" + urlPath);
    }
  }

  // For validating url only
  private boolean isPostUrlValid(String[] urlPath) {
    // Path: purchase/{storeID}/customer/{custID}/date/{date}
    if (urlPath.length <= 1) {
      return urlPath[0].equals("");
    } else if (urlPath.length < 7) {
      return false;
    } else if (urlPath.length == 7) {
      try {
        Integer.parseInt(urlPath[2]);
        Integer.parseInt(urlPath[4]);
        LocalDate.parse((urlPath[6]), DateTimeFormatter.ofPattern("yyyyMMdd"));
      } catch (NumberFormatException e) {
        return false;
      }
      return urlPath[1].equals("purchase") && urlPath[3].equals("customer") && urlPath[5].equals("date");
    }
    return true;
  }

  private boolean isGetUrlValid(String[] urlPath) {
    if (urlPath.length <= 1) {
      return urlPath[0].equals("");
    } else if (urlPath.length < 4) {  // Less than 3 arguments
      return false;
    } else if (urlPath.length == 4) {
      try {
        Integer.parseInt(urlPath[3]);
      } catch (NumberFormatException e) {
        return false;
      }
      return urlPath[1].equals("items") && (urlPath[2].equals("store") || urlPath[2].equals("top10"));
    }
    return true;
  }

  private String joinString(String storeId, String custId, String date, String items) {
    String delimiter = ":::";
    StringBuilder sb = new StringBuilder();
    sb.append(storeId);
    sb.append(delimiter);
    sb.append(custId);
    sb.append(delimiter);
    sb.append(date);
    sb.append(delimiter);
    sb.append(items);
    return sb.toString();
  }
}
