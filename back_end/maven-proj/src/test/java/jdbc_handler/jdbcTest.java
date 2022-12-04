package jdbc_handler;
import static jdbc_handler.jdbc_exp.getFromQuery;
import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class jdbcTest {
    @Test
    public void testAddPass() {
        // assertEquals(String message, long expected, long actual)
        String exmp_query = "SELECT * FROM users";
        String[] columns = {"user_id", "name", "password"};
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        try {
            result = getFromQuery(exmp_query, columns);
        } catch (SQLException e) {System.out.println("problem");}


        assertEquals("error in add()", result.get(0).get(0), "0");
        assertEquals("error in add()", result.get(0).get(1), "super-user");
        assertEquals("error in add()", result.get(0).get(2), "passwd");
    }


}
