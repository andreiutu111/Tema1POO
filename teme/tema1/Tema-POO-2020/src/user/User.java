package user;

import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
import com.fasterxml.jackson.databind.ser.impl.UnknownSerializer;
import entertainment.Season;
import video.Movie;
import video.SeasonModel;
import video.SerialSeason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class User {
    private final String username;
    private final String category;
    private final ArrayList<String> favorite;
    private final Map<String, Integer> viewed;
    private int noRating;

    public User(final String username, final String category, final ArrayList<String> favorite, final Map<String, Integer> viewed){
        this.username = username;
        this.category = category;
        this.favorite = favorite;
        this.viewed = viewed;
        this.noRating = 0;
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

    public int getNoRating() { return this.noRating; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + this.username + '\'' +
                '}';
    }

    public static ArrayList<User> sortMoviesWithValues(ArrayList<User> users, int[] values, String SortType) {
        ArrayList<User> sortedUsers = users;
        int len = sortedUsers.size();
        int aux;

        if (SortType.equals("asc")) {
            for (int i = 0 ; i < len - 1 ; i++) {
                for (int j = i + 1 ; j < len ; j++) {
                    if (values[i] > values[j]) {
                        aux = values[i];
                        values[i] = values[j];
                        values[j] = aux;
                        Collections.swap(sortedUsers, i, j);
                    } else if (values[i] == values[j]) {
                        if (sortedUsers.get(i).getUsername().compareTo(sortedUsers.get(j).getUsername()) > 0) {
                            Collections.swap(sortedUsers, i, j);
                        }
                    }
                }
            }
        } else if (SortType.equals("desc")) {
            for (int i = 0 ; i < len - 1 ; i++) {
                for (int j = i + 1 ; j < len ; j++) {
                    if (values[i] < values[j]) {
                        aux = values[i];
                        values[i] = values[j];
                        values[j] = aux;
                        Collections.swap(sortedUsers, i, j);
                    } else if (values[i] == values[j]) {
                        if (sortedUsers.get(i).getUsername().compareTo(sortedUsers.get(j).getUsername()) < 0) {
                            Collections.swap(sortedUsers, i, j);
                        }
                    }
                }
            }
        }

        return sortedUsers;
    }

    public static String getRezStr(ArrayList<User> sortedUsers) {
        StringBuilder str = new StringBuilder();
        str.append("Query result: [");

        if (sortedUsers.size() != 0) {
            for (User s : sortedUsers) {
                str.append(s.getUsername()).append(", ");
            }
            str.delete(str.length() - 2, str.length());
        }

        str.append("]");
        return str.toString();
    }

    public String setFavorite(String video) {
        if (!this.viewed.isEmpty()) {
            if (this.viewed.containsKey(video)) {
                if (this.favorite.indexOf(video) == -1) {
                    this.favorite.add(video);
                    return "success -> " + video + " was added as favourite";
                } else {
                    return "error -> " + video + " is already in favourite list";
                }
            }
        }
        return "error -> " + video + " is not seen";
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
                    this.noRating++;
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
                        this.noRating++;
                        return "success -> " + video + " was rated with " + grade + " by " + this.username;
                    } else {
                        return "error -> " + video + " has been already rated";
                    }
                }
            }
        }
        return "error -> " + video + " is not seen";
    }

    public static String getNumOfRat(int N, ArrayList<User> users, String SortType) {
        ArrayList<User> sortedUsers = new ArrayList<>();

        int[] noRatUser = new int[users.size()];
        int len = 0;

        for (User u:users) {
            if (u.getNoRating() != 0) {
                sortedUsers.add(u);
                noRatUser[len] = u.getNoRating();
                len++;
            }
        }

        sortedUsers = User.sortMoviesWithValues(sortedUsers, noRatUser, SortType);

        if (len > N) {
            sortedUsers.subList(N, len).clear();
        }

        return User.getRezStr(sortedUsers);
    }

    public String getStandard(ArrayList<Movie> movies, ArrayList<SerialSeason> serials) {
        for (Movie m:movies) {
            if (this.viewed.containsKey(m.getTitle()) == false) {
                return "StandardRecommendation result: " + m.getTitle();
            }
        }

        for (SerialSeason s:serials) {
            if (this.viewed.containsKey(s.getTitle()) == false) {
                return "StandardRecommendation result: " + s.getTitle();
            }
        }

        return "StandardRecommendation cannot be applied!";
    }

    public String getBestUnseen(ArrayList<Movie> movies, ArrayList<SerialSeason> serials) {
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        double[] movRatings = new double[movies.size()];
        int lenMov = 0;
        double aux;

        for (Movie m : movies) {
            sortedMovies.add(m);
            movRatings[lenMov] = m.getValueRating();
            lenMov++;
        }

        for (int i = 0 ; i < lenMov - 1 ; i++) {
            for (int j = i + 1 ; j < lenMov ; j++) {
                if (movRatings[i] < movRatings[j]) {
                   aux = movRatings[i];
                   movRatings[i] = movRatings[j];
                   movRatings[j] = aux;
                   Collections.swap(sortedMovies, i, j);
                }
            }
        }

        for (Movie m : sortedMovies) {
            if (this.viewed.containsKey(m.getTitle()) == false) {
                return "BestRatedUnseenRecommendation result: " + m.getTitle();
            }
        }

        ArrayList<SerialSeason> sortedSerials = new ArrayList<>();
        double[] serRatings = new double[serials.size()];
        int lenSer = 0;

        for (SerialSeason s : serials) {
            sortedSerials.add(s);
            serRatings[lenSer] = s.getValueRatingSerial();
            lenSer++;
        }

        for (int i = 0 ; i < lenSer - 1 ; i++) {
            for (int j = i + 1 ; j < lenSer ; j++) {
                if (serRatings[i] < serRatings[j]) {
                    aux = serRatings[i];
                    serRatings[i] = serRatings[j];
                    serRatings[j] = aux;
                    Collections.swap(sortedSerials, i, j);
                }
            }
        }

        for (SerialSeason s : sortedSerials) {
            if (this.viewed.containsKey(s.getTitle()) == false) {
                return "BestRatedUnseenRecommendation result: " + s.getTitle();
            }
        }

        return "BestRatedUnseenRecommendation cannot be applied!";
    }
}
