package main;

import action.Action;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import entertainment.Season;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;

import getactor.Actor;
import org.json.simple.JSONArray;
import user.User;
import video.Movie;
import video.SeasonModel;
import video.SerialSeason;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        ArrayList<User> extractedUsers = new ArrayList<>();
        List<UserInputData> listUsers = input.getUsers();
        if (listUsers.size() > 0) {
            for (UserInputData u : listUsers) {
                User extractNewUser = new User(u.getUsername(), u.getSubscriptionType(),
                        u.getFavoriteMovies(), u.getHistory());
                extractedUsers.add(extractNewUser);
            }
        }

        ArrayList<Movie> extractedMovies = new ArrayList<>();
        List<MovieInputData> listMovies = input.getMovies();
        if (listMovies.size() > 0) {
            for (MovieInputData m : listMovies) {
                Movie extractNewMovie = new Movie(m.getTitle(), m.getYear(),
                        m.getGenres(), m.getCast(), m.getDuration());
                extractedMovies.add(extractNewMovie);
            }
        }


        ArrayList<SerialSeason> extractedSerialsSeason = new ArrayList<>();
        List<SerialInputData> listSerialsSeason = input.getSerials();
        if (listSerialsSeason.size() > 0) {
            for (SerialInputData s : listSerialsSeason) {
                ArrayList<Season> seasonsSer = s.getSeasons();
                ArrayList<SeasonModel> newseason = new ArrayList<>();

                for (int i = 0; i < seasonsSer.size(); i++) {
                    SeasonModel seas = new SeasonModel(i, seasonsSer.get(i).getDuration());
                    newseason.add(seas);
                }

                SerialSeason extractNewSerials = new SerialSeason(s.getTitle(),
                        s.getYear(), s.getGenres(),
                        s.getCast(), s.getNumberSeason(), newseason);
                extractedSerialsSeason.add(extractNewSerials);
            }
        }

        ArrayList<Actor> extractedActors = new ArrayList<>();
        List<ActorInputData> listActors = input.getActors();
        if (listActors.size() > 0) {
            for (ActorInputData a : listActors) {
                Actor extractNewActor = new Actor(a.getName(), a.getCareerDescription(),
                        a.getFilmography(), a.getAwards());
                extractedActors.add(extractNewActor);
            }
        }

        ArrayList<Action> extractedActions = new ArrayList<>();
        List<ActionInputData> listActions = input.getCommands();
        if (listActions.size() > 0) {
            for (ActionInputData a : listActions) {
                Action extractNewAction = new Action(a.getSeasonNumber(), a.getGrade(),
                        a.getTitle(), a.getUsername(), a.getObjectType(), a.getNumber(),
                        a.getCriteria(), a.getSortType(), a.getGenre(), a.getActionType(),
                        a.getActionId(), a.getFilters(), a.getType());
                extractedActions.add(extractNewAction);
            }

            for (Action extractedAction : extractedActions) {
                String actionType = extractedAction.getActionType();
                int actId = extractedAction.getActionId();
                String output = null;

                if (actionType.equals("command")) {
                    String subType = extractedAction.getType();
                    String title = extractedAction.getTitle();
                    User usr = null;

                    for (User u : extractedUsers) {
                        if (u.getUsername().equals(extractedAction.getUsername())) {
                            usr = u;
                            break;
                        }
                    }

                    if (subType.equals("view")) {
                        output = usr.setView(title);
                    } else if (subType.equals("favorite")) {
                        output = usr.setFavorite(title);
                    } else if (subType.equals("rating")) {
                        output = usr.setRating(extractedMovies, extractedSerialsSeason, title,
                                extractedAction.getGrade(), extractedAction.getSeasonNumber());
                    }

                    if (output != null) {
                        arrayResult.add(fileWriter.writeFile(actId, title, output));
                    }
                } else if (actionType.equals("query")) {
                    String crit = extractedAction.getCriteria();
                    String sortType = extractedAction.getSortType();
                    String title = extractedAction.getTitle();

                    if (crit.equals("average")) {
                        output = Actor.getAverage(extractedAction.getNumber(), extractedActors,
                                extractedMovies, extractedSerialsSeason, sortType);
                    } else if (crit.equals("awards")) {
                        output = Actor.getAwaAct(extractedAction.getFilters().get(2 + 1),
                                extractedActors, sortType);
                    } else if (crit.equals("filter_description")) {
                        output = Actor.getFilterDescription(extractedAction.getFilters().get(2),
                                extractedActors, sortType);
                    } else if (crit.equals("ratings")) {
                        if (extractedAction.getObjectType().equals("movies")) {
                            output = Movie.getRatMov(extractedAction.getNumber(),
                                    extractedAction.getFilters().get(0),
                                    extractedAction.getFilters().get(1), extractedMovies, sortType);
                        } else if (extractedAction.getObjectType().equals("shows")) {
                            output = SerialSeason.getRatSerial(extractedAction.getNumber(),
                                    extractedAction.getFilters().get(0),
                                    extractedAction.getFilters().get(1),
                                    extractedSerialsSeason, sortType);
                        }
                    } else if (crit.equals("favorite")) {
                        if (extractedAction.getObjectType().equals("movies")) {
                            output = Movie.getFavMov(extractedAction.getNumber(),
                                    extractedAction.getFilters().get(0),
                                    extractedAction.getFilters().get(1),
                                    extractedMovies, extractedUsers, sortType);
                        } else if (extractedAction.getObjectType().equals("shows")) {
                            output = SerialSeason.getFavSerials(extractedAction.getNumber(),
                                    extractedAction.getFilters().get(0),
                                    extractedAction.getFilters().get(1),
                                    extractedSerialsSeason, extractedUsers, sortType);
                        }
                    } else if (crit.equals("longest")) {
                        if (extractedAction.getObjectType().equals("movies")) {
                            output = Movie.getLongestMovie(extractedAction.getNumber(),
                                    extractedAction.getFilters().get(0),
                                    extractedAction.getFilters().get(1),
                                    extractedMovies, sortType);
                        } else if (extractedAction.getObjectType().equals("shows")) {
                            output = SerialSeason.getLongestSerial(extractedAction.getNumber(),
                                    extractedAction.getFilters().get(0),
                                    extractedAction.getFilters().get(1),
                                    extractedSerialsSeason, sortType);
                        }
                    } else if (crit.equals("most_viewed")) {
                        if (extractedAction.getObjectType().equals("movies")) {
                            output = Movie.getMostViewedMovie(extractedAction.getNumber(),
                                    extractedAction.getFilters().get(0),
                                    extractedAction.getFilters().get(1),
                                    extractedMovies, extractedUsers, sortType);
                        } else if (extractedAction.getObjectType().equals("shows")) {
                            output = SerialSeason.getMostViewedSerial(extractedAction.getNumber(),
                                    extractedAction.getFilters().get(0),
                                    extractedAction.getFilters().get(1),
                                    extractedSerialsSeason, extractedUsers, sortType);
                        }
                    } else if (crit.equals("num_ratings")) {
                        output = User.getNumOfRat(extractedAction.getNumber(),
                                extractedUsers, sortType);
                    }

                    if (output != null) {
                        arrayResult.add(fileWriter.writeFile(actId, title, output));
                    }
                } else if (actionType.equals("recommendation")) {
                    String type = extractedAction.getType();
                    User usr = null;

                    for (User u : extractedUsers) {
                        if (u.getUsername().equals(extractedAction.getUsername())) {
                            usr = u;
                            break;
                        }
                    }

                    if (type.equals("standard")) {
                        output = usr.getStandard(extractedMovies, extractedSerialsSeason);
                    } else if (type.equals("best_unseen")) {
                        output = usr.getBestUn(extractedMovies, extractedSerialsSeason);
                    } else if (type.equals("popular")) {
                        if (usr.getCategory().equals("PREMIUM")) {
                            output = usr.getPopular(extractedMovies,
                                    extractedSerialsSeason, extractedUsers);
                        } else {
                            output = "PopularRecommendation cannot be applied!";
                        }
                    } else if (type.equals("favorite")) {
                        if (usr.getCategory().equals("PREMIUM")) {
                            output = usr.getRecFav(extractedMovies,
                                    extractedSerialsSeason, extractedUsers);
                        } else {
                            output = "FavoriteRecommendation cannot be applied!";
                        }
                    } else if (type.equals("search")) {
                        if (usr.getCategory().equals("PREMIUM")) {
                            output = usr.getSearch(extractedMovies,
                                    extractedSerialsSeason, extractedAction.getGenre());
                        } else {
                            output = "SearchRecommendation cannot be applied!";
                        }
                    }

                    if (output != null) {
                        arrayResult.add(fileWriter.writeFile(actId,
                                extractedAction.getUsername(), output));
                    }
                }
            }
        }

        fileWriter.closeJSON(arrayResult);
    }
}
