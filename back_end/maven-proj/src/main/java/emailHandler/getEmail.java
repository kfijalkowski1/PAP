package emailHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class getEmail {
    private static final Logger logger = LogManager.getLogger(getEmail.class);
    public static String run(String login) {
        String query = "SELECT email FROM users where login= ?";
        String[] columns = {"email"};
        String[] args = {login};
        ArrayList<ArrayList<String>> result;


        try {
            result = getFromQuery(query, args, columns);
            if (result.size() != 1) {
                return "";
            }
            return result.get(0).get(0);

        } catch (SQLException e) {
            logger.info("Problem with database");
            return "";
        }
    }
}
