package servletcontainer.routes;

import servletcontainer.api.HttpServlet;
import servletcontainer.api.HttpServletRequest;
import servletcontainer.api.HttpServletResponse;
import servletcontainer.api.HttpStatus;

public class DefaultServlet implements HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus(HttpStatus.NOT_FOUND);
    }
}
