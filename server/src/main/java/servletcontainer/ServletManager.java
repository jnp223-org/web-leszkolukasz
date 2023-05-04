package servletcontainer;

import servletcontainer.api.HttpServlet;
import servletcontainer.routes.DefaultServlet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServletManager {

    final private List<ServletWrapper> servlets;

    public ServletManager() {
        this.servlets = new ArrayList<>();
        addServlet(DefaultServlet.class, "/");
    }

    public synchronized void addServlet(Class<?> cls, String url) {
        servlets.add(new ServletWrapper(cls, url));
    }

    public synchronized HttpServlet getServlet(String url) {
        return getServletWrapper(url).getServlet();
    }

    public synchronized ServletWrapper getServletWrapper(String url) {
        ServletWrapper bestMatch = servlets.stream()
                .max(Comparator.comparingInt(wrapper -> wrapper.matches(url)))
                .orElse(null);

        return bestMatch;
    }
}
