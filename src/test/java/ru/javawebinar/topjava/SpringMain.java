package ru.javawebinar.topjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;

public class SpringMain {
    public static final Logger log = LoggerFactory.getLogger(SpringMain.class);
    public static void main(String[] args) {

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
        DishRestController dishRestController = appCtx.getBean(DishRestController.class);
        MenuRestController menuRestController = appCtx.getBean(MenuRestController.class);
        RestaurantController restaurantController = appCtx.getBean(RestaurantController.class);
        RestaurantRestController restaurantRestController = appCtx.getBean(RestaurantRestController.class);
        ProfileUserRestController profileRestController = appCtx.getBean(ProfileUserRestController.class);
        VoteRestController voteRestController = appCtx.getBean(VoteRestController.class);
*/
        System.out.println(thisDay);





        appCtx.close();
    }
}
