package ru.javawebinar.topjava.store;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Crud  {

    List<Meal> findAll();

    void save(Meal meal);

    Meal findById(int id);

    void update(Meal meal);

    void delete(Meal meal);

}
