package ru.javawebinar.topjava.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "dishes", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"local_date", "name", "restaurant_id"}, name = "dishes_date_restaurant_idx")})
public class Dish extends AbstractBaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(name="local_date", nullable = false)
    @NotNull
    private Date localDate;

    @Column(name="price", nullable = false)
    @Range(max = 100000, min = 0)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    @ApiModelProperty(hidden = true)
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(String name, Date localDate, Integer price) {
        this(null, name, localDate, price);
    }

    public Dish(Integer id, String name, Date localDate, Integer price) {
        super(id);
        this.name = name;
        this.localDate = localDate;
        this.price = price;
    }

    public Dish(Dish d) {
        this(d.getId(), d.getName(), d.getLocalDate(), d.getPrice());
    }

    public String getName() { return name; }

    public Integer getPrice() {
        return price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Date getLocalDate() { return localDate; }

    public void setLocalDate(Date date) { this.localDate = date; }

    public void setName(String name) { this.name = name; }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name=" + name +
                ", localDate=" + localDate +
                ", price='" + price + '\'' +
                '}';
    }
}
