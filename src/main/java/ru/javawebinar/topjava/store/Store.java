package ru.javawebinar.topjava.store;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Store implements Crud {

    private List<Meal> meals;

    private int count;

    public Store() {
        meals = new CopyOnWriteArrayList<Meal>(Arrays.asList(
                new Meal(count++, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(count++, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(count++, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(count++, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(count++, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(count++, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        ));
    }

    @Override
    public List<Meal> findAll() {
        return meals;
    }

    @Override
    public void save(Meal meal) {
        meal.setId(count++);
        meals.add(meal);
    }

    @Override
    public Meal findById(int id) {
        return meals.stream().filter(meal -> (meal.getId() == id)).findFirst().get();
    }

    @Override
    public void update(Meal meal) {

    }

    @Override
    public void delete(Meal meal) {
        meals.remove(meal);
    }
}
