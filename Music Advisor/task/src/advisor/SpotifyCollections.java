package advisor;

import java.util.ArrayList;
import java.util.List;

class SpotifyCollections <T> {
    public static int elementsOnPage;

    private List<T> list = new ArrayList<>();
    private int noOfElem = 0;
    private int pageNo = 0;

    public void add(T item) {
        list.add(item);
    }

    public void displayNextPage() {
        if (noOfElem == list.size()) {
            System.out.println("No more pages.");
        } else {
            ++pageNo;
            for (int i = 0; i < elementsOnPage && noOfElem < list.size(); ++i, ++noOfElem) {
                ((SpotifyModel)list.get(noOfElem)).print();
                System.out.println();
            }
            System.out.printf("---PAGE %d OF %d---\n", pageNo, (list.size() % elementsOnPage == 0 ? (list.size() / elementsOnPage) : (list.size() / elementsOnPage + 1)));
        }
    }

    public void displayPreviousPage() {
        if (noOfElem <= elementsOnPage) {
            System.out.println("No more pages.");
        } else {
            pageNo -= 2;
            noOfElem = (noOfElem % elementsOnPage) == 0 ? (noOfElem - elementsOnPage) : (noOfElem / elementsOnPage) * elementsOnPage;
            noOfElem -= elementsOnPage;
            displayNextPage();
        }
    }
}
