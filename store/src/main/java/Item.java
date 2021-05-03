public class Item {
  private int itemId;
  private int itemCount;

  public Item(int itemId, int itemCount) {
    this.itemId = itemId;
    this.itemCount = itemCount;
  }

  public int getItemCount() {
    return itemCount;
  }

  @Override
  public String toString() {
    return "Item ID: " + this.itemId + " Item Count: " + this.itemCount + "\n";
  }
}
