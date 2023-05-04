package servletcontainer;

import servletcontainer.api.HttpServletRequest;
import servletcontainer.api.HttpServletResponse;
import servletcontainer.api.RequestDispatcher;

import java.io.IOException;

public class RequestDispatcherImp implements RequestDispatcher {
    final private ServletWrapper servletWrapper;

    public RequestDispatcherImp(ServletWrapper servletWrapper) {
        this.servletWrapper = servletWrapper;
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setUrl(servletWrapper.getUrl());
        this.servletWrapper.getServlet().service(request, response);
    }

    @Override
    public void include(HttpServletRequest request, HttpServletResponse response) {
        response.setHeaderLock(true);
        servletWrapper.getServlet().dispatchRequest(request, response);
        response.setHeaderLock(false);
    }
}
