package ru.javawebinar.topjava.web.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.to.Menu;

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

    public List<Menu> getMenusToday() {
        log.info("getAllMenus");
        return toListMenus(restaurantController.getAllWithDishesOfDate(thisDay), voteController.authVote(), thisDay);
    }

    public Menu getMenuByRestaurantId(int restaurantId) {
        log.info("getMenu for restaurantId {}", restaurantId);
        return toMenu(restaurantController.getByIdWithDishesOfDate(restaurantId, thisDay), voteController.authVote(), thisDay);
    }

    public void deleteRestaurantAndDishes(int id) {
        log.info("deleteRestaurant with id {}", id);
        restaurantController.delete(id);
    }

    public void deleteDishes(int restaurantId, LocalDate date) {
        log.info("deleteDishes with restaurantId {} and date {}", restaurantId, date);
        dishController.deleteListOfMenu(restaurantId, date);
    }

    @Transactional
    public void deleteDishById(int dishId, int restaurantId) {
        log.info("deleteDishById with dishId {} and restaurantId {}", dishId, restaurantId);
        checkNotFound(countLowerLimit(restaurantController.getByIdWithDishesOfDate(restaurantId, thisDay).getDishes()),
                dishId+" so as dishes number of menu should be at least 2 ");
        dishController.delete(dishId, restaurantId);
    }

    @Transactional
    public Restaurant createRestaurantAndDishes(Restaurant restaurant){
        log.info("createRestaurantAndDishes restaurant {}", restaurant);
        checkNotFound(countWithin(restaurant.getDishes(), null),
                "menu so dishes number should be within from 2 to 5");
        List<Dish> dishes = restaurant.getDishes();
        Restaurant createdRestaurant = restaurantController.create(restaurant);
        List<Dish> createdDishes;
            createdDishes = (List<Dish>) dishController.createListOfMenu(dishes, createdRestaurant.id());
        createdRestaurant.setDishes(createdDishes);
        return createdRestaurant;
    }

    @Transactional
    public Restaurant addDishes(Restaurant restaurant) {
        log.info("addDishes for restaurant {}", restaurant);
        checkNotFound(countWithin(restaurant.getDishes(), restaurantController.getByIdWithDishesOfDate(restaurant.id(), thisDay).getDishes()),
                "menu so dishes number should be within from 2 to 5");
            restaurant.setDishes(dishController.createListOfMenu(restaurant.getDishes(), restaurant.id()));
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
/*    @Transactional
    @GetMapping
    public List<Menu> getAllToday(){
        LocalDate date = thisDay;
        log.info("getMenusToday date {}", date);
        boolean voteToday = voteRestController.authVote();
        return toListMenus(restaurantRestController.getAllWithDishesOfDate(date), voteToday, date);
    }

    @Transactional
    @GetMapping("/restaurants/{id}")
    public Menu getByRestaurantIdAndDate(@PathVariable(name = "id") int restaurantId, @RequestParam LocalDate date) {
        log.info("getTodayMenu for restaurant {}", restaurantId);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantId, date), voteRestController.authVote(), thisDay);
    }

    @Transactional
    @GetMapping("/restaurants/names/{name}")
    public Menu getByRestaurantNameWithDishesAndDate(@PathVariable String name, @RequestParam LocalDate date) {
        log.info("getTodayMenu for restaurant {}", name);
        Restaurant restaurantDB = restaurantRestController.getByName(name);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantDB.id(), date), voteRestController.authVote(), thisDay);
    }

    @Transactional
    @GetMapping(value = "/date/{date}")
    public List<Menu> getAllByDate(@PathVariable LocalDate date){
        log.info("getMenusToday date {}", date);
        return toListMenus(restaurantRestController.getAllWithDishesOfDate(date), voteRestController.authVote(), date);
    }

    @DeleteMapping("/restaurants/{id}/date/{date}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteByRestaurantAndDate(@PathVariable(name = "id") int restaurantId, @PathVariable LocalDate date) {
        log.info("delete by restaurant {} and date {}", restaurantId, date);
        dishRestController.deleteAllForRestaurantByDate(restaurantId, date);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dish>> createMenuForNewRestaurant(@Valid @RequestBody List<Dish> dishes,
                                                                 @RequestParam(name = "name") String restaurantName){
        log.info("create Menu for newRestaurant {} with quantity Dishes {}", restaurantName, dishes.size());
        Restaurant createdRestaurant = restaurantRestController.create(new Restaurant(null, restaurantName)).getBody();
        return dishRestController.createAllForMenu(dishes, createdRestaurant.id());
      }

    @Transactional
    @PutMapping(value = "/restaurants/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dish>> updateCreateMenuForRestaurantId(@Valid @RequestBody List<Dish> dishes,
                                                                      @PathVariable(name = "id") int restaurantId){
        log.info("create Menu for newRestaurant {} with Dishes number {}", restaurantId, dishes.size());
        return dishRestController.createAllForMenu(dishes, restaurantId);
    }*/
