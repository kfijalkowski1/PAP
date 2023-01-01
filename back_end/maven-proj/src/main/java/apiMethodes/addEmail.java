package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.sql.SQLException;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addEmail implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(Users.class);
    public JSONObject run(JSONObject request) {
        boolean success = true;
        JSONObject result = new JSONObject();
        String e_mail = request.getString("e_mail");
        String login = request.getString("login");
        String query = "UPDATE table\n" +
                "SET e_mail = '" + e_mail + "' \nwhere login = '" + login + "'";
        try {
            executeQuery(query);
        } catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        result.put("result", success);
        return result;
    }
}

