package http_server;

import com.sun.net.httpserver.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import jdbc_handler.jdbc_exp;

public class Httpserver {
    private static final Logger logger = LogManager.getLogger(Httpserver.class);
    public static void main(String[] args) throws Exception {
        jdbc_exp.connConstructor();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null);
        server.start();
        logger.info("SERVER STARTED");
    }
}
