package jdbc_handler;
import java.sql.*;

public class jdbc_exp {
    static final String DB_URL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    static final String USER = "kfijalk1";
    static final String PASS = "kfijalk1";
    static final String QUERY = "SELECT * FROM users";

    public static void main(String[] args) {
        // Open a connection
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);) {
            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                System.out.print("ID: " + rs.getInt("user_id"));
                System.out.print(", name: " + rs.getString("name"));
                System.out.print(", password: " + rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


