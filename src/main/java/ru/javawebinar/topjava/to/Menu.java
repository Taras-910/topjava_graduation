package ru.javawebinar.topjava.to;

import ru.javawebinar.topjava.model.Restaurant;

import java.time.LocalDate;

public class Menu {
    private Restaurant restaurant;
    private LocalDate date;
    private boolean voteAuth;
    private boolean toVote = false;

    public Menu(Restaurant restaurant, LocalDate date, boolean voteAuth, boolean toVote) {
        this.restaurant = restaurant;
        this.date = date;
        this.voteAuth = voteAuth;
        this.toVote = toVote;
    }

    public Restaurant getRestaurant() { return restaurant; }

    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public boolean isVoteAuth() { return voteAuth; }

    public void setVoteAuth(boolean voteAuth) { this.voteAuth = voteAuth; }

    public boolean isToVote() { return toVote; }

    public void setToVote(boolean toVote) { this.toVote = toVote; }

    @Override
    public String toString() {
        return "Menu{" +
                ", restaurant=" + restaurant +
                ", date=" + date +
                ", voteAuth=" + voteAuth +
                ", toVote=" + toVote +
                '}';
    }
}
