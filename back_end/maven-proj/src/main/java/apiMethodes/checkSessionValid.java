package apiMethodes;

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

public class checkSessionValid implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(changePassword.class);
    public JSONObject run(JSONObject request) {
        String token = request.getString("token");
        JSONObject result = new JSONObject();
        if (isTokenValid(token)) {
            result.put("valid", true);

            String newExpirationTime = getExpirationTime();
            String query = "UPDATE sessions SET expiration_time='" + newExpirationTime + "' WHERE token='" + token + "'";
            try {
                executeQuery(query);
                logger.info("Session updated");
            } catch (SQLException e) {
                logger.error("Problem with database");
            }
        }
        else {
            result.put("valid", false);
        }
        return result;
    }

    public static boolean isTokenValid(String token) {
//        user_id jest do wyjebania, kluczem glownym moglby byc login bo zakladamy ze jest unikatowy
        try {
            String query = "SELECT expiration_time FROM SESSIONS WHERE token='" + token + "'";
            String[] columns = {"expiration_time"};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, columns);
            logger.info("Get expiration time of token for token.");

            String exp_time = queryResult.get(0).get(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime expiration_time = LocalDateTime.parse(exp_time, formatter);
            LocalDateTime now = LocalDateTime.now();

            if (now.isBefore(expiration_time)) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
}
