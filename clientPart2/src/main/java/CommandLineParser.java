import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CommandLineParser {
  Map<String, String> args;
  Map<String, String> defaults;

  public CommandLineParser(String[] args) {
    this.defaults = getDefaults();
    this.args = index(args);
    this.setValues();
    this.validateArgs();
  }

  public Map<String, String> getArgs() {
    return this.args;
  }

  /**
   * Stores default values of command line arguments
   * @return Map representing the command line key and its expected default value.
   */
  private Map<String, String> getDefaults() {
    Map<String, String> map = new HashMap<>();
    map.put("cust", "1000");
    map.put("itemID", "100000");
    map.put("purch", "60");
    map.put("items", "5");
    map.put("date", "20210101");
    return map;
  }

  /**
   * Helper function to create initial index for command line arguments.
   * @param args - Command line arguments
   * @return Map representing index of command line tag and arguments
   */
  private Map<String, String> index(String[] args) {
    // Split the arguments first, place in map, then set values
    Map<String, String> map = new HashMap<>();
    for (int i = 0; i < args.length; i+=2) {
      map.put(args[i], args[i+1]);
    }
    return map;
  }

  /**
   * Helper function to populate missing command line arguments with default values
   */
  private void setValues() {
    for (String key : this.defaults.keySet()) {
      if (!this.args.containsKey(key)) {
        this.args.put(key, this.defaults.get(key));
      }
    }
  }

  /**
   * Validate the command line arguments according to specs.
   */
  public void validateArgs() {
    if (!this.args.containsKey("stores")) {
      throw new IllegalArgumentException("Missing required parameter: stores");
    } else if ( !this.args.containsKey("port")) {
      throw new IllegalArgumentException("Missing required parameter: port");
    } else {
      try {
        Integer.parseInt(this.args.get("cust"));
        Integer.parseInt(this.args.get("itemID"));
        Integer.parseInt(this.args.get("purch"));
        Integer.parseInt(this.args.get("items"));
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("cust id purch items --> require numeric argument");
      }

      try {
        LocalDate.parse(this.args.get("date"), DateTimeFormatter.ofPattern("yyyyMMdd"));
      } catch (Exception e) {
        throw new IllegalArgumentException("Date format should be: yyyyMMdd");
      }
    }
  }


  @Override
  public String toString() {
    return "CommandLineParser{" +
        "args=" + this.args +
        '}';
  }
}
