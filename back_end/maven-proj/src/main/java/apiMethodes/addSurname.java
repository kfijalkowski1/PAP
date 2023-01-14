package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addSurname implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(addSurname.class);
    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();
        String name;
        String login;
        try {
            name = request.getString("surname");
            login = request.getString("login");
        } catch (JSONException e) {
            logger.error(e);
            result.put("code", 400);
            result.put("message", "Bad request");
            return result;
        }

        String query = "UPDATE USERS SET surname = ? where login = ?";
        String[] args = {name, login};
        try {
            executeQuery(query, args);
            result.put("code", 200);
            result.put("message", "");
        } catch (SQLException e) {
            logger.error(e);
            result.put("code", 500);
            result.put("message", "DB malfunction");
        }

        return result;
    }
}

