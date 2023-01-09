package apiMethodes;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginValid implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(loginValid.class);
    public JSONObject run(JSONObject request) {
        String value = request.getString("login");

        JSONObject result = new JSONObject();
        boolean isValid = true;
        String message = "";

        // check login length
        if (value.length() < 5 || value.length() > 32) {
            isValid = false;
            message += "| login not too short (under 5 chars) or too long (over 32 chars) |";
        }


        // check if special characters exists in login
        Pattern p = Pattern.compile(
                "[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(value);
        boolean isSpecialChar = m.find();
        if (isSpecialChar) {
            isValid = false;
            message += "| login has illegal characters |";
        }

        // check if login not exists in DB
        try {
            String query = "SELECT COUNT(1) as count FROM USERS WHERE login= ?";
            String[] args = {value};
            String[] columns = {"count"};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, args, columns);
            logger.info("Checked number of users: " + queryResult.get(0).get(0));
            if ("1".equals(queryResult.get(0).get(0))) {
                isValid = false;
                message += "| login already exists |";
            }
        } catch (SQLException e) {
            logger.info("BD error");
            isValid = false;
            result.put("code", 500);
            result.put("message", "BD error");
            return result;
        }

        if(isValid) {
            result.put("code", 200);
        } else {
            result.put("code", 400);
        }
        result.put("message", message);
        return result;
    }
}