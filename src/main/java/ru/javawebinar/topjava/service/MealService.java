package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public interface MealService {

    Meal save(Meal meal, int userId);

    boolean delete(int id, int userId);

    Meal get(int id, int userId);

    Collection<MealWithExceed> getAll(int userId, LocalDate dBegin, LocalDate dEnd,
                                      LocalTime tBegin, LocalTime tEnd, int  caloriaLimit);
}