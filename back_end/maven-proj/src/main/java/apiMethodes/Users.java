package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class Users implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(Users.class);
    public JSONObject run(JSONObject request) {
        logger.info("Getting all users");
        JSONObject response = new JSONObject();
        String query = "SELECT * FROM users";
        String[] columns = {"login"};
        String[] args = {};
        ArrayList<ArrayList<String>> result;

        try {
            result = getFromQuery(query, args, columns);
            JSONArray resultUsers = new JSONArray();

            for (ArrayList<String> record : result) {
                JSONObject user = new JSONObject()
                        .put("login", record.get(0));
                resultUsers.put(user);
            }
            response.put("users", resultUsers);
            response.put("code", 200);

        } catch (SQLException e) {
            System.out.println("Problem with database");
        }
        return response;
    }
}
