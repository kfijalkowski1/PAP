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

    static class Handler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            if ("GET".equals(t.getRequestMethod())) {
                handleGetRequest(t);
            } else if ("POST".equals(t.getRequestMethod())) {
                handlePostRequest(t);
            }
        }


        public void handlePostRequest(HttpExchange t) throws IOException {
            // endpoints
            System.out.println("INTOO diff POST");
            InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);

            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            System.out.println(buf);
            br.close();
            isr.close();

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
//        @WebServlet("/hello")
//        static class GreetingServlet extends HttpServlet {
//            @Override
//            public void doPost(HttpServletRequest request, HttpServletResponse response)
//                    throws IOException {
//                System.out.println("POST");
//                String name = request.getParameter("name");
//
//                response.getWriter().println("<h1>Hello " + name + "!</h1>");
//            }
//        }
