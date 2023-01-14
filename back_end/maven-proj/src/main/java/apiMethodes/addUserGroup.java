package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.jar.JarException;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addUserGroup implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(addEmail.class);
    public JSONObject run(JSONObject request) {
        ;
        JSONObject result = new JSONObject();
        int groupId;
        String login;
        try {
            groupId = request.getInt("groupId");
            login = request.getString("login");
        } catch (JSONException e) {
            logger.error(e);
            result.put("code", 400);
            result.put("message", "Bad request");
            return result;
        }

        String query = "insert into user_groups (group_id, login) values (?, ?)";
        String[] args = {Integer.toString(groupId), login};
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

