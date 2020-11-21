package action;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private final int actionId;
    private final String actionType;
    private final String type;
    private final String username;
    private final String objectType;
    private final String sortType;
    private final String criteria;
    private final String title;
    private final String genre;
    private final int number;
    private final double grade;
    private final int seasonNumber;
    private final List<List<String>> filters = new ArrayList<>();

    public Action(final int seasonNumber, final double grade, final String title, final String username, final String objectType, final int number, final String criteria, final String sortType, final String genre, final String actionType, final int actionId, final List<List<String>> filters, final String type) {
        this.seasonNumber = seasonNumber;
        this.grade = grade;
        this.title = title;
        this.username = username;
        this.objectType = objectType;
        this.number = number;
        this.criteria = criteria;
        this.sortType = sortType;
        this.genre = genre;
        this.actionType = actionType;
        this.actionId = actionId;
        this.filters.addAll(filters);
        this.type = type;
    }

    public int getActionId() {
        return actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getSortType() {
        return sortType;
    }

    public String getCriteria() {
        return criteria;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getNumber() {
        return number;
    }

    public double getGrade() {
        return grade;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public List<List<String>> getFilters() {
        return filters;
    }
}
