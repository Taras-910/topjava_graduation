package ru.javawebinar.topjava.web.jpa.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class ProfileController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService service;

    public ProfileController(UserService service) {
        this.service = service;
    }

    public User get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }
}
