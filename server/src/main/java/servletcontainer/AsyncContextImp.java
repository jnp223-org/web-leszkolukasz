package servletcontainer;

import servletcontainer.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncContextImp implements AsyncContext {
    private HttpServletRequest request;
    private HttpServletResponse response;
    final private List<AsyncListenerWrapper> listeners;
    private long timeout;

    public AsyncContextImp(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.listeners = new ArrayList<>();
        this.timeout = 30000;
    }

    @Override
    public void addListener(AsyncListener listener) {
        listeners.add(new AsyncListenerWrapper(listener));
    }

    @Override
    public void addListener(AsyncListener listener, HttpServletRequest request, HttpServletResponse response) {
        listeners.add(new AsyncListenerWrapper(listener, request, response));
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) { this.request = request; };

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) { this.response = response; };

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
        if (timeout > 0) {
            CompletableFuture.runAsync(run)
                    .orTimeout(timeout, TimeUnit.MILLISECONDS)
                    .handle((result, throwable) -> {
                        if (throwable != null) {
                            if (throwable instanceof TimeoutException) {
                                listeners.forEach(AsyncListenerWrapper::notifyOnTimeout);

                                try {
                                    response.getOutputStream().println("ASYNC TIMEOUT");
//                                  request.getRequestDispatcher("/timeout").include(request, response);
                                    complete();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else
                                listeners.forEach(listener -> listener.notifyOnError(throwable));
                        }
                        return null;
                    });
        } else {
            CompletableFuture.runAsync(run)
                    .handle((result, throwable) -> {
                        if (throwable != null)
                            listeners.forEach(listener -> listener.notifyOnError(throwable));
                        return null;
                    });
        }
    }

    public void notifyAndClearListenersOnStart() {
        listeners.forEach(AsyncListenerWrapper::notifyOnStartAsync);
        listeners.clear();
    }

    @Override
    public void complete() {
        listeners.forEach(AsyncListenerWrapper::notifyOnComplete);
        try {
            request.complete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AsyncListenerWrapper {
        private HttpServletRequest request;
        private HttpServletResponse response;
        private AsyncListener listener;

        public AsyncListenerWrapper(AsyncListener listener, HttpServletRequest request, HttpServletResponse response) {
            this.listener = listener;
            this.request = request;
            this.response = response;
        }

        public AsyncListenerWrapper(AsyncListener listener) {
            this(listener, null, null);
        }

        public void notifyOnStartAsync() {
            listener.onStartAsync(new AsyncEventImp(AsyncContextImp.this, request, response));
        }

        public void notifyOnComplete() {
            listener.onComplete(new AsyncEventImp(AsyncContextImp.this, request, response));
        }

        public void notifyOnTimeout() {
            listener.onTimeout(new AsyncEventImp(AsyncContextImp.this, request, response));
        }

        public void notifyOnError(Throwable err) {
            listener.onError(new AsyncEventImp(AsyncContextImp.this, request, response, err));
        }
    }
}
