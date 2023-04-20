package servletcontainer;

public class Main {
    public static void main(String... args) {
        ServletContainer servletContainer = new ServletContainer();
        servletContainer.servletScan();
    }
}
