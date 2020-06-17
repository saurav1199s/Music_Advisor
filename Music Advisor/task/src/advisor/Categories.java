package advisor;

import com.google.gson.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class Categories {
    private static final String dstn = "/v1/browse/categories";
    public SpotifyCollections<SpotifyModel> collections = new SpotifyCollections<>();

    public void get(String host, String token) throws InterruptedException, ExecutionException {
        CompletableFuture<String> response = Controller.getResponse(token, host + dstn);
        String query = response.get();
        JsonObject jo = JsonParser.parseString(query).getAsJsonObject();
        if (jo.has("error")) {
            System.out.println(jo.get("message").getAsString());
        } else {
            jo = jo.get("categories").getAsJsonObject();
            JsonArray ja = jo.get("items").getAsJsonArray();
            for (JsonElement je : ja) {
                jo = je.getAsJsonObject();
                collections.add(new CategoriesModel(jo.get("name").getAsString()));
            }
        }
    }

    Categories() {}
}
