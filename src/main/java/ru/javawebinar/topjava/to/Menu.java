package ru.javawebinar.topjava.to;

import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.model.Vote;

import java.time.LocalDate;
import java.util.List;

public class Menu {
    private Integer id;
    private Restaurant restaurant;
    private LocalDate date;
    private List<Vote> votes;
    boolean toVote = false;

    public Menu(Integer id, Restaurant restaurant, LocalDate date, List<Vote> votes, boolean toVote) {
        this.id = id;
        this.restaurant = restaurant;
        this.date = date;
        this.votes = votes;
        this.toVote = toVote;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Restaurant getRestaurant() { return restaurant; }

    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public List<Vote> getVotes() { return votes; }

    public void setVotes(List<Vote> votes) { this.votes = votes; }

    public boolean isToVote() { return toVote; }

    public void setToVote(boolean toVote) { this.toVote = toVote; }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", " + restaurant +
                ", date=" + date +
                ", votes=" + votes +
                ", toVote=" + toVote +
                '}';
    }
}
