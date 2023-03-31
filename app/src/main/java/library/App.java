package library;

import library.routes.BookPage;
import library.routes.MainPage;
import server.Database;
import server.Server;

import java.io.IOException;

public class App {
    public static void main(String... args) throws IOException  {
        Server server = new Server(8000, 3);
        Database db = new DatabaseSQLite();

        server.addRoute("/", new MainPage(db));
        server.addRoute("/books", new BookPage(db));
        server.start();
    }
}
