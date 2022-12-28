package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addUser implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(Users.class);
    public JSONObject run(JSONObject request) {
        String value = request.getString("login");
        logger.info("Adding user: " + value);
        String query1 = "INSERT INTO users (login, passwd_hash) values ('" + value + "', '2321')";

        try {
            executeQuery(query1);
            logger.info("User added");
        } catch (SQLException e) {
            logger.error("Problem with database");
        }
        return new JSONObject();
    }
}

