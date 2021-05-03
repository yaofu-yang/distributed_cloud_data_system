import java.util.LinkedList;
import java.util.Queue;

/**
 * Class containing synchronized global variables to handle threading updates.
 *
 */
public class PurchaseSignal {
  private boolean startPhaseTwo;
  private boolean startPhaseThree;
  private Queue<Record> records;

  public PurchaseSignal() {
    this.records = new LinkedList<>();
  }

  public synchronized boolean isStartPhaseTwo() {
    return this.startPhaseTwo;
  }

  public synchronized void setStartPhaseTwo(boolean start) {
    this.startPhaseTwo = start;
  }

  public synchronized boolean isStartPhaseThree() {
    return this.startPhaseThree;
  }

  public synchronized void setStartPhaseThree(boolean start) {
    this.startPhaseThree = start;
  }

  public synchronized void enqueueRecord(Record record) {
    this.records.add(record);
  }

  public Queue<Record> getRecords() {
    return this.records;
  }

}