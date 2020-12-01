package video;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private final String title;
    private final int year;
    private final ArrayList<String> genres;
    private final List<String> cast;

    public Video(final String t, final int y, final ArrayList<String> g, final List<String> c) {
        this.title = t;
        this.year = y;
        this.genres = g;
        this.cast = c;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final List<String> getCast() {
        return cast;
    }
}
