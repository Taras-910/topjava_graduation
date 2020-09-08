package ru.javawebinar.topjava.web.jpa.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.jpa.MenuController;

import java.util.List;

@Controller
public class AnonymousMenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MenuController menuController;

    public AnonymousMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    @Transactional
    public List<Menu> getAllMenus() {
        log.info("getAllMenus");
        return menuController.getMenusToday();
    }

    @Transactional
    public Menu getMenuByRestaurantId(int restaurantId) {
        log.info("getMenu by restaurantId {}", restaurantId);
        return menuController.getMenuByRestaurantId(restaurantId);
    }
}
