package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Restaurant;
import ru.javawebinar.topjava.repository.restaurant.CrudRestaurantRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ru.javawebinar.topjava.util.ResponseEntityUtil.getResponseEntity;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantRestController.class);
    protected static final String REST_URL = "/rest/admin/restaurants";
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");
    public final CrudRestaurantRepository repository;

    public RestaurantRestController(CrudRestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(SORT_NAME);
    }

    @GetMapping("/{id}")
    public Restaurant getById(@PathVariable int id) {
        log.info("get restaurant for id {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @GetMapping("/names")
    public Restaurant getByName(@RequestParam String restaurantName) {
        log.info("getByName {}", restaurantName);
        return checkNotFound(repository.getByName(restaurantName), " restaurantName=" + restaurantName);
    }

    @GetMapping("/{id}/date")
    public Restaurant getByIdWithDishesOfDate(@PathVariable(name = "id") int restaurantId, @RequestParam Date localDate) {
        log.info("getByIdWithDishesOfDate id {} and date {}", restaurantId, localDate);
        return checkNotFound(Optional.ofNullable(repository.getByIdWithDishesOfDate(restaurantId, localDate))
                .orElse(null), " illegal variable restaurantId=" + restaurantId + " or date=" + localDate);
    }

    @GetMapping("/dishes")
    public List<Restaurant> getAllWithDishes() {
        log.info("get Restaurants With Dishes");
        return repository.getAllWithDishes();
    }

    @Cacheable("restaurants")
    @GetMapping("/menus")
    public List<Restaurant> getAllWithDishesOfDate(@RequestParam Date localDate) {
        log.info("getAllWithDishesOfDate {}",localDate);
        return checkNotFound(Optional.ofNullable(repository.getAllWithDishesOfDate(localDate))
                .orElse(new ArrayList<>()), " illegal argument date=" + localDate);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant for id {}", id);
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant {} for id {}", restaurant, id);
        assureIdConsistent(restaurant, id);
        return new ResponseEntity(checkNotFoundWithId(repository.save(restaurant), id), HttpStatus.OK);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        return getResponseEntity(repository.save(restaurant), REST_URL);
    }
}
