package servletcontainer;

import servletcontainer.api.AsyncContext;
import servletcontainer.api.AsyncEvent;
import servletcontainer.api.HttpServletRequest;
import servletcontainer.api.HttpServletResponse;

public class AsyncEventImp implements AsyncEvent {
    final private AsyncContext context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Throwable throwable;

    public AsyncEventImp(AsyncContext context) {
        this.context = context;
    }

    public AsyncEventImp(AsyncContext context, HttpServletRequest request, HttpServletResponse response) {
        this(context);
        this.request = request;
        this.response = response;
    }

    public AsyncEventImp(AsyncContext context, HttpServletRequest request, HttpServletResponse response, Throwable throwable) {
        this(context, request, response);
        this.throwable = throwable;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return context;
    }

    @Override
    public HttpServletRequest getSuppliedRequest() {
        return request;
    }

    @Override
    public HttpServletResponse getSuppliedResponse() {
        return response;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }
}
