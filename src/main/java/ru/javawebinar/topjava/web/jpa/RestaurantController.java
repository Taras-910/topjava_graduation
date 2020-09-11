package ru.javawebinar.topjava.web.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.repository.RestaurantRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
public class RestaurantController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantRepository repository;

    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant getById(int id) {
        log.info("get restaurant for id {}", id);
        return checkNotFoundWithId(repository.getById(id), id);
    }

    public Restaurant getByName(String name) {
        log.info("get restaurant {}", name);

        Restaurant restaurant = null;
        try {
            restaurant = checkNotFound(repository.getByName(name), "RestaurantName " + name);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException(" Illegal argument " + name + " in RestaurantController getByName");
        }
        return restaurant;
    }

    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return repository.getAll();
    }

    public List<Restaurant> getAllWithDishes() {
        log.info("get Restaurants With Dishes");
        return repository.getAllWithDishes();
    }
    @Cacheable("restaurants")
    public List<Restaurant> getAllWithDishesOfDate(LocalDate date) {
        log.info("getWithDishesForDate with date {}",date);
        List<Restaurant> restaurants = null;
        try {
            restaurants = repository.getAllWithDishesOfDate(date);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            throw new NotFoundException(" Illegal argument " + date);
        }
        return restaurants;
    }

    public Restaurant getByIdWithDishesOfDate(int id, LocalDate date) {
        log.info("getById with id {} and date {}", id, date);
        Restaurant restaurant = null;
        try {
            restaurant = checkNotFound(repository.getByIdWithDishesOfDate(id, date), " date='" + date +"'");
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            throw new NotFoundException(" Illegal argument " + date);
        }
        return restaurant;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        Restaurant created = null;
        try {
            Assert.notNull(restaurant, "restaurant must not be null");
            checkNew(restaurant);
            restaurant.setDishes(null);
            created = repository.save(restaurant);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException(" Illegal argument vote=" + restaurant + " or id=" + restaurant);
        }
        return created;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(Restaurant restaurant, int id) {
        log.info("update restaurant {} for id {}", restaurant, id);
        try {
            Assert.notNull(restaurant, "restaurant must not be null");
            Assert.notNull(restaurant.getId(), "id of restaurant must not be null");
            assureIdConsistent(restaurant, id);
            Restaurant updated = repository.save(restaurant);
            checkNotFoundWithId(updated, id);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException(" Illegal argument vote=" + restaurant + " or id=" + restaurant);
        }
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        log.info("delete restaurant for id {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }
}
