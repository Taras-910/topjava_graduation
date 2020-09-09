package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.jpa.RestaurantController;
import ru.javawebinar.topjava.web.rest.admin.DishRestController;
import ru.javawebinar.topjava.web.rest.admin.MenuRestController;
import ru.javawebinar.topjava.web.rest.admin.RestaurantRestController;
import ru.javawebinar.topjava.web.rest.admin.VoteRestController;
import ru.javawebinar.topjava.web.rest.profile.ProfileUserRestController;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.TIME_TEST;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
 /*       try {
            Class clazz = Class.forName("org.hsqldb.jdbcDriver");
            System.out.println(clazz.getSimpleName());
        } catch(ClassNotFoundException e) {
            System.out.println("fault");
        }*/
        ConfigurableApplicationContext appCtx =
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml",
                        "spring/spring-mvc.xml"}, false);
        appCtx.refresh();
        DateTimeUtil.setThisDay(DATE_TEST);
        DateTimeUtil.setСhangeVoteTime(TIME_TEST);
/*
        System.out.println("Bean definition names: ");
        for(String s : appCtx.getBeanDefinitionNames()) {
            System.out.println(s);
        }
*/
/*
        DishController dishController = appCtx.getBean(DishController.class);
        LocalDate today = LocalDate.of(2020, Month.JULY, 30);
        MenuController menuController = appCtx.getBean(MenuController.class);
        RestaurantController restaurantController = appCtx.getBean(RestaurantController.class);
        RestaurantRestController restaurantRestController = appCtx.getBean(RestaurantRestController.class);
        AdminUserController adminUserController = appCtx.getBean(AdminUserController.class);
        ProfileController profileController = appCtx.getBean(ProfileController.class);
*/
        DishRestController dishRestController = appCtx.getBean(DishRestController.class);
        MenuRestController menuRestController = appCtx.getBean(MenuRestController.class);
        RestaurantController restaurantController = appCtx.getBean(RestaurantController.class);
        RestaurantRestController restaurantRestController = appCtx.getBean(RestaurantRestController.class);
        ProfileUserRestController profileRestController = appCtx.getBean(ProfileUserRestController.class);
        VoteRestController voteRestController = appCtx.getBean(VoteRestController.class);






        DateTimeUtil.setThisDay(LocalDate.now());
        DateTimeUtil.setСhangeVoteTime(LocalTime.now());
        appCtx.close();
    }
}
