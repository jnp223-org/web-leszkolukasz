package library.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import servletcontainer.api.*;

@Servlet(
        url = "/hello"
)
public class Home implements HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setAttribute("year", 2023);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}