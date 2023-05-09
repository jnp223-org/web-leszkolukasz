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
        if (response.isBufferFlushed())
            throw new IllegalStateException();

        request.setUrl(servletWrapper.getUrl());
        response.resetBuffer();
        this.servletWrapper.getServlet().service(request, response);
    }

    @Override
    public void include(HttpServletRequest request, HttpServletResponse response)  throws IOException {
        response.setHeaderLock(true);
        servletWrapper.getServlet().dispatchRequest(request, response);
        response.setHeaderLock(false);
    }
}
