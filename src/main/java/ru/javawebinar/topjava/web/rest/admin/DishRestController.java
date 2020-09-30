package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.repository.DishRepository;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.util.ResponseEntityUtil.getResponseEntity;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {
    private static final Logger log = LoggerFactory.getLogger(DishRestController.class);
    protected static final String REST_URL = "/rest/admin/dishes";
    private final DishRepository repository;

    public DishRestController(DishRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    public List<Dish> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping(value = "/{id}")
    public Dish getById(@PathVariable int id, @RequestParam int restaurantId) {
        log.info("get dish {}", id);
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    @GetMapping(value = "/menus")
    public List<Dish> getByRestaurantAndDate(@RequestParam int restaurantId, @RequestParam Date localDate) {
        log.info("getByRestaurantAndDate {} localDate {}", restaurantId, localDate);
        return checkNotFoundWithId(repository.getByRestaurantAndDate(restaurantId, localDate), restaurantId);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> update(@Valid @RequestBody Dish dish, @PathVariable(name = "id") int dishId, @RequestParam int restaurantId) {
        log.info("update dish with id {} for restaurantId {}", dishId, restaurantId);
        assureIdConsistent(dish, dishId);
        return new ResponseEntity<>(checkNotFoundWithId(repository.save(dish, restaurantId), dish.id()), HttpStatus.OK);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish, @RequestParam int restaurantId) {
        log.info("create dish {} with restaurantId {}", dish, restaurantId);
        checkNew(dish);
        return getResponseEntity(repository.save(dish, restaurantId), REST_URL);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    @DeleteMapping("/{id}/restaurants/{restaurantId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int dishId, @PathVariable int restaurantId) {
        log.info("delete dish {} of restaurant {}", dishId, restaurantId);
        checkNotFoundWithId(repository.delete(dishId, restaurantId), dishId);
    }
}
