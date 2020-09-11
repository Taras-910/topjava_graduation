package ru.javawebinar.topjava.web.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.repository.DishRepository;
import ru.javawebinar.topjava.util.MenuUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.thisDay;
import static ru.javawebinar.topjava.util.MenuUtil.countWithin;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
public class DishController {
    private static final Logger log = LoggerFactory.getLogger(DishController.class);

    private final DishRepository repository;

    public DishController(DishRepository repository) {
        this.repository = repository;
    }

    public Dish get(int id, int restaurantId) {
        log.info("get dish for id {}", id);
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    public List<Dish> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public List<Dish> getAllByRestaurant(int restaurantId) {
        log.info("getAll dishes");
        return repository.getAllByRestaurant(restaurantId);
    }

    public List<Dish> getByRestaurantAndDate(int restaurantId, LocalDate date) {
        log.info("getAll dishes for restaurant {}", restaurantId);
        return checkNotFoundWithId(repository.getByRestaurantAndDate(restaurantId, date), restaurantId);
    }

    public void delete(int dishId, int restaurantId) {
        List<Dish> memoryDishes = repository.getByRestaurantAndDate(restaurantId, thisDay);
        log.info("delete dish {} of restaurant {} from memoryDishes {}", dishId, restaurantId, memoryDishes.size());
        checkNotFound(MenuUtil.countLowerLimit(memoryDishes),
                dishId+" so as dishes number of menu should be at least 2");
        boolean result = repository.delete(dishId, restaurantId);
        checkNotFoundWithId(result, dishId);
    }

    public void deleteListOfMenu(int restaurantId, LocalDate date) {
        log.info("deleteAll for restaurantId {} and date {}", restaurantId, date);
        checkNotFoundWithId(repository.deleteListOfMenu(restaurantId, date), restaurantId);
    }

    public Dish create(Dish dish, int restaurantId) {
        log.info("create dish {} for restaurantId {}", dish, restaurantId);
        Dish created = null;
        try {
            Assert.notNull(dish, "dish must not be null");
            checkNew(dish);
            checkNotFound(countWithin(List.of(dish), repository.getByRestaurantAndDate(restaurantId, thisDay)),
                    "dishes so number should be within from 2 to 5");
            created = repository.save(dish, restaurantId);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException("error argument or dish("+dish.getName()+") already exist today("+thisDay+"), or error data "+dish);
        }
        return created;
    }

    @Transactional
    public List<Dish> createListOfMenu(List<Dish> dishes, int restaurantId) {
        List<Dish> created = null;
        try {
            dishes.forEach(ValidationUtil::checkNew);
            dishes.forEach(d -> Assert.notNull(d, "dish must not be null"));
            dishes.stream().map(dish -> dish.getName().toLowerCase()).distinct().collect(Collectors.toList());
            List<Dish> storedDishes = Optional.ofNullable(repository.getByRestaurantAndDate(restaurantId, thisDay)).orElse(null);
            checkNotFound(countWithin(dishes, storedDishes), "dishes so number should be within from 2 to 5");

            created = checkNotFound(repository.saveAll(dishes, restaurantId), "restaurantId=" + restaurantId);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException | ExceptionInInitializerError e) {
            throw new NotFoundException("error argument or at least one dish from List (" + dishes + ") already exist today(" + thisDay + ")");
        }
        return created;
    }


    public void update(Dish dish, int dishId, int restaurantId) {
        log.info("update dish {} for dishId {} and restaurant {}", dish, dishId, restaurantId);
        try {
            Assert.notNull(dish, "dish must not be null");
            assureIdConsistent(dish, dishId);
            checkNotFoundWithId(repository.save(dish, restaurantId), dish.id());
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException("error data of " + dish);
        }
    }
}
