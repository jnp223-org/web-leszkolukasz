package servletcontainer;

import servletcontainer.api.HttpServletRequest;
import servletcontainer.api.HttpServletResponse;
import servletcontainer.api.RequestDispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequestImp implements HttpServletRequest {
    final Socket client;
    final ServletManager servletManager;
    HttpServletResponse httpServletResponse;
    final private String method;
    private String url;
    Map<String, String> headers;
    Map<String, Object> attributes;
    Map<String, String> parameters;
    Map<String, String> queryParameters;
    private boolean isAsync = false;

    public HttpServletRequestImp(Socket client, ServletManager servletManager) throws IOException  {
        this.client = client;
        this.servletManager = servletManager;
        var in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String line = in.readLine();
        method = line.split(" ")[0];
        setUrl(line.split(" ")[1]);

        attributes = new HashMap<>();
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

    @Override
    public HttpServletResponse createHttpServletResponse() throws IOException {
        if (httpServletResponse != null)
            return httpServletResponse;

        httpServletResponse = new HttpServletResponseImp(client);
        return httpServletResponse;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String otherUrl) {
        url = otherUrl;
        if (!url.endsWith("/"))
            url = url + "/";
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String getQueryParameter(String name) {
        return queryParameters.get(name);
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String url) {
        return new RequestDispatcherImp(servletManager.getServletWrapper(url));
    }

    @Override
    public boolean isAsync() {
        return isAsync;
    }

    @Override
    public void complete() throws IOException {
        httpServletResponse.close();
        client.close();
    }
}
