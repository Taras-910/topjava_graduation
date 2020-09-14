package ru.javawebinar.topjava.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.rest.admin.RestaurantRestController;
import ru.javawebinar.topjava.web.rest.admin.VoteRestController;

import java.time.LocalDate;
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
    public List<Menu> getAllToday(){
        log.info("getMenusToday date {}", thisDay);
        Vote vote = voteRestController.getByDateForUser(authUserId(), thisDay);
        return toListMenus(restaurantRestController.getAllWithDishesOfDate(thisDay), vote);
    }

    @Transactional
    @GetMapping("/all")
    public List<Menu> getAll(){
        log.info("getAll");
        return toListMenus(restaurantRestController.getAllWithDishes(), null);
    }

    @Transactional
    @GetMapping("/restaurants/{id}")
    public Menu getByRestaurantToday(@PathVariable(name = "id")int restaurantId) {
        log.info("getMenu for restaurantId {}", restaurantId);
        boolean toVote = voteRestController.isExistVote(authUserId(), thisDay);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantId, thisDay), toVote);
    }

    @Transactional
    @GetMapping("/restaurants/{id}/date/{date}")
    public Menu getByRestaurantIdAndDate(@PathVariable(name = "id") int restaurantId, @Nullable @PathVariable LocalDate date) {
        log.info("getTodayMenu for restaurant {}", restaurantId);
        boolean toVote = voteRestController.isExistVote(authUserId(), thisDay);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantId, date), toVote);
    }

    @Transactional
    @GetMapping("/restaurants/names/{name}/date/{date}")
    public Menu getByRestaurantNameAndDate(@PathVariable String name, @Nullable @PathVariable LocalDate date) {
        log.info("getTodayMenu for restaurant {}", name);
        Restaurant restaurantDB = restaurantRestController.getByName(name);
        boolean toVote = voteRestController.isExistVote(authUserId(), thisDay);
        return toMenu(restaurantRestController.getByIdWithDishesOfDate(restaurantDB.id(), date), toVote);
    }

    @Transactional
    @GetMapping(value = "/date/{date}")
    public List<Menu> getAllByDate(@Nullable @PathVariable LocalDate date){
        log.info("getMenusToday date {}", date);
        boolean toVote = voteRestController.isExistVote(authUserId(), date);
        Vote vote = voteRestController.getByDateForUser(authUserId(), thisDay);
        return toListMenus(restaurantRestController.getAllWithDishesOfDate(date), vote);
    }

}
