package user;

import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
import com.fasterxml.jackson.databind.ser.impl.UnknownSerializer;
import entertainment.Genre;
import entertainment.Season;
import video.Movie;
import video.SeasonModel;
import video.SerialSeason;

import java.rmi.MarshalledObject;
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
        int[] pozMov = new int[movies.size()];
        int lenMov = 0;
        int auxpoz;
        double aux;

        for (Movie m : movies) {
            sortedMovies.add(m);
            pozMov[lenMov] = lenMov;
            movRatings[lenMov] = m.getValueRating();
            lenMov++;
        }

        for (int i = 0 ; i < lenMov - 1 ; i++) {
            for (int j = i + 1 ; j < lenMov ; j++) {
                if (movRatings[i] < movRatings[j]) {
                   aux = movRatings[i];
                   movRatings[i] = movRatings[j];
                   movRatings[j] = aux;

                   auxpoz = pozMov[i];
                   pozMov[i] = pozMov[j];
                   pozMov[j] = auxpoz;

                   Collections.swap(sortedMovies, i, j);
                } else if (movRatings[i] == movRatings[j]) {
                    if (pozMov[i] > pozMov[j]) {
                        auxpoz = pozMov[i];
                        pozMov[i] = pozMov[j];
                        pozMov[j] = auxpoz;

                        Collections.swap(sortedMovies, i, j);
                    }
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
        int[] pozSerial = new int[serials.size()];
        int lenSer = 0;

        for (SerialSeason s : serials) {
            sortedSerials.add(s);
            pozSerial[lenSer] = lenSer;
            serRatings[lenSer] = s.getValueRatingSerial();
            lenSer++;
        }

        for (int i = 0 ; i < lenSer - 1 ; i++) {
            for (int j = i + 1 ; j < lenSer ; j++) {
                if (serRatings[i] < serRatings[j]) {
                    aux = serRatings[i];
                    serRatings[i] = serRatings[j];
                    serRatings[j] = aux;

                    auxpoz = pozSerial[i];
                    pozSerial[i] = pozSerial[j];
                    pozSerial[j] = auxpoz;

                    Collections.swap(sortedSerials, i, j);
                } else if (serRatings[i] == serRatings[j]) {
                    if (pozSerial[i] > pozSerial[j]) {
                        auxpoz = pozSerial[i];
                        pozSerial[i] = pozSerial[j];
                        pozSerial[j] = auxpoz;

                        Collections.swap(sortedSerials, i, j);
                    }
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

    public Genre convStrToGenre(String value) {
        StringBuilder chGen = new StringBuilder(value.toUpperCase());
        int poz = chGen.indexOf("&");
        if (poz != -1) {
            chGen.delete(poz, poz + 2);
        }

        poz = chGen.indexOf("-");
        if (poz != -1) {
            chGen.setCharAt(poz, '_');
        }

        poz = chGen.indexOf("_");
        if (poz != -1) {
            chGen.setCharAt(poz, '_');
        }

        poz = chGen.indexOf(" ");
        if (poz != -1) {
            chGen.setCharAt(poz, '_');
        }

        return Genre.valueOf(chGen.toString());
    }

    public String getPopular(ArrayList<Movie> movies, ArrayList<SerialSeason> serials, ArrayList<User> users) {
        ArrayList<Genre> sortedGenres = new ArrayList<>();

        int[] noView = new int[Genre.values().length];
        int len = 0;
        int aux;

        for (Genre g : Genre.values()) {
            noView[len] = 0;

            for (Movie m : movies) {
                ArrayList<String> movGen = m.getGenres();

                for(String gen : movGen) {
                    if (g.equals(convStrToGenre(gen))) {
                        for (User u : users) {
                            if (u.getViewed().containsKey(m.getTitle())) {
                                noView[len] += u.getViewed().get(m.getTitle());
                            }
                        }
                        break;
                    }
                }
            }

            for (SerialSeason s : serials) {
                ArrayList<String> movSer = s.getGenres();

                for(String gen : movSer) {
                    if (g.equals(convStrToGenre(gen))) {
                        for (User u : users) {
                            if (u.getViewed().containsKey(s.getTitle())) {
                                noView[len] += u.getViewed().get(s.getTitle());
                            }
                        }
                        break;
                    }
                }
            }

            sortedGenres.add(g);
            len++;
        }

        for (int i = 0 ; i < len - 1 ; i++) {
            for (int j = i + 1 ; j < len ; j++) {
                if (noView[i] < noView[j]) {
                    aux = noView[i];
                    noView[i] = noView[j];
                    noView[j] = aux;
                    Collections.swap(sortedGenres, i, j);
                }
            }
        }

        for (Genre g : sortedGenres) {
            for (Movie m : movies) {
                if (!this.getViewed().containsKey(m.getTitle())) {
                    ArrayList<String> movGen = m.getGenres();

                    for (String gen : movGen) {
                        if (g.equals(convStrToGenre(gen))) {
                            return "PopularRecommendation result: " + m.getTitle();
                        }
                    }
                }
            }
        }

        for (Genre g : sortedGenres) {
            for (SerialSeason s : serials) {
                if (!this.getViewed().containsKey(s.getTitle())) {
                    ArrayList<String> movSer = s.getGenres();

                    for (String gen : movSer) {
                        if (g.equals(convStrToGenre(gen))) {
                            return "PopularRecommendation result: " + s.getTitle();
                        }
                    }
                }
            }
        }

        return "PopularRecommendation cannot be applied!";
    }

    public String getRecFav(ArrayList<Movie> movies, ArrayList<SerialSeason> serials, ArrayList<User> users) {
        ArrayList<String> videos = new ArrayList<>();
        int[] noFav = new int[movies.size() + serials.size()];
        int[] poz = new int[movies.size() + serials.size()];
        int len = 0;
        int num;

        for (Movie m : movies) {
            num = 0;

            for (User u : users) {
                if (u.getFavorite().indexOf(m.getTitle()) != -1) {
                    num++;
                }
            }

            if (num != 0) {
                poz[len] = len;
                noFav[len] = num;
                videos.add(m.getTitle());
                len++;
            }
        }

        for (SerialSeason s : serials) {
            num = 0;

            for (User u : users) {
                if (u.getFavorite().indexOf(s.getTitle()) != -1) {
                    num++;
                }
            }

            if (num != 0) {
                poz[len] = len;
                noFav[len] = num;
                videos.add(s.getTitle());
                len++;
            }
        }

        int aux;
        for (int i = 0 ; i < len - 1 ; i++) {
            for (int  j = 0 ; j < len ; j++) {
                if (noFav[i] < noFav[j]) {
                    aux = noFav[i];
                    noFav[i] = noFav[j];
                    noFav[j] = aux;

                    aux = poz[i];
                    poz[i] = poz[j];
                    poz[j] = aux;

                    Collections.swap(videos, i, j);
                } else if (noFav[i] == noFav[j]) {
                    if (poz[i] > poz[j]) {
                        aux = poz[i];
                        poz[i] = poz[j];
                        poz[j] = aux;

                        Collections.swap(videos, i, j);
                    }
                }
            }
        }

        for (String s:videos) {
            if (!this.viewed.containsKey(s)) {
                return "FavoriteRecommendation result: " + s;
            }
        }

        return "FavoriteRecommendation cannot be applied!";
    }
}
