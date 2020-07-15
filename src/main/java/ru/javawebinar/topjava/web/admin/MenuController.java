package ru.javawebinar.topjava.web.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.dish.DishController;
import ru.javawebinar.topjava.web.restaurant.RestaurantController;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.MenuUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.web.user.ProfileController.authVote;

@Controller
public class MenuController {
    private static final Logger log = LoggerFactory.getLogger(DishController.class);

    private final DishController dishController;
    private final RestaurantController restaurantController;

    public MenuController(DishController dishController, RestaurantController restaurantController) {
        this.dishController = dishController;
        this.restaurantController = restaurantController;
    }

    public List<Menu> getAllMenusOfDay() {
        log.info("getAllMenus");
        return toListMenus(restaurantController.getAllByDateWithDishes(thisDay), authVote(), thisDay);
    }

    public Menu getMenuByRestaurantId(int restaurantId) {
        log.info("getMenu for restaurantId {}", restaurantId);
        return toMenu(restaurantController.getByIdAndDate(restaurantId, thisDay), authVote(), thisDay);
    }

    public void deleteRestaurantAndDishes(int id) {
        log.info("deleteRestaurant with id {}", id);
        restaurantController.delete(id);
    }

    public void deleteDishes(int restaurantId, LocalDate date) {
        log.info("deleteDishes with restaurantId {} and date {}", restaurantId, date);
        dishController.deleteAll(restaurantId, date);
    }

    @Transactional
    public void deleteDishById(int dishId, int restaurantId) {
        log.info("deleteDishById with dishId {} and restaurantId {}", dishId, restaurantId);
        checkNotFound(countLowerLimit(restaurantController.getByIdAndDate(restaurantId, thisDay).getDishes()),
                dishId+" so as dishes number of menu should be at least 2 ");
        dishController.delete(dishId, restaurantId);
    }

    @Transactional
    public Restaurant createRestaurantAndDishes(Restaurant restaurant) {
        log.info("createRestaurantAndDishes restaurant {}", restaurant);
        checkNotFound(countWithin(restaurant.getDishes(), null),
                "menu so dishes number should be within from 2 to 5");
        List<Dish> dishes = restaurant.getDishes();
        Restaurant createdRestaurant = restaurantController.create(restaurant);
        List<Dish> createdDishes = dishController.createAll(dishes, createdRestaurant.id());
        createdRestaurant.setDishes(createdDishes);
        return createdRestaurant;
    }

    @Transactional
    public Restaurant addDishes(Restaurant restaurant) {
        log.info("addDishes for restaurant {}", restaurant);
        checkNotFound(countWithin(restaurant.getDishes(), restaurantController.getByIdAndDate(restaurant.id(), thisDay).getDishes()),
                "menu so dishes number should be within from 2 to 5");
        restaurant.setDishes(dishController.createAll(restaurant.getDishes(), restaurant.id()));
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
