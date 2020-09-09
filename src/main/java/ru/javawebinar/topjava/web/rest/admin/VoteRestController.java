package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.DateTimeUtil.сhangeVoteTime;
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
    public Vote get(@PathVariable Integer id) {
        log.info("get vote {} ", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll votes");
        return repository.getAll();
    }

    @GetMapping(value = "/restaurants/{id}")
    public List<Vote> getByRestaurant(@PathVariable int id) {
        log.info("get all for restaurant {}", id);
        return repository.getByRestaurant(id);
    }

    @GetMapping(value = "/date/{date}")
    public List<Vote> getByDate(@PathVariable LocalDate date) {
        log.info("get by date {}", date);
        return repository.getByDate(date);
    }

    @GetMapping(value = "/users/{id}/date/{date}")
    public boolean isExistForUserByDate(@PathVariable(name = "id") int userId, @PathVariable LocalDate date) {
        log.info("get for user {} by date {}", userId, date);
        return repository.isExistVote(date, userId );
    }

    @GetMapping(value = "/users/{id}")
    public List<Vote> getAllForUser(@PathVariable(name = "id") int userId) {
        log.info("getAllForUser with userId {}", userId);
        return repository.getAllForAuthUser(userId);
    }

    @GetMapping(value = "/between/users/{id}")
    public List<Vote> getBetweenForUser(@RequestParam @Nullable LocalDate startDate, @RequestParam @Nullable LocalDate endDate,
                                        @PathVariable(name = "id") int userId) {
        log.info("getBetween with dates({} - {}) for userId {}", startDate, endDate, userId);
        return repository.getBetween(startDate, endDate, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int voteId) {
        log.info("delete vote {} for userId {}", voteId, authUserId());
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), voteId +" for change vote up to 11:00");
        checkNotFoundWithId(repository.delete(voteId, authUserId()), voteId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Vote vote, @PathVariable(name = "id") int voteId) {
        log.info("update vote {} for userId {}", vote, authUserId());
        Assert.notNull(vote, "vote must not be null");
        assureIdConsistent(vote, voteId);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), voteId +" for change vote up to 11:00");
        checkNotFoundWithId(repository.save(vote, authUserId()), vote.id());
    }

    @PostMapping(value = "/restaurants/{id}/users/{userId}")
    public ResponseEntity<Vote> create(@PathVariable(name = "id") int restaurantId, @PathVariable int userId) {
        log.info("create Vote {} for restaurantId", restaurantId);
        Vote created;
        try {
            created = repository.save(new Vote(null, thisDay, restaurantId, userId), userId);
        } catch (Exception e) {
            throw new NotFoundException("vote of user " + userId + " for this date (" + thisDay + ") already exist");
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    public boolean authVote(){
        return isExistForUserByDate(authUserId(), thisDay);
    }
}
