import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientPostPurchases {

  public static void main(String[] args) throws InterruptedException, IOException {
    // Initialize necessary parameters from the command line
    CommandLineParser clp = new CommandLineParser(args);
    Map<String, String> parameters = clp.getArgs();
    int maxStores = Integer.parseInt(parameters.get("stores"));  // Get max stores for threading
    int purchasesPerHour = Integer.parseInt(parameters.getOrDefault("purch", "60"));

    // Initialize necessary parameters for thread creation
    AtomicInteger totalPurchases = new AtomicInteger();
    PurchaseSignal signal = new PurchaseSignal();
    CountDownLatch completed = new CountDownLatch(maxStores);
    Thread[] threads = PurchaseThreadHandler.createThreads(parameters, totalPurchases, signal, completed);

    // Commence the store opening phases and POST request processing via threads.
    long start = System.currentTimeMillis();
    PurchaseThreadHandler.runPhaseThreads(threads, signal);
    completed.await();
    long wallTime = (System.currentTimeMillis() - start) / 1000;
    int expectedPurchases = 9 * maxStores * purchasesPerHour;

    // Print a summary report of test run and generate CSV file of each request data.
    GenerateResults.generateResults(null, null, 0, signal.getRecords(),
        totalPurchases.longValue(), expectedPurchases, wallTime);
  }
}