package ru.javawebinar.topjava.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.jpa.DishController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.MenuUtil.toListMenus;
import static ru.javawebinar.topjava.util.MenuUtil.toMenu;

@RestController
@RequestMapping(value = MenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuRestController {
    private static final Logger log = LoggerFactory.getLogger(DishController.class);
    protected static final String REST_URL = "/rest/admin/menus";
    private final DishRestController dishRestController;
    private final VoteRestController voteRestController;
    private final RestaurantRestController restaurantRestController;

    public MenuRestController(DishRestController dishRestController, RestaurantRestController restaurantRestController,
                              VoteRestController voteRestController) {
        this.dishRestController = dishRestController;
        this.voteRestController = voteRestController;
        this.restaurantRestController = restaurantRestController;
    }

    @Transactional
    @GetMapping
    public List<Menu> getAllToday(){
        LocalDate date = thisDay;
        log.info("getMenusToday date {}", date);
        boolean voteToday = voteRestController.authVote();
        return toListMenus(restaurantRestController.getAllWithDishesOfDate(date), voteToday, date);
    }

    @Transactional
    @GetMapping("/restaurants/{id}")
    public Menu getByRestaurantIdAndDate(@PathVariable(name = "id") int restaurantId,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getTodayMenu for restaurant {}", restaurantId);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantId, date), voteRestController.authVote(), thisDay);
    }

    @Transactional
    @GetMapping("/restaurants/names/{name}")
    public Menu getByNameWithDishesAndDate(@PathVariable String name,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getTodayMenu for restaurant {}", name);
        Restaurant restaurantDB = restaurantRestController.getByName(name);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantDB.id(), date), voteRestController.authVote(), thisDay);
    }

    @Transactional
    @GetMapping(value = "/date/{date}")
    public List<Menu> getAllByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        log.info("getMenusToday date {}", date);
        return toListMenus(restaurantRestController.getAllWithDishesOfDate(date), voteRestController.authVote(), date);
    }

    @DeleteMapping("/restaurants/{id}/date/{date}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteByRestaurantAndDate(@PathVariable(name = "id") int restaurantId,
                                          @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("delete by restaurant {} and date {}", restaurantId, date);
        dishRestController.deleteAllForRestaurantByDate(restaurantId, date);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dish>> createMenuForNewRestaurant(@Valid @RequestBody List<Dish> dishes,
                                                                 @RequestParam String name){
        log.info("create Menu for newRestaurant {} with quantity Dishes {}", name, dishes.size());
        Restaurant createdRestaurant = restaurantRestController.create(new Restaurant(null, name)).getBody();
        return dishRestController.createAllForMenu(dishes, createdRestaurant.id());
      }

    @Transactional
    @PutMapping(value = "/restaurants/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dish>> updateCreateMenuForRestaurantId(@Valid @RequestBody List<Dish> dishes,
                                                                      @PathVariable(name = "id") int restaurantId){
        log.info("create Menu for newRestaurant {} with Dishes number {}", restaurantId, dishes.size());
        return dishRestController.createAllForMenu(dishes, restaurantId);
    }
}
