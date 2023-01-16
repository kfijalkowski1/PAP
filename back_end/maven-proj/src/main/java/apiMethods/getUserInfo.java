package apiMethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class getUserInfo implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getUserInfo.class);
    public JSONObject run(JSONObject request) {
        logger.info("Getting user info");
        JSONObject response = new JSONObject();

        String login = null;
        try {
            login = request.getString("login");
        } catch (JSONException e) {
            response.put("code", 400);
            response.put("message", "incorrect request");
            logger.info("Problem with database");
        }

        String query = "SELECT email, name, surname FROM users where login=?";
        String[] columns = {"email", "name", "surname"};
        String[] args = {login};
        ArrayList<ArrayList<String>> result;

        try {
            result = getFromQuery(query, args, columns);

            String email = result.get(0).get(0);

            String name = result.get(0).get(1);

            String surname = result.get(0).get(2);


            response.put("email", email);
            response.put("firstname", name);
            response.put("surname", surname);
            response.put("code", 200);

        } catch (SQLException e) {
            response.put("code", 500);
            logger.info("Problem with database");
        }

        return response;
    }
}