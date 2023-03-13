package library.routes;

public class MainPage extends AbstractPage {
    @Override
    public String getGETResponse(String url) {
        return "hello world";
    }

}
