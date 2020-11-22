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

    public void setRating(double grade, String name) {
        this.ratings.add(grade);
        this.userName.add(name);
    }

    public List<String> getUserName() { return userName; }

    public int getCurrentSeason() {
        return currentSeason;
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRating() {
        return ratings;
    }
}
