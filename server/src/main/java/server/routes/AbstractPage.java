package library.routes;

import library.Database;

import java.util.Map;

public abstract class AbstractPage {
    protected Database db;

    protected AbstractPage(Database db) {
        this.db = db;
    }

    protected String injectIntoHTML(String body)
    {
        StringBuilder str = new StringBuilder();

        str.append("<html>");
        str.append("<body>");
        str.append(body);
        str.append("</body>");
        str.append("</html>");

        return str.toString();
    }
}
