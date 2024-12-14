package user;

import entertainment.Genre;
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

    public User(final String username, final String category,
                final ArrayList<String> favorite, final Map<String, Integer> viewed) {
        this.username = username;
        this.category = category;
        this.favorite = favorite;
        this.viewed = viewed;
        this.noRating = 0;
    }


    public final String getUsername() {
        return this.username;
    }

    public final String getCategory() {
        return this.category;
    }

    public final ArrayList<String> getFavorite() {
        return this.favorite;
    }

    public final Map<String, Integer> getViewed() {
        return this.viewed;
    }

    public final int getNoRating() {
        return this.noRating;
    }

    @Override
    public final String toString() {
        return "User{" + "username='" + this.username + '\'' + '}';
    }

    /**
     * Parcurgem lista de valori si sortam in functie de acestea si numele utilizatorilor
     * @param users - lista de useri
     * @param values - lista cu numerele de actiuni de rating ale fiecarui user
     * @param sortType - tipul de sortare
     * @return - Metoda returneaza un vector de utilizatori
     */
    public static ArrayList<User> srtUserWithValues(final ArrayList<User> users,
                                         final ArrayList<Integer> values, final String sortType) {
        ArrayList<User> sortedUsers = users;
        int len = sortedUsers.size();

        String username1;
        String username2;
        if (sortType.equals("asc")) {
            for (int i = 0; i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    if (values.get(i).intValue() > values.get(j).intValue()) {
                        Collections.swap(values, i, j);
                        Collections.swap(sortedUsers, i, j);
                    } else if (values.get(i).intValue() == values.get(j).intValue()) {
                        username1 = sortedUsers.get(i).getUsername();
                        username2 = sortedUsers.get(j).getUsername();
                        if (username1.compareTo(username2) > 0) {
                            Collections.swap(sortedUsers, i, j);
                        }
                    }
                }
            }
        } else if (sortType.equals("desc")) {
            for (int i = 0; i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    if (values.get(i).intValue() < values.get(j).intValue()) {
                        Collections.swap(values, i, j);
                        Collections.swap(sortedUsers, i, j);
                    } else if (values.get(i).intValue() == values.get(j).intValue()) {
                        username1 = sortedUsers.get(i).getUsername();
                        username2 = sortedUsers.get(j).getUsername();
                        if (username1.compareTo(username2) < 0) {
                            Collections.swap(sortedUsers, i, j);
                        }
                    }
                }
            }
        }

        return sortedUsers;
    }

    /**
     * - Creeam un StringBuilder in care concatenam o parte de inceput,
     * numele utilizatorilor si partea de final.
     * @param sortedUsers - lista de useri sortati
     * @return - Metoda returneaza un String reprezentand raspunsul final
     */
    public static String getRezStr(final ArrayList<User> sortedUsers) {
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

    /**
     * - Adauga titlul videoului dat ca parametru in lista de favorite,
     * verficand daca acesta a fost vizualizat si daca este deja continut.
     * @param video - titlul videoului dat
     * @return - Metoda returneaza raspunsul final concatenand titlul videoului cu un text dat
     */
    public final String setFavorite(final String video) {
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

    /**
     * - Metoda adauga titlul videoclipului in Map,
     * actualizand numarul de vizualizari.
     * - Verifica daca acesta a fost vizualizat anterior.
     * @param video - titlul videoului dat
     * @return - Metoda returneaza raspunsul final concatenand titlul videoului cu un text dat
     */
    public final String setView(final String video) {
        String text = " was viewed with total views of ";
        if (this.viewed.containsKey(video)) {
            this.viewed.put(video, this.viewed.get(video) + 1);
            return "success -> " + video + text + this.viewed.get(video);
        } else {
            this.viewed.put(video, 1);
            return "success -> " + video + text + "1";
        }
    }

    /**
     * - Metoda verifica daca video dat este film sau serial
     * si daca utilizatorul a mai dat rating acestui video.
     * - Aceasta adauga ratingul in lista de ratinguri si usernameul in alta lista
     * si se incrementeaza numarul de ratinguri date.
     * @param movies - filemele din baza de date
     * @param serials - serialele din baza de date
     * @param video - titlul videoului care primeste rating
     * @param grade - valoarea ratingului dat
     * @param seasonNumber - numarul sezonului caruia i se aplica ratingul
     * @return Metoda returneaza rezultatul final reprezentand ratingul
     * oferit de un anumit utilizator la un anumit video
     */
    public final String setRating(final ArrayList<Movie> movies,
                                  final ArrayList<SerialSeason> serials,
                                  final String video, final double grade, final int seasonNumber) {
        if (this.viewed.containsKey(video)) {
            Movie movie = null;
            for (Movie v : movies) {
                if (v.getTitle().equals(video)) {
                    movie = v;
                    break;
                }
            }

            if (movie != null) {
                if (movie.getuserName().indexOf(this.username) == -1) {
                    movie.setRating(grade, this.username);
                    this.noRating++;
                    String text = video + " was rated with " + grade + " by " + this.username;
                    return "success -> " + text;
                } else {
                    return "error -> " + video + " has been already rated";
                }
            } else {
                SeasonModel ser = null;
                for (SerialSeason s : serials) {
                    if (s.getTitle().equals(video)) {
                        ser = s.getSeasons().get(seasonNumber - 1);
                        break;
                    }
                }

                if (ser != null) {
                    if (ser.getUserName().indexOf(this.username) == -1) {
                        ser.setRating(grade, this.username);
                        this.noRating++;
                        String text = video + " was rated with " + grade + " by " + this.username;
                        return "success -> " + text;
                    } else {
                        return "error -> " + video + " has been already rated";
                    }
                }
            }
        }
        return "error -> " + video + " is not seen";
    }

    /**
     * - Metoda adauga utilizatorii cu numarul de ratinguri dat intr-o lista
     * si valorile acestora in alta.
     * - Listele sunt sortate in functie de tipul sortarii.
     * - Se extrag primii N utilizatori si se afiseaza.
     * @param n - numarul de useri ceruti
     * @param usr - lista de useri din baza de date
     * @param srtType - tipul sortarii utilizatorilor
     * @return - Metoda returneaza un string reprezentand primii N utilizatori
     * sortati dupa numarul de ratinguri date
     */
    public static String getNumOfRat(final int n, final ArrayList<User> usr, final String srtType) {
        ArrayList<User> sortedUsers = new ArrayList<>();
        ArrayList<Integer> noRatUser = new ArrayList<>();

        int len = 0;

        for (User u:usr) {
            if (u.getNoRating() != 0) {
                sortedUsers.add(u);
                noRatUser.add(u.getNoRating());
            }
        }

        len = noRatUser.size();

        sortedUsers = User.srtUserWithValues(sortedUsers, noRatUser, srtType);

        if (len > n) {
            sortedUsers.subList(n, len).clear();
        }

        return User.getRezStr(sortedUsers);
    }

    /**
     * Parcurge lista de filme/seriale din baza de date si returneaza primul fim gasit nevizionat.
     * @param mov - lista filmelor din baza de date
     * @param ser - lista serialelor din baza de date
     * @return Metoda returneaza primul video nevizionat de un anumit user din baza de date
     */
    public final String getStandard(final ArrayList<Movie> mov, final ArrayList<SerialSeason> ser) {
        for (Movie m:mov) {
            if (!this.viewed.containsKey(m.getTitle())) {
                return "StandardRecommendation result: " + m.getTitle();
            }
        }

        for (SerialSeason s:ser) {
            if (!this.viewed.containsKey(s.getTitle())) {
                return "StandardRecommendation result: " + s.getTitle();
            }
        }

        return "StandardRecommendation cannot be applied!";
    }

    /**
     * - Adaugam intr-o lista filemele/serialele si valorile ratingurilor acestor,
     * sortam listele descrescator in functie de valori si le parcurgem.
     * - Afisam prima valoare gasita.
     * @param mov - lista de filme din baza de date
     * @param ser - lista de seriale din baza de date
     * @return - Metoda returneaza primul videoclip nevizualizat de utilizator,
     * videourile fiind ordonate descrescator dupa rating
     * al doilea criteriu fiind pozitia in lista sa din baza de date
     */
    public final String getBestUn(final ArrayList<Movie> mov, final ArrayList<SerialSeason> ser) {
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        double[] movRatings = new double[mov.size()];
        int[] pozMov = new int[mov.size()];
        int lenMov = 0;
        int auxpoz;
        double aux;

        for (Movie m : mov) {
            sortedMovies.add(m);
            pozMov[lenMov] = lenMov;
            movRatings[lenMov] = m.getValueRating();
            lenMov++;
        }

        for (int i = 0; i < lenMov - 1; i++) {
            for (int j = i + 1; j < lenMov; j++) {
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
            if (!this.viewed.containsKey(m.getTitle())) {
                return "BestRatedUnseenRecommendation result: " + m.getTitle();
            }
        }

        ArrayList<SerialSeason> sortedSerials = new ArrayList<>();
        double[] serRatings = new double[ser.size()];
        int[] pozSerial = new int[ser.size()];
        int lenSer = 0;

        for (SerialSeason s : ser) {
            sortedSerials.add(s);
            pozSerial[lenSer] = lenSer;
            serRatings[lenSer] = s.getValueRatingSerial();
            lenSer++;
        }

        for (int i = 0; i < lenSer - 1; i++) {
            for (int j = i + 1; j < lenSer; j++) {
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
            if (!this.viewed.containsKey(s.getTitle())) {
                return "BestRatedUnseenRecommendation result: " + s.getTitle();
            }
        }

        return "BestRatedUnseenRecommendation cannot be applied!";
    }

    /**
     *
     * @param value - valoarea Stringului
     * @return Metoda converteste un String la tipul enumeratiei Genre
     */
    public final Genre convStrToGenre(final String value) {
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

    /**
     * - Metoda parcurge listele de filme si seriale calculand care este cel mai popular gen.
     * - Retinem intr-o lista numarul de vizualizari a tuturor videoclipurilor din fiecare gen.
     * - Sortam dupa numarul de vizualizari lista de genuri. Parcurgem lista de genuri sortat
     * si cautam primul video nevizualizat din acel gen, daca nu se gaseste trecem la urmatorul.
     * @param movies - lista de filme din baza de date
     * @param serials - lista de seriale din baza de date
     * @param users - lista de utilizatori din baza de date
     * @return - Intoarce primul video nevizualizat din cel mai popular gen
     */
    public final String getPopular(final ArrayList<Movie> movies,
                                   final ArrayList<SerialSeason> serials,
                                   final ArrayList<User> users) {
        ArrayList<Genre> sortedGenres = new ArrayList<>();

        int[] noView = new int[Genre.values().length];
        int len = 0;
        int aux;

        for (Genre g : Genre.values()) {
            noView[len] = 0;

            for (Movie m : movies) {
                ArrayList<String> movGen = m.getGenres();

                for (String gen : movGen) {
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

                for (String gen : movSer) {
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

        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
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

    /**
     * - Metoda parcurge listele de filme si seriale si adauga intr-o
     * lista videourile care nu sunt vizualizate de utilizator.
     * - De asemenea, adauga in alt vector numarul de aparitii
     * la favorite al videoului si pozitia acestuia in lista initiala.
     * - Listele formate sunt sortate descrescator dupa care lista de videouri este
     * parcursa si primul element este afisat daca acesta exista.
     * @param movies - lista de filme din baza de date
     * @param serials - lista de seriale din baza de date
     * @param users - lista de utilizatori din baza de date
     * @return - Returneaza videoclipul care este intalnit cel mai des in lista de favorite
     */
    public final String getRecFav(final ArrayList<Movie> movies,
                                  final ArrayList<SerialSeason> serials,
                                  final ArrayList<User> users) {
        ArrayList<String> videos = new ArrayList<>();
        int[] noFav = new int[movies.size() + serials.size()];
        int[] poz = new int[movies.size() + serials.size()];
        int len = 0;
        int num;

        for (Movie m : movies) {
            if (!this.viewed.containsKey(m.getTitle())) {
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
        }

        for (SerialSeason s : serials) {
            if (!this.viewed.containsKey(s.getTitle())) {
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
        }

        int aux;
        for (int i = 0; i < len - 1; i++) {
            for (int  j = i + 1; j < len; j++) {
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
            return "FavoriteRecommendation result: " + s;
        }

        return "FavoriteRecommendation cannot be applied!";
    }

    /**
     * - Metoda creeaza o lista in care toate videoclipurile nevazute sunt adaugate
     * si un vector in care se afla ratingurile acestora.
     * - Sunt sortate in ordine crescatoare dupa ratinguri dupa care este format stringul final
     * care reprezinta toate videoclipurile din lista.
     * @param movies - lista de filme din baza de date
     * @param serials - lista de seriale din baza de date
     * @param gen - genul cerut
     * @return Returneaza toate videoclipurile nevazute de utilizator dintr-un anumit gen
     */
    public final String getSearch(final ArrayList<Movie> movies,
                                  final ArrayList<SerialSeason> serials, final String gen) {
        ArrayList<String> videos = new ArrayList<>();
        double[] ratings = new double[movies.size() + serials.size()];
        int len = 0;
        double aux;

        for (Movie m : movies) {
            if (!this.viewed.containsKey(m.getTitle())) {
                ArrayList<String> movGen = m.getGenres();

                for (String g : movGen) {
                    if (gen.equals(g)) {
                        videos.add(m.getTitle());
                        ratings[len] = m.getValueRating();
                        len++;
                        break;
                    }
                }
            }
        }

        for (SerialSeason s : serials) {
            if (!this.viewed.containsKey(s.getTitle())) {
                ArrayList<String> movSer = s.getGenres();

                for (String g : movSer) {
                    if (gen.equals(g)) {
                        videos.add(s.getTitle());
                        ratings[len] = s.getValueRatingSerial();
                        len++;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                if (ratings[i] > ratings[j]) {
                   aux = ratings[i];
                   ratings[i] = ratings[j];
                   ratings[j] = aux;

                   Collections.swap(videos, i, j);
                } else if (ratings[i] == ratings[j]) {
                    if (videos.get(i).compareTo(videos.get(j)) > 0) {
                        Collections.swap(videos, i, j);
                    }
                }
            }
        }

        if (len != 0) {
            StringBuilder str = new StringBuilder();
            str.append("SearchRecommendation result: [");

            if (videos.size() != 0) {
                for (String s : videos) {
                    str.append(s).append(", ");
                }
                str.delete(str.length() - 2, str.length());
            }

            str.append("]");
            return str.toString();
        }

        return "SearchRecommendation cannot be applied!";
    }
}
