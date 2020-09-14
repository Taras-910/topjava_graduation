package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.repository.RestaurantRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantRestController.class);
    protected static final String REST_URL = "/rest/admin/restaurants";
    private final RestaurantRepository repository;

    public RestaurantRestController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping("/{id}")
    public Restaurant getById(@PathVariable int id) {
        log.info("get restaurant for id {}", id);
        return checkNotFoundWithId(repository.getById(id), id);
    }

    @GetMapping("/names/{name}")
    public Restaurant getByName(@PathVariable String name) {
        log.info("getByName {}", name);

        Restaurant restaurant = null;
        try {
            restaurant = checkNotFound(repository.getByName(name), "RestaurantName " + name);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException(" Illegal argument " + name + " in RestaurantController getByName");
        }
        return restaurant;
    }

    @GetMapping("/{id}/date/{date}")
    public Restaurant getByIdWithDishesOfDate(@PathVariable(name = "id") int restaurantId, @PathVariable LocalDate date) {
        log.info("getByIdWithDishesOfDate id {} and date {}", restaurantId, date);
        Restaurant restaurant = null;
        try {
            restaurant = checkNotFound(repository.getByIdWithDishesOfDate(restaurantId, date), " date='" + date +"'");
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            throw new NotFoundException(" Illegal argument date=" + date + "in getByIdWithDishesOfDate");
        }
        return restaurant;
    }

    @GetMapping("/dishes")
    public List<Restaurant> getAllWithDishes() {
        log.info("get Restaurants With Dishes");
        return repository.getAllWithDishes();
    }

    @Cacheable("rest_restaurants")
    @GetMapping("/date/{date}")
    public List<Restaurant> getAllWithDishesOfDate(@PathVariable LocalDate date) {
        if (date == null ){
            date = thisDay;
        }
        log.info("getAllWithDishesOfDate {}",date);
        List<Restaurant> restaurants = null;
        try {
            restaurants = repository.getAllWithDishesOfDate(date);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            throw new NotFoundException(" Illegal argument " + date);
        }
        return restaurants;
    }

    @CacheEvict(value = "rest_restaurants", allEntries = true)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("delete restaurant for id {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @CacheEvict(value = "rest_restaurants", allEntries = true)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Restaurant> update(@RequestBody Restaurant restaurant, @Valid @PathVariable int id) {
        log.info("update restaurant {} for id {}", restaurant, id);
        Restaurant updated;
        try {
            Assert.notNull(restaurant, "restaurant must not be null");
            Assert.notNull(restaurant.getId(), "id of restaurant must not be null");
            assureIdConsistent(restaurant, id);
            updated = repository.save(restaurant);
            checkNotFoundWithId(updated, id);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException(" Illegal argument restaurant=" + restaurant + " or id=" + id);
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(updated.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(updated);
    }

    @CacheEvict(value = "rest_restaurants", allEntries = true)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        Restaurant created;
        try {
            Assert.notNull(restaurant, "restaurant must not be null");
            checkNew(restaurant);
            restaurant.setDishes(null);
            created = repository.save(restaurant);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException(" Illegal argument restaurant=" + restaurant + " in create RestaurantController");
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
