package server;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.routes.AbstractPage;
import server.routes.GetPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.*;
import static com.codeborne.selenide.Selenide.*;
import static org.mockito.Mockito.*;


class ServerTest {

    Server server;
    Database db;
    static int port = 1234;

    public ServerTest() {
        db = mock(Database.class);
    }

    void setupServer(int add) {
        port += add;
        System.setProperty("selenide.baseUrl", "http://localhost:"+port);
        server = new Server(port, 2);
    }

    Thread startServer() {
        Thread s = new Thread(() -> {
            assertDoesNotThrow(() -> {server.start();});
        });
        s.start();

        sleep(3000);
        return s;
    }
    @Test
    void testThreads() {
        class Page extends AbstractPage implements GetPage {
            protected Page(Database db) {
                super(db);
            }

            @Override
            public String getGETResponse(String url) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return "";
            }
        }

        setupServer(0);
        server.addRoute("/", new Page(db));
        startServer();

        List<Long> waitTimes = Collections.synchronizedList(new ArrayList());;

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            threads.add(new Thread(() -> {
                long start = System.currentTimeMillis();
                open("/");
                System.out.println(url());
                long stop = System.currentTimeMillis();
                waitTimes.add(stop-start);
            }));
        }

        threads.forEach(Thread::start);
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        waitTimes.sort(Long::compare);
        assertTrue(Math.abs(waitTimes.get(0)-waitTimes.get(1)) < waitTimes.get(0) / 50 );
        assertTrue(waitTimes.get(0) * 1.4 <= waitTimes.get(2));
    }

    @Test
    void testRouting() {
        class Page extends AbstractPage implements GetPage {
            private String html;
            protected Page(Database db, String html) {
                super(db);
                this.html = html;
            }

            @Override
            public String getGETResponse(String url) {
                return html;
            }
        }

        setupServer(1);
        server.addRoute("/", new Page(db, "home"));
        server.addRoute("/test", new Page(db, "test"));
        startServer();

        open("/test");
        System.out.println(url());
        $("body").shouldHave(text("test"));

        open("/nonexistingurl");
        $("body").shouldHave(text("home"));
    }
}
