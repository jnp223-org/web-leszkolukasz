package servletcontainer.servlets;

import servletcontainer.api.HttpServlet;
import servletcontainer.api.HttpServletRequest;
import servletcontainer.api.HttpServletResponse;

import java.io.IOException;

public class SlowServlet implements HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
