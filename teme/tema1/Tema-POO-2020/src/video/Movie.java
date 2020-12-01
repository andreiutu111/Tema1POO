package video;

import user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Movie extends Video {
    private final int duration;
    private List<Double> ratings;
    private List<String> userName;

    public Movie(final String title, final int year,
                 final ArrayList<String> genres, final ArrayList<String> cast,
                 final int duration) {
        super(title, year, genres, cast);
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

    /**
     * @return Metodele returneaza usernameul, lista de ratinguri si durata filmului
     */
    public final List<String> getuserName() {
        return userName;
    }
    public final List<Double> getRating() {
        return ratings;
    }
    public final int getDuration() {
        return duration;
    }

    /**
     * @return Returneaza ratingul filmului
     */
    public final double getValueRating() {
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

    /**
     * - Functia sorteaza o lista de filme dupa valorile din vectorul
     * dat ca parametru in functie de tipul sortarii
     * @param movies - lista de filme din baza de date
     * @param values - lista de valori care se sorteaza
     * @param sortType - tipul sortarii
     * @return Returneaza o lista de filme sortate corespunzator
     */
    public static ArrayList<Movie> sortMoviesWithValues(final ArrayList<Movie> movies,
                                                        final double[] values,
                                                        final String sortType) {
        ArrayList<Movie> sortedMovies = movies;
        int len = sortedMovies.size();
        double aux;

        String movieName1;
        String movieName2;
        if (sortType.equals("asc")) {
            for (int i = 0; i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    if (values[i] > values[j]) {
                        aux = values[i];
                        values[i] = values[j];
                        values[j] = aux;
                        Collections.swap(sortedMovies, i, j);
                    } else if (values[i] == values[j]) {
                        movieName1 = sortedMovies.get(i).getTitle();
                        movieName2 = sortedMovies.get(j).getTitle();
                        if (movieName1.compareTo(movieName2) > 0) {
                            Collections.swap(sortedMovies, i, j);
                        }
                    }
                }
            }
        } else if (sortType.equals("desc")) {
            for (int i = 0; i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    if (values[i] < values[j]) {
                        aux = values[i];
                        values[i] = values[j];
                        values[j] = aux;
                        Collections.swap(sortedMovies, i, j);
                    } else if (values[i] == values[j]) {
                        movieName1 = sortedMovies.get(i).getTitle();
                        movieName2 = sortedMovies.get(j).getTitle();
                        if (movieName1.compareTo(movieName2) < 0) {
                            Collections.swap(sortedMovies, i, j);
                        }
                    }
                }
            }
        }

        return sortedMovies;
    }

    /**
     * Metoda creeaza un string la care se lipesc numele filmelor care sunt primite ca parametru.
     * @param sortMovies
     * @return Returneaza un string rezultat
     */
    public static String getRezStr(final ArrayList<Movie> sortMovies) {
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

    /**
     * - Metoda parcurge lista de ani si verifica daca
     * cel mult unul din acestia este egal cu anul filmului.
     * - Aceasta face acelasi lucru si pentru lista de genuri
     * dupa care returneaza ce caracteristici sunt valide
     * @param year - lista de ani din filtre
     * @param genre - lista de genuri din filtre
     * @param m - filmul care se verifica
     * @return Returneaza daca anii si/sau genurile sunt date
     * in filtrele din baza de date si daca filmul are aceste caracteristici
     */
    public static int checkYearAndGenre(final List<String> year,
                                        final List<String> genre,
                                        final Movie m) {
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

        if (ok1 && !ok2) {
            return 1;
        } else if (!ok1 && ok2) {
            return 2;
        } else if (ok1 && ok2) {
            return -1;
        }

        return 0;
    }

    /**
     * - Metoda parcurge lista de filme si verifica care sunt caracteristicile (an si gen)
     * dupa care acestea sunt adaugate in lista de sortare.
     * - Lista este sortata, scurtata daca este nevoie si este afisata,
     * cu ajutorul functiilor de mai sus.
     * @param n - numarul de filme cerute
     * @param year - lista de ani din filtre
     * @param genre - lista de genuri din filtre
     * @param movies - lista filmelor din baza de date
     * @param sortType - tipul sortarii
     * @return - Returneaza primele n filme sortate dupa rating
     */
    public static String getRatMov(final int n, final List<String> year,
                                   final List<String> genre, final ArrayList<Movie> movies,
                                   final String sortType) {
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
                        if (ok == -1) {
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

        sortMovies = Movie.sortMoviesWithValues(sortMovies, sortVal, sortType);

        if (len > n) {
            sortMovies.subList(n, len).clear();
        }

        return Movie.getRezStr(sortMovies);
    }

    /**
     * - Metoda parcurge lista de filme, calculeaza numarul de aparente
     * in lista de favorite si verifica (an si gen)
     * care sunt caracteristicile dupa care acestea sunt adaugate in lista de sortare.
     * - Lista este sortata dupa numarul de aparente si este afisata,
     * cu ajutorul functiilor de mai sus.
     * @param n - numarul de filme cerute
     * @param year - lista de ani din filtre
     * @param genre - lista de genuri din filtre
     * @param movies - lista filmelor din baza de date
     * @param sortType - tipul sortarii
     * @return - Returneaza primele n filme sortate dupa nr de aparitii in listele de favorite
     */
    public static String getFavMov(final int n, final List<String> year,
                                   final List<String> genre, final ArrayList<Movie> movies,
                                   final ArrayList<User> users, final String sortType) {
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
                        if (ok == -1) {
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

        if (len > n) {
            sortedMovies.subList(n, len).clear();
        }

        return Movie.getRezStr(sortedMovies);
    }

    /**
     * - Metoda parcurge lista de filme, ia durata filmului
     * si verifica care sunt caracteristicile (an si gen)
     * dupa care acestea sunt adaugate in lista de sortare.
     * - Lista este sortata dupa durate si este afisata, cu ajutorul functiilor de mai sus.
     * @param n - numarul de filme cerute
     * @param year - lista de ani din filtre
     * @param genre - lista de genuri din filtre
     * @param movies - lista filmelor din baza de date
     * @param sortType - tipul sortarii
     * @return - Returneaza primele n filme sortate dupa durata lor
     */
    public static String getLongestMovie(final int n, final List<String> year,
                                         final List<String> genre, final ArrayList<Movie> movies,
                                         final String sortType) {
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
                        if (ok == -1) {
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

        if (len > n) {
            sortedMovies.subList(n, len).clear();
        }

        return Movie.getRezStr(sortedMovies);
    }

    /**
     * - Metoda parcurge lista de filme, retine numarul de vizualizari
     * si verifica care sunt caracteristicile (an si gen)
     * dupa care acestea sunt adaugate in lista de sortare.
     * - Lista este sortata dupa numarul de vizualizari si este afisata,
     * cu ajutorul functiilor de mai sus.
     * @param n - numarul de filme cerute
     * @param year - lista de ani din filtre
     * @param genre - lista de genuri din filtre
     * @param movies - lista filmelor din baza de date
     * @param sortType - tipul sortarii
     * @return - Returneaza primele n filme sortate dupa numarul de vizualizari
     */
    public static String getMostViewedMovie(final int n, final List<String> year,
                                            final List<String> genre, final ArrayList<Movie> movies,
                                            final ArrayList<User> users, final String sortType) {
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
                        if (ok == -1) {
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

        if (len > n) {
            sortedMovies.subList(n, len).clear();
        }

        return Movie.getRezStr(sortedMovies);
    }
}
