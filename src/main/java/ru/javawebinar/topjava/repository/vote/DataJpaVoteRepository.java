package ru.javawebinar.topjava.repository.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.repository.VoteRepository;
import ru.javawebinar.topjava.repository.user.CrudUserRepository;

import java.time.LocalDate;
import java.util.List;

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
    public Vote save(Vote vote, int userId) {
        vote.setUserId(userRepository.getOne(userId).getId());
        return vote.isNew() || get(vote.id(), userId) != null ? voteRepository.save(vote) : null;
    }

    @Override
    public boolean delete(int id, int userId) { return voteRepository.delete(id, userId) != 0; }

    @Override
    public Vote get(int id, int userId) {
        Vote vote = voteRepository.findById(id).orElse(null);
        return vote != null && vote.getUserId() == userId ? vote : null;
    }

    @Override
    public List<Vote> getForLoggedUser(int userId) {
        return voteRepository.getForLoggedUser(userId);
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll();
    }

    @Override
    public List<Vote> getBetween(LocalDate startDate, LocalDate endDate, int userId) {
        return voteRepository.getBetween(startDate, endDate, userId);
    }
}
