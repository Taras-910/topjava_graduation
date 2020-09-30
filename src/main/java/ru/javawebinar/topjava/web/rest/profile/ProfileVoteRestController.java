package ru.javawebinar.topjava.web.rest.profile;

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
import ru.javawebinar.topjava.util.exception.MethodNotAllowedException;

import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.ResponseEntityUtil.getResponseEntity;
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

    @GetMapping(value = "/restaurant")
    public List<Vote> getByRestaurantAuth(@RequestParam int restaurantId) {
        log.info("get all of restaurant {}", restaurantId);
        return checkNotFound(voteRepository.getByRestaurantAuth(restaurantId, authUserId()), " for restaurant " + restaurantId);
    }

    @GetMapping(value = "/date")
    public Vote getByDateForAuth(@RequestParam Date localDate) {
        log.info("get for user {} by date {}", authUserId(), localDate);
        return voteRepository.getByDateForAuth(localDate, authUserId());
    }

    @GetMapping(value = "/between")
    public List<Vote> getBetween(@RequestParam @Nullable Date startDate, @RequestParam @Nullable Date endDate) {
        log.info("getBetween with dates({} - {}) for userId {}", startDate, endDate, authUserId());
        return voteRepository.getBetween(startDate, endDate, authUserId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int voteId) {
        log.info("delete vote {} for userId {}", voteId, authUserId());
        checkInTime(voteId);
        checkNotFoundWithId(voteRepository.delete(voteId, authUserId()), voteId);
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Vote>  update(@RequestParam int voteId, @RequestParam int restaurantId) {
        log.info("update Vote {} for restaurantId {}", voteId, restaurantId);
        checkInTime(voteId);
        Vote vote = new Vote(voteId, new Date(), restaurantId, authUserId());
        return new ResponseEntity(checkNotFoundWithId(voteRepository.save(vote, authUserId()), voteId), HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Vote> create(@RequestParam int restaurantId) {
        log.info("create vote for restaurantId {}", restaurantId);
        checkExist();
        return getResponseEntity(voteRepository.save(new Vote(null, thisDay, restaurantId, authUserId()), authUserId()), REST_URL);
    }

    private void checkExist() {
        if(getByDateForAuth(thisDay) != null){
            throw new MethodNotAllowedException("method allowed for " + authUserId()
                    + ", because of vote already exist for this day=" + thisDay);
        }
    }
}
