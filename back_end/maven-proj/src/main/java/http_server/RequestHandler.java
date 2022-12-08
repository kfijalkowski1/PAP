package http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;


public class RequestHandler implements HttpHandler {
    static ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
    static StaticRequestHandler staticRequestHandler = new StaticRequestHandler();

    public void handle(HttpExchange t) throws IOException {
        if (t.getRequestMethod().equals("GET")) {
            staticRequestHandler.handleRequest(t);
        } else if (t.getRequestMethod().equals("POST")) {
            apiRequestHandler.handleRequest(t);
        } else {
//            odpowiedz "spadaj"
        }
    }
}
