package servletcontainer.api;

public interface AsyncEvent {
    AsyncContext getAsyncContext();
    HttpServletRequest getSuppliedRequest();
    HttpServletResponse getSuppliedResponse();
    Throwable getThrowable();
}
