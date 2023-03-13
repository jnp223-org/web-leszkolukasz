package library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import library.routes.AbstractPage;
import library.routes.BookPage;
import library.routes.MainPage;

public class App {
    private final static int PORT = 8000;
    private static ServerSocket serverSocket;
    private static AbstractPage mainPage = new MainPage();
    private static AbstractPage bookPage = new BookPage();

    public static void main(String[] args) throws IOException {
        start();
    }

    private static void start() throws IOException {
        serverSocket = new ServerSocket(PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            var out = new PrintWriter(clientSocket.getOutputStream(), true);
            var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line = in.readLine();
            String requestMethod = line.split(" ")[0];

            if (requestMethod.equals("GET"))
                handleGET(line, in, out);
            else if (requestMethod.equals("POST"))
                handlePOST(line, in, out);
            else
                System.out.println("Error in request parsing");

            in.close();
            out.close();
            clientSocket.close();
        }

    }

    private static void handlePOST(String firstLine, BufferedReader in, PrintWriter out) throws IOException {
        String url = firstLine.split(" ")[1];
        String response = "";
        Map<String, String> params = new HashMap<>();

        // https://stackoverflow.com/questions/3033755/reading-post-data-from-html-form-sent-to-serversocket
        while((in.readLine()).length() != 0) {}

        StringBuilder payload = new StringBuilder();
        while(in.ready()){
            payload.append((char) in.read());
        }

        String[] pairs = payload.toString().split("&");
        for (String pair: pairs) {
            params.put(pair.split("=")[0], pair.split("=")[1]);
        }

        if (url.startsWith("/book"))
            response = bookPage.getPOSTResponse(url, params);
        else
            response = mainPage.getPOSTResponse(url, params);

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println();
        out.println(response);
    }

    private static void handleGET(String firstLine, BufferedReader in, PrintWriter out) {
        String url = firstLine.split(" ")[1];
        String response = "";

        if (url.startsWith("/book"))
            response = bookPage.getGETResponse(url);
        else
            response = mainPage.getGETResponse(url);

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println();
        out.println(response);
    }
}
