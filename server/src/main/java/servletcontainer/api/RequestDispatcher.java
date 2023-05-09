package servletcontainer.api;

import java.io.IOException;

public interface RequestDispatcher {
    void forward(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void include(HttpServletRequest request, HttpServletResponse response)  throws IOException;
}
