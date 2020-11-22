package user;

import video.Movie;
import video.SeasonModel;
import video.SerialSeason;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private final String username;
    private final String category;
    private final ArrayList<String> favorite;
    private final Map<String, Integer> viewed;

    public User(final String username, final String category, final ArrayList<String> favorite, final Map<String, Integer> viewed){
        this.username = username;
        this.category = category;
        this.favorite = favorite;
        this.viewed = viewed;
    }

    public String getUsername() {
        return this.username;
    }

    public String getCategory() {
        return this.category;
    }

    public ArrayList<String> getFavorite() {
        return this.favorite;
    }

    public Map<String, Integer> getViewed() {
        return this.viewed;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + this.username + '\'' +
                '}';
    }

    public String setFavorite(String video) {
        if (!this.viewed.isEmpty()) {
            if (this.viewed.containsKey(video)) {
                this.favorite.add(video);
                return "success -> " + video + " was added as favourite";
            }
        }
        return null;
    }

    public String setView(String video) {
        if (this.viewed.containsKey(video)) {
            this.viewed.put(video, this.viewed.get(video) + 1);
            return "success -> "+ video +" was viewed with total views of " + this.viewed.get(video);
        } else {
            this.viewed.put(video, 1);
            return "success -> "+ video +" was viewed with total views of 1";
        }
    }

    public String setRating(ArrayList<Movie> movies, ArrayList<SerialSeason> serials, String video, double grade, int seasonNumber) {
        if (this.viewed.containsKey(video)) {
            Movie movie = null;
            for (Movie v : movies) {
                if (v.getTitle().equals(video) == true) {
                    movie = v;
                    break;
                }
            }

            if (movie != null) {
                if (movie.getuserName().indexOf(this.username) == -1) {
                    movie.setRating(grade, this.username);
                    return "success -> " + video + " was rated with " + grade + " by " + this.username;
                } else {
                    return "error -> " + video + " has been already rated";
                }
            } else {
                SeasonModel ser = null;
                for (SerialSeason s : serials) {
                    if (s.getTitle().equals(video) == true) {
                        ser = s.getSeasons().get(seasonNumber - 1);
                        break;
                    }
                }

                if (ser != null) {
                    if (ser.getUserName().indexOf(this.username) == -1) {
                        ser.setRating(grade, this.username);
                        return "success -> " + video + " was rated with " + grade + " by " + this.username;
                    } else {
                        return "error -> " + video + " has been already rated";
                    }
                }
            }
        }
        return null;
    }
}
