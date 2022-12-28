package jdbc_handler;
import java.sql.*;
import java.util.ArrayList;

import loger.logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class jdbc_exp {
    static final String DB_URL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    static final String USER = "z27";
    static final String PASS = "9wdzsz";
    private static final Logger logger = LogManager.getLogger(loger.logger.class);



    public static void main(String[] args) {

    }
    public static ArrayList<ArrayList<String>> getFromQuery(String QUERY, String[] columnLabels) throws SQLException {
        // gets given query from database
        // returns array of arrays of strings in same order as columnLabel
        logger.info("JDBC (select) received query: " + QUERY);
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY);
        logger.info("JDBC (select) executed query");
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
        conn.close();
        return result;
    }

    public static int executeQuery(String QUERY) throws SQLException {
        /*
        usable for update, delete and insert
        puts data in query
        or updates
        returns number of rows modified
         */
        logger.info("JDBC (execute) received query: " + QUERY);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e)
        {
            logger.error(e);
        }
        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = con.createStatement();
        int count = stmt.executeUpdate(QUERY);
        logger.info("JDBC (execute) executed query");
        return count;
    }
}


