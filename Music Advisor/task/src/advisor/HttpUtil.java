package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


class HttpUtil {
    private static final String clientID = "5c5ee7cbe3104f7ab3ae0d2cab0df7fd";
    private static final String clientSecret = "04e20314219c481483cbb5f9c42ef38c";
    private static final String REDIRECT_URI = "http://localhost:8000";

    private static String accessURI = "https://accounts.spotify.com";
    private static String resourceURI = "https://api.spotify.com";

    private static HttpServer server;
    private static String code;
    private static boolean authenticated = false;
    private static String accessToken;

    private HttpUtil() {}

    public static void start() throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(8000), 0);

        server.start();

        server.createContext("/", new HttpHandler() {
            private final String str1 = "Got the code. Return back to your program.";
            private final String str2 = "Not found authorization code. Try again.";

            @Override
            public void handle(HttpExchange exchange) throws IOException {

                String query = exchange.getRequestURI().getQuery();
                if (query == null || query.equals("") || query.startsWith("error")) {
                    exchange.sendResponseHeaders(401, str2.length());
                    exchange.getResponseBody().write(str2.getBytes());
                } else {
                    code = query;
                    System.out.println("code received");
                    exchange.sendResponseHeaders(200, str1.length());
                    exchange.getResponseBody().write(str1.getBytes());
                }
                exchange.close();
            }
        });
    }

    public static void stop() {
        server.stop(2);
    }

    public static void fetchAccessToken() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();

        System.out.println("Making http request for access_token...");
        StringBuilder postBody = new StringBuilder("");
        postBody.append("client_id=")
                .append(clientID)
                .append("&client_secret=")
                .append(clientSecret)
                .append("&grant_type=authorization_code&")
                .append(code)
                .append("&redirect_uri=")
                .append(REDIRECT_URI);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(accessURI + "/api/token"))
                .header("Content-type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(postBody.toString())).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        JsonObject jo = JsonParser.parseString(responseBody).getAsJsonObject();
        if (jo.has("error")) {
            System.out.println(responseBody);
        } else {
            authenticated = true;
            accessToken = jo.get("access_token").getAsString();
        }
    }

    public static boolean isAuthenticated() {
        return authenticated;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getAccessURI() {
        return accessURI;
    }

    public static void setAccessURI(String uri) {
        accessURI = uri;
    }

    public static String getResourceURI() {
        return resourceURI;
    }

    public static void setResourceURI(String uri) {
        resourceURI = uri;
    }
}
