package library.servlets;

import library.Database;

import servletcontainer.api.*;
import java.io.IOException;

@Servlet(url = "/books/add")
public class BookAdd implements HttpServlet {
    private Database db;
    public BookAdd() {
        db = new Database();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.getRequestDispatcher("/bookAdd.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String name = request.getParameter("name");
        String author = request.getParameter("author");

        db.add(name, author);

//        response.sendRedirect("/app/books/show");
    }
}