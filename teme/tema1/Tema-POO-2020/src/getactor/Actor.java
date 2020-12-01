package getactor;

import actor.ActorsAwards;
import video.Movie;
import video.SerialSeason;

import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<String> filmography;
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography, final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public final String getName() {
        return name;
    }

    public final String getCareerDescription() {
        return careerDescription;
    }

    public final ArrayList<String> getFilmography() {
        return filmography;
    }

    public final Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    private static double getMed(final ArrayList<String> film,
                                 final ArrayList<Movie> movies, final ArrayList<SerialSeason> ser) {
        double rez = 0;
        int nr = 0;
        boolean ok;

        for (String f : film) {
            ok = false;

            for (Movie m : movies) {
                if (m.getTitle().equals(f)) {
                    if (m.getValueRating() != 0) {
                        rez += m.getValueRating();
                        nr++;
                    }

                    ok = true;
                    break;
                }
            }

            if (!ok) {
                for (SerialSeason s : ser) {
                    if (s.getTitle().equals(f)) {
                        if (s.getValueRatingSerial() != 0) {
                            rez += s.getValueRatingSerial();
                            nr++;
                        }
                        break;
                    }
                }
            }
        }

        return rez / nr;
    }

    /**
     * - Metoda parcurge toti utilizatorii si calculeaza pentru acestia media ratingului
     * in videoclipurile in care au jucat cu ajutorul metodei getMed().
     * - Actorii sunt adaugati intr-o lista iar mediile intr-un vector.
     * - Acestea sunt sortate in functie de medii. In final lista de actori este
     * scurtat la numarul cerut daca este nevoie
     * si este format stringul final prin concatenarea numelor actorilor.
     * @param n - numarul de actori ceruti
     * @param actors - lista de actori din baza de date
     * @param movies - lista de filme din baza de date
     * @param seasons - lista de seriale din baza de date
     * @param sortType - tipul sortarii listei de actori
     * @return - Returneaza primii n actori sortati in functie de
     * media ratingurilor videoclipurilor in care au jucat
     */
    public static String getAverage(final int n, final ArrayList<Actor> actors,
                                    final ArrayList<Movie> movies,
                                    final ArrayList<SerialSeason> seasons,
                                    final String sortType) {
        ArrayList<Actor> sortAct = actors;

        int lenArray = sortAct.size();

        double[] ratmed = new double[lenArray];
        ArrayList<Actor> checkedAct = new ArrayList<>();
        int len = 0;
        double medie;

        for (int i = 0; i < lenArray; i++) {
            medie = getMed(sortAct.get(i).getFilmography(), movies, seasons);
            if (medie > 0) {
                ratmed[len] = medie;
                checkedAct.add(sortAct.get(i));
                len++;
            }
        }

        lenArray = len;
        sortAct = checkedAct;

        boolean ok = true;
        if (sortType.equals("asc")) {
            ok = true;
        } else if (sortType.equals("desc")) {
            ok = false;
        }

        double aux;
        for (int i = 0; i < lenArray - 1; i++) {
            for (int j = i + 1; j < lenArray; j++) {
                if (ok) {
                    if (ratmed[i] > ratmed[j]) {
                        aux = ratmed[i];
                        ratmed[i] = ratmed[j];
                        ratmed[j] = aux;
                        Collections.swap(sortAct, i, j);
                    } else if (ratmed[i] == ratmed[j]) {
                        if (sortAct.get(i).getName().compareTo(sortAct.get(j).getName()) > 0) {
                            aux = ratmed[i];
                            ratmed[i] = ratmed[j];
                            ratmed[j] = aux;
                            Collections.swap(sortAct, i, j);
                        }
                    }
                } else {
                    if (ratmed[i] < ratmed[j]) {
                        aux = ratmed[i];
                        ratmed[i] = ratmed[j];
                        ratmed[j] = aux;
                        Collections.swap(sortAct, i, j);
                    } else if (ratmed[i] == ratmed[j]) {
                        if (sortAct.get(i).getName().compareTo(sortAct.get(j).getName()) < 0) {
                            aux = ratmed[i];
                            ratmed[i] = ratmed[j];
                            ratmed[j] = aux;
                            Collections.swap(sortAct, i, j);
                        }
                    }
                }
            }
        }

        if (lenArray > n) {
            sortAct.subList(n, lenArray).clear();
        }

        StringBuilder str = new StringBuilder();
        str.append("Query result: [");

        if (lenArray > 0) {
            for (Actor a : sortAct) {
                str.append(a.getName() + ", ");
            }
            str.delete(str.length() - 2, str.length());
        }

        str.append("]");

        String retstr = str.toString();
        return retstr;
    }

    /**
     * - Metoda parcurge lista de actori si verifica cate premii are ficare actor.
     * - Daca acesta le detine pe toate cele cerute este adugat intr-o lista,
     * iar numarul de premii intr-un vector.
     * - Acestea sunt sortate dupa numarul de premii, in ordinea ceruta.
     * - In final este format un string in care sunt afisati toti actorii sortati.
     * @param filtAwards - lista de filtre din baza de date
     * @param actors - lista de actori din baza de date
     * @param sortType - tipul de sortare din query
     * @return Returneaza actorii cu toate premiile oferite in filtre
     */
    public static String getAwaAct(final List<String> filtAwards,
                                   final ArrayList<Actor> actors, final String sortType) {
        ArrayList<Actor> finSortActors = new ArrayList<>();
        int[] noAwa = new int[actors.size()];

        boolean ok;
        int len = 0;
        int aux;

        for (Actor a:actors) {
            ok = true;

            for (String fa:filtAwards) {
                if (!a.getAwards().containsKey(ActorsAwards.valueOf(fa))) {
                    ok = false;
                    break;
                }
            }

            Collection<Integer> vals = a.getAwards().values();

            if (ok) {
                finSortActors.add(a);
                noAwa[len] = vals.stream().mapToInt(Integer::valueOf).sum();
                len++;
            }
        }

        String name1;
        String name2;
        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                if (sortType.equals("asc")) {
                    if (noAwa[i] > noAwa[j]) {
                        aux = noAwa[i];
                        noAwa[i] = noAwa[j];
                        noAwa[j] = aux;
                        Collections.swap(finSortActors, i, j);
                    } else if (noAwa[i] == noAwa[j]) {
                        name1 = finSortActors.get(i).getName();
                        name2 = finSortActors.get(j).getName();
                        if (name1.compareTo(name2) > 0) {
                            aux = noAwa[i];
                            noAwa[i] = noAwa[j];
                            noAwa[j] = aux;
                            Collections.swap(finSortActors, i, j);
                        }
                    }
                } else if (sortType.equals("desc")) {
                    if (noAwa[i] < noAwa[j]) {
                        aux = noAwa[i];
                        noAwa[i] = noAwa[j];
                        noAwa[j] = aux;
                        Collections.swap(finSortActors, i, j);
                    } else if (noAwa[i] == noAwa[j]) {
                        name1 = finSortActors.get(i).getName();
                        name2 = finSortActors.get(j).getName();
                        if (name1.compareTo(name2) < 0) {
                            aux = noAwa[i];
                            noAwa[i] = noAwa[j];
                            noAwa[j] = aux;
                            Collections.swap(finSortActors, i, j);
                        }
                    }
                }
            }
        }

        StringBuilder str = new StringBuilder();
        str.append("Query result: [");

        if (len != 0) {
            for (Actor a : finSortActors) {
                str.append(a.getName()).append(", ");
            }
            str.delete(str.length() - 2, str.length());
        }

        str.append("]");

        String retstr = str.toString();
        return retstr;
    }

    /**
     * - Metoda parcurge toti actorii si verifica
     * daca in descrierilor lor se gasesc toate cuvintele.
     * - Daca se gasesc actorii sunt adaugati intr-o lista
     * care este sortat in functie de tip, in ordine alfabetica.
     * - In final este creeat un string in care sunt adaugate numele actorilor din lista sortat.
     * @param words - lista de cuvinte din filtrele din baza de date
     * @param actors - lista de actori din baza de date
     * @param sortType - tipul sortarii cerute
     * @return Returneaza toti actorii in descrierea carora apar toate keywordurile din filtre
     */
    public static String getFilterDescription(final List<String> words,
                                              final ArrayList<Actor> actors,
                                              final String sortType) {
        ArrayList<Actor> sortActors = new ArrayList<>();

        boolean ok;

        for (Actor a:actors) {
            ok = true;

            for (String w:words) {
                String text = "[ '.,!{}()-]";
                Pattern patt = Pattern.compile(text + w + text, Pattern.CASE_INSENSITIVE);
                Matcher m = patt.matcher(a.careerDescription);

                ok = m.find();
                if (!ok) {
                    break;
                }
            }

            if (ok) {
                sortActors.add(a);
            }
        }

        if (sortType.equals("asc")) {
            sortActors.sort((Actor a1, Actor a2) -> a1.getName().compareTo(a2.getName()));
        } else {
            sortActors.sort((Actor a1, Actor a2) -> a2.getName().compareTo(a1.getName()));
        }

        StringBuilder str = new StringBuilder();
        str.append("Query result: [");

        if (sortActors.size() != 0) {
            for (Actor a : sortActors) {
                str.append(a.getName()).append(", ");
            }
            str.delete(str.length() - 2, str.length());
        }

        str.append("]");

        String retstr = str.toString();
        return retstr;
    }
}
