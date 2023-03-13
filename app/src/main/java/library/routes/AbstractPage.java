package library.routes;

import library.Database;

import java.util.Map;

public abstract class AbstractPage {
    protected Database db;

    protected AbstractPage() {
        db = new Database();
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

    public String getGETResponse(String url) {
        return "";
    };
    public String getPOSTResponse(String url, Map<String, String> params) {
        return "";
    };
}
