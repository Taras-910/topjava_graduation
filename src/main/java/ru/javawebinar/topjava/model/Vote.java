package ru.javawebinar.topjava.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes")
public class Vote extends AbstractBaseEntity {

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    private Integer restaurantId;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Integer userId;

    public Vote(){}

    public Vote(Integer id, @NotNull LocalDate date, @NotNull Integer restaurantId, @NotNull Integer userId) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public Integer getRestaurantId() { return restaurantId; }

    public void setRestaurantId(Integer restaurantId) { this.restaurantId = restaurantId; }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer user) { this.userId = user; }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", date=" + date +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                '}';
    }
}
