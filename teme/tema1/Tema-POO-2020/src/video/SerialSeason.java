package video;

import entertainment.Season;

import java.util.ArrayList;

public class SerialSeason extends Video{
    private final int no;
    private int duration;
    private double rating;
    private final ArrayList<Season> seasons;

    public SerialSeason(final String title, final int year, final ArrayList<String> genres, final ArrayList<String> cast, final int no, final ArrayList<Season> seasons) {
        super(title, year, genres, cast);
        this.no = no;
        this.seasons = seasons;
        this.duration = 0;
        this.rating = 0;
    }

    public void setRating(double grade) {
        this.rating += grade;
        this.rating /= 2;
    }

    public int getDuration() {
        return this.duration;
    }

    public double getRating() {
        return this.rating;
    }

    public int getNo() {
        return this.no;
    }

    public ArrayList<Season> getSeasons() {
        return this.seasons;
    }
}
