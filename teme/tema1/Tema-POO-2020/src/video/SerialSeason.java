package video;

import java.util.ArrayList;

public class SerialSeason extends Video{
    private final int no;
    private double rating;
    private ArrayList<SeasonModel> seasons;

    public SerialSeason(final String title, final int year, final ArrayList<String> genres, final ArrayList<String> cast, final int no, final ArrayList<SeasonModel> seas) {
        super(title, year, genres, cast);
        this.no = no;
        this.seasons = seas;
        this.rating = 0;
    }

    public void setRating(double grade) {
        this.rating += grade;
        this.rating /= 2;
    }

    public double getRating() {
        return this.rating;
    }

    public int getNo() {
        return this.no;
    }

    public ArrayList<SeasonModel> getSeasons() {
        return this.seasons;
    }
}
