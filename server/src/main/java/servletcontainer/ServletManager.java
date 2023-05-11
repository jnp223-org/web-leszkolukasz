package servletcontainer;

import servletcontainer.api.HttpServlet;
import servletcontainer.routes.DefaultServlet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ServletManager {

    final private List<ServletWrapper> servlets;

    public ServletManager() {
        this.servlets = new ArrayList<>();
        addServlet(DefaultServlet.class, "/");
    }

    public synchronized void addServlet(Class<?> cls, String url) {
        for (int i = 0; i < servlets.size(); i++) {
            if (servlets.get(i).getUrl().equals(url)) {
                servlets.remove(i);
                break;
            }
        }

        servlets.add(new ServletWrapper(cls, url));
    }

    public synchronized HttpServlet getServlet(String url) {
        var wrapper = getServletWrapper(url);
        return wrapper == null ? null : wrapper.getServlet();
    }

    public synchronized ServletWrapper getServletWrapper(String url) {
        ServletWrapper bestMatch = servlets.stream()
                .max(Comparator.comparingInt(wrapper -> wrapper.matches(url)))
                .orElse(null);

        if (bestMatch.matches(url) == 0)
            return null;

        return bestMatch;
    }

    public synchronized ServletWrapper getServletWrapperWithRelativeURL(String url) {
        ServletWrapper bestMatch = servlets.stream()
                .max(Comparator.comparingInt(wrapper -> wrapper.matchesRelative(url)))
                .orElse(null);

        if (bestMatch.matchesRelative(url) == 0)
            return null;

        return bestMatch;
    }
}
