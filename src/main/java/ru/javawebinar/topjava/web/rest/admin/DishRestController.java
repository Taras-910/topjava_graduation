package ru.javawebinar.topjava.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
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
    public List<Dish> getAll(@PathVariable(name = "id") int restaurantId) {
        log.info("getAll dishes for restaurant {}", restaurantId);
        return checkNotFoundWithId(repository.getAll(restaurantId), restaurantId);
    }

    @GetMapping(value = "/restaurants/{id}/date/{date}")
    public List<Dish> getByRestaurantAndDate(@PathVariable(name = "id") int restaurantId,
                                             @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAll dishes for restaurant {}", restaurantId);

        return checkNotFoundWithId(repository.getByRestaurantAndDate(restaurantId, date), restaurantId);
    }

    @DeleteMapping("/{id}")                        /* delete with test limit points from 2 to 5*/
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
    public void deleteAllForRestaurantByDate(@PathVariable(name = "id") @Nullable int restaurantId,
                                @PathVariable @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("deleteAll for restaurantId {} and date {}", restaurantId, date);
        checkNotFoundWithId(repository.deleteAll(restaurantId, date), restaurantId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable(name = "id") int dishId, @RequestParam int restaurantId) {
        log.info("update dish {} with restaurantId {}", dishId, restaurantId);
        Assert.notNull(dish, "dish must not be null");
        assureIdConsistent(dish, dishId);
        checkNotFoundWithId(repository.save(dish, restaurantId), dish.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)         /* create with test limit points from 2 to 5*/
    public ResponseEntity<Dish> create_lim(@Valid @RequestBody Dish dish, @RequestParam int restaurantId) {
        log.info("create dish {} for restaurantId {}", dish, restaurantId);
        Assert.notNull(dish, "dish must not be null");
        checkNew(dish);
        checkNotFound(countWithin(List.of(dish), repository.getByRestaurantAndDate(restaurantId, thisDay)),
                "dishes so number should be within from 2 to 5");
        Dish created = null;
        try {
            created = repository.save(dish, restaurantId);
        } catch (Exception e) {
            throw new NotFoundException("*** this dish("+dish.getName()+") already exist today("+thisDay+")***");
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @PostMapping(value = "/restaurants/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Dish>> createAllForMenu(@Valid @RequestBody List<Dish> dishes, @PathVariable(name = "id") int restaurantId) {
        dishes.forEach(ValidationUtil::checkNew);
        checkNotFound(countWithin(dishes, repository.getByRestaurantAndDate(restaurantId, thisDay)),
                "dishes so number should be within from 2 to 5");
        List<Dish> createdDishes = new ArrayList<>();
        final URI[] uriOfNewResource = new URI[1];
        dishes.forEach(dish -> {
            Dish storedDish = null;
            try {
                storedDish = repository.save(dish, restaurantId);
            } catch (Exception e) {
                throw new NotFoundException("*** this dish("+dish.getName()+") already exist today("+thisDay+")***");
            }
            createdDishes.add(storedDish);
            uriOfNewResource[0] = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(storedDish.getId()).toUri();
        });
        return ResponseEntity.created(uriOfNewResource[0]).body(createdDishes);
    }
}
