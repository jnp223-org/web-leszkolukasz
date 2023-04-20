package library;

import servletcontainer.api.*;

@Servlet(url="/*")
public class BookShow implements HttpServlet {

    @Override
    public void doGet() {
        System.out.println("Hello from BookShow");
    }
}
