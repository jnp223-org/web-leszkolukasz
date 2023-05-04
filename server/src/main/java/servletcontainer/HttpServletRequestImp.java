package servletcontainer.api;

import servletcontainer.ServletManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {
    final Socket client;
    final ServletManager servletManager;
    HttpServletResponse httpServletResponse;
    final private String method;
    private String url;
    Map<String, String> headers;
    Map<String, String> parameters;
    Map<String, String> queryParameters;
    private boolean isAsync = false;

    public HttpServletRequest(Socket client, ServletManager servletManager) throws IOException  {
        this.client = client;
        this.servletManager = servletManager;
        var in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String line = in.readLine();
        method = line.split(" ")[0];
        setUrl(line.split(" ")[1]);

        headers = new HashMap<>();
        parameters = new HashMap<>();
        queryParameters = new HashMap<>();

        int questionMarkIndex = url.indexOf('?');
        if (questionMarkIndex != -1) {
            String query = url.substring(questionMarkIndex + 1);
            String[] queryParamsArray = query.split("&");
            for (String queryParam : queryParamsArray) {
                String[] keyValue = queryParam.split("=");
                String key = keyValue[0];
                String value = keyValue.length > 1 ? keyValue[1] : "";
                queryParameters.put(key, value);
            }
        }

        while((line = in.readLine()).length() != 0) {
            headers.put(line.split(": ")[0], line.split(": ")[1]);
        }

        StringBuilder payload = new StringBuilder();
        while(in.ready()){
            payload.append((char) in.read());
        }

        String payloadStr = payload.toString();
        if (payloadStr.equals(""))
            return;

        String[] pairs = payloadStr.split("&");
        for (String pair: pairs) {
            parameters.put(pair.split("=")[0], pair.split("=")[1]);
        }
    }

    public HttpServletResponse createHttpServletResponse() throws IOException {
        if (httpServletResponse != null)
            return httpServletResponse;

        httpServletResponse = new HttpServletResponse(client);
        return httpServletResponse;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String otherUrl) {
        url = otherUrl;
        if (!url.endsWith("/"))
            url = url + "/";
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getQueryParameter(String name) {
        return queryParameters.get(name);
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public RequestDispatcher getRequestDispatcher(String url) {
        return new RequestDispatcher(servletManager.getServletWrapper(url));
    }
    
    public boolean isAsync() {
        return isAsync;
    }

    public void complete() throws IOException {
        httpServletResponse.close();
        client.close();
    }
}
