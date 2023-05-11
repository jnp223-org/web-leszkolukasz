package servletcontainer;

import servletcontainer.api.HttpServlet;

import java.lang.reflect.InvocationTargetException;

public class ServletWrapper {
    final private Class<?> cls;
    final private String url;
    private HttpServlet instance;

    public ServletWrapper(Class cls, String url) {
        this.cls = cls;
        this.url = url;
        this.instance = null;
    }

    public int matches(String otherUrl) {
        if (otherUrl.startsWith(url)) {
            return url.length();
        } else {
            return 0;
        }
    }

    public int matchesRelative(String otherUrl) {
        if (otherUrl.startsWith(getRelativeUrl())) {
            return getRelativeUrl().length();
        } else {
            return 0;
        }
    }

    // e.g. /app/home -> /home
    public String getRelativeUrl() {
        int secondSlash = url.indexOf("/", 1);

        if(secondSlash == -1)
            return "";

        return url.substring(secondSlash);
    }

    public HttpServlet getServlet() {
        if (instance == null) {
            try {
                this.instance = (HttpServlet) cls.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public String getUrl() {
        return url;
    }
}