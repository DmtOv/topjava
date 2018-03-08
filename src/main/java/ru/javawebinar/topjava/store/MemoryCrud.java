package ru.javawebinar.topjava.store;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class MemoryCrud implements Crud {

    private ConcurrentMap<Integer, Meal> store;

    private int count;

    private synchronized int getId() {
        return count++;
    }

    public MemoryCrud() {
        store = new ConcurrentHashMap<Integer, Meal>();
        Stream.of(
                new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        ).forEach(meal -> {
            store.put(meal.getId(), meal);
        });
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Meal save(Meal meal) {
        int id = getId();
        meal.setId(id);
        store.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal findById(int id) {
        return store.getOrDefault(id, null);
    }

    @Override
    public void update(Meal meal) {
        store.replace(meal.getId(), meal);
    }

    @Override
    public void delete(Meal meal) {
        store.remove(meal.getId(), meal);
    }
}
