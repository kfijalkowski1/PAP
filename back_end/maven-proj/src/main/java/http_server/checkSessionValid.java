package http_server;

import apiMethodes.ApiMethodes;
import apiMethodes.changePassword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static apiMethodes.authenticateUser.getExpirationTime;
import static jdbc_handler.jdbc_exp.executeQuery;
import static jdbc_handler.jdbc_exp.getFromQuery;

public class checkSessionValid {
    private static final Logger logger = LogManager.getLogger(checkSessionValid.class);
    public static boolean run(JSONObject request) {
        String login;
        String token;
        try {
            login = request.getString("login");
            token = request.getString("token");
        } catch (JSONException e) {
            return false;
        }

        boolean result = false;
        if (isTokenValid(login, token)) {
            result = true;

            String newExpirationTime = getExpirationTime();
            String query = "UPDATE sessions SET expiration_time= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') WHERE login= ? and token=?";
            String[] args = {newExpirationTime, login, token};
            try {
                executeQuery(query, args);
                logger.info("Session updated");
            } catch (SQLException e) {
                logger.error("Problem with database");
            }
        }
        return result;
    }

    public static boolean isTokenValid(String login, String token) {
        try {
            String query = "SELECT expiration_time FROM SESSIONS WHERE login=? and token=? and sysdate < expiration_time";
            String[] args = {login, token};
            String[] columns = {"expiration_time"};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, args, columns);

            if (queryResult.size() < 1){
                logger.info("data for given login and token doesn't exist");
                return false;
            } else {
                return true;
            }


        } catch (SQLException e) {
            logger.info(e);
            return false;
        }
    }
}
