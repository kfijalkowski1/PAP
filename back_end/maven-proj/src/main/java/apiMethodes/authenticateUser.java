package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
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
        JSONObject result = new JSONObject();
        String password;
        String login;
        try {
            login = request.getString("login");
            password = request.getString("password");
        } catch (JSONException e) {
            result.put("code", 400);
            result.put("message", "wrong request");
            return result;
        }

        boolean isValid = false;
        logger.info("Try to validate user: " + login);
        try {
            String query = "SELECT passwd_hash FROM USERS WHERE login= ?";
            String[] columns = {"passwd_hash"};
            String[] args = {login};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, args, columns);
            if (queryResult.size() == 0){
                result.put("code", 400);
                result.put("message", "incorrect password or login");
                return result;
            }
            if (validatePassword(password, queryResult.get(0).get(0))) {
                logger.info("Correct logging for user: " + login);
                isValid = true;

                String token = generateNewToken();
                result.put("token", token);
                createNewSession(login, token);
            }
        } catch (SQLException e) {
            isValid = false;
        }
        if (isValid) {
            result.put("code", 200);
            result.put("message", "");
        } else {
            result.put("code", 400);
            result.put("message", "incorrect password or login");
        }

        return result;
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public static void createNewSession(String login, String token) {
        String expiration_time = getExpirationTime();
        String query = "INSERT INTO sessions (TOKEN, LOGIN, EXPIRATION_TIME) values (?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'))";
        String args[] = {token, login, expiration_time};
        try {
            executeQuery(query, args);
            logger.info("Session started for user: " + login);
        } catch (SQLException e) {
            logger.error(e);
            logger.error("Problem with database");
        }
    }


    public static String getExpirationTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(40);
        return expirationTime.format(formatter);
    }
}
