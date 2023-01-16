package apiMethods;

import emailHandler.EmailSender;
import org.json.JSONObject;


public class userReport implements ApiMethodes {

    public JSONObject run(JSONObject request) {
        String login = request.getString("login");
        String userMessage = request.getString("message");

        JSONObject result = new JSONObject();

        EmailSender.sendReportLog(login, userMessage);

        result.put("code", 200);
        result.put("message", "Report sent");
        return result;
    }
}
