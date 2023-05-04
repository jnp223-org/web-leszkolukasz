package library;

import library.orm.Book;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static int id = 0;
    private static List<Book> books;

    static {
        books = new ArrayList<>();
        books.add(new Book(1, "Krzyzacy", "Henryk Sienkiewicz"));
    }

    public List<Book> getAll() {
        return books;
    }

    public void add(String name, String author) {
        books.add(new Book(id++, name, author));
    }

    public void delete(int id) {
        int idx = -1;
        for(int i = 0; i < books.size(); i++)
            if (books.get(i).id() == id) {
                idx = id;
                break;
            }

        if (idx != -1)
            books.remove(idx);
    }

    public Book getById(int id) {
        for(int i = 0; i < books.size(); i++)
            if (books.get(i).id() == id) {
                return books.get(i);
            }
        return null;
    }

    public void update(int id, String name, String author) {
        for(int i = 0; i < books.size(); i++)
            if (books.get(i).id() == id) {
                books.set(i, new Book(id, name, author));
            }
    }
}