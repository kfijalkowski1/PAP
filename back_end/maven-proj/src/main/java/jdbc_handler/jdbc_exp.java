package jdbc_handler;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.*; // import the pool package

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolDataSource;



public class jdbc_exp {
    static final String DB_URL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    static final String USER = "z27";
    static final String PASS = "9wdzsz";
    private static final Logger logger = LogManager.getLogger(jdbc_exp.class);
    private static PoolDataSource pds = null;


    public static void connConstructor() {
        try {
            pds = PoolDataSourceFactory.getPoolDataSource();
            pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
            pds.setURL(DB_URL);
            pds.setUser(USER);
            pds.setPassword(PASS);

            //Override any pool properties.

            pds.setInitialPoolSize(5);
        } catch( SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws SQLException {
        connConstructor();
        String query = "select column_value from table(?)";
        Integer[] args2 = {10, 11, 12};
        String[] columns = {"column_value"};
        ArrayList<ArrayList<String>> result = getFromArrayQuery(query, args2, columns);
        System.out.println(result.get(0).get(0));
}

    public static ArrayList<ArrayList<String>> getFromQuery(String QUERY, String[] args, String[] columnLabels) throws SQLException {
        // gets given query from database
        // returns array of arrays of strings in same order as columnLabel
        // arg:
        // Query = "Select login, token from users where id=?"
        Connection conn =  null;
        try {
            logger.info("JDBC (select) received query: " + QUERY + " with args: " + String.join(", ", args));
            conn = pds.getConnection();
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
            return result;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        } finally {
            conn.close();
        }
    }

    public static ArrayList<ArrayList<String>> getFromArrayQuery(String QUERY, Integer[] args, String[] columnLabels) throws SQLException {
        // gets given query from database
        // returns array of arrays of strings in same order as columnLabel
        // arg:
        // Query = "Select login, token from users where id=?"
        OracleConnection conn =  null;
        try {
            logger.info("JDBC (select array) received query: " + QUERY + " with args: ");
            conn = (OracleConnection) pds.getConnection();
            Array array = conn.createOracleArray("ARRAY_OF_NUMBERS", args);
            PreparedStatement pstmt = conn.prepareStatement(QUERY);
            pstmt.setArray(1, array);
            ResultSet rs = pstmt.executeQuery();
            logger.info("JDBC (select array) executed query");
            // Extract data from result set
            ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
            while (rs.next()) {
                ArrayList<String> col_result = new ArrayList<String>();
                for (String colLabel : columnLabels) {
                    col_result.add(rs.getString(colLabel));
                }
                result.add(col_result);
            }
            return result;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        } finally {
            conn.close();
        }
    }

    public static int executeQuery(String QUERY, String args[]) throws SQLException {
        /*
        usable for update, delete and insert
        puts data in query
        or updates
        returns number of rows modified
         */
        Connection con = null;
        try {

            logger.info("JDBC (execute) received query: " + QUERY + " with args: " + String.join(", ", args));
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (Exception e) {
                logger.error(e);
            }
            con = pds.getConnection();
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
        } finally {
            con.close();
        }
    }


    public static int executeExchangeFunc(int arg1, int arg2) {
        int result=-1;
        logger.info("executing exchange sql func");
        try {
            // Connect to the database
            Connection conn = pds.getConnection();

            // Create a CallableStatement
            CallableStatement cstmt = conn.prepareCall("{? = call exchange_func(?, ?)}");

            logger.info("exchange args: " + Integer.toString(arg1) + " " + Integer.toString(arg2));

            // Register the OUT parameter
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setInt(2, arg1);
            cstmt.setInt(3, arg2);

            // Execute the function
            cstmt.execute();

            // Get the result
            result = cstmt.getInt(1);
            System.out.println("Result: " + result);

            // Close the CallableStatement and connection
            cstmt.close();
            conn.close();
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        } finally {
            return result;
        }
    }
}


