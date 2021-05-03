import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

  private static HikariConfig config = new HikariConfig();
  private static HikariDataSource ds;
//  private static final String HOST_NAME = "supermarket.cgoephdwzy7u.us-east-1.rds.amazonaws.com";  // 1
//  private static final String HOST_NAME = "supermarket2.cgoephdwzy7u.us-east-1.rds.amazonaws.com";  // 2
//  private static final String HOST_NAME = "supermarket3.cgoephdwzy7u.us-east-1.rds.amazonaws.com";  // 3
  private static final String HOST_NAME = "supermarket4.cgoephdwzy7u.us-east-1.rds.amazonaws.com";  // 4
  private static final String PORT = "3306";
//  private static final String DATABASE = "Supermarket";
//  private static final String DATABASE = "Supermarket2";
//  private static final String DATABASE = "Supermarket3";
  private static final String DATABASE = "Supermarket4";
  private static final String USERNAME = "yangRDS";
  private static final String PASSWORD = "testPassword";
  private static final int MAX_POOL = 60;

  static {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("Found Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);

    config.setJdbcUrl(url);
    config.setUsername(USERNAME);
    config.setPassword(PASSWORD);
    config.addDataSourceProperty( "cachePrepStmts" , "true" );
    config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
    config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
    System.out.println("About to create data source.");
    ds = new HikariDataSource( config );
    System.out.println("Completed configuration");
    ds.setMaximumPoolSize(MAX_POOL);
  }

  private DataSource() {}

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

}
