package ru.javawebinar.topjava.web.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.menu.MenuController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Controller
public class AdminMenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final MenuController menuController;

    public AdminMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    @Transactional
    public List<Menu> getAllMenus() {
        log.info("getAllMenus");
        return menuController.getAll(new ArrayList<Vote>(), thisDay);
    }

    @Transactional
    public Menu getMenuByRestaurantId(int restaurantId) {
        log.info("getMenuByRestaurantId with restaurantId {}", restaurantId);
        return menuController.getMenuByRestaurantId(restaurantId, new ArrayList<Vote>(), thisDay);
    }

    public void deleteRestaurantAndDishes(int restaurantId) {
        log.info("deleteRestaurantAndDishes with restaurantId {}", restaurantId);
        menuController.deleteRestaurant(restaurantId);
    }

    public void deleteDishes(int restaurantId, LocalDate date) {
        log.info("deleteDishes with restaurantId {} and date {}", restaurantId, date);
        menuController.deleteDishes(restaurantId, date);
    }

    @Transactional
    public void deleteDishById(int dishId, int restaurantId) {
        log.info("deleteDishById  with dishId {} and restaurantId {}", dishId, restaurantId);
        checkNotFound(menuController.deleteDishById(dishId, restaurantId),
                dishId+" so as dishes number of menu should be at least 2 ");
    }

    public Restaurant createRestaurantAndDishes(Restaurant newRestaurantWithDishes) {
        log.info("createRestaurantAndDishes with newRestaurantWithDishes {} ", newRestaurantWithDishes);
        return menuController.createRestaurantAndDishes(newRestaurantWithDishes);
    }

    public Restaurant addDishes(Restaurant restaurant) {
        log.info("addDishes with restaurant {}", restaurant);
        return menuController.addDishes(restaurant);
    }

    public void updateDish(Dish dish, int dishId, int restaurantId) {
        log.info("updateDish dish {} with dishId {} and restaurantId {}", dish, dishId, restaurantId);
        menuController.updateDish(dish, dishId, restaurantId);
    }

    public void updateRestaurant(Restaurant restaurant, int restaurantId) {
        log.info("updateRestaurant restaurant {} with restaurantId {}", restaurant, restaurantId);
        menuController.updateRestaurant(restaurant, restaurantId);
    }
}
