package ru.javawebinar.topjava.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.admin.MenuController;

import java.util.List;

@Controller
public class UserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MenuController menuController;

    public UserController(MenuController menuController) {
        this.menuController = menuController;
    }

    @Transactional
    public List<Menu> getAllMenus() {
        log.info("getAllMenus");
        return menuController.getAllMenusOfDay();
    }

    @Transactional
    public Menu getMenuByRestaurantId(int restaurantId) {
        log.info("getByRestaurantId with restaurantId {}", restaurantId);
        return menuController.getMenuByRestaurantId(restaurantId);
    }
}
