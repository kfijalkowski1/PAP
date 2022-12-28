package http_server;

import apiMethodes.addUser;
import com.sun.net.httpserver.HttpExchange;
import apiMethodes.ApiMethodes;
import apiMethodes.Users;
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

        if (methodes.get(uri) != null) {
            response = methodes.get(uri).run(request);
        } else {
            // TODO set response to method not in
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
