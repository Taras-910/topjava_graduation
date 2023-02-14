package ru.javawebinar.topjava.to;

import io.swagger.annotations.ApiModelProperty;
import ru.javawebinar.topjava.model.Dish;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Menu {

    @NotNull
    @ApiModelProperty(hidden = true)
    Integer restaurantId;

    @NotNull
    private String restaurantName;

    @NotNull
    private List<Dish> dishes;

    private boolean toVote = false;

    public Menu(@NotNull int restaurantId, @NotNull String restaurantName, @NotNull List<Dish> dishes, boolean toVote) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.dishes = dishes;
        this.toVote = toVote;
    }

    public Menu(Menu menu){
        this(menu.getRestaurantId() ,menu.getRestaurantName() ,menu.getDishes() ,menu.isToVote());
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public boolean isToVote() {
        return toVote;
    }

    public void setToVote(boolean toVote) {
        this.toVote = toVote;
    }

    @Override
    public String toString() {
        return "Menu\n{" +
                "restaurantId=" + restaurantId +
                ",\n restaurantName='" + restaurantName + '\'' +
                ",\n dishes=" + dishes +
                ",\n toVote=" + toVote +
                '}';
    }
}
