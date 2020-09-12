package ru.javawebinar.topjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Dish;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;

public class SpringMain {
    public static final Logger log = LoggerFactory.getLogger(SpringMain.class);
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
/*
        DishRestController dishRestController = appCtx.getBean(DishRestController.class);
        MenuRestController menuRestController = appCtx.getBean(MenuRestController.class);
        RestaurantController restaurantController = appCtx.getBean(RestaurantController.class);
        RestaurantRestController restaurantRestController = appCtx.getBean(RestaurantRestController.class);
        ProfileUserRestController profileRestController = appCtx.getBean(ProfileUserRestController.class);
        VoteRestController voteRestController = appCtx.getBean(VoteRestController.class);
*/

        Dish testDish1 = new Dish(null, "testEating1", thisDay, 10.0F);
        Dish testDish2 = new Dish(null, "testEating2", thisDay, 10.0F);
        Dish testDish3 = new Dish(null, "testEating3", thisDay, 10.0F);

        System.out.println(thisDay);
//        List<Dish> dishes = List.of(testDish1, testDish2, testDish3);
//        dishRestController.create(testDish1, RESTAURANT1_ID);

//        dishRestController.createListOfMenu(dishes, RESTAURANT1_ID);

//        log.info("dish {}", dishRestController.getByRestaurantAndDate(RESTAURANT1_ID, thisDay));
//        log.info("---------------------------------------------------------------\n\n\n\n\n");
//        dishRestController.delete(DISH10_ID, RESTAURANT2_ID);
//        log.info("dish {}", dishRestController.getByRestaurantAndDate(RESTAURANT2_ID, DATE_TEST));
//        log.info("---------------------------------------------------------------\n\n");
//
//        Dish dish = new Dish(DISH10);
//        dish.setId(null);
//        dishRestController.create(dish, RESTAURANT2_ID);
//        log.info("dish {}", dishRestController.getByRestaurantAndDate(RESTAURANT2_ID, DATE_TEST));
        log.info("---------------------------------------------------------------\n\n\n\n\n");


        appCtx.close();
    }
}
