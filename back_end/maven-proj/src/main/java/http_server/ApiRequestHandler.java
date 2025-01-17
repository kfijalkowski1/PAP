package http_server;

import apiMethods.*;
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

    private static Map<String, ApiMethodes> methods;
    static {
        methods = new HashMap<>();
        methods.put("/users", new Users());
        methods.put("/addUser", new addUser());
        methods.put("/loginValid", new loginValid());
        methods.put("/authenticateUser", new authenticateUser());
        methods.put("/changePassword", new changePassword());
        methods.put("/addEmail", new addEmail());
        methods.put("/enterExchange", new enterExchange());
        methods.put("/addLecturer", new addLecturer());
        methods.put("/addGroup", new addGroup());
        methods.put("/addClassroom", new addClassroom());
        methods.put("/addCourse", new addCourse());
        methods.put("/getCourseTypes", new getCourseTypes());
        methods.put("/getFaculties", new getFaculties());
        methods.put("/getLecturerDegrees", new getLecturerDegrees());
        methods.put("/getClassrooms", new getClassrooms());
        methods.put("/getLecturers", new getLecturers());
        methods.put("/getCourses", new getCourses());
        methods.put("/getExchanges", new getExchanges());
        methods.put("/getUserInfo", new getUserInfo());
        methods.put("/addFirstname", new addFirstName());
        methods.put("/addSurname", new addSurname());
        methods.put("/addUserGroup", new addUserGroup());
        methods.put("/getGroups", new getGroups());
        methods.put("/getUserGroups", new getUserGroups());
        methods.put("/userReport", new userReport());
        methods.put("/logout", new logout());
        methods.put("/getAllExchanges", new getAllExchanges());
    }

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


        if (uri.equals("/authenticateUser") || uri.equals("/addUser") || uri.equals("/loginValid") || checkSessionValid.run(request)) {
            if (methods.get(uri) != null) {
                response = methods.get(uri).run(request);
            }
            else {
                response.put("code", 404);
                response.put("message", "invalid endpoint");
            }
        }
        else {
            response.put("code", 401);
            response.put("message", "session expired");
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
