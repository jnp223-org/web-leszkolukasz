package servletcontainer.api;

import java.io.IOException;

public interface HttpServlet {

    default void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        dispatchRequest(req, resp);
        if (!req.isAsyncStarted())
            req.complete();
    }

    default void dispatchRequest(HttpServletRequest req, HttpServletResponse resp)  throws IOException {
        switch (req.getMethod()) {
            case "GET" -> doGet(req, resp);
            case "POST" -> doPost(req, resp);
            case "PUT" -> doPut(req, resp);
            case "DELETE" -> doDelete(req, resp);
            default -> throw new RuntimeException("Unknown HTTP method");
        }
    }

    default void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        throw new UnsupportedOperationException("GET not supported");
    }

    default void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        throw new UnsupportedOperationException("POST not supported");
    }

    default void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        throw new UnsupportedOperationException("PUT not supported");
    }

    default void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        throw new UnsupportedOperationException("DELETE not supported");
    }
}
