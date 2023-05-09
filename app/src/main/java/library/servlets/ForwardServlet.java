package library.servlets;

import servletcontainer.api.*;

import java.io.IOException;

@Servlet(url = "/forward")
public class ForwardServlet implements HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var out = resp.getOutputStream();
        out.println("Hello from forward sevlet");
        req.setAttribute("test", 10);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/forwarded");
        dispatcher.forward(req, resp);
        out.println("This won't appear");
    }
}
