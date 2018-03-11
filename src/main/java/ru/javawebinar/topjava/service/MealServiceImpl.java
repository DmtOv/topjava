package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int id, int userId) {
        repository.delete(id, userId);
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id, userId);
        if (Objects.nonNull(meal)) {
            return meal;
        } else {
            throw new NotFoundException("Not found meal with this criteias");
        }
    }

    @Override
    public Collection<Meal> getAll(int userId, LocalDate dBegin, LocalDate dEnd, LocalTime tBegin, LocalTime tEnd) {
        List<Meal> meals = new ArrayList<>(repository.getAll(userId, dBegin, dEnd, tBegin, tEnd));
        if (meals.size() > 0) {
            return meals;
        } else {
            throw new NotFoundException("Not found meals with this criteias");
        }
    }
}