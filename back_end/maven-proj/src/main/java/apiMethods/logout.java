package apiMethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import java.sql.SQLException;
import static jdbc_handler.jdbc_exp.executeQuery;


public class logout implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(logout.class);
    public JSONObject run(JSONObject request) {
        String login = request.getString("login");
        String token = request.getString("token");
        JSONObject response = new JSONObject();

        try {
            String query = "DELETE FROM sessions WHERE login = ? AND token = ?";
            String[] args = {login, token};
            executeQuery(query, args);
            response.put("code", 200);
            logger.error("Deleted sessions for user: " + login);

        } catch (SQLException e) {
            response.put("code", 500);
            logger.error("Problem with database");
        }
        response.put("message", "");
        return response;
    }
}
