package library.servlets;

import servletcontainer.api.*;

import java.io.IOException;

@Servlet(url = "/forwarded")
public class ForwardedServlet implements HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var out = resp.getOutputStream();
        out.println("Hello from forwarded sevlet");
        out.println("I got " + req.getAttribute("test"));
    }
}
