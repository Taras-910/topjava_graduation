package ru.javawebinar.topjava.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.to.Menu;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.menu.MenuController;
import ru.javawebinar.topjava.web.vote.VoteController;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class ProfileController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final MenuController menuController;
    private final VoteController voteController;

    public ProfileController(MenuController menuController, VoteController voteController) {
        this.menuController = menuController;
        this.voteController = voteController;
    }

    @Transactional
    public List<Menu> getAllMenus() {
        log.info("getAllMenus");
        return menuController.getAll(voteController.getAllForUser(authUserId()), thisDay);
    }

    @Transactional
    public Menu getByRestaurantId(int restaurantId) {
        log.info("getByRestaurantId with restaurantId {}", restaurantId);
        return menuController.getMenuByRestaurantId(restaurantId, voteController.getAllForUser(authUserId()), thisDay);
    }

    public Vote createVote(Vote vote) {
        log.info("create vote {}", vote);
        return voteController.create(vote, authUserId());
    }

    public void updateVote(Vote vote, int id) {
        log.info("update vote {} with id {}", vote, id);
        voteController.update(vote, id, authUserId());
    }

    public void deleteVote(int id) {
        log.info("delete vote with id {}", id);
        voteController.delete(id, authUserId());
    }

    public Vote getVote(int id) {
        log.info("get vote with id {}", id);
        return voteController.get(id, authUserId());
    }

    public List<Vote> getAllVote() {
        log.info("getAll votes");
        int userId = SecurityUtil.authUserId();
        return voteController.getAllForUser(authUserId());
    }

    public List<Vote> getVotesBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) for user {}", startDate, endDate, userId);
        return voteController.getBetween(startDate, endDate, userId);
    }
}
