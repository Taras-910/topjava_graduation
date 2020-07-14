package ru.javawebinar.topjava.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Dish;
import ru.javawebinar.topjava.repository.DishRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
public class DishController {
    private static final Logger log = LoggerFactory.getLogger(DishController.class);

    private final DishRepository repository;

    public DishController(DishRepository repository) {
        this.repository = repository;
    }

    public Dish get(int id) {
        log.info("get dish for id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Dish> getAll() {
        log.info("getAll dishes");
        return repository.getAll();
    }

    public void delete(int id, int restaurantId) {
        log.info("delete dish for id {} restaurantId {}", id, restaurantId);
        checkNotFoundWithId(repository.delete(id, restaurantId), id);
    }

    public void deleteAll(int restaurantId, LocalDate date) {
        log.info("deleteAll for restaurantId {} and date {}", restaurantId, date);
        checkNotFoundWithId(repository.deleteAll(restaurantId, date), restaurantId);
    }

    public Dish create(Dish dish, int restaurantId) {
        log.info("create dish {} for restaurantId {}", dish, restaurantId);
        Assert.notNull(dish, "dish must not be null");
        checkNew(dish);
        return repository.save(dish, restaurantId);
    }

    public List<Dish> createAll(List<Dish> newDishes, int restaurantId) {
        log.info("saveAll for newDishes {} with restaurantId {}", newDishes, restaurantId);
        Assert.notNull(newDishes, "dish must not be null");
        newDishes.forEach(newDish -> checkNew(newDish));
        List<Dish> dishes = new ArrayList<>();
        newDishes.forEach(newDish -> dishes.add(create(newDish, restaurantId)));
        return dishes;
    }

    public void update(Dish dish, int dishId, int restaurantId) {
        log.info("update dish {} for dishId {} and restaurant {}", dish, dishId, restaurantId);
        Assert.notNull(dish, "dish must not be null");
        assureIdConsistent(dish, dishId);
        checkNotFoundWithId(repository.save(dish, restaurantId), dish.id());
    }
}
