package advisor;

import com.google.gson.*;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FeaturedPlayList {
    private static final String dstn = "/v1/browse/featured-playlists";
    public SpotifyCollections<SpotifyModel> collections = new SpotifyCollections<>();

    public void get(String host, String token) throws InterruptedException, ExecutionException {
        CompletableFuture<String> response = Controller.getResponse(token, host + dstn);
        String query = response.get();

        JsonObject jo = JsonParser.parseString(query).getAsJsonObject();
        if (jo.has("error")) {
            System.out.println(jo.get("error").getAsJsonObject().get("message").getAsString());
        } else {
            jo = jo.get("playlists").getAsJsonObject();
            JsonArray ja = jo.getAsJsonArray("items");
            for (JsonElement je : ja) {
                jo = je.getAsJsonObject();
                collections.add(new PlaylistModel(jo.get("name").getAsString(), jo.get("external_urls").getAsJsonObject().get("spotify").getAsString()));
            }
        }
    }

    FeaturedPlayList() {}
}
