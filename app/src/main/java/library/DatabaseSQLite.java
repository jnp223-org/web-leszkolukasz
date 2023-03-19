package library;

import library.orm.Book;
import server.Database;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseSQLite implements Database {
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

    public List<Object> executeSelectQuery(String query) {
        List<Object> result = new ArrayList<>();

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
