package servletcontainer.routes;

import servletcontainer.api.HttpServlet;
import servletcontainer.api.HttpServletRequest;
import servletcontainer.api.HttpServletResponse;
import servletcontainer.api.HttpStatus;

import java.io.IOException;

public class TimeoutServlet implements HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpStatus.REQUEST_TIMEOUT);
        resp.resetBuffer();
        resp.getOutputStream().println("TIMEOUT FOR REQUEST");
    }
}
