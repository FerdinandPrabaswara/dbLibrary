import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public Connection conn = null;

    public Database(String host, String schema,String username, String password) throws SQLException {
        String connUrl = String.format("jdbc:mariadb://%s/%s", host, schema);

        System.out.println("Connecting to the database...");
        conn = DriverManager.getConnection(connUrl, username, password);
        System.out.println("Connected to the database successfully...");
    }
}
