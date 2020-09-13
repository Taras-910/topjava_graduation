package ru.javawebinar.topjava.web.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.DateTimeUtil.сhangeVoteTime;
import static ru.javawebinar.topjava.util.ValidationUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private VoteRepository repository;

    public VoteController(VoteRepository repository) {
        this.repository = repository;
    }

    public Vote getById(int id) {
        log.info("get by id {} for user {}", id, authUserId());
        return checkNotFoundWithId(repository.get(id, authUserId()), id);
    }

    public Vote getByDateForAuth(LocalDate date) {
        log.info("get for user {} by date {}", authUserId(), date);
        return repository.getByDateForAuth(date, authUserId());
    }

    public List<Vote> getAllForAuthUser(int userId) {
        log.info("get all for User {}", userId);
        return repository.getAllForAuthUser(userId);
    }

    public List<Vote> getAllForRestaurant(int restaurantId) {
        log.info("get all for restaurant {}", restaurantId);
        return repository.getByRestaurant(restaurantId);
    }

    public List<Vote> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        log.info("getBetween with dates({} - {}) for userId {}", startDate, endDate, userId);
        return repository.getBetween(startDate, endDate, userId);
    }

    public List<Vote> getAll() {
        log.info("getAll votes");
        return repository.getAll();
    }

    public void delete(int id) {
        log.info("delete vote {} for userId {}", id, authUserId());
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), id +" for change vote up to 11:00");
        checkNotFoundWithId(repository.delete(id, authUserId()), id);
    }

    public Vote create(Vote vote, int userId) {
        log.info("create {} for userId", vote);
        Vote created = null;
        try {
            Assert.notNull(userId, "user must be logged-in");
            checkNew(vote);
            checkNotFound(getByDateForAuth(vote.getDate()) == null, userId +" - so as vote already exist for this day " + thisDay.toString());
            created = repository.save(vote, userId);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            throw new NotFoundException(" Illegal argument vote=" + vote + " or id=" + userId);
        }
        return created;
    }

    public void update(Vote vote, int id, int userId) {
        log.info("update vote {} with id {} for userId {}", vote, id, userId);
        Vote updated = null;
        try {
            Assert.notNull(vote, "vote must not be null");
            assureIdConsistent(vote, id);
            checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), id +" for change vote up to 11:00");
            updated = repository.save(vote, userId);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            throw new NotFoundException(" Illegal argument vote=" + vote + " or id=" + userId);
        }
        checkNotFoundWithId(updated, vote.id());
    }
}
