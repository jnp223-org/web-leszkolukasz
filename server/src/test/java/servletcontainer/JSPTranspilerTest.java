package servletcontainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import servletcontainer.api.HttpServlet;
import servletcontainer.api.HttpServletRequest;
import servletcontainer.api.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class JSPTranspilerTest {
    private ByteArrayOutputStream clientStream;

    @Mock
    private Socket socketMock;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        clientStream = new ByteArrayOutputStream();
        when(socketMock.getOutputStream()).thenReturn(clientStream);
    }

    @Test
    void transpile() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        File appDir = new File("src/test/resources/test");
        File jspFile = new File("src/test/resources/test/test.jsp");

        JSPTranspiler transpiler = new JSPTranspiler();
        transpiler.compile(appDir, jspFile, "/test/test.jsp");

        URL[] urls = {appDir.toURI().toURL()};
        ClassLoader cl = new URLClassLoader(urls);

        Class<?> servletCls = cl.loadClass("test");
        HttpServlet servlet = (HttpServlet) servletCls.getDeclaredConstructor().newInstance();

        HttpServletRequest request = mock(HttpServletRequestImp.class);
        when(request.getAttribute("year")).thenReturn(2023);
        HttpServletResponse response = new HttpServletResponseImp(socketMock);

        servlet.doGet(request, response);
        response.close();

        System.out.println(clientStream);
        String expected = "HTTP/1.1 200 Ok\n" +
                "Content-Type text/html; charset=utf-8\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "Hello world 2023\n" +
                "<br>\n" +
                "Hello world 2077\n" +
                "<br>\n" +
                "\n" +
                "\n" +
                "20\n" +
                "<br>\n" +
                "-1 <br>\n" +
                "0 <br>\n" +
                "1 <br>\n" +
                "2 <br>\n" +
                "3 <br>\n" +
                "3\n" +
                "</body>\n" +
                "</html>";

        assertEquals(expected, clientStream.toString());
    }
}