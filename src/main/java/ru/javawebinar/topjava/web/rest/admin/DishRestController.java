package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.repository.DishRepository;
import ru.javawebinar.topjava.util.MenuUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.MenuUtil.countWithin;
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
    public Dish get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish {}", id);
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    @GetMapping(value = "/restaurants/{id}")
    public List<Dish> getAllByRestaurant(@PathVariable(name = "id") int restaurantId) {
        log.info("getAllByRestaurant restaurant {}", restaurantId);
        return checkNotFoundWithId(repository.getAllByRestaurant(restaurantId), restaurantId);
    }

    @GetMapping(value = "/restaurants/{id}/date/{date}")
    public List<Dish> getByRestaurantAndDate(@PathVariable(name = "id") int restaurantId,
                                             @PathVariable LocalDate date) {
        log.info("getAll dishes for restaurant {}", restaurantId);
        return checkNotFoundWithId(repository.getByRestaurantAndDate(restaurantId, date), restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int dishId, @RequestParam int restaurantId) {
        List<Dish> memoryDishes = repository.getByRestaurantAndDate(restaurantId, thisDay);
        log.info("delete dish {} of restaurant {} from memoryDishes {}", dishId, restaurantId, memoryDishes.size());
        checkNotFound(MenuUtil.countLowerLimit(memoryDishes),
                dishId+" so as dishes number of menu should be at least 2");
        checkNotFoundWithId(repository.delete(dishId, restaurantId), dishId);
    }

    @DeleteMapping("restaurants/{id}/date/{date}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteListOfMenu(@PathVariable(name = "id") @Nullable int restaurantId,
                                @PathVariable @Nullable LocalDate date) {
        log.info("deleteListOfMenu for restaurant {} and date {}", restaurantId, date);
        checkNotFoundWithId(repository.deleteListOfMenu(restaurantId, date), restaurantId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable(name = "id") int dishId, @RequestParam int restaurantId) {
        log.info("update dish {} with restaurantId {}", dish, restaurantId);
        try {
            Assert.notNull(dish, "dish must not be null");
            assureIdConsistent(dish, dishId);
            checkNotFoundWithId(repository.save(dish, restaurantId), dish.id());
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException("error data of " + dish);
        }
    }

    /* create with test limit dishesPerDay from 2 to 5*/
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish, @RequestParam int restaurantId) {
        log.info("create dish {} for restaurantId {}", dish, restaurantId);
        Dish created;
        try {
            Assert.notNull(dish, "dish must not be null");
            checkNew(dish);
            checkNotFound(countWithin(List.of(dish), repository.getByRestaurantAndDate(restaurantId, thisDay)),
                    "dishes so number should be within from 2 to 5");
            created = repository.save(dish, restaurantId);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException("error data dish or " + dish.getName() + " already exist in menu today(" + thisDay + ")");
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @PostMapping(value = "/restaurants/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dish>> createListOfMenu(@Valid @RequestBody List<Dish> dishes, @PathVariable(name = "id") int restaurantId) {
        List<Dish> createdDishes = new ArrayList<>();
        final URI[] uriOfNewResource = new URI[1];
        try {
            dishes.forEach(d -> Assert.notNull(d, "dish must not be null"));
            dishes.forEach(ValidationUtil::checkNew);
            dishes.stream().map(dish -> dish.getName().toLowerCase()).distinct().collect(Collectors.toList());
            List<Dish> storedDishes = Optional.ofNullable(repository.getByRestaurantAndDate(restaurantId, thisDay)).orElse(null);
            checkNotFound(countWithin(dishes, storedDishes), "dishes so number should be within from 2 to 5");
            dishes.forEach(dish -> {
                Dish storedDish;
                storedDish = repository.save(dish, restaurantId);
                createdDishes.add(storedDish);
                uriOfNewResource[0] = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(REST_URL + "/{id}")
                        .buildAndExpand(storedDish.getId()).toUri();
            });
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException | ExceptionInInitializerError e) {
            throw new NotFoundException("error argument or at least one dish from List (" + dishes + ")");
        }
        return ResponseEntity.created(uriOfNewResource[0]).body(createdDishes);
    }
}
