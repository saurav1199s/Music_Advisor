package advisor;

class CategoriesModel extends SpotifyModel {
    private final String name;

    CategoriesModel(String name) {
        this.name = name;
    }

    public void print() {
        System.out.print(this.name);
    }
}
