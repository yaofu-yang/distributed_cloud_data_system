import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.PurchaseApi;
import io.swagger.client.model.Purchase;
import io.swagger.client.model.PurchaseItems;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public final class PurchaseThreadHandler {

  /**
   * Instantiate all threads necessary for posting the purchases to servlet.
   * Returns a collection of pending threads
   * @param parameters - Collection of parameter key and values
   * @param totalPurchases - Atomic Integer tallying purchases
   * @param signal - Instance containing synchronized global variables.
   * @param completed - CountDownLatch for all anticipated store threads.
   * @return a collection of pending threads.
   */
  public static Thread[] createThreads(Map<String, String> parameters, AtomicInteger totalPurchases,
      PurchaseSignal signal, CountDownLatch completed) {

    // Initialize parameters
    Random rand = new Random();
    int maxStores = Integer.parseInt(parameters.get("stores"));  // Get max stores for threading
    Integer custPerStore = Integer.parseInt(parameters.getOrDefault("cust", "1000"));
    Integer maxItemId = Integer.parseInt(parameters.getOrDefault("itemID", "100000"));
    int purchasesPerHour = Integer.parseInt(parameters.getOrDefault("purch", "60"));
    Integer itemsPerPurchase = Integer.parseInt(parameters.getOrDefault("items", "5"));
    String date = parameters.getOrDefault("date", "20210101");
    String portIp = parameters.get("port");
//    String port = "http://" + portIp + "/server_war_exploded";  // Local
    String port = "http://" + portIp + "/server_war/";  // Ec2
    String port2 = "http://54.205.182.39:8080/server_war/";
//    String port3 = "http://35.173.177.173:8080/server_war/";
    String[] ports = new String[]{port, port2};
//    System.out.println(port);
//    System.out.println(port2);
//    System.out.println(port3);
    Thread[] threads = new Thread[maxStores];

    // Creating a new thread for each store that will be POSTing purchases
    for (int storeId = 0; storeId < maxStores; storeId++) {
      ApiClient shop = new ApiClient();
      shop.setBasePath(ports[storeId % 2]);
      shop.setConnectTimeout(1000000);  // Accounting for local connecting to EC2 server
      shop.setReadTimeout(1000000);
      PurchaseApi apiInstance = new PurchaseApi(shop);

      int finalStoreId = storeId;
      String prefix = String.valueOf(storeId * 1000);
      Runnable thread = () -> {
        totalPurchases.addAndGet(
            makePurchases(apiInstance, finalStoreId, custPerStore, prefix, purchasesPerHour,
                maxItemId, itemsPerPurchase, date, rand, signal)); completed.countDown();
      };
      threads[storeId] = new Thread(thread);
    }
    return threads;
  }


  /**
   * Create and run three threads simulating the store opening phases.
   * @param threads - Collection of pending threads
   * @param signal - Instance containing synchronized global variables.
   * @throws InterruptedException - Thrown if threads are interrupted prematurely/unexpectedly.
   */
  public static void runPhaseThreads(Thread[] threads, PurchaseSignal signal)
      throws InterruptedException {

    // Create and run 3 threads representing each phase.
    CountDownLatch phasesCompleted = new CountDownLatch(3);
    Runnable phase1 = () -> {
      phaseOne(threads, signal);
      phasesCompleted.countDown();
    };

    Runnable phase2 = () -> {
      try {
        phaseTwo(threads, signal);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      phasesCompleted.countDown();
    };

    Runnable phase3 = () -> {
      try {
        phaseThree(threads, signal);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      phasesCompleted.countDown();
    };

    new Thread(phase1).start();
    new Thread(phase2).start();
    new Thread(phase3).start();
    phasesCompleted.await();
  }

  /**
   * Helper function to run phase 1 threads
   * @param threads - Collection of pending threads
   */
  private static void phaseOne(Thread[] threads, PurchaseSignal signal) {
    for (int storeId = 0; storeId < threads.length / 4; storeId++) {
      new Thread(threads[storeId]).start();
    }
  }

  /**
   * Helper function to run phase 2 threads.
   * @param threads - Collection of pending threads
   * @param signal - Instance containing synchronized global variables.
   */
  private static void phaseTwo(Thread[] threads, PurchaseSignal signal)
      throws InterruptedException {
    while (!signal.isStartPhaseTwo()) {
      Thread.sleep(100);
      // Busy locking
    }
    for (int storeId = threads.length / 4; storeId < threads.length / 2; storeId++) {
      new Thread(threads[storeId]).start();
    }
  }

  /**
   * Helper function to run phase 3 threads
   * @param threads - Collection of pending threads
   * @param signal - Instance containing synchronized global variables.
   */
  private static void phaseThree(Thread[] threads, PurchaseSignal signal)
      throws InterruptedException {
    while (!signal.isStartPhaseThree()) {
      Thread.sleep(100);
      // Busy locking
    }
    for (int storeId = threads.length / 2; storeId < threads.length; storeId++) {
      new Thread(threads[storeId]).start();
    }
  }

  /**
   * Posts all item purchases for a single 9-hour day to servlet.
   * Returns an int representing the number of successful purchases
   * @param api - Swagger PurchaseApi instance
   * @param storeId - ID of store making POST requests
   * @param custPerStore - Maximum customers per store
   * @param idPrefix - Prefix of of customer ID
   * @param purchPerH - Number of purchases per hour
   * @param maxItemId - Number of items, used for ID purposes
   * @param itemsPerPurch - Number of items in each purchase.
   * @param date - Date of purchase
   * @param rand - Instance of Random class for generating pseudorandom numbers
   * @param signal - Instance containing synchronized global variables.
   * @return an int representing the number of successful purchases.
   */
  private static int makePurchases(PurchaseApi api, Integer storeId, Integer custPerStore,
      String idPrefix, Integer purchPerH, Integer maxItemId, Integer itemsPerPurch, String date,
      Random rand, PurchaseSignal signal) {

    // Buffer has capacity to run thread.
    System.out.println("Store " + storeId  + " started");
    int purchases = 0;
    int phaseTwoThreshold = 3;
    int phaseThreeThreshold = 5;
    int storeHours = 9;

    // Outer loop: Loops through each store hour
    for (int hour = 0; hour < storeHours; hour++) {
      // Create a purchase details and POST request to servlet.
      for (int purchase = 0; purchase < purchPerH; purchase++) {
        Purchase body = new Purchase(); // Purchase | items purchased
        // Populate each purchase with the specified amount of items.
        for (int i = 0; i < itemsPerPurch; i++) {  // Add items to Purchase request
          PurchaseItems item = new PurchaseItems();
          item.setItemID(String.valueOf(rand.nextInt(maxItemId)));  // Random ID
          item.setNumberOfItems(1);  // Default of 1
          body.addItemsItem(item);
        }

        // POST the purchase details to servlet.
        Integer custId = Integer.valueOf(idPrefix + rand.nextInt(custPerStore));
        int response = -1;
        long startRequest = System.currentTimeMillis();
        try {
          response = api.newPurchaseWithHttpInfo(body, storeId, custId, date).getStatusCode();
          purchases = response == 200 | response == 201 ? purchases + 1 : purchases;
        } catch (ApiException e) {
          response = e.getCode();
          System.err.println("Exception when calling PurchaseApi#newPurchase with code: " + response);
          e.printStackTrace();
        }  finally {  // Finished transmission, update synchronized global variables.
          long latency = System.currentTimeMillis() - startRequest;
          signal.enqueueRecord(new Record(startRequest, latency, response));
        }

        // Unlock phase 2 upon meeting conditions.
        if (purchases >= purchPerH * phaseTwoThreshold) {
          signal.setStartPhaseTwo(true);
        }
        // Unlock phase 3 upon meeting conditions.
        if (purchases >= purchPerH * phaseThreeThreshold) {
          signal.setStartPhaseThree(true);
        }
      }
    }
    System.out.println("Store " + storeId  + " completed");
    return purchases;
  }
}
