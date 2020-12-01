package video;

import java.util.ArrayList;
import java.util.List;

public class SeasonModel {
    private final int currentSeason;
    private int duration;
    private List<Double> ratings;
    private List<String> userName;

    public SeasonModel(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
        this.userName = new ArrayList<>();
    }

    /**
     * Metoda retine in liste ratingul si numele utilizatorului
     * @param grade - ratingul dat
     * @param name - numele utilizatorului
     */
    public final void setRating(final double grade, final String name) {
        this.ratings.add(grade);
        this.userName.add(name);
    }

    public final List<String> getUserName() {
        return userName;
    }

    public final int getDuration() {
        return duration;
    }

    public final List<Double> getRating() {
        return ratings;
    }
}
