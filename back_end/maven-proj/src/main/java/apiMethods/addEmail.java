package apiMethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addEmail implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(addEmail.class);
    public JSONObject run(JSONObject request) {
        ;
        JSONObject result = new JSONObject();
        String e_mail;
        String login;
        try {
            e_mail = request.getString("email");
            login = request.getString("login");
        } catch (JSONException e) {
            logger.error(e);
            result.put("code", 400);
            result.put("message", "Bad request");
            return result;
        }

        String query = "UPDATE USERS SET email = ? where login = ?";
        String[] args = {e_mail, login};
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

