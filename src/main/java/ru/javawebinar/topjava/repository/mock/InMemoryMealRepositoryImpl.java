package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
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
        int userId = 0;
        for (Meal m : MealsUtil.MEALS) {
            save(m, ++userId);
        }
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
            Meal mealRepo = repository.get(meal.getId());
            if (mealRepo == null) {
                return null;
            }
            return mealRepo.getUserId().equals(userId) ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
        }
    }

    @Override
    public Boolean delete(int id, Integer userId) {
        Meal meal = repository.get(id);
        if (Objects.nonNull(meal) && meal.getUserId().equals(userId)) {
            return Objects.nonNull(repository.remove(id));
        } else {
            return false;
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
                .sorted((m1, m2) -> ((-1) * m1.getTime().compareTo(m2.getTime())))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAll(Integer userId, LocalDate dBegin, LocalDate dEnd) {
        return getAll(userId).stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDate(),
                        (dBegin == null) ? LocalDate.MIN : dBegin,
                        (dEnd == null) ? LocalDate.MAX : dEnd))
                .collect(Collectors.toList());
    }
}
