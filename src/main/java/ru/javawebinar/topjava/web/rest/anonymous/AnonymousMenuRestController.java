package ru.javawebinar.topjava.web.rest.anonymous;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.jpa.MenuController;

import java.util.List;

@RestController
@RequestMapping(AnonymousMenuRestController.REST_URL)
public class AnonymousMenuRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static final String REST_URL = "/rest/anonymous";
    private final MenuController menuController;

    public AnonymousMenuRestController(MenuController menuController) {
        this.menuController = menuController;
    }

    @Transactional
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getAllMenus() {
        log.info("getAllMenus");
        return menuController.getMenusToday();
    }

    @Transactional
    @GetMapping(value = "/restaurants/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Menu getMenuByRestaurantId(@PathVariable(name = "id") int restaurantId) {
        log.info("getMenu by restaurant id {}", restaurantId);
        return menuController.getMenuByRestaurantId(restaurantId);
    }
}
