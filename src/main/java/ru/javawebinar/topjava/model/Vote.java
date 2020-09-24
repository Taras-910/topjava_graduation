package ru.javawebinar.topjava.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "local_date"}, name = "votes_idx")})
public class Vote extends AbstractBaseEntity {

    @Column(name = "local_date", nullable = false)
    @NotNull
    private Date localDate;

    @Column(name = "restaurant_id", nullable = false)
    @NotNull
    private Integer restaurantId;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Integer userId;

    public Vote(){}

    public Vote(Integer id, @NotNull Date localDate, @NotNull Integer restaurantId, @NotNull Integer userId) {
        super(id);
        this.localDate = localDate;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    public Vote(Vote v) {
        this(v.getId(), v.getLocalDate(), v.getRestaurantId(), v.getUserId());
    }

    public Date getLocalDate() { return localDate; }

    public void setLocalDate(Date date) { this.localDate = date; }

    public Integer getRestaurantId() { return restaurantId; }

    public void setRestaurantId(Integer restaurantId) { this.restaurantId = restaurantId; }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer user) { this.userId = user; }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", localDate=" + localDate +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                '}';
    }
}
