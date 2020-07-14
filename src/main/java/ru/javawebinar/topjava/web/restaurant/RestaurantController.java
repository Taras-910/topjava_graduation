package ru.javawebinar.topjava.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.repository.RestaurantRepository;
import ru.javawebinar.topjava.web.dish.DishController;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
public class RestaurantController {
    private static final Logger log = LoggerFactory.getLogger(DishController.class);
    private final RestaurantRepository repository;

    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNew(restaurant);
        restaurant.setDishes(null);
        return repository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(Restaurant restaurant, int id) {
        log.info("update restaurant {} for id {}", restaurant, id);
        Assert.notNull(restaurant, "restaurant must not be null");
        assureIdConsistent(restaurant, id);
        checkNotFoundWithId(repository.save(restaurant), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        log.info("delete restaurant for id {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant get(int id) {
        log.info("get restaurant for id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return repository.getAll();
    }

    @Cacheable("restaurants")
    public List<Restaurant> getWithDishes(LocalDate date) {
        log.info("getWithDishesForDate with date {}",date);
        return repository.getWithDishes(date);
    }

    public Restaurant getById(int id, LocalDate date) {
        log.info("getById with id {} and date", id, date);
        return checkNotFoundWithId(repository.getById(id, date), id);
    }
}
