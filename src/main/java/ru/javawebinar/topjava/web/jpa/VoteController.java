package ru.javawebinar.topjava.web.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;
import ru.javawebinar.topjava.util.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.DateTimeUtil.сhangeVoteTime;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private VoteRepository voteRepository;

    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote getById(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get by id {} for user {}", id, userId);
        return checkNotFoundWithId(voteRepository.get(id, userId), id);
    }

    public Vote getByDateForAuth(LocalDate date) {
        int userId = SecurityUtil.authUserId();
        log.info("get for user {} by date {}", userId, date);
        return voteRepository.getByDateForAuth(date, userId);
    }

    public boolean isExistVote(LocalDate date) {
        log.info("isExistVote for user {} by date {}", SecurityUtil.authUserId(), date);
        return voteRepository.isExistVote(date, SecurityUtil.authUserId());
    }

    public List<Vote> getAllForAuthUser(int userId) {
        log.info("get all for User {}", userId);
        return voteRepository.getAllForAuthUser(userId);
    }

    public List<Vote> getAllForRestaurant(int restaurantId) {
        log.info("get all for restaurant {}", restaurantId);
        return voteRepository.getByRestaurant(restaurantId);
    }

    public List<Vote> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        log.info("getBetween with dates({} - {}) for userId {}", startDate, endDate, userId);
        return voteRepository.getBetween(startDate, endDate, userId);
    }

    public List<Vote> getAll() {
        log.info("getAll votes");
        return voteRepository.getAll();
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete vote {} for userId {}", id, userId);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), id +" for change vote up to 11:00");
        checkNotFoundWithId(voteRepository.delete(id, userId), id);
    }

    public Vote create(Vote vote, int userId) {
        log.info("create {} for userId", vote);
        Assert.notNull(userId, "user must be logged-in");
        checkNew(vote);
        checkNotFound(getByDateForAuth(vote.getDate()) == null, userId +" - so as vote already exist for this day " + thisDay.toString());
        return voteRepository.save(vote, userId);
    }

    public void update(Vote vote, int id, int userId) {
        log.info("update vote {} with id {} for userId {}", vote, id, userId);
        Assert.notNull(vote, "vote must not be null");
        assureIdConsistent(vote, id);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), id +" for change vote up to 11:00");
        checkNotFoundWithId(voteRepository.save(vote, userId), vote.id());
    }

    public boolean authVote(){
        return isExistVote(thisDay);
    }

}
