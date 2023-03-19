package server.routes;

import java.util.Map;

public class RouteMatcher {
    private String url;
    private AbstractPage page;

    public RouteMatcher(String url, AbstractPage page) {
        this.url = url;
        this.page = page;
    }

    public boolean matches(String requestURL, boolean isPostRequest) {
        if (!requestURL.startsWith(url))
            return false;

        if (!isPostRequest && !(page instanceof GetPage))
            return false;

        else if (isPostRequest && !(page instanceof PostPage))
            return false;

        return true;
    }

    public String getGETResponse(String url) {
        if (page instanceof GetPage p)
            return p.getGETResponse(url);
        return "";
    }

    public String getPOSTResponse(String url, Map<String, String> params) {
        if (page instanceof PostPage p)
            return p.getPOSTResponse(url, params);
        return "";
    }
}
