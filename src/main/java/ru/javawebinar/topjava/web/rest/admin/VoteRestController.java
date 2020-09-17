package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.DateTimeUtil.сhangeVoteTime;
import static ru.javawebinar.topjava.util.RestUtil.getResponseEntity;
import static ru.javawebinar.topjava.util.ValidationUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static final String REST_URL = "/rest/admin/votes";

    private final VoteRepository repository;

    public VoteRestController(VoteRepository voteRepository) {
        this.repository = voteRepository;
    }

    @GetMapping(value = "/{id}")
    public Vote getById(@PathVariable int id) {
        log.info("getById {} ", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll votes");
        return repository.getAll();
    }

    @GetMapping(value = "/restaurants/{id}")
    public List<Vote> getAllForRestaurant(@PathVariable int id) {
        log.info("get all for restaurant {}", id);
        return repository.getByRestaurant(id);
    }

    @GetMapping(value = "/date/{date}")
    public List<Vote> getAllByDate(@PathVariable @Nullable LocalDate date) {
        log.info("get by date {}", date);
        return repository.getByDate(date);
    }

    @GetMapping(value = "/date/{date}/users/{id}")
    public Vote getByDateForUser(@PathVariable(name = "id") int userId, @PathVariable LocalDate date) {
        log.info("get for user {} by date {}", userId, date);
        return repository.getByDateForAuth(date, userId );
    }

    @GetMapping(value = "/users/{id}")
    public List<Vote> getAllForUser(@PathVariable(name = "id") int userId) {
        log.info("getAllForUser with userId {}", userId);
        return repository.getAllForAuthUser(userId);
    }

    @GetMapping(value = "/between/users/{id}/start/{startDate}/end/{endDate}")
    public List<Vote> getBetweenForUser(@PathVariable @Nullable LocalDate startDate, @PathVariable @Nullable LocalDate endDate,
                                        @PathVariable(name = "id") int userId) {
        log.info("getBetween with dates({} - {}) for userId {}", startDate, endDate, userId);
        return repository.getBetween(startDate, endDate, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int voteId) {
        log.info("delete vote {} for userId {}", voteId, authUserId());
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), voteId +" for change vote up to " + сhangeVoteTime);
        checkNotFoundWithId(repository.delete(voteId, authUserId()), voteId);
    }

    @PutMapping(value = "/{id}/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Vote> update(@Valid @RequestBody Vote vote, @PathVariable(name = "id") int voteId, @PathVariable int userId) {
        log.info("update vote {}", vote);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), voteId +" for change vote up to " + сhangeVoteTime);
        assureIdConsistent(vote, voteId);
        return getResponseEntity(checkNotFoundWithId(repository.save(vote, userId), vote.id()), REST_URL);
    }

    @Transactional
    @PostMapping(value = "/restaurants/{id}/users/{userId}")
    public ResponseEntity<Vote> create(@PathVariable(name = "id") int restaurantId, @PathVariable int userId) {
        log.info("create Vote {} for restaurantId", restaurantId);
        checkNotFound(!isExistVote(userId, thisDay), userId +" so as vote already exist for this day=" + thisDay);
        return getResponseEntity(checkNotFound(repository.save(
                new Vote(null, thisDay, restaurantId, userId), userId), "id=" + restaurantId), REST_URL);
    }

    public boolean isExistVote(int userId, LocalDate date){
        return getByDateForUser(userId, date) != null;
    }
}
