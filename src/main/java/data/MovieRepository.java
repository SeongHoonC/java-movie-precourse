package data;

import model.Movie;

import java.util.HashMap;
import java.util.Map;

public class MovieRepository {

    Map<Integer, Movie> movies = initMovies();

    private Map<Integer, Movie> initMovies() {

        HashMap<Integer, Movie> movieStore = new HashMap<>();

        movieStore.put(1, new Movie(1,"귀멸의 칼날:무한성편", 142));
        movieStore.put(2, new Movie(2,"어쩔수가없다", 175));
        movieStore.put(3, new Movie(3,"모노노키히메", 152));
        movieStore.put(4, new Movie(4,"얼굴", 154));
        movieStore.put(5, new Movie(5, "너의 이름은", 142));

        return movieStore;
    }

    public Movie getMovie(int id) {
        return movies.get(id);
    }
}
