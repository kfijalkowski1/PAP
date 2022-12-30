package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.sql.SQLException;
import static hashingHandler.PasswordHashing.generateHash;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addUser implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(Users.class);
    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();
        String login = request.getString("login");
        String password = request.getString("password");
        logger.info("Adding user: " + login);
        String hash = generateHash(password);
        String query1 = "INSERT INTO users (login, passwd_hash) values ('" + login + "', '" + hash + "')";

        try {
            executeQuery(query1);
            logger.info("User added");
            result.put("success", true);
        } catch (SQLException e) {
            result.put("success", false);
            logger.error("Problem with database");
        }
        return result;
    }
}

