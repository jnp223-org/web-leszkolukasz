package servletcontainer.api;

public interface AsyncContext {
    void addListener(AsyncListener listener);
    HttpServletRequest getRequest();
    HttpServletResponse getResponse();
    long getTimeout();
    void setTimeout(long timeout);
    void start(Runnable run);
    void complete();
}
