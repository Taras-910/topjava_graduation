package ru.javawebinar.topjava.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dishes")
public class Dish extends AbstractBaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(name="date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name="price", nullable = false)
    @Range(max = 100, min = 0)
    private float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(String name, LocalDate date, float price) {
        this(null, name, date, price);
    }

    public Dish(Integer id, String name, LocalDate date, float price) {
        super(id);
        this.name = name;
        this.date = date;
        this.price = price;
    }

    public Dish(Dish d) {
        this(d.getId(), d.getName(), d.getDate(), d.getPrice());
    }

    public String getName() { return name; }

    public float getPrice() {
        return price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public void setName(String name) { this.name = name; }

    public void setPrice(float price) {
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
                ", dateTime=" + date +
                ", price='" + price + '\'' +
                '}';
    }
}
