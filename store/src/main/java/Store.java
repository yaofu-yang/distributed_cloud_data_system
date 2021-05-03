public class Store {
  private int storeId;
  private int itemCount;

  public Store(int storeId, int itemCount) {
    this.storeId = storeId;
    this.itemCount = itemCount;
  }

  public int getItemCount() {
    return itemCount;
  }

  @Override
  public String toString() {
    return "Store ID: " + this.storeId + " Item Count: " + this.itemCount + "\n";
  }
}
