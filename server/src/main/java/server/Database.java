package server;

import java.util.List;

public interface Database {
    public void executeUpdateQuery(String query);

    public List<Object> executeSelectQuery(String query);
}
