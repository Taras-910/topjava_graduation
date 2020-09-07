package ru.javawebinar.topjava.web.jpa.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractJpaControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.testdata.UserTestData.*;

public class ProfileControllerTest extends AbstractJpaControllerTest {

    @Autowired
    ProfileController controller;

    @Test
    public void get() {
        User user = controller.get(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
    }

    @Test
    public void delete() {
        controller.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> controller.get(USER_ID));
    }

    @Test
    public void update() {
        User updated = getUpdated();
        controller.update(updated, USER_ID);
        USER_MATCHER.assertMatch(controller.get(USER_ID), getUpdated());
    }
}
