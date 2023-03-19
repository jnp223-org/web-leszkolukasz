package library.routes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import library.DatabaseSQLite;
import library.orm.Book;
import server.Database;
import server.routes.AbstractPage;
import server.routes.GetPage;
import server.routes.PostPage;

public class BookPage extends AbstractPage implements GetPage, PostPage {

    public BookPage(Database db) {
        super(db);
    }

    public String getGETResponse(String url) {
        String addBookPattern = "\\/books\\/add.*";
        String deleteBookPattern = "\\/books\\/delete.*";
        String updateBookPattern = "\\/books\\/update.*";

        if (Pattern.matches(addBookPattern, url))
            return addBook(url);
        else if (Pattern.matches(updateBookPattern, url))
            return updateBook(url);
        else if (Pattern.matches(deleteBookPattern, url))
            return deleteBook(url);

        return showBooks(url);
    }

    public String getPOSTResponse(String url, Map<String, String> params) {
        String addBookPattern = "\\/books\\/add.*";
        String deleteBookPattern = "\\/books\\/delete.*";
        String updateBookPattern = "\\/books\\/update.*";

        if (Pattern.matches(addBookPattern, url))
            return addBookPOST(url, params);
        else if (Pattern.matches(updateBookPattern, url))
            return updateBookPOST(url, params);
        else if (Pattern.matches(deleteBookPattern, url))
            return deleteBookPOST(url, params);

        return showBooks(url);
    }

    private String showBooks(String url) {
        List<Object> books = db.executeSelectQuery("SELECT id, name, author FROM Books;");
        StringBuilder str = new StringBuilder();
        str.append("<table><tr><th>ID</th><th>Name</th><th>Author</th></tr>");

        for (Object bookObj : books) {
            Book book = (Book)bookObj;
            str.append("<tr><td>");
            str.append(book.id());
            str.append("</td><td>");
            str.append(book.name());
            str.append("</td><td>");
            str.append(book.author());
            str.append("</td></tr>");
        }

        str.append("</table>");

        return injectIntoHTML(str.toString());
    }

    private String addBook(String url) {
        String form = """
                <form action="/books/add" method="post">
                  <label for="name">Name:</label><br>
                  <input type="text" id="name" name="name" required><br>
                  <label for="author">Author:</label><br>
                  <input type="text" id="author" name="author" required><br><br>
                  <input type="submit" value="Submit">
                </form>
                """;

        return injectIntoHTML(form);
    }

    private String addBookPOST(String url, Map<String, String> params) {
        db.executeUpdateQuery("INSERT INTO Books VALUES (NULL, \"" + params.get("name") + "\", \"" + params.get("author") + "\");");
        return "Added";
    }

    private String updateBook(String url) {
        Pattern updateQuery = Pattern.compile(".*\\/(\\d+).*");
        Matcher updateMatcher = updateQuery.matcher(url);

        if (!updateMatcher.matches())
            return "Wrong URL";

        String id = updateMatcher.group(1);
        List<Object> books = db.executeSelectQuery("SELECT id, name, author FROM Books WHERE id=\"" + id + "\";");

        if (books.size() == 0) {
            return "No book in database";
        }

        Book book = (Book)books.get(0);

        String form = String.format("""
                <form action="/books/update/%s/" method="post">
                  <label for="name">Name:</label><br>
                  <input type="text" id="name" name="name" value="%s" required><br>
                  <label for="author">Author:</label><br>
                  <input type="text" id="author" name="author" value="%s" required><br><br>
                  <input type="submit" value="Update">
                </form>
                """, book.id(), book.name(), book.author());

        return injectIntoHTML(form);
    }

    private String updateBookPOST(String url, Map<String, String> params) {
        Pattern updateQuery = Pattern.compile(".*\\/(\\d+).*");
        Matcher updateMatcher = updateQuery.matcher(url);

        if (!updateMatcher.matches())
            return "Incorrect URL";

        String id = updateMatcher.group(1);
        db.executeUpdateQuery("UPDATE Books SET name = \"" + params.get("name") + "\", author = \"" + params.get("author") + "\" WHERE id = " + id + ";");
        return "Updated";
    }

    private String deleteBook(String url) {
        String form = """
                <form action="/books/delete" method="post">
                  <label for="id">ID:</label><br>
                  <input type="text" id="id" name="id" required><br><br>
                  <input type="submit" value="Submit">
                </form>
                """;

        return injectIntoHTML(form);
    }

    private String deleteBookPOST(String url, Map<String, String> params) {
        db.executeUpdateQuery("DELETE FROM Books WHERE id = " + params.get("id") + ";");
        return "Deleted";
    }
}