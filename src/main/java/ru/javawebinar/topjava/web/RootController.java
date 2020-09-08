package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.util.SecurityUtil;
import ru.javawebinar.topjava.web.jpa.MenuController;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RootController {
    private Logger log = LoggerFactory.getLogger(RootController.class);
    @Autowired
    private MenuController controller;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        log.info("RootController getUsers");
        model.addAttribute("menus", controller.getMenusToday());
        return "users";
    }

    @GetMapping("/login")
    public String login() {
        log.info("RootController login");
        return "login";
    }

    @PostMapping("/users")
    public String setUser(HttpServletRequest request) {
        log.info("RootController setUsers");
        int userId = Integer.parseInt(request.getParameter("userId"));
        SecurityUtil.setAuthUserId(userId);
        return "redirect:menus";
    }
}
