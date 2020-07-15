package ru.javawebinar.topjava.web.admin;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.web.testdata.UserTestData.*;

public class AdminUserControllerTest extends AbstractControllerTest {

    @Autowired
    AdminUserController controller;

    @Test
    public void get() throws Exception {
        User user = controller.get(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.get(NOT_FOUND));
    }

    @Test
    public void create() throws Exception {
        User created = controller.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(controller.get(newId), newUser);
    }

    @Test
    public void delete() throws Exception {
        controller.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> controller.get(USER_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND));
    }

    @Test
    public void getByMail() throws Exception {
        User user = controller.getByMail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, ADMIN);
    }

    @Test
    public void duplicateUserCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                controller.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdated();
        controller.update(updated, USER_ID);
        USER_MATCHER.assertMatch(controller.get(USER_ID), getUpdated());
    }

    @Test
    public void getAll() throws Exception {
        List<User> all = controller.getAll();
        USER_MATCHER.assertMatch(all, ADMIN, USER);
    }
}
