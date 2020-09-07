package ru.javawebinar.topjava.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
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

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.DateTimeUtil.сhangeVoteTime;
import static ru.javawebinar.topjava.util.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static final String REST_URL = "/rest/profile/votes";
    private final VoteRepository voteRepository;

    public VoteRestController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping(value = "/{id}")
    public Vote get(@PathVariable Integer id) {
        log.info("get vote {} ", id);
        return checkNotFoundWithId(voteRepository.get(id, SecurityUtil.authUserId()), id);
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll votes");
        return voteRepository.getAll();
    }

    @GetMapping(value = "/restaurants/{id}")
    public List<Vote> getByRestaurant(@PathVariable int id) {
        log.info("get all for restaurant {}", id);
        return voteRepository.getByRestaurant(id);
    }

    @GetMapping(value = "date/{date}")
    public Vote getByDateForAuth(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get for user {} by date {}", SecurityUtil.authUserId(), date);
        return voteRepository.getByDateForAuth(date, SecurityUtil.authUserId());
    }

    @GetMapping(value = "exist/date/{date}")
    public boolean isExistVote(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get for user {} by date {}", SecurityUtil.authUserId(), date);
        return voteRepository.isExistVote(date, SecurityUtil.authUserId());
    }

    @GetMapping(value = "/auth")
    public List<Vote> getAllForAuth() {
        log.info("getAllForUser with userId {}", SecurityUtil.authUserId());
        return voteRepository.getAllForAuthUser(SecurityUtil.authUserId());
    }

    @GetMapping(value = "/between")
    public List<Vote> getBetween(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("getBetween with dates({} - {}) for userId {}", startDate, endDate, SecurityUtil.authUserId());
        return voteRepository.getBetween(startDate, endDate, SecurityUtil.authUserId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete vote {} for userId {}", id, userId);
        checkNotFoundWithId(voteRepository.delete(id, userId), id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Vote vote, @PathVariable int id) {
        log.info("update vote {} with id {} for userId {}", vote, id, authUserId());
        Assert.notNull(vote, "vote must not be null");
        assureIdConsistent(vote, id);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), id +" for change vote up to 11:00");
        checkNotFoundWithId(voteRepository.save(vote, authUserId()), vote.id());
    }

    @PostMapping(value = "/restaurants/{id}")
    public ResponseEntity<Vote> create(@Valid @PathVariable(name = "id") int restaurantId) {
        log.info("create Vote {} for restaurantId", restaurantId);
        Vote created = null;
        try {
            created = voteRepository.save(new Vote(null, thisDay, restaurantId, authUserId()), authUserId());
        } catch (Exception e) {
            throw new NotFoundException("vote for this date (" + thisDay + ") already exist");
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    public boolean authVote(){
        return isExistVote(thisDay);
    }
}
