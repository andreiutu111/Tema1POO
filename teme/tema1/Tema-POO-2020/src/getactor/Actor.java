package getactor;

import actor.ActorsAwards;
import video.Movie;
import video.SeasonModel;
import video.SerialSeason;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<String> filmography;
    private final Map<ActorsAwards, Integer> awards;

    public Actor(final String name, final String careerDescription, final ArrayList<String> filmography, final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    private static double getMed (ArrayList<String> film, ArrayList<Movie> movies, ArrayList<SerialSeason> ser) {
        double rez = 0;
        double rezsez = 0;
        int nr = 0;

        List<Double> rats;

        for (String f : film) {
            for (Movie m : movies) {
                if (m.getTitle().equals(f)) {
                    rez = m.getValueRating();
                    break;
                }
            }

            if (rez == 0.0) {
                for (SerialSeason s : ser) {
                    if (s.getTitle().equals(f)) {
                        ArrayList<SeasonModel> seasonsSerial = s.getSeasons();

                        for (SeasonModel sez : seasonsSerial) {
                            rats = sez.getRating();
                            rezsez = 0;
                            nr = 0;

                            for (Double num : rats) {
                                    rezsez += num.doubleValue();
                                    nr++;
                            }

                            if (nr != 0) {
                                rez += rezsez / nr;
                            }
                        }

                        rez /= s.getNo();
                        break;
                    }
                }
            }
        }

        return rez;
    }

    public static String getAverage(int N, ArrayList<Actor> actors, ArrayList<Movie> movies, ArrayList<SerialSeason> seasons, String sortType) {
        ArrayList<Actor> sortAct = actors;

        int lenArray = sortAct.size();

        double[] ratmed = new double[lenArray];
        ArrayList<Actor> checkedAct = new ArrayList<>();
        int len = 0;
        double medie;

        for (int i = 0 ; i < lenArray; i++) {
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
        for (int i = 0 ; i < lenArray - 1; i++){
            for (int j = i + 1 ; j < lenArray; j++){
                if (ok == true) {
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

        if (lenArray > N) {
            sortAct.subList(N, lenArray).clear();
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

    public static String getAwaAct(List<String> filtAwards, ArrayList<Actor> actors, String sortType) {
        ArrayList<Actor> finSortActors = new ArrayList<>();
        int[] noAwa = new int[actors.size()];

        boolean ok;
        int len = 0;
        int aux;

        for (Actor a:actors){
            ok = true;

            for (String fa:filtAwards) {
                if (!a.getAwards().containsKey(ActorsAwards.valueOf(fa))){
                    ok = false;
                    break;
                }
            }

            Collection<Integer> vals = a.getAwards().values();

            if (ok == true) {
                finSortActors.add(a);
                noAwa[len] = vals.stream().mapToInt(Integer::valueOf).sum();
                len++;
            }
        }

        for (int i = 0 ; i < len - 1; i++){
            for (int j = i + 1 ; j < len; j++) {
                if (sortType.equals("asc")) {
                    if (noAwa[i] > noAwa[j]) {
                        aux = noAwa[i];
                        noAwa[i] = noAwa[j];
                        noAwa[j] = aux;
                        Collections.swap(finSortActors, i, j);
                    } else if (noAwa[i] == noAwa[j]) {
                        if (finSortActors.get(i).getName().compareTo(finSortActors.get(j).getName()) > 0) {
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
                        if (finSortActors.get(i).getName().compareTo(finSortActors.get(j).getName()) < 0) {
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

    public static String getFilterDescription(List<String> words, ArrayList<Actor> actors, String sortType) {
        ArrayList<Actor> sortActors = new ArrayList<>();

        boolean ok;

        for (Actor a:actors) {
            ok = true;

            for (String w:words) {
                Pattern patt = Pattern.compile("[ '.,!{}()-]" + w + "[ '.,!{}()-]", Pattern.CASE_INSENSITIVE);
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
