package video;

import java.util.ArrayList;

public class Movie extends Video {
    private final int duration;
    private double rating;

    public Movie(final String title, final int year, final ArrayList<String> genres, final ArrayList<String> cast, final int duration) {
        super(title, year, genres, cast);
        this.duration = duration;
        this.rating = 0;
    }

    public void setRating(double grade) {
        this.rating += grade;
        this.rating /= 2;
    }
    public double getRating() {
        return rating;
    }
    public int getDuration() {
        return duration;
    }
}
