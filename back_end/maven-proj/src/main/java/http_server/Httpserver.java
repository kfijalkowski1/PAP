package http_server;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.StringBuilder;
import java.net.InetSocketAddress;
import org.apache.commons.text.StringEscapeUtils;


public class Httpserver {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new Handler());
        server.setExecutor(null);
        server.start();
    }

    static class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestParamValue = null;
            System.out.println("1");
            if("GET".equals(httpExchange.getRequestMethod())) {
                System.out.println("2");
                requestParamValue = handleGetRequest(httpExchange);
                System.out.println("3");
            }
//            else if("POST".equals(httpExchange)) {
//                requestParamValue = handlePostRequest(httpExchange);
//            }
            handleResponse(httpExchange, requestParamValue);
        }
        private String handleGetRequest(HttpExchange httpExchange) {
            System.out.println(httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1]);
            return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
        }
        private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
            OutputStream outputStream = httpExchange.getResponseBody();
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<html>").
            append("<body>").
            append("<h1>").
            append("Hello ")
                    .append(requestParamValue)
                    .append("</h1>")
                    .append("</body>")
                    .append("</html>");
            // encode HTML content
            String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());

            // this line is a must
            httpExchange.sendResponseHeaders(200, htmlResponse.length());
            outputStream.write(htmlResponse.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }
//    static class Handler implements HttpHandler {
//        @Override
//        public void handle(HttpExchange t) throws IOException {
//            String response = "Test response";
//            t.sendResponseHeaders(200, response.length());
//            OutputStream os = t.getResponseBody();
//            os.write(response.getBytes());
//            os.close();
//        }
//    }
}
