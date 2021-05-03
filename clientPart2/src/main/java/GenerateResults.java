import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public final class GenerateResults {

  /**
   * Prints out a summary of results and writes additional data to csv file
   * @param  - Collection of POST request records.
   * @param  - Sorted (descending) collection of request latencies
   * @param  - Sum of latency for all requests.
   * @param actualPurchases - Number of successful purchases
   * @param expectedPurchases - Expected number of successful purchases
   * @param wallTime - Time from first request to last request.
   * @throws IOException - Thrown when file cannot be created/accessed for writing.
   */
  public static void generateResults(List<Record> r, PriorityQueue<Long> l, long lT,
      Queue<Record> allRecords, long actualPurchases, long expectedPurchases, long wallTime) throws IOException {

    // From queue of records, push records to list, add to PriorityQueue of latencies, tally totalLatencies,
    List<Record> records = new ArrayList<>();
    PriorityQueue<Long> latencies = new PriorityQueue<>(Comparator.reverseOrder());
    long latencyTotal = 0;

    // Todo: Beginning of modified version
    // Extract needed data.
    Record record;
    while (!allRecords.isEmpty()) {
      record = allRecords.remove();
      latencies.add(record.getLatency());
      latencyTotal += record.getLatency();
      records.add(record);
    }

    // Todo: End of modified version.

    long max = !latencies.isEmpty() ? latencies.peek() : -1;  // -1 indicates no max
    long mean = latencyTotal / expectedPurchases;
    long topPercent = expectedPurchases / 100;  // Top 1 percent will not be counted.
    long midpoint = latencies.size() / 2;
    long counter = 0;
    while (!latencies.isEmpty() && counter < topPercent) {
      counter++;
      latencies.poll();
    }
    long p99 = !latencies.isEmpty() ? latencies.poll() : -1;
    while (!latencies.isEmpty() && counter < midpoint) {
      counter++;
      latencies.poll();
    }
    long median = !latencies.isEmpty() ? latencies.poll() : -1;

    System.out.println("Total number of successful requests: " + actualPurchases);
    System.out.println("Total number of failed requests: " + (expectedPurchases - actualPurchases));
    System.out.println("Mean response time for POSTs: " + mean + "ms");
    System.out.println("Median response time for POSTs: " + median + "ms");
    System.out.println("Total wall time: " + wallTime);
    System.out.println("Throughput (requests per second): " + (actualPurchases/wallTime));
    System.out.println("p99 value: " + p99 + "ms");  // Find value of 99th percentile element. Use max heap
    System.out.println("Max response time for POSTs: " + max + "ms");

    writeToCsv(records);
  }

  /**
   * Write each POST request record to CSV file named "POST_results.csv"
   * @param records - Collection of records of each POST request
   * @throws IOException - Thrown if cannot create/access file.
   */
  private static void writeToCsv(List<Record> records) throws IOException {
    FileWriter csvWriter = new FileWriter("POST_results.csv");
    csvWriter.append("Start Time");
    csvWriter.append(",");
    csvWriter.append("Request Type");
    csvWriter.append(",");
    csvWriter.append("Latency");
    csvWriter.append(",");
    csvWriter.append("Response Code");
    csvWriter.append("\n");

    for (Record record : records) {
      csvWriter.append(String.valueOf(record.getStartTime()));
      csvWriter.append(",");
      csvWriter.append("POST");
      csvWriter.append(",");
      csvWriter.append(String.valueOf(record.getLatency()));
      csvWriter.append(",");
      csvWriter.append(String.valueOf(record.getResponseCode()));
      csvWriter.append("\n");
    }

    csvWriter.flush();
    csvWriter.close();
  }
}
