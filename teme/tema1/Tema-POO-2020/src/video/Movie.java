package video;

import getactor.Actor;
import user.User;

import java.rmi.MarshalledObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.SocketHandler;

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
            return 0.0;
        }

        return val / nr;
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

    public static String getRezStr(ArrayList<Movie> sortMovies) {
        StringBuilder str = new StringBuilder();
        str.append("Query result: [");

        if (sortMovies.size() != 0) {
            for (Movie m: sortMovies) {
                str.append(m.getTitle()).append(", ");
            }
            str.delete(str.length() - 2, str.length());
        }

        str.append("]");
        return str.toString();
    }

    public static int checkYearAndGenre(List <String> year, List <String> genre, Movie m) {
        boolean ok1 = false;
        boolean ok2 = false;

        if (year.get(0) != null) {
            for (String y:year) {
                if (Integer.toString(m.getYear()).equals(y)) {
                    ok1 = true;
                    break;
                }
            }
        }

        if (genre.get(0) != null) {
            List<String> movieGen = m.getGenres();

            String gen = genre.get(0);
            for (String mg:movieGen) {
                if (mg.equals(gen)) {
                    ok2 = true;
                    break;
                }
            }
        }

        if (ok1 == true && ok2 == false) {
            return 1;
        } else if (ok1 == false && ok2 == true) {
            return 2;
        } else if (ok1 == true && ok2 == true) {
            return 3;
        }

        return 0;
    }


    public static String getRatMov(int N, List<String> year, List<String> genre, ArrayList<Movie> movies, String SortType) {
        ArrayList<Movie> sortMovies = new ArrayList<>();
        double[] sortVal = new double[movies.size()];
        int len = 0;

        double rating;
        int ok;

        for (Movie m:movies) {
            rating = m.getValueRating();

            ok = checkYearAndGenre(year, genre, m);

            if (rating != 0.0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == 3) {
                            sortVal[len] = rating;
                            sortMovies.add(m);
                            len++;
                        }
                    } else {
                        if (ok == 1) {
                            sortVal[len] = rating;
                            sortMovies.add(m);
                            len++;
                        }
                    }
                } else {
                    if (genre.get(0) != null) {
                        if (ok == 2) {
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

        return Movie.getRezStr(sortMovies);
    }

    public static String getFavMov(int N, List<String> year, List<String> genre, ArrayList<Movie> movies, ArrayList<User> users, String sortType) {
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        double[] noApp = new double[movies.size()];
        int len = 0;

        int ok;
        double no;
        for (Movie m:movies) {
            no = 0;

            for (User u:users) {
                if (u.getFavorite().indexOf(m.getTitle()) != -1) {
                    no++;
                }
            }

            ok = checkYearAndGenre(year, genre, m);

            if (no != 0.0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == 3) {
                            noApp[len] = no;
                            sortedMovies.add(m);
                            len++;
                        }
                    } else {
                        if (ok == 1) {
                            noApp[len] = no;
                            sortedMovies.add(m);
                            len++;
                        }
                    }
                } else {
                    if (genre.get(0) != null) {
                        if (ok == 2) {
                            noApp[len] = no;
                            sortedMovies.add(m);
                            len++;
                        }
                    } else {
                        noApp[len] = no;
                        sortedMovies.add(m);
                        len++;
                    }
                }
            }
        }

        sortedMovies = Movie.sortMoviesWithValues(sortedMovies, noApp, sortType);

        if (len > N) {
            sortedMovies.subList(N, len).clear();
        }

        return Movie.getRezStr(sortedMovies);
    }

    public static String getLongestMovie(int N, List<String> year, List<String> genre, ArrayList<Movie> movies, String sortType) {
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        double[] dur = new double[movies.size()];
        int ok;
        int len = 0;
        int duration;

        for (Movie m:movies) {
            ok = checkYearAndGenre(year, genre, m);
            duration = m.getDuration();

            if (duration != 0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == 3) {
                            dur[len] = duration * 1.0;
                            sortedMovies.add(m);
                            len++;
                        }
                    } else {
                        if (ok == 1) {
                            dur[len] = duration * 1.0;
                            sortedMovies.add(m);
                            len++;
                        }
                    }
                } else {
                    if (genre.get(0) != null) {
                        if (ok == 2) {
                            dur[len] = duration * 1.0;
                            sortedMovies.add(m);
                            len++;
                        }
                    } else {
                        dur[len] = duration * 1.0;
                        sortedMovies.add(m);
                        len++;
                    }
                }
            }
        }

        sortedMovies = Movie.sortMoviesWithValues(sortedMovies, dur, sortType);

        if (len > N) {
            sortedMovies.subList(N, len).clear();
        }

        return Movie.getRezStr(sortedMovies);
    }

    public static String getMostViewedMovie(int N, List<String> year, List<String> genre, ArrayList<Movie> movies, ArrayList<User> users, String sortType) {
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        double[] noView = new double[movies.size()];
        int len = 0;

        double no;
        int ok;
        for (Movie m:movies) {
            no = 0;
            for (User u:users) {
                if (u.getViewed().containsKey(m.getTitle())) {
                    no += u.getViewed().get(m.getTitle());
                }
            }

            ok = checkYearAndGenre(year, genre, m);

            if (no != 0.0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == 3) {
                            noView[len] = no;
                            sortedMovies.add(m);
                            len++;
                        }
                    } else {
                        if (ok == 1) {
                            noView[len] = no;
                            sortedMovies.add(m);
                            len++;
                        }
                    }
                } else {
                    if (genre.get(0) != null) {
                        if (ok == 2) {
                            noView[len] = no;
                            sortedMovies.add(m);
                            len++;
                        }
                    } else {
                        noView[len] = no;
                        sortedMovies.add(m);
                        len++;
                    }
                }
            }
        }

        sortedMovies = Movie.sortMoviesWithValues(sortedMovies, noView, sortType);

        if (len > N) {
            sortedMovies.subList(N, len).clear();
        }

        return Movie.getRezStr(sortedMovies);
    }
}
