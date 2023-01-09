package jdbc_handler;
import java.sql.*;
import java.util.ArrayList;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class jdbc_exp {
    static final String DB_URL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    static final String USER = "z27";
    static final String PASS = "9wdzsz";
    private static final Logger logger = LogManager.getLogger(jdbc_exp.class);



    public static void main(String[] args) throws SQLException {
        String exmp_query = "SELECT ? FROM test_users";
        String[] args2 = {"*"};
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement pstmt = conn.prepareStatement(exmp_query);
        pstmt.setString(0, "*");
        String[] columns = {"user_id", "login", "PASSWD_HASH"};
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        try {
            result = getFromQuery(exmp_query, args2, columns);
        } catch (SQLException e) {System.out.println("problem");}
        System.out.println(result);

    }
    public static ArrayList<ArrayList<String>> getFromQuery(String QUERY, String[] args, String[] columnLabels) throws SQLException {
        // gets given query from database
        // returns array of arrays of strings in same order as columnLabel
        // arg:
            // Query = "Select login, token from users where id=?"
        try {
            logger.info("JDBC (select) received query: " + QUERY + " with args: " + String.join(", ", args));
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement pstmt = conn.prepareStatement(QUERY);
            for (int i = 1; i < args.length + 1; i++) {
                pstmt.setString(i, args[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();
            logger.info("JDBC (select) executed query");
            // Extract data from result set
            ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
            while (rs.next()) {
                ArrayList<String> col_result = new ArrayList<String>();
                for (String colLabel : columnLabels) {
                    col_result.add(rs.getString(colLabel));
                }
                result.add(col_result);
            }
            conn.close();
            return result;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    public static int executeQuery(String QUERY, String args[]) throws SQLException {
        /*
        usable for update, delete and insert
        puts data in query
        or updates
        returns number of rows modified
         */
        try {

            logger.info("JDBC (execute) received query: " + QUERY + " with args: " + String.join(", ", args));
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (Exception e) {
                logger.error(e);
            }
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement pstmt = con.prepareStatement(QUERY);
            for (int i = 1; i < args.length + 1; i++) {
                pstmt.setString(i, args[i - 1]);
            }
            int count = pstmt.executeUpdate();
            logger.info("JDBC (execute) executed query");
            return count;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }
}


