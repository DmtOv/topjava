package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(m -> save(m, 0));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        } else {
            // treat case: update, but absent in storage
            return meal.getUserId().equals(userId) ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
        }
    }

    @Override
    public void delete(int id, Integer userId) {
        Meal meal = repository.get(id);
        if (Objects.nonNull(meal) && meal.getUserId().equals(userId)) {
            repository.remove(id);
        }
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.get(id);
        return (Objects.nonNull(meal) && meal.getUserId() != null && meal.getUserId().equals(userId)) ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return repository.values().stream()
                .filter(m -> (m.getUserId().equals(userId)))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAll(Integer userId, LocalDate dBegin, LocalDate dEnd, LocalTime tBegin, LocalTime tEnd) {
        return getAll(userId).stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDate(), dBegin, dEnd))
                .filter(m -> DateTimeUtil.isBetween(m.getTime(), tBegin, tEnd))
                .collect(Collectors.toList());
    }
}
