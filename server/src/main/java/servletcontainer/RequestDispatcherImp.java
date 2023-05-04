package servletcontainer.api;

import java.io.IOException;

public class RequestDispatcher {
    final private ServletWrapper servletWrapper;

    public RequestDispatcher(ServletWrapper servletWrapper) {
        this.servletWrapper = servletWrapper;
    }

    public void forward(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setUrl(servletWrapper.getUrl());
        this.servletWrapper.getServlet().service(request, response);
    }

    public void include(HttpServletRequest request, HttpServletResponse response) {
        response.setHeaderLock(true);
        servletWrapper.getServlet().dispatchRequest(request, response);
        response.setHeaderLock(false);
    }
}
