package servletcontainer;

import servletcontainer.api.*;

import java.util.ArrayList;
import java.util.List;

public class AsyncContextImp implements AsyncContext {
    final private HttpServletRequest request;
    final private HttpServletResponse response;
    final private List<AsyncListener> listeners;
    private long timeout;

    public AsyncContextImp(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.listeners = new ArrayList<>();
        this.timeout = 30000;
    }

    @Override
    public void addListener(AsyncListener listener) {
        listeners.add(listener);
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public void start(Runnable run) {
        //if (timeout > 0)
            // CompleatableFuture future = new CompleteableFuture(() -> {run.start()}).orTimeout(...).handle(...)
        //else
        //CompleatableFuture future = new CompleteableFuture(() -> {run.start()}).handle(...)
    }

    @Override
    public void complete() {
        // TODO: dodaj addListener z parametrami i wtedy trzeba tu poprawic
        listeners.forEach(listener -> listener.onComplete(new AsyncEventImp(this)));
        try {
            request.complete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
