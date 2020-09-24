package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.DateTimeUtil.сhangeVoteTime;
import static ru.javawebinar.topjava.util.ResponseEntityUtil.getResponseEntity;
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

    @GetMapping(value = "/date")
    public List<Vote> getAllByDate(@RequestParam @Nullable Date date) {
        log.info("get by date {}", date);
        return repository.getByDate(date);
    }

    @GetMapping(value = "/users/{id}")
    public Vote getByDateForUser(@PathVariable(name = "id") int userId, @RequestParam Date localDate) {
        log.info("get for user {} by date {}", userId, localDate);
        return repository.getByDateForAuth(localDate, userId );
    }

    @GetMapping(value = "/users")
    public List<Vote> getAllForUser(@RequestParam(name = "id") int userId) {
        log.info("getAllForUser with userId {}", userId);
        return repository.getAllForAuthUser(userId);
    }

    @GetMapping(value = "/between")
    public List<Vote> getBetweenForUser(@RequestParam @Nullable Date startDate, @RequestParam @Nullable Date endDate,
                                        @RequestParam int userId) {
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

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> update(@Valid @RequestBody Vote vote, @PathVariable(name = "id") int voteId, BindingResult result) {
        if (result != null && result.hasErrors()) {
            return new ResponseEntity<>(vote, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        log.info("update vote {}", vote);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), voteId +" for change vote up to " + сhangeVoteTime);
        assureIdConsistent(vote, voteId);
        return new ResponseEntity(checkNotFoundWithId(repository.save(vote, authUserId()), vote.id()), HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Vote> create(@RequestParam int restaurantId) {
        log.info("create Vote {} for restaurantId", restaurantId);
        checkNotFound(!isExistVote(authUserId(), thisDay), authUserId() +" so as vote already exist for this day=" + thisDay);
        return getResponseEntity(checkNotFound(repository.save(
                new Vote(null, thisDay, restaurantId, authUserId()), authUserId()), "id=" + restaurantId), REST_URL);
    }

    public boolean isExistVote(int userId, Date date){
        return getByDateForUser(userId, date) != null;
    }
}
