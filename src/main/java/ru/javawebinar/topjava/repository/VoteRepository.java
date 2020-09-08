package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {
    // null if not found, when updated
    Vote save(Vote vote, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Vote get(int id, int userId);

    // null if not found
    Vote get(int id);

    // null if not found
    List<Vote> getAllForAuthUser(int userId);

    List<Vote> getAll();

    List<Vote> getBetween(LocalDate startDate, LocalDate endDate, int userId);

    Vote getByDateForAuth(LocalDate date, int userId);

    List<Vote> getByRestaurant(int restaurantId);

    boolean isExistVote(LocalDate date, int authUserId);

    List<Vote> getByRestaurantAuth(int id, int authUserId);

    List<Vote> getByDate(LocalDate date);
}
