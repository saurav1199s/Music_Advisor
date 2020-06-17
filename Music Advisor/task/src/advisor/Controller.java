package advisor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

class Controller {
    private static final HttpClient client = HttpClient.newBuilder().build();

    private Controller() {

    }

    public static CompletableFuture<String> getResponse(String token, String targetURL) {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .uri(URI.create(targetURL)).build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);
    }
}
