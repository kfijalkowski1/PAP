package apiMethods;

import emailHandler.sendEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import static hashingHandler.PasswordHashing.generateHash;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addUser implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(Users.class);
    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();
        String login = null;
        String password = null;
        String name = null;
        String surname = null;
        String email = null;
        try {
            login = request.getString("login");
            password = request.getString("password");
            name = request.getString("name");
            surname = request.getString("surname");
            email = request.getString("email");
        } catch (JSONException e) {
            String msg = "Incorrect request, not all obligatory fields";
            logger.error(msg);
            result.put("code", 400);
            result.put("message", msg);
        }


        logger.info("Adding user: " + login);
        String hash = generateHash(password);
        String query1 = "INSERT INTO users (login, passwd_hash, name, surname, email) values (?, ?, ?, ?, ?)";
        String[] args = {login, hash, name, surname, email};

        try {
            executeQuery(query1, args);
            logger.info("User added");
            sendEmail.registrationConfirm(login, email);

            result.put("code", 200);
            result.put("message", "");
        } catch (SQLException e) {
            result.put("code", 500);
            result.put("message", "DB malfunction");
            logger.error("Problem with database");
        }
        return result;
    }
}

