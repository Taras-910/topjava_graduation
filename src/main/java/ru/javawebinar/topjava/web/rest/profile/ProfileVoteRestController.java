package ru.javawebinar.topjava.web.rest.profile;

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
import ru.javawebinar.topjava.util.SecurityUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalDate.now;
import static ru.javawebinar.topjava.util.DateTimeUtil.сhangeVoteTime;
import static ru.javawebinar.topjava.util.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@RestController
@RequestMapping(value = ProfileVoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static final String REST_URL = "/rest/profile/votes";
    private final VoteRepository voteRepository;

    public ProfileVoteRestController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping(value = "/{id}")
    public Vote get(@PathVariable(name = "id") Integer voteId) {
        log.info("get vote {} ", voteId);
        return checkNotFoundWithId(voteRepository.get(voteId, SecurityUtil.authUserId()), voteId);
    }

    @GetMapping(value = "/auth")
    public List<Vote> getAllForAuth() {
        log.info("getAllForUser with userId {}", SecurityUtil.authUserId());
        return voteRepository.getAllForAuthUser(SecurityUtil.authUserId());
    }

    @GetMapping(value = "/restaurants/{id}")
    public List<Vote> getByRestaurantAuth(@PathVariable(name = "id") int restaurantId) {
        log.info("get all of restaurant {}", restaurantId);
        return checkNotFound(voteRepository.getByRestaurantAuth(restaurantId, authUserId()), " for restaurant " + restaurantId);
    }

    @GetMapping(value = "date/{date}")
    public Vote getByDateForAuth(@PathVariable LocalDate date) {
        log.info("get for user {} by date {}", SecurityUtil.authUserId(), date);
        return checkNotFound(voteRepository.getByDateForAuth(date, authUserId()), "for date " + date);
    }

    @GetMapping(value = "exist/date/{date}")
    public boolean isExistVote(@PathVariable LocalDate date) {
        log.info("get for user {} by date {}", authUserId(), date);
        return voteRepository.isExistVote(date, authUserId());
    }

    @GetMapping(value = "/between")
    public List<Vote> getBetween(@RequestParam @Nullable LocalDate startDate, @RequestParam @Nullable LocalDate endDate) {
        log.info("getBetween with dates({} - {}) for userId {}", startDate, endDate, authUserId());
        return voteRepository.getBetween(startDate, endDate, authUserId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int voteId) {
        int userId = SecurityUtil.authUserId();
        log.info("delete vote {} for userId {}", voteId, userId);
        checkNotFoundWithId(voteRepository.delete(voteId, userId), voteId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Vote vote, @PathVariable(name = "id") int voteId) {
        log.info("update vote {} with id {} for userId {}", vote, voteId, authUserId());
        Assert.notNull(vote, "vote must not be null");
        assureIdConsistent(vote, voteId);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), voteId +" for change vote up to 11:00");
        checkNotFoundWithId(voteRepository.save(vote, authUserId()), voteId);
    }

    @PostMapping(value = "/restaurants/{id}")
    public ResponseEntity<Vote> create(@PathVariable(name = "id") int restaurantId) {
        log.info("create Vote for restaurantId {} ", restaurantId);
        Vote createdVote = null;
        try {
            createdVote = voteRepository.save(new Vote(null, now(), restaurantId, authUserId()), authUserId());
        } catch (Exception e) {
            throw new NotFoundException("vote for this date (" + now() + ") already exist");
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(createdVote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(createdVote);
    }
}
