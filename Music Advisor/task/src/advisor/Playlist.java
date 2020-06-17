package advisor;

import com.google.gson.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class Playlist {
    private static final String dstn = "/v1/browse/categories";
    public SpotifyCollections<SpotifyModel> collections = new SpotifyCollections<>();

    public void get(String host, String token, String categoryName) throws InterruptedException, ExecutionException {
        CompletableFuture<String> response = Controller.getResponse(token, host + dstn);
        String query = response.get();
        JsonObject jo = JsonParser.parseString(query).getAsJsonObject();
        if (jo.has("error")) {
            System.out.println(jo.get("error").getAsJsonObject().get("message").getAsString());
        } else {
            jo = jo.get("categories").getAsJsonObject();
            JsonArray ja = jo.get("items").getAsJsonArray();
            String categoryId = null;
            for (JsonElement je : ja) {
                jo = je.getAsJsonObject();
                if (jo.get("name").getAsString().equals(categoryName)) {
                    categoryId = jo.get("id").getAsString();
                    break;
                }
            }

            if (categoryId == null) {
                System.out.println("Unknown category name");
            } else {
                StringBuilder strBuilder = new StringBuilder("");
                strBuilder.append(host);
                strBuilder.append(dstn);
                strBuilder.append("/");
                strBuilder.append(categoryId);
                strBuilder.append("/");
                strBuilder.append("playlists");
                response = Controller.getResponse(token, strBuilder.toString());
                query = response.get();
                jo = JsonParser.parseString(query).getAsJsonObject();
                if (jo.has("error")) {
                    System.out.println(jo.get("error").getAsJsonObject().get("message").getAsString());
                } else {
                    jo = jo.get("playlists").getAsJsonObject();
                    ja = jo.getAsJsonArray("items");
                    for (JsonElement je : ja) {
                        jo = je.getAsJsonObject();
                        collections.add(new PlaylistModel(jo.get("name").getAsString(),
                                jo.get("external_urls").getAsJsonObject().get("spotify").getAsString()));
                    }
                }
            }
        }
    }

    Playlist() {}
}
