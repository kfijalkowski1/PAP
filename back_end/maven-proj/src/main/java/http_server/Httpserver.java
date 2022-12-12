package http_server;

import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;


public class Httpserver {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null);
        server.start();
    }
}
