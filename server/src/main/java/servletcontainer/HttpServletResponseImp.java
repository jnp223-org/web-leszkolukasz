package servletcontainer.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServletResponse {
    final private Socket client;
    final private StringWriter buffer;
    final private PrintWriter bufferWriter;
    final private PrintWriter clientOutputStream;
    private Map<String, String> headers;
    private HttpStatus httpStatus = HttpStatus.OK;
    private boolean bufferFlushed = false;
    private boolean headerLocked = false;

    public HttpServletResponse(Socket client) throws IOException {
        this.client = client;
        this.buffer = new StringWriter();
        this.bufferWriter = new PrintWriter(this.buffer, true);
        this.clientOutputStream = new PrintWriter(client.getOutputStream(), true);
        this.headers = new HashMap<>();
        this.headers.put("Content-Type", "text/html; charset=utf-8");
    }

    public void setStatus(HttpStatus status) {
        if (headerLocked)
            return;
        this.httpStatus = status;
    }

    public int getStatus() { return httpStatus.getValue(); };

    public void setHeader(String name, String value) {
        if (headerLocked)
            return;
        headers.put(name, value);
    }

    public String getHeader(String name) { return headers.get(name); }

    public PrintWriter getOutputStream() throws IOException {
        return bufferWriter;
    }

    private void flushHeaders() {
        if (bufferFlushed)
            return;

        clientOutputStream.println("HTTP/1.1 " + httpStatus);

        for(Map.Entry<String, String> entry: headers.entrySet()) {
            clientOutputStream.println(entry.getKey() + " " + entry.getValue());
        }

        clientOutputStream.println();
    }

    public void flushBuffer() {
        flushHeaders();
        clientOutputStream.write(buffer.getBuffer().toString());
        resetBuffer();
        bufferFlushed = true;
    }

    public void reset() {
        if (bufferFlushed)
            throw new IllegalStateException();

        httpStatus = HttpStatus.OK;
        headers.clear();
        headers.put("Content-Type", "text/html; charset=utf-8");
        resetBuffer();
    }

    public void setHeaderLock(boolean locked) {
        headerLocked = locked;
    }

    public void resetBuffer() {
        buffer.getBuffer().setLength(0);
    }

    public void close() {
        flushBuffer();
        bufferWriter.close();
        clientOutputStream.close();
    }
}
