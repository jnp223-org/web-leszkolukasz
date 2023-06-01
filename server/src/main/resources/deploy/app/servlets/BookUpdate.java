package library.servlets;

import library.Database;
import library.orm.Book;

import servletcontainer.api.*;
import java.io.IOException;

@Servlet(url = "/books/update")
public class BookUpdate implements HttpServlet {
    private Database db;
    public BookUpdate() {
        db = new Database();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getQueryParameter("id"));
        Book book = db.getById(id);
        System.out.println(book);
        request.setAttribute("book", book);
        request.getRequestDispatcher("/bookUpdate.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String author = request.getParameter("author");

        db.update(id, name, author);

//        response.sendRedirect("/app/books/show");
    }
}