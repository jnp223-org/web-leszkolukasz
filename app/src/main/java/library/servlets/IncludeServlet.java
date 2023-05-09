package library.servlets;

import servletcontainer.api.*;

import java.io.IOException;

@Servlet(url="/include")
public class IncludeServlet implements HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var out = resp.getOutputStream();
        out.println("Hello from include sevlet");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/included");
        dispatcher.include(req, resp);
        out.println("This will appear");
    }
}
