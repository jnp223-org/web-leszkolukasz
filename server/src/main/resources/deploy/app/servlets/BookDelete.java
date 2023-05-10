package library.servlets;

import library.Database;

import servletcontainer.api.*;
import java.io.IOException;

@Servlet(url = "/books/delete")
public class BookDelete implements HttpServlet {
    private Database db;

    public BookDelete() {
        db = new Database();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.getRequestDispatcher("/bookDelete.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        db.delete(id);

//        response.sendRedirect("/app/books/show");
    }
}