package library.servlets;

import library.orm.Book;
import servletcontainer.api.*;
import library.Database;

import java.io.IOException;
import java.util.List;

@Servlet(url="/")
public class BookShow implements HttpServlet {
    private Database db;

    public BookShow() {
        db = new Database();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var out = resp.getOutputStream();

            List<Book> books = db.getAll();
            out.println("<table><tr><th>ID</th><th>Name</th><th>Author</th></tr>");

            for (Book book  : books) {
                out.println("<tr><td>");
                out.println(book.id());
                out.println("</td><td>");
                out.println(book.name());
                out.println("</td><td>");
                out.println(book.author());
                out.println("</td></tr>");
            }

            out.println("</table>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
