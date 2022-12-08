package http_server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;


public class StaticRequestHandler {
    private static final String root = "webpage";

    public void prepareHeaders(HttpExchange t, String fileName) throws IOException {
        String mime = "text/html";
        String extension = fileName.substring(fileName.length() - 3);
        if (extension.equals(".js")) mime = "application/javascript";
        if (extension.equals("css")) mime = "text/css";

        Headers h = t.getResponseHeaders();
        h.set("Content-Type", mime);
        t.sendResponseHeaders(200, 0);
    }
    public void serveFile(HttpExchange t, String fileName) throws IOException {
        prepareHeaders(t, fileName);

        File file = new File(root + fileName).getCanonicalFile();
        OutputStream os = t.getResponseBody();
        FileInputStream fs = new FileInputStream(file);

        final byte[] buffer = new byte[0x10000];
        int count;
        while ((count = fs.read(buffer)) >= 0) {
            os.write(buffer, 0, count);
        }

        fs.close();
        os.close();
    }
    public void handleRequest(HttpExchange t) throws IOException {
        URI uri = t.getRequestURI();
        String path = uri.getPath();
        File file = new File(root + path).getCanonicalFile();

        if (file.isFile()) {
            serveFile(t, path);
        } else {
            serveFile(t, "/index.html");
        }
    }
}
