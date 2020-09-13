package ru.javawebinar.topjava.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.testdata.MenuTestData.MENU;
import static ru.javawebinar.topjava.testdata.MenuTestData.MENU_MATCHER;
import static ru.javawebinar.topjava.testdata.RestaurantTestData.RESTAURANT1_ID;
import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TEST;
import static ru.javawebinar.topjava.util.DateTimeUtil.setThisDay;

class AnonymousMenuRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AnonymousMenuRestController.REST_URL + '/';

    @Autowired
    private AnonymousMenuRestController controller;

    @Test
    void getAllMenusThisDay() throws Exception {
        setThisDay(DATE_TEST);
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//        MENU_MATCHER.assertMatch(controller.getAllMenusThisDay(), allMenusOfDay());
    }
/*    @Transactional
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getAllMenusThisDay() {
        setThisDay(DATE_TEST);
        log.info("getAllMenus");
        return toListMenus(restaurantController.getAllWithDishesOfDate(thisDay), new Vote());
    }
*/
    @Test
    void getMenuByRestaurantIdThisDay() throws Exception {
        setThisDay(DATE_TEST);
        perform(MockMvcRequestBuilders.get(REST_URL + "restaurants/" + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        MENU_MATCHER.contentJson(controller.getMenuByRestaurantIdThisDay(RESTAURANT1_ID), MENU);
    }
}
