package ru.javawebinar.topjava.web.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.util.exception.NotFoundException;

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
    private final VoteController voteController;

    public MenuController(DishController dishController, RestaurantController restaurantController, VoteController voteController) {
        this.dishController = dishController;
        this.restaurantController = restaurantController;
        this.voteController = voteController;
    }

    @Transactional
    public List<Menu> getMenusToday(){
        LocalDate date = thisDay;
        log.info("getMenusToday date {}", date);
        boolean voteToday = voteController.authVote();
        return toListMenus(restaurantController.getAllWithDishesOfDate(date), voteToday, date);
    }

    public Menu getMenuByRestaurantId(int restaurantId) {
        log.info("getMenu for restaurantId {}", restaurantId);
        return toMenu(restaurantController.getByIdWithDishesOfDate(restaurantId, thisDay), voteController.authVote(), thisDay);
    }

    @Transactional
    public Menu getByRestaurantIdAndDate(int restaurantId, LocalDate date) {
        log.info("getTodayMenu for restaurant {}", restaurantId);
        return toMenu(restaurantController.getByIdWithDishesOfDate(restaurantId, date), voteController.authVote(), thisDay);
    }

    @Transactional
    public Menu getByRestaurantNameAndDate(String name, LocalDate date) {
        log.info("getTodayMenu for restaurant {}", name);
        Restaurant restaurantDB = restaurantController.getByName(name);
        return toMenu(restaurantController.getByIdWithDishesOfDate(restaurantDB.id(), date), voteController.authVote(), thisDay);
    }

    public void deleteRestaurantAndDishes(int id) {
        log.info("deleteRestaurant with id {}", id);
        restaurantController.delete(id);
    }

    public void deleteListOfMenu(int restaurantId, LocalDate date) {
        log.info("deleteDishes with restaurantId {} and date {}", restaurantId, date);
        dishController.deleteListOfMenu(restaurantId, date);
    }

    @Transactional
    public void deleteDishOfMenu(int dishId, int restaurantId) {
        log.info("deleteDishById with dishId {} and restaurantId {}", dishId, restaurantId);
        checkNotFound(countLowerLimit(restaurantController.getByIdWithDishesOfDate(restaurantId, thisDay).getDishes()),
                dishId+" so as dishes number of menu should be at least 2 ");
        dishController.delete(dishId, restaurantId);
    }

    public Menu createNewRestaurantByNameWithDishes(List<Dish> dishes, String restaurantName){
        log.info("createByRestaurantAndDishes {} with quantity Dishes {}", restaurantName, dishes);
        Restaurant createdRestaurant = null;
        Menu menu = null;
        try {
            Assert.notNull(dishes, "dishes must not be null");
            dishes.forEach(d -> Assert.notNull(d, "dish must not be null"));
            createdRestaurant = restaurantController.create(new Restaurant(null, restaurantName));
            List<Dish> created =  dishController.createListOfMenu(dishes, createdRestaurant.id());
            menu = toMenu(restaurantController.getByIdWithDishesOfDate(createdRestaurant.id(), thisDay), voteController.authVote(), thisDay);
        } catch (IllegalArgumentException | DataIntegrityViolationException | TransactionSystemException | NullPointerException e) {
            throw new NotFoundException(" Illegal argument dishes=" + dishes + " or restaurantName=" + restaurantName);
        }
        return menu;
    }

    @Transactional
    public List<Dish> addListOfMenuForRestaurantName(List<Dish> dishes, String restaurantName){
        log.info("createByRestaurantAndDishes {} with quantity Dishes {}", restaurantName, dishes);
        Restaurant restaurantDB = restaurantController.getByName(restaurantName);
        return dishController.createListOfMenu(dishes, restaurantDB.id());
    }

    @Transactional
    public List<Dish> addListOfMenuForRestaurantId(List<Dish> dishes, int restaurantId){
        log.info("create Menu for newRestaurant {} with Dishes number {}", restaurantId, dishes);
        return dishController.createListOfMenu(dishes, restaurantId);
    }

    @Transactional
    public Restaurant createNewRestaurantWithDishes(Restaurant restaurant){
        log.info("createByRestaurantAndDishes restaurant {}", restaurant);
        Restaurant createdRestaurant = null;
        try {
            createdRestaurant = restaurantController.create(restaurant);
            List<Dish> createdDishes;
            createdDishes = (List<Dish>) dishController.createListOfMenu(restaurant.getDishes(), createdRestaurant.id());
            createdRestaurant.setDishes(createdDishes);
        } catch (IllegalArgumentException | DataIntegrityViolationException | TransactionSystemException | NullPointerException e) {
            throw new NotFoundException(" Illegal argument restaurant=" + restaurant);
        }
        return createdRestaurant;
    }

}

