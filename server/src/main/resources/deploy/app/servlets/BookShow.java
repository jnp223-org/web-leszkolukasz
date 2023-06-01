package library.servlets;

import library.Database;
import library.orm.Book;

import servletcontainer.api.*;
import java.io.IOException;
import java.util.List;

@Servlet(url = "/books/show")
public class BookShow implements HttpServlet {
    private Database db;
    public BookShow() {
        db = new Database();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        List<Book> books = db.getAll();
        System.out.println(books);

        request.setAttribute("books", books);
        request.getRequestDispatcher("/bookShow.jsp").forward(request, response);
    }
}