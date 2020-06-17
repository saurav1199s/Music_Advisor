package advisor;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        HttpUtil.setAccessURI("https://accounts.spotify.com");
        HttpUtil.setResourceURI("https://api.spotify.com");
        SpotifyCollections.elementsOnPage = 5;

        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-access":
                    HttpUtil.setAccessURI(args[i + 1]);
                    break;
                case "-resource":
                    HttpUtil.setResourceURI(args[i + 1]);
                    break;
                case "-page":
                    SpotifyCollections.elementsOnPage = Integer.parseInt(args[i + 1]);
                    break;
            }
        }

        boolean authenticated = false;
        String accessToken = "";

        SpotifyCollections<SpotifyModel> collections = new SpotifyCollections<>();

        while (true) {
            String s = scanner.nextLine();
            if ("auth".equals(s)) {
                System.out.println("use this link to request the access code:");
                String link = "https://accounts.spotify.com/authorize?client_id=5c5ee7cbe3104f7ab3ae0d2cab0df7fd&redirect_uri=http://localhost:8000&response_type=code";
                System.out.println(link);
                System.out.println("\nwaiting for code...");

                HttpUtil.start();
                Thread.sleep(2000);
                HttpUtil.stop();
                HttpUtil.fetchAccessToken();
                Thread.sleep(2000);
                authenticated = HttpUtil.isAuthenticated();
                accessToken = HttpUtil.getAccessToken();
            }


            if (authenticated) {
                try {
                    if (s.startsWith("playlists")) {
                        Playlist playlist = new Playlist();
                        playlist.get(HttpUtil.getResourceURI(), accessToken, s.substring(9).trim());
                        collections = playlist.collections;
                        collections.displayNextPage();
                    } else {
                        switch (s) {
                            case "new":
                            {
                                NewAlbum newAlbum = new NewAlbum();
                                newAlbum.get(HttpUtil.getResourceURI(), accessToken);
                                collections = newAlbum.collections;
                                collections.displayNextPage();
                                break;
                            }
                            case "featured":
                            {
                                FeaturedPlayList featuredPlayList = new FeaturedPlayList();
                                featuredPlayList.get(HttpUtil.getResourceURI(), accessToken);
                                collections = featuredPlayList.collections;
                                collections.displayNextPage();
                                break;
                            }
                            case "categories":
                            {
                                Categories categories = new Categories();
                                categories.get(HttpUtil.getResourceURI(), accessToken);
                                collections = categories.collections;
                                collections.displayNextPage();
                                break;
                            }
                            case "next":
                            {
                                collections.displayNextPage();
                                break;
                            }
                            case "prev":
                            {
                                collections.displayPreviousPage();
                                break;
                            }
                            case "exit":
                                System.out.println("---GOODBYE!---");
                                System.exit(0);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (s.startsWith("exit")) {
                System.out.println("---GOODBYE!---");
                System.exit(0);
            } else {
                System.out.println("Please, provide access for application.");
            }
        }
    }
}