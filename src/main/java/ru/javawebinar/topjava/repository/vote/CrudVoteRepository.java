package ru.javawebinar.topjava.repository.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id And v.userId=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.userId=:userId")
    List<Vote> getAllForAuthUser(@Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.restaurantId=:restaurantId")
    List<Vote> getByRestaurant(@Param("restaurantId") int restaurantId);

    @Query("SELECT v FROM Vote v WHERE v.restaurantId=:restaurantId AND v.userId=:userId")
    List<Vote> getByRestaurantAuth(@Param("restaurantId") int restaurantId, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.date>=:startDate AND v.date<=:endDate AND v.userId=:userId")
    List<Vote> getBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.date=:date ORDER BY v.date DESC ")
    List<Vote> getByDate(@Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.date=:date AND v.userId=:userId")
    Vote getByDateForAuth(@Param("date") LocalDate date, @Param("userId") int userId);
}
