package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MealRestController {

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealWithExceed> getAll() {
        return new ArrayList<>(service.getAll(AuthorizedUser.id(),
                LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()));
    }

    public List<MealWithExceed> getAllFiltered(LocalDate startDate, LocalDate endDate,
                                               LocalTime startTime, LocalTime endTime) {
        return new ArrayList<>(service.getAll(
                AuthorizedUser.id(),
                startDate, endDate, startTime, endTime,
                AuthorizedUser.getCaloriesPerDay()));
    }

    public Meal get(int id) {
        return service.get(id, AuthorizedUser.id());
    }

    public Meal save(Meal meal) {
        return service.save(meal, AuthorizedUser.id());
    }

    public Meal update(Meal meal) {
        return save(meal);
    }
}
