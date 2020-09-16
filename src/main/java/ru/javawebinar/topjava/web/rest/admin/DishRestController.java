package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.repository.DishRepository;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.javawebinar.topjava.util.MenuUtil.countLowerLimit;
import static ru.javawebinar.topjava.util.MenuUtil.countWithin;
import static ru.javawebinar.topjava.util.RestUtil.getResponseEntity;
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

    @GetMapping(value = "/{id}/restaurants/{restaurantId}")
    public Dish getById(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish {}", id);
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    @GetMapping(value = "/restaurants/{id}")
    public List<Dish> getAllByRestaurant(@PathVariable(name = "id") int restaurantId) {
        log.info("getAllByRestaurant restaurant {}", restaurantId);
        return checkNotFoundWithId(repository.getAllByRestaurant(restaurantId), restaurantId);
    }

    @GetMapping(value = "/restaurants/{id}/date/{date}")
    public List<Dish> getByRestaurantAndDate(@PathVariable(name = "id") int restaurantId, @PathVariable LocalDate date) {
        log.info("getAll dishes for restaurant {}", restaurantId);
        return checkNotFoundWithId(repository.getByRestaurantAndDate(restaurantId, date), restaurantId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> update(@Valid @RequestBody Dish dish, @PathVariable(name = "id") int dishId, @RequestParam int restaurantId, BindingResult result) {
        log.info("update dish {} with restaurantId {}", dish, restaurantId);
        if (result != null && result.hasErrors()) {
            return new ResponseEntity<>(dish, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            assureIdConsistent(dish, dishId);
            return getResponseEntity(checkNotFoundWithId(repository.save(dish, restaurantId), dish.id()), REST_URL);
        } catch (NullPointerException e) {
            throw new NotFoundException("error data of dish=" + dish);
        }
    }

    // from 2 to 5
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createLimit(@Valid @RequestBody Dish dish, @RequestParam int restaurantId, BindingResult result) {
        log.info("create dish {} for restaurantId {}", dish, restaurantId);
        log.info("update dish {} with restaurantId {}", dish, restaurantId);
        if (result != null && result.hasErrors()) {
            return new ResponseEntity<>(dish, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        checkNew(dish);
        checkNotFound(countWithin(List.of(dish), repository.getByRestaurantAndDate(restaurantId, dish.getDate())),
                "dishes so number should be within from 2 to 5");
        return getResponseEntity(repository.save(dish, restaurantId), REST_URL);
    }

    @Transactional
    @PostMapping(value = "/restaurants/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dish>> createListOfMenu(@Valid @RequestBody List<Dish> dishes, @PathVariable(name = "id") int restaurantId) {
        List<Dish> createdDishes = new ArrayList<>();
        final URI[] uriOfNewResource = new URI[1];
        try {
            dishes.forEach(ValidationUtil::checkNew);
            List<Dish> storedDishes = Optional.ofNullable(repository.getByRestaurantAndDate(restaurantId, dishes.get(0).getDate())).orElse(null);
            checkNotFound(countWithin(dishes, storedDishes), "dishes so number should be within from 2 to 5");
            dishes.forEach(dish -> {
                Dish storedDish = repository.save(dish, restaurantId);
                createdDishes.add(storedDish);
                uriOfNewResource[0] = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(REST_URL + "/{id}")
                        .buildAndExpand(storedDish.getId()).toUri();
            });
        } catch (NullPointerException e) {
            throw new NotFoundException("error argument dish on dishes=" + dishes );
        }
        return ResponseEntity.created(uriOfNewResource[0]).body(createdDishes);
    }
    // at least 2
    @Transactional
    @DeleteMapping("/{id}/restaurants/{restaurantId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int dishId, @PathVariable int restaurantId, @RequestParam LocalDate date) {
        List<Dish> memoryDishes = Optional.ofNullable(repository.getByRestaurantAndDate(restaurantId, date)).orElse(new ArrayList<>());
        log.info("delete dish {} of restaurant {} from count {}", dishId, restaurantId, memoryDishes.size());
        checkNotFound(countLowerLimit(memoryDishes), dishId+" so as dishes number of menu should be at least 2");
        checkNotFoundWithId(repository.delete(dishId, restaurantId), dishId);
    }

    @DeleteMapping("restaurants/{id}/date/{date}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteListOfMenu(@PathVariable(name = "id") @Nullable int restaurantId, @PathVariable @Nullable LocalDate date) {
        log.info("deleteListOfMenu for restaurant {} and date {}", restaurantId, date);
        checkNotFoundWithId(repository.deleteListOfMenu(restaurantId, date), restaurantId);
    }
}
