package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;

import static hashingHandler.PasswordHashing.generateHash;
import static hashingHandler.PasswordHashing.validatePassword;
import static jdbc_handler.jdbc_exp.executeQuery;
import static jdbc_handler.jdbc_exp.getFromQuery;

public class authenticateUser implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(authenticateUser.class);
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public JSONObject run (JSONObject request) {
        String login = request.getString("login");
        String password = request.getString("password");

        JSONObject result = new JSONObject();
        boolean isValid = false;
        logger.info("Trying to validate user: " + login);
        try {
            //String query = "SELECT passwd_hash FROM USERS WHERE login='" + login + "'";
            String query = "SELECT passwd_hash FROM USERS";
            String[] columns = {"passwd_hash"};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, columns);
            if (validatePassword(password, queryResult.get(0).get(0))) {
                logger.info("Correct logging for user: " + login);
                isValid = true;

                String token = generateNewToken();
                result.put("token", token);

                String user_id = getUserID(login);
                createNewSession(user_id, token);
            }
        } catch (SQLException e) {
            isValid = false;
        }

        result.put("valid", isValid);
        return result;
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static void createNewSession(String user_id, String token) {
        String expiration_time = getExpirationTime();
        String query = "INSERT INTO sessions values (null, '" +
                user_id + "', '" +
                token + "', '" +
                "to_date('" + expiration_time + "', 'YYYY/MM/DD HH24:MI:SS'))";

        try {
            executeQuery(query);
            logger.info("Session started for user: " + user_id);
        } catch (SQLException e) {
            logger.error("Problem with database");
        }
    }

    public static String getUserID(String login) {
        String user_id = "";
        try {
            String query = "SELECT user_id FROM users WHERE login='" + login + "'";
            String[] columns = {"user_id"};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, columns);
            user_id = queryResult.get(0).get(0);
        } catch (SQLException e) {
            logger.info("No such User in DB: " + login);
        }
        return user_id;
    }

    public static String getExpirationTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(40);
        return expirationTime.format(formatter);
    }
}
