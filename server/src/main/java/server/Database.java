package server;

import java.util.List;

public interface Database {
    void executeUpdateQuery(String query);

    List<Object> executeSelectQuery(String query);
}
