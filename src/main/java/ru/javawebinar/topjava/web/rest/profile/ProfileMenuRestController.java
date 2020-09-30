package ru.javawebinar.topjava.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.rest.admin.RestaurantRestController;
import ru.javawebinar.topjava.web.rest.admin.VoteRestController;

import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.MenuUtil.toListMenus;
import static ru.javawebinar.topjava.util.MenuUtil.toMenu;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = ProfileMenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileMenuRestController {
    private static final Logger log = LoggerFactory.getLogger(ProfileMenuRestController.class);
    protected static final String REST_URL = "/rest/profile/menus";

    private final VoteRestController voteRestController;
    private final RestaurantRestController restaurantRestController;

    public ProfileMenuRestController(RestaurantRestController restaurantRestController, VoteRestController voteRestController) {
        this.voteRestController = voteRestController;
        this.restaurantRestController = restaurantRestController;
    }

    @Transactional
    @GetMapping
    public List<Menu> getAll(){
        log.info("getAll");
        return toListMenus(restaurantRestController.getAllWithDishes(), null);
    }

    @Transactional
    @GetMapping("/today")
    public List<Menu> getAllToday(){
        log.info("getMenusToday date {}", thisDay);
        Vote vote = voteRestController.getByDateForUser(authUserId(), thisDay);
        return toListMenus(restaurantRestController.getAllWithDishesOfDate(thisDay), vote);
    }

    @Transactional
    @GetMapping("/restaurants/{id}")
    public Menu getByRestaurantToday(@PathVariable(name = "id")int restaurantId) {
        log.info("getMenu for restaurantId {}", restaurantId);
        boolean toVote = voteRestController.isExist(authUserId(), thisDay);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantId, thisDay), toVote);
    }

    @Transactional
    @GetMapping("/menu/{id}")
    public Menu getByRestaurantIdAndDate(@PathVariable(name = "id") int restaurantId, @Nullable @RequestParam Date localDate) {
        log.info("getTodayMenu for restaurant {}", restaurantId);
        boolean toVote = voteRestController.isExist(authUserId(), thisDay);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantId, localDate), toVote);
    }

    @Transactional
    @GetMapping("/menu")
    public Menu getByRestaurantNameAndDate(@RequestParam String restaurantName, @Nullable @RequestParam Date localDate) {
        log.info("getTodayMenu for restaurant {}", restaurantName);
        Restaurant restaurantDB = restaurantRestController.getByName(restaurantName);
        boolean toVote = voteRestController.isExist(authUserId(), thisDay);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantDB.id(), localDate), toVote);
    }

    @Transactional
    @GetMapping(value = "/date")
    public List<Menu> getAllByDate(@Nullable @RequestParam Date localDate){
        log.info("getMenusToday date {}", localDate);
        Vote vote = voteRestController.getByDateForUser(authUserId(), thisDay);
        return toListMenus(restaurantRestController.getAllWithDishesOfDate(localDate), vote);
    }
}
