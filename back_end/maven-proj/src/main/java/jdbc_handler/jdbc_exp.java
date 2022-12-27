package jdbc_handler;
import java.sql.*;
import java.util.ArrayList;

public class jdbc_exp {
    static final String DB_URL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    static final String USER = "kfijalk1";
    static final String PASS = "kfijalk1";


    public static void main(String[] args) {
        // Open a connection
        String exmp_query = "SELECT * FROM users";
        String[] columns = {"user_id", "name", "password"};
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        try {
            result = getFromQuery(exmp_query, columns);
        } catch (SQLException e) {System.out.println("problem");}

        for (ArrayList<String> row : result) {
            System.out.println("NEXT");
            for (String cell : row){
                System.out.println(cell);
            }
        }
    }
    public static ArrayList<ArrayList<String>> getFromQuery(String QUERY, String[] columnLabels) throws SQLException {
        // gets given query from database
        // returns array of arrays of strings in same order as columnLabel
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY);
        // Extract data from result set
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        while (rs.next())
        {
            ArrayList<String> col_result = new ArrayList<String>();
            for (String colLabel : columnLabels)
            {
                col_result.add(rs.getString(colLabel));
            }
            result.add(col_result);
        }
        return result;
    }

    public static int executeQuery(String QUERY) throws SQLException {
        /*
        usable for update, delete and insert
        puts data in query
        or updates
        returns number of rows modified
         */
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        int count = stmt.executeUpdate(QUERY);
        return count;
    }
}


