package ru.javawebinar.topjava.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.сhangeVoteTime;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private VoteRepository voteRepository;

    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote get(int id, int userId) {
        log.info("get vote for {}", id);
        return checkNotFoundWithId(voteRepository.get(id, userId), id);
    }

    public List<Vote> getAllForUser(int userId) {
        log.info("getAllForUser for userId {}", userId);
        return voteRepository.getForLoggedUser(userId);
    }

    public List<Vote> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        log.info("getBetween with dates({} - {}) for userId {}", startDate, endDate, userId);
        return voteRepository.getBetween(startDate, endDate, userId);
    }

    public List<Vote> getAll() {
        log.info("getAll votes");
        return voteRepository.getAll();
    }

    public void delete(int id, int userId) {
        log.info("delete vote {} for userId {}", id, userId);
        checkNotFoundWithId(voteRepository.delete(id, userId), id);
    }

    public Vote create(Vote vote, int userId) {
        log.info("create {} for userId", vote);
        Assert.notNull(userId, "user must be logged-in");
        checkNew(vote);
        return voteRepository.save(vote, userId);
    }

    public void update(Vote vote, int id, int userId) {
        log.info("update vote {} with id {} for userId {}", vote, id, userId);
        Assert.notNull(vote, "vote must not be null");
        assureIdConsistent(vote, id);
        checkNotFound(LocalTime.now().isBefore(сhangeVoteTime), id +" for change vote up to 11:00");
        checkNotFoundWithId(voteRepository.save(vote, userId), vote.id());
    }


}
