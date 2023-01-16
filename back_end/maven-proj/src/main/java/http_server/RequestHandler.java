package http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;


public class RequestHandler implements HttpHandler {
    static ApiRequestHandler apiRequestHandler = new ApiRequestHandler();
    static StaticRequestHandler staticRequestHandler = new StaticRequestHandler();
    private static final Logger logger = LogManager.getLogger(RequestHandler.class);

    public class HandleThread extends Thread {
        private HttpExchange t;

        public HandleThread(HttpExchange t) {
            this.t = t;
        }

        @Override
        public void run() {
            // Use the arguments in the run method
            if (t.getRequestMethod().equals("GET")) {
                try {
                    staticRequestHandler.handleRequest(t);
                } catch (IOException e) {
                    logger.error(e);
                }
            } else if (t.getRequestMethod().equals("POST")) {
                try {
                    apiRequestHandler.handleRequest(t);
                } catch (IOException e) {
                    logger.error(e);
                }
            } else {
//            odpowiedz "spadaj"
            }
        }
    }

    public void handle(HttpExchange t) throws IOException {
        HandleThread thread = new HandleThread(t);
        thread.start();
    }
}


