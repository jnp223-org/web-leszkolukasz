package servletcontainer.api;

import java.io.IOException;

public interface HttpServletRequest {
    HttpServletResponse createHttpServletResponse() throws IOException;
    String getMethod();
    String getUrl();
    void setUrl(String otherUrl);
    void setAttribute(String name, Object value);
    Object getAttribute(String name);
    String getHeader(String name);
    String getQueryParameter(String name);
    String getParameter(String name);
    RequestDispatcher getRequestDispatcher(String url);
    boolean isAsyncStarted();
    AsyncContext getAsyncContext();
    AsyncContext startAsync();
    AsyncContext startAsync(HttpServletRequest request, HttpServletResponse response);
    void complete() throws IOException;
}
