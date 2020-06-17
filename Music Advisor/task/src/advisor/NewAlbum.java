package advisor;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NewAlbum {
    private static final String dstn = "/v1/browse/new-releases";
    public SpotifyCollections<SpotifyModel> collections = new SpotifyCollections<>();

    public void get(String host, String token) throws InterruptedException, ExecutionException {
        CompletableFuture<String> response = Controller.getResponse(token, host + dstn);
        String query = response.get();
        JsonObject jo = JsonParser.parseString(query).getAsJsonObject();
        if (jo.has("error")) {
            System.out.println(jo.get("error").getAsJsonObject().get("message").getAsString());
        } else {
            jo = jo.get("albums").getAsJsonObject();
            JsonArray ja = jo.getAsJsonArray("items");
            for (JsonElement je : ja) {
                jo = je.getAsJsonObject();
                List<String> listArtist = new ArrayList<>();
                for (JsonElement je1 : jo.get("artists").getAsJsonArray()) {
                    listArtist.add(je1.getAsJsonObject().get("name").getAsString());
                }
                collections.add(new NewAlbumModel(jo.get("name").getAsString(), listArtist,
                        jo.get("external_urls").getAsJsonObject().get("spotify").getAsString()));
            }
        }
    }

    NewAlbum() {}
}
