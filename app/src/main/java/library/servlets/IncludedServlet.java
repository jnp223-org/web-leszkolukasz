package library.servlets;

import servletcontainer.api.HttpServlet;
import servletcontainer.api.HttpServletRequest;
import servletcontainer.api.HttpServletResponse;
import servletcontainer.api.Servlet;

import java.io.IOException;

@Servlet(url="/included")

public class IncludedServlet implements HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var out = resp.getOutputStream();
        out.println("Hello from included servlet");
    }
}
