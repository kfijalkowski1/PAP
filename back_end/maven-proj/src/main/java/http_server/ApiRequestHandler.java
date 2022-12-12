package http_server;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import static java.net.HttpURLConnection.HTTP_OK;

import static jdbc_handler.jdbc_exp.getFromQuery;


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
        String uri = t.getRequestURI().getPath();

        String requestString = parseInputRequest(t);
        JSONObject request = new JSONObject(requestString);

//        String testValue = request.getString("test");

        JSONObject response = new JSONObject();

        switch (uri) {
            case "/users":
                String query = "SELECT * FROM users";
                String[] columns = {"user_id", "name"};
                ArrayList<ArrayList<String>> result;

                try {
                    result = getFromQuery(query, columns);
                    JSONArray resultUsers = new JSONArray();

                    for (ArrayList<String> record : result) {
                        JSONObject user = new JSONObject()
                                .put("user_id", record.get(0))
                                .put("name", record.get(1));
                        resultUsers.put(user);
                    }
                    response.put("users", resultUsers);

                } catch (SQLException e) {
                    System.out.println("Problem with database");
                }
                break;
            case "/addUser":
                String value = request.getString("login");
                String query1 = "INSERT INTO users (user_id, name, password) values (3,'" + value + "','')";
                String[] columns1 = {};

                try {
                    getFromQuery(query1, columns1);
                } catch (SQLException e) {
                    System.out.println("Problem with database");
                }
                break;

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
