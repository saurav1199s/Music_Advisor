package advisor;

import java.util.List;

class NewAlbumModel extends SpotifyModel {
    private String name;
    private List<String> artists;
    private String url;

    NewAlbumModel(String name, List<String> artists, String url) {
        this.name = name;
        this.artists = artists;
        this.url = url;
    }

    public void print() {
        System.out.println(this.name);
        System.out.println(this.artists);
        System.out.println(this.url);
    }
}
