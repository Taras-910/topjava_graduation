package ru.javawebinar.topjava.repository.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Modifying
    @Query("SELECT r FROM Restaurant r ORDER BY r.name ASC")
    List<Restaurant> getAll();

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.dishes d WHERE d.localDate=:date ORDER BY r.name ASC")
    List<Restaurant> getAllWithDishesOfDate(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.dishes d ORDER BY r.name ASC")
    List<Restaurant> getAllWithDishes();

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.dishes d " +
            "WHERE r.id=:restaurantId AND d.localDate=:date ORDER BY r.name")
    Restaurant getByIdWithDishesOfDate(@Param("restaurantId") int restaurantId, @Param("date") LocalDate date);

    @Query("SELECT r FROM Restaurant r WHERE r.name=:name")
    Restaurant getByName(@Param("name") String name);
}
