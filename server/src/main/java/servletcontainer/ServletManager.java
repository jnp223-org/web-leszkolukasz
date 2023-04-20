package servletcontainer;

import servletcontainer.api.HttpServlet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServletManager {

    final private List<ServletWrapper> servlets;

    public ServletManager() {
        this.servlets = new ArrayList<>();
    }

    public synchronized void addServlet(Class<?> cls, String url) {
        servlets.add(new ServletWrapper(cls, url));
    }

    public synchronized HttpServlet getServlet(String url) {
        ServletWrapper bestMatch = servlets.stream()
                .max(Comparator.comparingInt(wrapper -> wrapper.matches(url)))
                .orElse(null);

        if (bestMatch != null && bestMatch.matches(url) == 0)
            bestMatch = null;

        return bestMatch.getServlet();
    }

    private class  ServletWrapper {
        final private Class<?> cls;
        final private String url;
        private HttpServlet instance;

        public ServletWrapper(Class cls, String url) {
            this.cls = cls;
            this.url = url;
            this.instance = null;
        }

        public int matches(String otherUrl) {
            Pattern pattern = Pattern.compile(url);
            Matcher matcher = pattern.matcher(otherUrl);
            if (matcher.find()) {
                return url.length();
            } else {
                return 0;
            }
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
    }
}
