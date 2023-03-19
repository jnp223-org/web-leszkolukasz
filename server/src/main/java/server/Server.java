package server;

import library.routes.AbstractPage;
import library.routes.RouteMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private List<RouteMatcher> routes;
    private ExecutorService executor;

    public Server(int port) {
        this.port = port;
        this.routes = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            executor.submit(() -> {
                try {
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
                catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

    }

    public void addRoute(String url, AbstractPage page) {
        routes.add(0, new RouteMatcher(url, page));
    }

    private void handlePOST(String firstLine, BufferedReader in, PrintWriter out) throws IOException {
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

        for (RouteMatcher r: routes)
            if (r.matches(url, true)) {
                response = r.getPOSTResponse(url, params);
                break;
            }

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println();
        out.println(response);
    }

    private void handleGET(String firstLine, BufferedReader in, PrintWriter out) {
        String url = firstLine.split(" ")[1];
        String response = "";

        for (RouteMatcher r: routes)
            if (r.matches(url, false)) {
                response = r.getGETResponse(url);
                break;
            }

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=utf-8");
        out.println();
        out.println(response);
    }
}
