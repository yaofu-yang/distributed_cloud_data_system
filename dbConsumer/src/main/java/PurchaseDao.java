import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public final class PurchaseDao {
  private static Connection conn;

  // Todo: Modify to accept single purchase for now
  public static void createPurchase(Purchase purchase) throws SQLException {
    PreparedStatement preparedStatement = null;
//    String additionalRow = ",(?,?,?,?)";
    StringBuilder insertQueryStatement = new StringBuilder("INSERT INTO Purchases (storeId, custId, date, items) "
        + "VALUES (?,?,?,?)");

    // Append the number of inserts per connection.
//    for (int i = 1; i < purchases.size(); i++) {
//      insertQueryStatement.append(additionalRow);
//    }
    try {
      conn = DataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement.toString());
//      int counter = 1;
//      for (Purchase purchase : purchases) {
//        preparedStatement.setString(counter++, purchase.getStoreId());
//        preparedStatement.setString(counter++, purchase.getCustId());
//        preparedStatement.setString(counter++, purchase.getDate());
//        preparedStatement.setString(counter++, purchase.getItems());
//      }
//
        preparedStatement.setString(1, purchase.getStoreId());
        preparedStatement.setString(2, purchase.getCustId());
        preparedStatement.setString(3, purchase.getDate());
        preparedStatement.setString(4, purchase.getItems());

      // Execute insert SQL statement
      preparedStatement.executeUpdate();  // Inserts entire list of purchases once

    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}
