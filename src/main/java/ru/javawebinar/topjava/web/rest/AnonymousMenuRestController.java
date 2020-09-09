package ru.javawebinar.topjava.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.jpa.RestaurantController;

import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.MenuUtil.toListMenus;
import static ru.javawebinar.topjava.util.MenuUtil.toMenu;

@RestController
@RequestMapping(AnonymousMenuRestController.REST_URL)
public class AnonymousMenuRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static final String REST_URL = "/anonymous";
    private final RestaurantController restaurantController;

    public AnonymousMenuRestController(RestaurantController restaurantController) {
        this.restaurantController = restaurantController;
    }


    @Transactional
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getAllMenus() {
        log.info("getAllMenus");
        return toListMenus(restaurantController.getAllWithDishesOfDate(thisDay), false, thisDay);
    }

    @Transactional
    @GetMapping(value = "/restaurants/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Menu getMenuByRestaurantId(@PathVariable(name = "id") int restaurantId) {
        log.info("getMenu by restaurant id {}", restaurantId);
        return toMenu(restaurantController.getByIdWithDishesOfDate(restaurantId, thisDay),false,thisDay);
    }
}
