package video;

import user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerialSeason extends Video {
    private final int no;
    private final ArrayList<SeasonModel> seasons;

    public SerialSeason(final String title, final int year, final ArrayList<String> genres,
                        final ArrayList<String> cast, final int no,
                        final ArrayList<SeasonModel> seas) {
        super(title, year, genres, cast);
        this.no = no;
        this.seasons = seas;
    }

    public final int getNo() {
        return this.no;
    }

    public final ArrayList<SeasonModel> getSeasons() {
        return this.seasons;
    }

    /**
     * Functia calculeaza ratingul fiecarui sezon dupa care ratingul serialului.
     * @return Returneaza ratingul unui serial
     */
    public final double getValueRatingSerial() {
        double val = 0;
        int nr = 0;

        double sezval;
        int seznr;
        for (SeasonModel s:seasons) {
            List<Double> rating = s.getRating();

            sezval = 0;
            seznr = 0;
            for (Double r:rating) {
                sezval += r.doubleValue();
                seznr++;
            }

            if (seznr != 0) {
                val += sezval / seznr;
                nr++;
            } else {
                nr++;
            }
        }

        if (nr == 0) {
            return 0;
        }

        return val / nr;
    }

    /**
     * Metoda aduna durata tuturor sezoanelor unui serial
     * @return Returneaza durata unui serial
     */
    public final int getValueDurationSerial() {
        int duration = 0;

        for (SeasonModel s:seasons) {
            duration += s.getDuration();
        }

        return duration;
    }

    /**
     * - Metoda parcurge lista de ani si verifica daca
     * cel mult unul din acestia este egal cu anul serialului.
     * - Aceasta face acelasi lucru si pentru lista de genuri
     * dupa care returneaza ce caracteristici sunt valide.
     * @param ye - lista de ani din filtre
     * @param ge - lista de genuri din filtre
     * @param s - serialul care sse verifica
     * @return Returneaza daca anii si/sau genurile sunt date
     * in filtrele din baza de date si daca serialul are aceste caracteristici
     */
    public static int checkYAG(final List<String> ye, final List<String> ge, final SerialSeason s) {
        boolean ok1 = false;
        boolean ok2 = false;

        if (ye.get(0) != null) {
            for (String y:ye) {
                if (Integer.toString(s.getYear()).equals(y)) {
                    ok1 = true;
                    break;
                }
            }
        }

        if (ge.get(0) != null) {
            List<String> movieGen = s.getGenres();

            String gen = ge.get(0);
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
     * - Functia sorteaza o lista de seriale dupa valorile din vectorul
     * dat ca parametru in functie de tipul sortarii
     * @param serials - lista de seriale din baza de date
     * @param values - lista de valori care se sorteaza
     * @param sortType - tipul sortarii
     * @return Returneaza o lista de seriale sortate corespunzator
     */
    public static ArrayList<SerialSeason>
    sortSerialsWithValues(final ArrayList<SerialSeason> serials,
                          final double[] values,
                          final String sortType) {
        ArrayList<SerialSeason> sortedSerials = serials;
        int len = sortedSerials.size();
        double aux;

        String serialname1;
        String serialname2;
        if (sortType.equals("asc")) {
            for (int i = 0; i < len - 1; i++) {
                for (int j = i + 1; j < len; j++) {
                    if (values[i] > values[j]) {
                        aux = values[i];
                        values[i] = values[j];
                        values[j] = aux;
                        Collections.swap(sortedSerials, i, j);
                    } else if (values[i] == values[j]) {
                        serialname1 = sortedSerials.get(i).getTitle();
                        serialname2 = sortedSerials.get(j).getTitle();
                        if (serialname1.compareTo(serialname2) > 0) {
                            Collections.swap(sortedSerials, i, j);
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
                        Collections.swap(sortedSerials, i, j);
                    } else if (values[i] == values[j]) {
                        serialname1 = sortedSerials.get(i).getTitle();
                        serialname2 = sortedSerials.get(j).getTitle();
                        if (serialname1.compareTo(serialname2) < 0) {
                            Collections.swap(sortedSerials, i, j);
                        }
                    }
                }
            }
        }

        return sortedSerials;
    }

    /**
     * Metoda creeaza un string la care se lipesc numele serialelor care sunt primite ca parametru.
     * @param sortSerials
     * @return Returneaza un string rezultat
     */
    public static String getRezStr(final ArrayList<SerialSeason> sortSerials) {
        StringBuilder str = new StringBuilder();
        str.append("Query result: [");

        if (sortSerials.size() != 0) {
            for (SerialSeason s:sortSerials) {
                str.append(s.getTitle()).append(", ");
            }
            str.delete(str.length() - 2, str.length());
        }

        str.append("]");
        return str.toString();
    }

    /**
     * - Metoda parcurge lista de seriale si verifica care sunt caracteristicile (an si gen)
     * dupa care acestea sunt adaugate in lista de sortare.
     * - Lista este sortata, scurtata daca este nevoie si este afisata,
     * cu ajutorul functiilor de mai sus.
     * @param n - numarul de seriale cerute
     * @param year - lista de ani din filtre
     * @param genre - lista de genuri din filtre
     * @param serials - lista serialelor din baza de date
     * @param sortType - tipul sortarii
     * @return - Returneaza primele n seriale sortate dupa rating
     */
    public static String
    getRatSerial(final int n, final List<String> year, final List<String> genre,
                 final ArrayList<SerialSeason> serials, final String sortType) {
        ArrayList<SerialSeason> sortSerials = new ArrayList<>();
        double[] sortVal = new double[serials.size()];
        int len = 0;

        double rating;
        int ok;

        for (SerialSeason s : serials) {
            rating = s.getValueRatingSerial();

            ok = checkYAG(year, genre, s);

            if (rating != 0.0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == -1) {
                            sortVal[len] = rating;
                            sortSerials.add(s);
                            len++;
                        }
                    } else {
                        if (ok == 1) {
                            sortVal[len] = rating;
                            sortSerials.add(s);
                            len++;
                        }
                    }
                } else {
                    if (genre.get(0) != null) {
                        if (ok == 2) {
                            sortVal[len] = rating;
                            sortSerials.add(s);
                            len++;
                        }
                    } else {
                        sortVal[len] = rating;
                        sortSerials.add(s);
                        len++;
                    }
                }
            }
        }

        sortSerials = SerialSeason.sortSerialsWithValues(sortSerials, sortVal, sortType);

        if (len > n) {
            sortSerials.subList(n, len).clear();
        }

        return SerialSeason.getRezStr(sortSerials);
    }

    /**
     * - Metoda parcurge lista de seriale, calculeaza numarul de aparente
     * in lista de favorite si verifica (an si gen)
     * care sunt caracteristicile dupa care acestea sunt adaugate in lista de sortare.
     * - Lista este sortata dupa numarul de aparente si este afisata,
     * cu ajutorul functiilor de mai sus.
     * @param n - numarul de seriale cerute
     * @param year - lista de ani din filtre
     * @param genre - lista de genuri din filtre
     * @param serials - lista serialelor din baza de date
     * @param sortType - tipul sortarii
     * @return - Returneaza primele n seriale sortate dupa nr de aparitii in listele de favorite
     */
    public static String getFavSerials(final int n, final List<String> year,
                                       final List<String> genre,
                                       final ArrayList<SerialSeason> serials,
                                       final ArrayList<User> users, final String sortType) {
        ArrayList<SerialSeason> sortedSerials = new ArrayList<>();
        double[] noApp = new double[serials.size()];
        int len = 0;

        int ok;
        double no;
        for (SerialSeason s:serials) {
            no = 0;

            for (User u:users) {
                if (u.getFavorite().indexOf(s.getTitle()) != -1) {
                    no++;
                }
            }

            ok = checkYAG(year, genre, s);

            if (no != 0.0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == -1) {
                            noApp[len] = no;
                            sortedSerials.add(s);
                            len++;
                        }
                    } else {
                        if (ok == 1) {
                            noApp[len] = no;
                            sortedSerials.add(s);
                            len++;
                        }
                    }
                } else {
                    if (genre.get(0) != null) {
                        if (ok == 2) {
                            noApp[len] = no;
                            sortedSerials.add(s);
                            len++;
                        }
                    } else {
                        noApp[len] = no;
                        sortedSerials.add(s);
                        len++;
                    }
                }
            }
        }


        sortedSerials = SerialSeason.sortSerialsWithValues(sortedSerials, noApp, sortType);

        if (len > n) {
            sortedSerials.subList(n, len).clear();
        }

        return SerialSeason.getRezStr(sortedSerials);
    }

    /**
     * - Metoda parcurge lista de seriale, ia durata serialului
     * si verifica care sunt caracteristicile (an si gen)
     * dupa care acestea sunt adaugate in lista de sortare.
     * - Lista este sortata dupa durate si este afisata, cu ajutorul functiilor de mai sus.
     * @param n - numarul de seriale cerute
     * @param year - lista de ani din filtre
     * @param genre - lista de genuri din filtre
     * @param serials - lista serialelor din baza de date
     * @param sortType - tipul sortarii
     * @return - Returneaza primele n seriale sortate dupa durata lor
     */
    public static String getLongestSerial(final int n, final List<String> year,
                                          final List<String> genre,
                                          final ArrayList<SerialSeason> serials,
                                          final String sortType) {
        ArrayList<SerialSeason> sortedSerials = new ArrayList<>();
        double[] dur = new double[serials.size()];
        int ok;
        int len = 0;
        int duration;

        for (SerialSeason s:serials) {
            ok = checkYAG(year, genre, s);

            duration = s.getValueDurationSerial();

            if (duration != 0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == -1) {
                            dur[len] = duration * 1.0;
                            sortedSerials.add(s);
                            len++;
                        }
                    } else {
                        if (ok == 1) {
                            dur[len] = duration * 1.0;
                            sortedSerials.add(s);
                            len++;
                        }
                    }
                } else {
                    if (genre.get(0) != null) {
                        if (ok == 2) {
                            dur[len] = duration * 1.0;
                            sortedSerials.add(s);
                            len++;
                        }
                    } else {
                        dur[len] = duration * 1.0;
                        sortedSerials.add(s);
                        len++;
                    }
                }
            }
        }

        sortedSerials = SerialSeason.sortSerialsWithValues(sortedSerials, dur, sortType);

        if (len > n) {
            sortedSerials.subList(n, len).clear();
        }

        return SerialSeason.getRezStr(sortedSerials);
    }

    /**
     * - Metoda parcurge lista de seriale, retine numarul de vizualizari
     * si verifica care sunt caracteristicile (an si gen)
     * dupa care acestea sunt adaugate in lista de sortare.
     * - Lista este sortata dupa numarul de vizualizari si este afisata,
     * cu ajutorul functiilor de mai sus.
     * @param n - numarul de seriale cerute
     * @param year - lista de ani din filtre
     * @param genre - lista de genuri din filtre
     * @param serials - lista serialelor din baza de date
     * @param sortType - tipul sortarii
     * @return - Returneaza primele n seriale sortate dupa numarul de vizualizari
     */
    public static String getMostViewedSerial(final int n, final List<String> year,
                                             final List<String> genre,
                                             final ArrayList<SerialSeason> serials,
                                             final ArrayList<User> users, final String sortType) {
        ArrayList<SerialSeason> sortedSerials = new ArrayList<>();
        double[] noView = new double[serials.size()];
        int len = 0;

        double no;
        int ok;
        for (SerialSeason s:serials) {
            no = 0;
            for (User u:users) {
                if (u.getViewed().containsKey(s.getTitle())) {
                    no += u.getViewed().get(s.getTitle());
                }
            }

            ok = checkYAG(year, genre, s);

            if (no != 0.0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == -1) {
                            noView[len] = no;
                            sortedSerials.add(s);
                            len++;
                        }
                    } else {
                        if (ok == 1) {
                            noView[len] = no;
                            sortedSerials.add(s);
                            len++;
                        }
                    }
                } else {
                    if (genre.get(0) != null) {
                        if (ok == 2) {
                            noView[len] = no;
                            sortedSerials.add(s);
                            len++;
                        }
                    } else {
                        noView[len] = no;
                        sortedSerials.add(s);
                        len++;
                    }
                }
            }
        }

        sortedSerials = SerialSeason.sortSerialsWithValues(sortedSerials, noView, sortType);

        if (len > n) {
            sortedSerials.subList(n, len).clear();
        }

        return SerialSeason.getRezStr(sortedSerials);
    }
}
