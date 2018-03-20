package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealsTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;


import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealsTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealServiceImpl service;

    @Test
    public void get() {
        assertThat(service.get(USER_MEAL_ID, USER_ID).equals(mealUser));
    }

    @Test(expected = NotFoundException.class)
    public void notFoundGet() {
        service.get(1, 1);
    }

    @Test
    public void delete() {
        service.delete(MealsTestData.mealUser.getId(), MealsTestData.USER_ID);
        assertThat(service.getAll(MealsTestData.USER_ID).isEmpty());
    }

    @Test(expected = NotFoundException.class)
    public void notFoundDelete()
    {
        service.delete(1, 1);
    }

    @Test
    public void getBetweenDates() {
    }

    @Test
    public void getBetweenDateTimes() {
    }

    @Test
    public void getAll() {
        assertThat(service.getAll(USER_ID).size()).isEqualTo(1);
        assertThat(service.getAll(ADMIN_ID).size()).isEqualTo(1);
    }

    @Test
    public void update() {
        Meal meal = new Meal(MealsTestData.mealUser.getId(),
                MealsTestData.mealUser.getDateTime(),
                "test description",
                777);
        service.create(meal, USER_ID);
        Meal updated = service.get(MealsTestData.mealUser.getId(), USER_ID);

        assertThat(updated.getId()).isEqualTo(meal.getId());
        assertThat(updated.getDescription()).isEqualTo(meal.getDescription());
        assertThat(updated.getCalories()).isEqualTo(meal.getCalories());
    }

    @Test
    public void create() {
        Meal newMealUser = new Meal(
                LocalDateTime.of(2015, Month.JUNE, 2, 21, 0),
                "user meal 2",
                700);
        Meal created = service.create(newMealUser, MealsTestData.USER_ID);
        newMealUser.setId(created.getId());
        assertThat(service.getAll(MealsTestData.USER_ID).size()).isEqualTo(2);
        assertMatch(service.getAll(MealsTestData.USER_ID), MealsTestData.mealUser, newMealUser);
    }
}