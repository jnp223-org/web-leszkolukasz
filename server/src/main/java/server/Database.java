package server;

import library.orm.Book;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final static String URL = "jdbc:sqlite:./app/src/main/resources/db.sqlite";
    private final static String DRIVER = "org.sqlite.JDBC";

    public void executeUpdateQuery(String query) {
        try {
            Class.forName(DRIVER);
            var connection = DriverManager.getConnection(URL);
            var statement = connection.createStatement();
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Book> executeSelectQuery(String query) {
        List<Book> result = new ArrayList<>();

        try {
            Class.forName(DRIVER);
            var connection = DriverManager.getConnection(URL);
            var statement = connection.createStatement();
            var rows = statement.executeQuery(query);

            while (rows.next()) {
                result.add(new Book(rows.getInt("id"), rows.getString("name"), rows.getString("author")));
            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
