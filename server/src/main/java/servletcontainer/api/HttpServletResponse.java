package servletcontainer.api;

import java.io.IOException;
import java.io.PrintWriter;

public interface HttpServletResponse {
    void setStatus(HttpStatus status);
    int getStatus();
    void setHeader(String name, String value);
    String getHeader(String name);
    PrintWriter getOutputStream() throws IOException;
    void flushBuffer();
    boolean isBufferFlushed();
    void reset();
    void setHeaderLock(boolean locked);
    void resetBuffer();
    void close();
}
