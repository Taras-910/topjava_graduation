package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.repository.RestaurantRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.RestUtil.getResponseEntity;
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
        return checkNotFound(repository.getByName(name), "RestaurantName=" + name);
    }

    @GetMapping("/{id}/date/{date}")
    public Restaurant getByIdWithDishesOfDate(@PathVariable(name = "id") int restaurantId, @PathVariable LocalDate date) {
        log.info("getByIdWithDishesOfDate id {} and date {}", restaurantId, date);
        return checkNotFound(repository.getByIdWithDishesOfDate(restaurantId, date), " illegal argument date=" + date);
    }

    @GetMapping("/dishes")
    public List<Restaurant> getAllWithDishes() {
        log.info("get Restaurants With Dishes");
        return repository.getAllWithDishes();
    }

    @Cacheable("restaurants")
    @GetMapping("/dishes/date/{date}")
    public List<Restaurant> getAllWithDishesOfDate(@PathVariable LocalDate date) {
        log.info("getAllWithDishesOfDate {}",date);
        return checkNotFound(repository.getAllWithDishesOfDate(date), " illegal argument date=" + date);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant for id {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant {} for id {}", restaurant, id);
        Assert.notNull(restaurant.getId(), "id of restaurant must not be null");
        assureIdConsistent(restaurant, id);
        return getResponseEntity(checkNotFoundWithId(repository.save(restaurant), id), REST_URL);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        return getResponseEntity(repository.save(restaurant), REST_URL);
    }
}
