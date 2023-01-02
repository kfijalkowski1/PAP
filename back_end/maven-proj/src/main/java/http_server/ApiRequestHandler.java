package http_server;

import apiMethodes.*;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import java.io.*;
import java.util.Collections;
import static java.net.HttpURLConnection.HTTP_OK;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ApiRequestHandler {
    private static final Logger logger = LogManager.getLogger(ApiRequestHandler.class);
    public String parseInputRequest(HttpExchange t) throws IOException {
        StringBuilder requestBuffer = new StringBuilder();
        InputStream is = t.getRequestBody();

        int rByte;
        while ((rByte = is.read()) != -1) {
            requestBuffer.append((char) rByte);
        }
        is.close();

        return requestBuffer.toString();
    }

    public void handleRequest(HttpExchange t) throws IOException {
        String uri = t.getRequestURI().getPath();
        logger.info("Received uri: " + uri);

        String requestString = parseInputRequest(t);
        JSONObject request = new JSONObject(requestString);

        JSONObject response = new JSONObject();
        Map<String, ApiMethodes> methodes = new HashMap<>();

        // Add all methods, TODO move this map somewhere
        methodes.put("/users", new Users());
        methodes.put("/addUser", new addUser());
        methodes.put("/loginValid", new loginValid());
        methodes.put("/authenticateUser", new authenticateUser());
        methodes.put("/changePassword", new changePassword());
        methodes.put("/addEmail", new addEmail());

        if (checkSessionValid.run(request) || uri.equals("/authenticateUser") || uri.equals("/addUser") || uri.equals("/loginValid")) {
            if (methodes.get(uri) != null) {
                response = methodes.get(uri).run(request);
            }
            else {
                response.put("code", 404);
            }
        }
        else {
            response.put("code", 401);
        }

        String responseString = response.toString();
        t.getResponseHeaders().put("Content-Type", Collections.singletonList("application/json"));
        t.sendResponseHeaders(HTTP_OK, responseString.length());

        OutputStream os;
        os = t.getResponseBody();
        os.write(responseString.getBytes());
        os.close();
        t.close();
    }
}
