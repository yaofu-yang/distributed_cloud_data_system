public class Purchase {
  private String storeId;
  private String custId;
  private String date;
  String items;

  // Overloaded for when items are just stored as a string
  public Purchase(String storeId, String custId, String date, String items) {
    this.storeId = storeId;
    this.custId = custId;
    this.date = date;
    this.items = items;
  }

  public String getStoreId() {
    return this.storeId;
  }

  public String getCustId() {
    return this.custId;
  }

  public String getDate() {
    return this.date;
  }

  public String getItems() {
    return this.items;
  }
}
