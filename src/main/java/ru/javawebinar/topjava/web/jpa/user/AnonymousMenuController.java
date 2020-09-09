package ru.javawebinar.topjava.web.jpa.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.jpa.RestaurantController;

import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.MenuUtil.toListMenus;
import static ru.javawebinar.topjava.util.MenuUtil.toMenu;

@Controller
public class AnonymousMenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantController restaurantController;

    public AnonymousMenuController(RestaurantController restaurantController) {
        this.restaurantController = restaurantController;
    }

    @Transactional
    public List<Menu> getMenusToday() {
        log.info("getAllMenus");
        return toListMenus(restaurantController.getAllWithDishesOfDate(thisDay), false, thisDay);
    }

    @Transactional
    public Menu getMenuByRestaurantId(int restaurantId) {
        log.info("getMenu by restaurantId {}", restaurantId);
        return toMenu(restaurantController.getByIdWithDishesOfDate(restaurantId, thisDay),false,thisDay);
    }
}
