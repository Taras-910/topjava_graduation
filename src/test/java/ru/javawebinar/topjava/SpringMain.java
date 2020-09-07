package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.jpa.RestaurantController;
import ru.javawebinar.topjava.web.rest.DishRestController;
import ru.javawebinar.topjava.web.rest.MenuRestController;
import ru.javawebinar.topjava.web.rest.RestaurantRestController;
import ru.javawebinar.topjava.web.rest.VoteRestController;
import ru.javawebinar.topjava.web.rest.user.ProfileRestController;

import java.time.LocalDate;
import java.time.Month;

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
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml",
                        "spring/spring-db.xml",
                        "spring/spring-mvc.xml"},
                        false);
        appCtx.refresh();
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
        LocalDate today = LocalDate.of(2020, Month.JULY, 30);

        DishRestController dishRestController = appCtx.getBean(DishRestController.class);
        MenuRestController menuRestController = appCtx.getBean(MenuRestController.class);
        RestaurantController restaurantController = appCtx.getBean(RestaurantController.class);
        RestaurantRestController restaurantRestController = appCtx.getBean(RestaurantRestController.class);
        ProfileRestController profileRestController = appCtx.getBean(ProfileRestController.class);
        VoteRestController voteRestController = appCtx.getBean(VoteRestController.class);

        appCtx.close();
    }
}
