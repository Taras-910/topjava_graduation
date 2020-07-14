package ru.javawebinar.topjava.web.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.dish.DishController;
import ru.javawebinar.topjava.web.restaurant.RestaurantController;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.MenuUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Controller
public class MenuController {
    private static final Logger log = LoggerFactory.getLogger(DishController.class);
    private final DishController dishController;
    private final RestaurantController restaurantController;

    public MenuController(DishController dishController, RestaurantController restaurantController) {
        this.dishController = dishController;
        this.restaurantController = restaurantController;
    }

    public List<Menu> getAll(List<Vote> votes, LocalDate date) {
        log.info("getAll menus with votes {} for date {}", votes, date);
        return toListMenus(restaurantController.getWithDishes(date), votes, date);
    }

    public Menu getMenuByRestaurantId(int restaurantId, List<Vote> votes, LocalDate date) {
        log.info("getMenu for restaurantId {} with votes {} for date {}", restaurantId, votes, date);
        return toMenu(restaurantController.getById(restaurantId, date), votes, date);
    }

    public void deleteRestaurant(int id) {
        log.info("deleteRestaurant with id {}", id);
        restaurantController.delete(id);
    }

    public void deleteDishes(int restaurantId, LocalDate date) {
        log.info("deleteDishes with restaurantId {} and date {}", restaurantId, date);
        dishController.deleteAll(restaurantId, date);
    }

    @Transactional
    public boolean deleteDishById(int dishId, int restaurantId) {
        log.info("deleteDishById with dishId {} and restaurantId {}", dishId, restaurantId);
        if (countLowerLimit(restaurantController.getById(restaurantId, thisDay))) {
            return false;
        }
        dishController.delete(dishId, restaurantId);
        return false;
    }

    @Transactional
    public Restaurant createRestaurantAndDishes(Restaurant newRestaurantWithDishes) {
        log.info("createRestaurantAndDishes with newRestaurantWithDishes {}", newRestaurantWithDishes);
        checkNotFound(countWithin(newRestaurantWithDishes, null),
                "menu so dishes number should be within from 2 to 5");
        Restaurant restaurant = restaurantController.create(newRestaurantWithDishes);
        restaurant.setDishes(dishController.createAll(newRestaurantWithDishes.getDishes(), restaurant.getId()));
        return restaurant;
    }

    public Restaurant addDishes(Restaurant restaurant) {
        log.info("addDishes for restaurant {}", restaurant);
        checkNotFound(countWithin(restaurant, restaurantController.getById(restaurant.getId(), thisDay)),
                "menu so dishes number should be within from 2 to 5");
        restaurant.setDishes(dishController.createAll(restaurant.getDishes(), restaurant.getId()));
        return restaurant;
    }

    public void updateDish(Dish dish, int dishId, int restaurantId) {
        log.info("updateDish dish {} with dishId {} for restaurantId {}",dish, dishId, restaurantId);
        dishController.update(dish, dishId, restaurantId);
    }

    public void updateRestaurant(Restaurant restaurant, int restaurantId) {
        log.info("updateRestaurant for restaurant {} and restaurantId {}", restaurant, restaurantId);
        restaurantController.update(restaurant, restaurantId);
    }
}
