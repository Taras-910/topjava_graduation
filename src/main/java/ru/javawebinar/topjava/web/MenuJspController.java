package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.jpa.MenuController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(value = "/menus")
public class MenuJspController {
    Logger log = LoggerFactory.getLogger(MenuJspController.class);

    @Autowired
    private MenuController controller;

    @GetMapping
    public String getMenusToday(HttpServletRequest request){
        List<Menu> menus = controller.getMenusToday();
        log.info("getAllMenus {}", menus);
        request.setAttribute("menus", menus);
        return "menus";
    }

    @DeleteMapping("/delete")
    public String deleteMenu(HttpServletRequest request) {
        int id = getId(request);
        log.info("delete deleteRestaurantAndDishes {}", id);
        controller.deleteRestaurantAndDishes(id);
        return "redirect:menus";
    }

    @PutMapping("/update")
    public String update(HttpServletRequest request, Model model) {
        int id = getId(request);
        log.info("update menu for restaurant {}", id);
        model.addAttribute("menu", controller.getMenuByRestaurantId(id));
        return "menusForm";
    }

    @GetMapping("/create")
    public String create(Model model) {
        log.info("update restaurant menu");
        Restaurant newRestaurant = new Restaurant(null,"Созданный ресторан");
        Dish newDish1 = new Dish(null, "",LocalDate.now(), 0);
        Dish newDish2 = new Dish(null, "",LocalDate.now(), 0);
        Dish newDish3 = new Dish(null, "",LocalDate.now(), 0);
        Dish newDish4 = new Dish(null, "",LocalDate.now(), 0);
        Dish newDish5 = new Dish(null, "",LocalDate.now(), 0);
        newRestaurant.setDishes(List.of(newDish1, newDish2, newDish3, newDish4, newDish5));
        model.addAttribute("menu", new Menu(newRestaurant, LocalDate.now(), false, false));
        return "menusForm";
    }

    @PostMapping
    public String updateOrCreate(HttpServletRequest request) {
        log.info("updateOrCreate menu");
        return "redirect:menus";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
