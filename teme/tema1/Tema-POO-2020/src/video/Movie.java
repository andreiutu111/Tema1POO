package video;

import getactor.Actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Movie extends Video {
    private final int duration;
    private List<Double> ratings;
    private List<String> userName;

    public Movie(final String title, final int year, final ArrayList<String> genres, final ArrayList<String> cast, final int duration) {
        super(title, year, genres, cast);
        this.duration = duration;
        this.ratings = new ArrayList<>();
        this.userName = new ArrayList<>();
    }

    public void setRating(double grade, String name) {
        this.ratings.add(grade);
        this.userName.add(name);
    }

    public List<String> getuserName() { return userName; }
    public List<Double> getRating() {
        return ratings;
    }
    public int getDuration() {
        return duration;
    }

    public double getValueRating() {
        double val = 0;
        int nr = 0;

        for (Double d:ratings) {
            val += d.doubleValue();
            nr++;
        }

        if (nr == 0) {
            return 0;
        }

        val /= nr;
        return val;
    }

    public static ArrayList<Movie> sortMoviesWithValues(ArrayList<Movie> movies, double[] values, String SortType) {
        ArrayList<Movie> sortedMovies = movies;
        int len = sortedMovies.size();
        double aux;

        if (SortType.equals("asc")) {
            for (int i = 0 ; i < len - 1 ; i++) {
                for (int j = i + 1 ; j < len ; j++) {
                    if (values[i] > values[j]) {
                        aux = values[i];
                        values[i] = values[j];
                        values[j] = aux;
                        Collections.swap(sortedMovies, i, j);
                    } else if (values[i] == values[j]) {
                        if (sortedMovies.get(i).getTitle().compareTo(sortedMovies.get(j).getTitle()) > 0) {
                            Collections.swap(sortedMovies, i, j);
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
                        Collections.swap(sortedMovies, i, j);
                    } else if (values[i] == values[j]) {
                        if (sortedMovies.get(i).getTitle().compareTo(sortedMovies.get(j).getTitle()) < 0) {
                            Collections.swap(sortedMovies, i, j);
                        }
                    }
                }
            }
        }

        return sortedMovies;
    }

    public static String getRatMov(int N, List<String> year, List<String> genre, ArrayList<Movie> movies, String SortType) {
        ArrayList<Movie> sortMovies = new ArrayList<>();
        double[] sortVal = new double[movies.size()];
        int len = 0;

        double rating;
        boolean ok1;
        boolean ok2;
        for (Movie m:movies) {
            ok1 = false;
            ok2 = false;
            rating = m.getValueRating();
            if (year != null) {
                for (String y:year) {
                    if (Integer.toString(m.getYear()).equals(y)) {
                        ok1 = true;
                        break;
                    }
                }
            }

            if (genre != null) {
                List<String> movieGen = m.getGenres();

                String gen = genre.get(0);
                for (String mg:movieGen) {
                    if (mg.equals(gen)) {
                        ok2 = true;
                        break;
                    }
                }

            }

            if (rating != 0.0) {
                if (year != null) {
                    if (genre != null) {
                        if (ok1 == true && ok2 == true) {
                            sortVal[len] = rating;
                            sortMovies.add(m);
                            len++;
                        }
                    } else {
                        if (ok1 == true) {
                            sortVal[len] = rating;
                            sortMovies.add(m);
                            len++;
                        }
                    }
                } else {
                    if (genre != null) {
                        if (ok2 == true) {
                            sortVal[len] = rating;
                            sortMovies.add(m);
                            len++;
                        }
                    } else {
                        sortVal[len] = rating;
                        sortMovies.add(m);
                        len++;
                    }
                }
            }
        }

        sortMovies = Movie.sortMoviesWithValues(sortMovies, sortVal, SortType);

        if (len > N) {
            sortMovies.subList(N, len).clear();
        }

        StringBuilder str = new StringBuilder();
        str.append("Query result: [");

        if (len != 0) {
            for (Movie m: sortMovies) {
                str.append(m.getTitle()).append(", ");
            }
            str.delete(str.length() - 2, str.length());
        }

        str.append("]");

        return str.toString();
    }
}
