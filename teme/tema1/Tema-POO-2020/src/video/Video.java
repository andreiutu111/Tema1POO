package video;

import java.util.ArrayList;

public class Video {
    private final String title;
    private final int year;
    private final ArrayList<String> genres;
    private final ArrayList<String> cast;

    public Video(final String title, final int year, final ArrayList<String> genres, final ArrayList<String> cast) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.cast = cast;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<String> getCast() {
        return cast;
    }
}
