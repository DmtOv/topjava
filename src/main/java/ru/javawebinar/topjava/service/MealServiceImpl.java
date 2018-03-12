package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;


@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        return checkNotFound(repository.save(meal, userId), meal.toString() + " for " + userId);
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.delete(id, userId);
    }

    @Override
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public Collection<MealWithExceed> getAll(int userId,
                                             LocalDate dBegin, LocalDate dEnd,
                                             LocalTime tBegin, LocalTime tEnd,
                                             int caloriaLimit) {

        List<Meal> meals = new ArrayList<>(repository.getAll(userId, dBegin, dEnd));
        checkNotFound(meals.size() > 0 , "meals for userId=" + userId);
        return MealsUtil.getWithExceeded(
                meals.stream()
                        .filter(m -> DateTimeUtil.isBetween(m.getTime(), tBegin, tEnd))
                        .collect(Collectors.toList()),
                caloriaLimit);
    }
}