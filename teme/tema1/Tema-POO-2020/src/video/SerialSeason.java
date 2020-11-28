package video;

import user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerialSeason extends Video{
    private final int no;
    private final ArrayList<SeasonModel> seasons;

    public SerialSeason(final String title, final int year, final ArrayList<String> genres, final ArrayList<String> cast, final int no, final ArrayList<SeasonModel> seas) {
        super(title, year, genres, cast);
        this.no = no;
        this.seasons = seas;
    }

    public int getNo() {
        return this.no;
    }

    public ArrayList<SeasonModel> getSeasons() {
        return this.seasons;
    }

    public double getValueRatingSerial() {
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
                sezval /= seznr;
                val += sezval;
                nr++;
            }
        }

        if (nr == 0) {
            return 0;
        }

        return val / nr;
    }

    public int getValueDurationSerial() {
        int duration = 0;

        for (SeasonModel s:seasons) {
            duration += s.getDuration();
        }

        return duration;
    }

    public static int checkYearAndGenre(List <String> year, List <String> genre, SerialSeason s) {
        boolean ok1 = false;
        boolean ok2 = false;

        if (year.get(0) != null) {
            for (String y:year) {
                if (Integer.toString(s.getYear()).equals(y)) {
                    ok1 = true;
                    break;
                }
            }
        }

        if (genre.get(0) != null) {
            List<String> movieGen = s.getGenres();

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

    public static ArrayList<SerialSeason> sortSerialsWithValues(ArrayList<SerialSeason> serials, double[] values, String SortType) {
        ArrayList<SerialSeason> sortedSerials = serials;
        int len = sortedSerials.size();
        double aux;

        if (SortType.equals("asc")) {
            for (int i = 0 ; i < len - 1 ; i++) {
                for (int j = i + 1 ; j < len ; j++) {
                    if (values[i] > values[j]) {
                        aux = values[i];
                        values[i] = values[j];
                        values[j] = aux;
                        Collections.swap(sortedSerials, i, j);
                    } else if (values[i] == values[j]) {
                        if (sortedSerials.get(i).getTitle().compareTo(sortedSerials.get(j).getTitle()) > 0) {
                            Collections.swap(sortedSerials, i, j);
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
                        Collections.swap(sortedSerials, i, j);
                    } else if (values[i] == values[j]) {
                        if (sortedSerials.get(i).getTitle().compareTo(sortedSerials.get(j).getTitle()) < 0) {
                            Collections.swap(sortedSerials, i, j);
                        }
                    }
                }
            }
        }

        return sortedSerials;
    }

    public static String getRezStr(ArrayList<SerialSeason> sortSerials) {
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

    public static String getRatSerial(int N, List<String> year, List<String> genre, ArrayList<SerialSeason> serials, String SortType) {
        ArrayList<SerialSeason> sortSerials = new ArrayList<>();
        double[] sortVal = new double[serials.size()];
        int len = 0;

        double rating;
        int ok;

        for (SerialSeason s:serials) {
            rating = s.getValueRatingSerial();

            ok = checkYearAndGenre(year, genre, s);

            if (rating != 0.0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == 3) {
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

        sortSerials = SerialSeason.sortSerialsWithValues(sortSerials, sortVal, SortType);

        if (len > N) {
            sortSerials.subList(N, len).clear();
        }

        return SerialSeason.getRezStr(sortSerials);
    }

    public static String getFavSerials(int N, List<String> year, List<String> genre, ArrayList<SerialSeason> serials, ArrayList<User> users, String sortType) {
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

            ok = checkYearAndGenre(year, genre, s);

            if (no != 0.0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == 3) {
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

        if (len > N) {
            sortedSerials.subList(N, len).clear();
        }

        return SerialSeason.getRezStr(sortedSerials);
    }

    public static String getLongestSerial(int N, List<String> year, List<String> genre, ArrayList<SerialSeason> serials, String sortType) {
        ArrayList<SerialSeason> sortedSerials = new ArrayList<>();
        double[] dur = new double[serials.size()];
        int ok;
        int len = 0;
        int duration;

        for (SerialSeason s:serials) {
            ok = checkYearAndGenre(year, genre, s);

            duration = s.getValueDurationSerial();

            if (duration != 0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == 3) {
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

        if (len > N) {
            sortedSerials.subList(N, len).clear();
        }

        return SerialSeason.getRezStr(sortedSerials);
    }

    public static String getMostViewedSerial(int N, List<String> year, List<String> genre, ArrayList<SerialSeason> serials, ArrayList<User> users, String sortType) {
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

            ok = checkYearAndGenre(year, genre, s);

            if (no != 0.0) {
                if (year.get(0) != null) {
                    if (genre.get(0) != null) {
                        if (ok == 3) {
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

        if (len > N) {
            sortedSerials.subList(N, len).clear();
        }

        return SerialSeason.getRezStr(sortedSerials);
    }
}
