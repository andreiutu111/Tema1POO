package getactor;

import actor.ActorsAwards;
import video.Movie;
import video.SerialSeason;

import java.util.ArrayList;
import java.util.Map;

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

    private static double getMed (String actor, ArrayList<Movie> movies, ArrayList<SerialSeason> seasons){
        double rez = 0;
        int nr = 0;

        for (Movie m:movies) {
            int poz  = m.getCast().indexOf(actor);
            if (poz != -1) {
                rez += m.getRating();
                nr++;
            }
        }

        for (SerialSeason s:seasons) {
            int poz  = s.getCast().indexOf(actor);
            if (poz != -1) {
                rez += s.getRating();
                nr++;
            }
        }

        rez /= nr;
        return rez;
    }

    public static ArrayList<Actor> getAverage(int N, ArrayList<Actor> actors, ArrayList<Movie> movies, ArrayList<SerialSeason> seasons) {
        ArrayList<Actor> sortAct = new ArrayList<>();
        sortAct.addAll(actors);

        int lenArray = sortAct.size();
        for (int i = 0 ; i < lenArray - 1; i++){
            for (int j = i + 1 ; j < lenArray ; j++){
                if (getMed(sortAct.get(i).getName(), movies, seasons) > getMed(sortAct.get(j).getName(), movies, seasons)){
                    Actor aux = sortAct.get(i);
                    sortAct.remove(i);
                    sortAct.add(i, sortAct.get(j));
                    sortAct.remove(j);
                    sortAct.add(j, aux);
                }
            }
        }

        if (lenArray > N + 1) {
            sortAct.subList(N + 1, lenArray).clear();
        }

        return sortAct;
    }
}
