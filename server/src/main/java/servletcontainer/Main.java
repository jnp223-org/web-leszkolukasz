package servletcontainer;

import servletcontainer.api.HttpServlet;

public class Main {
    public static void main(String... args) {
        ServletContainer servletContainer = new ServletContainer();
        servletContainer.servletScan();

        var servletManager = servletContainer.getServletManager();
        HttpServlet servlet = servletManager.getServlet("/app/");
        servlet.doGet();

        System.out.println(servlet + " " + servletManager.getServlet("/app/"));
    }
}
