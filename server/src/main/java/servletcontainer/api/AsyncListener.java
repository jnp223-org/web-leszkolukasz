package servletcontainer.api;

public interface AsyncListener {
    void onStartAsync(AsyncEvent event);
    void onError(AsyncEvent event);
    void onTimeout(AsyncEvent event);
    void onComplete(AsyncEvent event);
}
