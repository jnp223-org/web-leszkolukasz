package library.routes;

import library.DatabaseSQLite;
import server.Database;
import server.routes.AbstractPage;
import server.routes.GetPage;

public class MainPage extends AbstractPage implements GetPage {

    public MainPage(Database db) {
        super(db);
    }
    @Override
    public String getGETResponse(String url) {
        return "Hello world";
    }
}
