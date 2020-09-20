package ru.javawebinar.topjava.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalDate.now;
import static ru.javawebinar.topjava.util.DateTimeUtil.сhangeVoteTime;
import static ru.javawebinar.topjava.util.RestUtil.getResponseEntity;
import static ru.javawebinar.topjava.util.ValidationUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = ProfileVoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static final String REST_URL = "/rest/profile/votes";
    private final VoteRepository voteRepository;

    public ProfileVoteRestController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping
    public List<Vote> getAllForAuth() {
        log.info("getAllForUser with userId {}", authUserId());
        return voteRepository.getAllForAuthUser(authUserId());
    }

    @GetMapping(value = "/{id}")
    public Vote get(@PathVariable(name = "id") int voteId) {
        log.info("get vote {} for user {}", voteId, authUserId());
        return checkNotFoundWithId(voteRepository.get(voteId, authUserId()), voteId);
    }

    @GetMapping(value = "/restaurants/{id}")
    public List<Vote> getByRestaurantAuth(@PathVariable(name = "id") int restaurantId) {
        log.info("get all of restaurant {}", restaurantId);
        return checkNotFound(voteRepository.getByRestaurantAuth(restaurantId, authUserId()), " for restaurant " + restaurantId);
    }

    @GetMapping(value = "/date")
    public Vote getByDateForAuth(@RequestParam LocalDate date) {
        log.info("get for user {} by date {}", authUserId(), date);
        return checkNotFound(voteRepository.getByDateForAuth(date, authUserId()), "for date " + date);
    }

    @GetMapping(value = "/between")
    public List<Vote> getBetween(@RequestParam @Nullable LocalDate startDate, @RequestParam @Nullable LocalDate endDate) {
        log.info("getBetween with dates({} - {}) for userId {}", startDate, endDate, authUserId());
        return voteRepository.getBetween(startDate, endDate, authUserId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int voteId) {
        int userId = authUserId();
        log.info("delete vote {} for userId {}", voteId, userId);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), voteId +" for change vote up to " + сhangeVoteTime);
        checkNotFoundWithId(voteRepository.delete(voteId, userId), voteId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Vote>  update(@Valid @RequestBody Vote vote, @PathVariable(name = "id") int voteId, BindingResult result) {
        log.info("update vote {} with id {} for userId {}", vote, voteId, authUserId());
        if (result != null && result.hasErrors()) {
            return new ResponseEntity<>(vote, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        assureIdConsistent(vote, voteId);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), voteId +" for change vote up to " + сhangeVoteTime);
        return  new ResponseEntity(checkNotFoundWithId(voteRepository.save(vote, authUserId()), voteId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Vote> create(@RequestParam int restaurantId) {
        log.info("create Vote for restaurantId {} ", restaurantId);
        return  getResponseEntity(voteRepository.save(new Vote(null, now(), restaurantId, authUserId()), authUserId()), REST_URL);
    }
}
