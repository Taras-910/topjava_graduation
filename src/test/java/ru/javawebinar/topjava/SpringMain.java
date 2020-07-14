package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.web.admin.AdminMenuController;
import ru.javawebinar.topjava.web.dish.DishController;
import ru.javawebinar.topjava.web.restaurant.RestaurantController;

import java.time.LocalDate;
import java.time.Month;

import static ru.javawebinar.topjava.DishTestData.RESTAURANT_ID;

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
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, false);
        appCtx.refresh();

/*        System.out.println("Bean definition names: ");
        for(String s : appCtx.getBeanDefinitionNames()) {
            System.out.println(s);
        }*/


/*
        BusinessAdminController adminController = appCtx.getBean(BusinessAdminController.class);
        adminController.create(new User(null, "Newname", "newemail@mail.ru", "newpassword", Role.ADMIN));
        adminController.delete(100018);
        adminController.update(new User(ADMIN_ID, "NEW", "NEW@mail.ru", "NEWpassword", Role.ADMIN),ADMIN_ID);
        System.out.println(adminController.getByMail("user@yandex.ru"));
        System.out.println(adminController.get(ADMIN_ID));
        System.out.println(adminController.getAll());
        System.out.println(adminController.get(USER_ID));
*/

/* нужно пакетом записавать и пакетом удалять - или каждый раз проверять количество
        DishController dishController = appCtx.getBean(DishController.class);
        System.out.println(dishController.getAll(RESTAURANT_ID+1));
        System.out.println(dishController.get(DishTestData.DISH1_ID));
        dishController.create(new Dish("Созданный ужин", of(2020, Month.FEBRUARY, 1, 18, 0), 5.0F), 100002);
        dishController.update(new Dish(100012,"Обновленный обед", of(2020, Month.FEBRUARY, 1, 18, 0), 5.55F),100012,100003);
        dishController.delete(100011);
        System.out.println(dishController.getForThisDay());
*/
/*
       RestaurantController restaurantController = appCtx.getBean(RestaurantController.class);
        LocalDate today = LocalDate.of(2020, Month.JULY, 30);
        List<Restaurant> restaurants = restaurantController.getWithDishes(today);
        System.out.println();
*/

 /*        DishController dishController = appCtx.getBean(DishController.class);
        LocalDate today = LocalDate.of(2020, Month.JULY, 30);
        System.out.println("============================================================\n\n");

        List<Dish> dishes = dishController.getForToday(today);
        System.out.println("-----------------------------------------------------------");

        List<Restaurant> restaurants = restaurantController.getForToday(today);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
        dishes.forEach(System.out::println);
        System.out.println("-----------------------------------------------------------");
        restaurants.forEach(System.out::println);
        System.out.println("============================================================\n\n");
*/
//        MenuController menuController = appCtx.getBean(MenuController.class);


/*
        restaurantController.create(new Restaurant(null,"Созданный ресторан"));
        System.out.println(restaurantController.getAll());
        System.out.println(restaurantController.get(RESTAURANT_ID));
        restaurantController.update(new Restaurant(RESTAURANT_ID,"Обновленный ресторан"), RESTAURANT_ID);
        restaurantController.delete(RESTAURANT_ID+1);
 */

//        VoteController voteController = appCtx.getBean(VoteController.class);
//        voteController.create(new Vote(null, now(), RESTAURANT2_ID, ADMIN_ID));
//        voteController.delete(100016);
//        System.out.println(voteController.get(VOTE2_ID));
//        voteController.update(new Vote(VOTE2_ID, LocalDate.of(2020, Month.JUNE, 28), RESTAURANT2_ID, USER_ID),VOTE2_ID);
//        System.out.println(voteController.getAll());
//        System.out.println(voteController.getForLoggedUser());
 //       System.out.println(voteController.get(VOTE2_ID));
//        System.out.println(voteController.getBetween(LocalDate.of(2020, Month.JULY, 28),
//                LocalDate.of(2020, Month.JULY, 29)));


//        ProfileController profileController = appCtx.getBean(ProfileController.class);
/*
        UserController userController = appCtx.getBean(UserController.class);

        System.out.println(userController.getAllMenus());
*/      DishController dishController = appCtx.getBean(DishController.class);
        LocalDate today = LocalDate.of(2020, Month.JULY, 30);
        AdminMenuController adminMenuController = appCtx.getBean(AdminMenuController.class);
        RestaurantController restaurantController = appCtx.getBean(RestaurantController.class);

        Restaurant newRestaurant = new Restaurant(null,"Pizza");

        Restaurant restaurant = restaurantController.get(RESTAURANT_ID);
        restaurant.setName("Калинка");
        adminMenuController.updateRestaurant(restaurant, RESTAURANT_ID);
/*
//        restaurantController.create(new Restaurant(null,"Созданный ресторан"));
        Dish DISH1 = new Dish(null, "НовыйСуп", LocalDate.of(2020, Month.JUNE, 14), 0.1F);
        Dish DISH2 = new Dish(null, "НовыйЧай",LocalDate.of(2020, Month.JUNE, 14), 0.2F);
        Dish DISH3 = new Dish(null, "НовыйБорщ",LocalDate.of(2020, Month.JUNE, 14), 0.3F);
        Dish DISH4 = new Dish(null, "НовыйСалат", LocalDate.of(2020, Month.JUNE, 14), 0.4F);
        Dish DISH5 = new Dish(null, "НовыйРагу",LocalDate.of(2020, Month.JUNE, 14), 0.5F);
        Dish DISH6 = new Dish(null, "НовыйОмлет",LocalDate.of(2020, Month.JUNE, 14), 1.6F);
//        List<Dish> dishes = Arrays.asList(DISH1, DISH2, DISH3, DISH4, DISH5, DISH6 );
        List<Dish> dishes = Arrays.asList(DISH1, DISH2);

        newRestaurant.setDishes(dishes);

//        Restaurant restaurant = adminMenuController.createRestaurantAndDishes(newRestaurant);
//        Restaurant restaurant = restaurantController.create(newRestaurant);
        System.out.println(adminMenuController.createRestaurantAndDishes(newRestaurant));
//        System.out.println(dishController.saveAll(dishes, restaurant.getId()));
*/

//    public static Menu toMenu (Restaurant restaurant, List<Vote> votes, LocalDate date){
//        return new Menu(id(), restaurant, date, votes, false);
//    }
//    public static final Dish DISH1 = new Dish(DISH1_ID, "Суп", of(2020, Month.JUNE, 29), 0.1F);
//    public static final Dish DISH2 = new Dish(DISH1_ID + 1, "Чай",of(2020, Month.JUNE, 29), 0.2F);
//    public static final Dish DISH3 = new Dish(DISH1_ID + 2, "Борщ",of(2020, Month.JUNE, 30), 1.1F);
//    public static final Dish DISH4 = new Dish(DISH1_ID + 3, "Компот",of(2020, Month.JUNE, 30), 1.2F);
/*
        MonetaryAmountFactory<?> amountFactory = Monetary.getDefaultAmountFactory();
        MonetaryAmount monetaryAmount = amountFactory.setCurrency(Monetary.getCurrency("EUR")).setNumber(12345.67).create();
        MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(Locale.getDefault());
        System.out.println(format.format(monetaryAmount));

*/
        System.out.println("_________________________________________________test\n");

        appCtx.close();
    }
}
/*
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Office> officeList;

@JoinColumn


private int marketId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "marketId", referencedColumnName = "market_id", insertable = false, updatable = false)
private MarketInfo market
*/
