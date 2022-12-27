package jdbc_handler;
import static jdbc_handler.jdbc_exp.getFromQuery;
import static jdbc_handler.jdbc_exp.executeQuery;
import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class jdbcTest {
    @Test
    public void testGetData() {
        // assertEquals(String message, long expected, long actual)
        String exmp_query = "SELECT * FROM test_users";
        String[] columns = {"user_id", "login", "PASSWD_HASH"};
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        try {
            result = getFromQuery(exmp_query, columns);
        } catch (SQLException e) {System.out.println("problem");}


        assertEquals("result user_id", result.get(0).get(0), "1");
        assertEquals("", result.get(0).get(1), "super-user");
        assertEquals("error in add()", result.get(0).get(2), "passwd");
    }
    @Test
    public void testInsertDeleteData() {
        // insert
        String insertQuery = "INSERT INTO test_users (user_id, login, passwd_hash) values (99, 'delete-user','passwd')";
        int insertResult=-1;
        try {
            insertResult = executeQuery(insertQuery);
        } catch (SQLException e) {System.out.println("problem");}
        assertEquals("number of rows inserted", 1, insertResult);


        // check
        String exmp_query = "select count(user_id) as num from test_users where login = 'delete-user'";
        String[] columns = {"num"};
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        try {
            result = getFromQuery(exmp_query, columns);
        } catch (SQLException e) {System.out.println("problem");}
        assertEquals("number of current delete-users, should be 1", result.get(0).get(0), "1");

        // delete
        String deleteQuery = "delete from test_users where login = 'delete-user'";
        int deleteResult=-1;
        try {
            deleteResult = executeQuery(deleteQuery);
        } catch (SQLException e) {System.out.println("problem");}
        assertEquals("number of rows deleted", deleteResult, 1);

        //check delete
        String afterDeleteQuery = "select count(user_id) as num from test_users where login = 'delete-user'";
        String[] delColumns = {"num"};
        ArrayList<ArrayList<String>> delResult = new ArrayList<>();
        try {
            delResult = getFromQuery(afterDeleteQuery, delColumns);
        } catch (SQLException e) {System.out.println("problem");}
        assertEquals("number of users after delete", delResult.get(0).get(0), "0");

    }


}
