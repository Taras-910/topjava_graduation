package ru.javawebinar.topjava.repository.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;
import ru.javawebinar.topjava.repository.restaurant.CrudRestaurantRepository;
import ru.javawebinar.topjava.repository.user.CrudUserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaVoteRepository implements VoteRepository {
    private static final Logger log = LoggerFactory.getLogger("");
    private final CrudVoteRepository voteRepository;
    private final CrudUserRepository userRepository;
    private final CrudRestaurantRepository restaurantRepository;

    public DataJpaVoteRepository(CrudVoteRepository voteRepository, CrudUserRepository userRepository,
                                 CrudRestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Vote save(Vote vote, int userID) throws NullPointerException {
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
    public List<Vote> getBetween(LocalDate startDate, LocalDate endDate, int userId) {
        return Optional.ofNullable(voteRepository.getBetween(startDate, endDate, userId)).orElse(null);
    }

    @Override
    public Vote getByDateForAuth(LocalDate date, int userId) {
        return Optional.ofNullable(voteRepository.getByDateForAuth(date, userId)).orElse(null);
    }

    @Override
    public List<Vote> getByRestaurant(int restaurantId) throws NullPointerException{
        return Optional.ofNullable(voteRepository.getByRestaurant(restaurantId)).orElse(null);
    }

    @Override
    public List<Vote> getByRestaurantAuth(int restaurantId, int authUserId) {
        return Optional.ofNullable(voteRepository.getByRestaurantAuth(restaurantId, authUserId)).orElse(null);
    }

    @Override
    public List<Vote> getByDate(LocalDate date) {
        return Optional.ofNullable(voteRepository.getByDate(date)).orElse(null);
    }
}
