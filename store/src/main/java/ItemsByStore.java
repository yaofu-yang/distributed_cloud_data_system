public class ItemsByStore {
  private static final int MAX_STORES = 1024;
  private static final int MAX_ITEM_ID = 100000;
  private int[][] itemsByStore;

  public ItemsByStore() {
    this.itemsByStore = new int[MAX_STORES][MAX_ITEM_ID];
  }

  public synchronized void incrementItemCount(int storeId, int itemId, int numItems) {
    // Id starts at 0, no need for shift in 0-indexed array
    this.itemsByStore[storeId][itemId] += numItems;
  }

  public int[][] getItemsByStore() {
    return this.itemsByStore;
  }
}
