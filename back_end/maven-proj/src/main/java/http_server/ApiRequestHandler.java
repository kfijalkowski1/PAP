package http_server;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import java.io.*;
import java.util.Collections;
import static java.net.HttpURLConnection.HTTP_OK;


public class ApiRequestHandler {
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
        String requestString = parseInputRequest(t);
        JSONObject request = new JSONObject(requestString);

        String testValue = request.getString("test");


        JSONObject response = new JSONObject();
        response.put("testValue", testValue);
        response.put("key", "xddd");

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
