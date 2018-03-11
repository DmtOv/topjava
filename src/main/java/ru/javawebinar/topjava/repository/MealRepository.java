package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public interface MealRepository {

    // null if not found
    Meal save(Meal meal, Integer userId);

    void delete(int id, Integer userId);

    // null if not found
    Meal get(int id, Integer userId);

    // List.Empty if not found
    Collection<Meal> getAll(Integer userId);

    Collection<Meal> getAll(Integer userId, LocalDate dBegin, LocalDate dEnd, LocalTime tBegin, LocalTime tEnd);
}
