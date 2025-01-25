import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            // Set Connection to DB with credential
            Database db = new Database("localhost:3306", "library", "root", "rahasiaku45");

            // Start the GUI Program
            System.out.println("Program Initialized...");
            LoginFrame fr = new LoginFrame(db);

            fr.setVisible(true);    
        } catch (SQLException err) {
            err.printStackTrace();
        }
    }
}
