package http_server;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import java.util.Scanner; // Import the Scanner class to read text files


import java.lang.StringBuilder;
import java.net.InetSocketAddress;
import org.apache.commons.text.StringEscapeUtils;


public class Httpserver {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new Handler());
        server.setExecutor(null);
        server.start();
        // localhost:8000/home//?path="test.html"
    }

    @WebServlet("/hello")
    static class GreetingServlet extends HttpServlet {
        @Override
        public void doPost(HttpServletRequest request, HttpServletResponse response)
                throws IOException {
            System.out.println("POST");
            String name = request.getParameter("name");

            response.getWriter().println("<h1>Hello " + name + "!</h1>");
        }
    }

    static class Handler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            if ("GET".equals(t.getRequestMethod())) {
                handleGetRequest(t);
            } else if ("POST".equals(t.getRequestMethod())) {
                handlePostRequest(t);
            }
        }

        public void handlePostRequest(HttpExchange t) {
            // endpoints

        }
        public void handleGetRequest(HttpExchange t) throws IOException {
            String root = "src/main/java/http_server";
            URI uri = t.getRequestURI();
            System.out.println("looking for: " + root + uri.getPath());
            String path = uri.getPath();
            File file = new File(root + path).getCanonicalFile();

            if (!file.isFile()) {
                // Object does not exist or is not a file: reject with 404 error.
                String response = "404 (Not Found)\n";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Object exists and is a file: accept with response code 200.
                String mime = "text/html";
                if (path.substring(path.length() - 3).equals(".js")) mime = "application/javascript";
                if (path.substring(path.length() - 3).equals("css")) mime = "text/css";

                Headers h = t.getResponseHeaders();
                h.set("Content-Type", mime);
                t.sendResponseHeaders(200, 0);

                OutputStream os = t.getResponseBody();
                FileInputStream fs = new FileInputStream(file);
                final byte[] buffer = new byte[0x10000];
                int count = 0;
                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
                fs.close();
                os.close();
            }
        }
    }
//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
//
//        out.println("Wczytanie 3 parametrow z zadania :");
//        out.println(request.getParameter("parametr1"));
//        out.println(request.getParameter("parametr2"));
//        out.println(request.getParameter("parametr3"));
//    }

}

//    static class Handler implements HttpHandler {
//        @Override
//        public void handle(HttpExchange httpExchange) throws IOException {
//            String requestParamValue = null;
//            if("GET".equals(httpExchange.getRequestMethod())) {
//                requestParamValue = handleGetRequest(httpExchange);
//            }
////            else if("POST".equals(httpExchange)) {
////                requestParamValue = handlePostRequest(httpExchange);
////            }
//            handleResponse(httpExchange, requestParamValue);
//        }
//        private String handleGetRequest(HttpExchange httpExchange) {
//            System.out.println(httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1]);
//            return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
//        }
//        private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
//            OutputStream outputStream = httpExchange.getResponseBody();
//            StringBuilder htmlBuilder = new StringBuilder();
//            try {
//                File myObj = new File(requestParamValue);
//                Scanner myReader = new Scanner(myObj);
//                while (myReader.hasNextLine()) {
//                    String data = myReader.nextLine();
//                    System.out.println(data);
//                    htmlBuilder.append(data);
//                }
//                myReader.close();
//            } catch (FileNotFoundException e) {
//                System.out.println("An error occurred.");
//                e.printStackTrace(); }
//
//
//
////            htmlBuilder.append("<html>").
////            append("<body>").
////            append("<h1>").
////            append("Hello ")
////                    .append(requestParamValue)
////                    .append("</h1>")
////                    .append("</body>")
////                    .append("</html>");
//
//            // encode HTML content
//            String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());
//
//            // this line is a must
//            httpExchange.sendResponseHeaders(200, htmlResponse.length());
//            outputStream.write(htmlResponse.getBytes());
//            outputStream.flush();
//            outputStream.close();
//        }
//    }
////    static class Handler implements HttpHandler {
////        @Override
////        public void handle(HttpExchange t) throws IOException {
////            String response = "Test response";
////            t.sendResponseHeaders(200, response.length());
////            OutputStream os = t.getResponseBody();
////            os.write(response.getBytes());
////            os.close();
////        }
////    }
//}
