package ru.javawebinar.topjava.repository.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;
import ru.javawebinar.topjava.repository.user.CrudUserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Logger log = LoggerFactory.getLogger("");
    private final CrudVoteRepository voteRepository;
    private final CrudUserRepository userRepository;

    public DataJpaVoteRepository(CrudVoteRepository voteRepository, CrudUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Vote save(Vote vote, int userID) {
        log.info("vote {}", vote);
        vote.setUserId(userRepository.getOne(userID).getId());
        return vote.isNew() || get(vote.id(), vote.getUserId()) != null ? voteRepository.save(vote) : null;
    }

    @Override
    public boolean delete(int id, int userId) { return voteRepository.delete(id, userId) != 0; }

    @Override
    public Vote get(int id, int userId) {
        Vote vote = voteRepository.findById(id).orElse(null);
        return vote != null && vote.getUserId() == userId ? vote : null;
    }

    @Override
    public Vote get(int id) {
        return voteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vote> getAllForAuthUser(int userId) {
        return Optional.ofNullable(voteRepository.getAllForAuthUser(userId)).orElse(null);
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll();
    }

    @Override
    public List<Vote> getBetween(Date startDate, Date endDate, int userId) {
        return Optional.ofNullable(voteRepository.getBetween(startDate, endDate, userId)).orElse(null);
    }

    @Override
    public Vote getByDateForAuth(Date date, int userId) {
        return Optional.ofNullable(voteRepository.getByDateForAuth(date, userId)).orElse(null);
    }

    @Override
    public List<Vote> getByRestaurant(int restaurantId) {
        return Optional.ofNullable(voteRepository.getByRestaurant(restaurantId)).orElse(null);
    }

    @Override
    public List<Vote> getByRestaurantAuth(int restaurantId, int authUserId) {
        return Optional.ofNullable(voteRepository.getByRestaurantAuth(restaurantId, authUserId)).orElse(null);
    }

    @Override
    public List<Vote> getByDate(Date date) {
        return Optional.ofNullable(voteRepository.getByDate(date)).orElse(null);
    }
}
