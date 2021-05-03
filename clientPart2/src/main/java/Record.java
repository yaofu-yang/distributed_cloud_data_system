public class Record {
  private long startTime;
  private long latency;
  private int responseCode;

  /**
   * Wrapper class for data points of interest from the POST requests
   * @param startTime - Start time (ms)
   * @param latency - Latency of the request (ms)
   * @param responseCode - Code returned from server.
   */
  public Record(long startTime, long latency, int responseCode) {
    this.startTime = startTime;
    this.latency = latency;
    this.responseCode = responseCode;
  }

  public long getStartTime() {
    return this.startTime;
  }

  public long getLatency() {
    return this.latency;
  }

  public int getResponseCode() {
    return this.responseCode;
  }

  @Override
  public String toString() {
    return "Record{" +
        "startTime=" + this.startTime +
        ", latency=" + this.latency +
        ", responseCode=" + this.responseCode +
        '}';
  }
}
