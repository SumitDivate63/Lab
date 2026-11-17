package com.example.helloweb;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public final class HelloWebApplication {
    private HelloWebApplication() {
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
        server.createContext("/", HelloWebApplication::handleRequest);
        server.start();
        System.out.println("Server started on http://0.0.0.0:8080");
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        int status;
        String contentType;
        String response;

        if (!"GET".equals(exchange.getRequestMethod())) {
            status = 405;
            contentType = "text/plain; charset=utf-8";
            response = "Method Not Allowed";
        } else if ("/health".equals(path)) {
            status = 200;
            contentType = "text/plain; charset=utf-8";
            response = "OK";
        } else if ("/".equals(path)) {
            status = 200;
            contentType = "text/html; charset=utf-8";
            response = """
                    <!doctype html>
                    <html lang="en">
                    <head>
                      <meta charset="utf-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1">
                      <title>Simple Java Web Application</title>
                    </head>
                    <body>
                      <main>
                        <h1>Hello from Java!</h1>
                        <p>Your web application is running in Docker.</p>
                      </main>
                    </body>
                    </html>
                    """;
        } else {
            status = 404;
            contentType = "text/plain; charset=utf-8";
            response = "Not Found";
        }

        byte[] body = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, body.length);
        try (var output = exchange.getResponseBody()) {
            output.write(body);
        }
    }
}
