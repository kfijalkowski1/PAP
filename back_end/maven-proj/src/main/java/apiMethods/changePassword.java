package apiMethods;

import emailHandler.sendEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static hashingHandler.PasswordHashing.generateHash;
import static hashingHandler.PasswordHashing.validatePassword;
import static jdbc_handler.jdbc_exp.executeQuery;
import static jdbc_handler.jdbc_exp.getFromQuery;

public class changePassword implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(changePassword.class);

    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();

        String login = null;
        String oldPasswd = null;
        String newPasswd = null;
        try {
            login = request.getString("login");
            oldPasswd = request.getString("old");
            newPasswd = request.getString("new");
        } catch (JSONException e) {
            result.put("code", 400);
            result.put("message", "incorrect request");
        }


        if (isPasswordValid(login, oldPasswd)) {
            String newHash = generateHash(newPasswd);
            String query = "UPDATE users SET passwd_hash= ? WHERE login= ?";
            String args[] = {newHash, login};
            try {
                executeQuery(query, args);
                logger.info("Password changed for user: " + login);
                result.put("code", 200);
                result.put("message", "");

                sendEmail.passwordChangeConfirm(login);

            } catch (SQLException e) {
                logger.error("Problem with database");
                result.put("code", 500);
                result.put("message", "problem with DB");
            }
        } else {
            result.put("code", 400);
            result.put("message", "incorrect old password");
        }
        return result;
    }

    public static boolean isPasswordValid(String login, String password) {
        boolean isValid = false;
        try {
            String query = "SELECT passwd_hash FROM USERS WHERE login= ?";
            String[] args = {login};
            String[] columns = {"passwd_hash"};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, args, columns);
            logger.info("Checked number of users: " + queryResult.get(0).get(0));
            if (validatePassword(password, queryResult.get(0).get(0))) {
                isValid = true;
            }
        } catch (SQLException e) {
            isValid = false;
        }
        return isValid;
    }
}
