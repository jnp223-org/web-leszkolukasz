package library.servlets;

import library.Database;
import library.orm.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/books/show")
public class BookShow extends HttpServlet {
    private Database db;
    public BookShow() {
        db = new Database();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<Book> books = db.getAll();
        request.setAttribute("books", books);
        request.getRequestDispatcher("/bookShow.jsp").forward(request, response);
    }
}
