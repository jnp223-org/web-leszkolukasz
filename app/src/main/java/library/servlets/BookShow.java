package library;

import servletcontainer.api.*;

import java.io.IOException;

@Servlet(url="/*")
public class BookShow implements HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setStatus("200 OK");
            resp.setHeader("Content-Type", "text/html; charset=utf-8");
            var out = resp.getOutputStream();
            out.println("Hello world");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
