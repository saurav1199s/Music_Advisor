package advisor;

class PlaylistModel extends SpotifyModel {
    private String name;
    private String url;

    PlaylistModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public void print() {
        System.out.println(this.name);
        System.out.println(this.url);
    }
}
