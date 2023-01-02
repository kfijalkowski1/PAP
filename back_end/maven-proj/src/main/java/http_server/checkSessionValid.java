package http_server;

import apiMethodes.ApiMethodes;
import apiMethodes.changePassword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static apiMethodes.authenticateUser.getExpirationTime;
import static jdbc_handler.jdbc_exp.executeQuery;
import static jdbc_handler.jdbc_exp.getFromQuery;

public class checkSessionValid {
    private static final Logger logger = LogManager.getLogger(changePassword.class);
    public static boolean run(JSONObject request) {
        String login = request.getString("login");
        String token = request.getString("token");
        boolean result = false;
        if (isTokenValid(login, token)) {
            result = true;

            String newExpirationTime = getExpirationTime();
            String query = "UPDATE sessions SET expiration_time='" + newExpirationTime + "' WHERE login='" + login + "'";
            try {
                executeQuery(query);
                logger.info("Session updated");
            } catch (SQLException e) {
                logger.error("Problem with database");
            }
        }
        else {
            try {
                String deleteQuery = "DELETE FROM sessions WHERE login='" + login + "'";
                executeQuery(deleteQuery);
                logger.info("Session deleted for user: " + login);
            } catch (SQLException e) {
                logger.error("Problem with database");
            }
        }
        return result;
    }

    public static boolean isTokenValid(String login, String token) {
        try {
            String query = "SELECT expiration_time, token FROM SESSIONS WHERE login='" + login + "'";
            String[] columns = {"expiration_time, token"};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, columns);
            logger.info("Get expiration time of token for login.");

            String exp_time = queryResult.get(0).get(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime expiration_time = LocalDateTime.parse(exp_time, formatter);
            LocalDateTime now = LocalDateTime.now();

            String trueToken = queryResult.get(0).get(1);
            if (token.equals(trueToken) && now.isBefore(expiration_time)) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
}
