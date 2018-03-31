package ru.javawebinar.topjava.repository.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JpaMealRepositoryImplTest {

    @Autowired
    MealRepository repository;

    @Test
    public void save() {
        int calories = 500;
        String description = "Завтрак";
        Meal newMeal = new Meal(
                LocalDateTime.of(2015, Month.MAY, 30, 10, 25),
                description,
                calories);

        Meal savedMeal = repository.save(newMeal, UserTestData.USER_ID);
        Meal meal = repository.get(savedMeal.getId(), UserTestData.USER_ID);

        assertEquals(meal.getDescription(), description);
        assertEquals(meal.getCalories(), calories);
        assertEquals(meal.getUser(), UserTestData.USER);
    }

    @Test
    public void delete() {
        assertTrue(repository.delete(MealTestData.MEAL1.getId(), UserTestData.USER_ID));
        assertNull(repository.get(MealTestData.MEAL1.getId(), UserTestData.USER_ID));
        assertEquals(repository.getAll(UserTestData.USER_ID).size(), MealTestData.MEALS.size() - 1);
    }

    @Test
    public void deleteNotExist() {
        assertFalse(repository.delete(MealTestData.MEAL1.getId(), UserTestData.ADMIN_ID));
    }

    @Test
    public void getNoExist() {
        assertNull(repository.get(MealTestData.MEAL1.getId(), UserTestData.ADMIN_ID));
    }

    @Test
    public void get() {
        Meal meal = repository.get(MealTestData.MEAL1.getId(), UserTestData.USER_ID);
        assertEquals(meal.getId(), MealTestData.MEAL1.getId());
        assertEquals(meal.getUser(), UserTestData.USER);
    }

    @Test
    public void getAll() {
        List<Meal> userMeals = repository.getAll(UserTestData.USER_ID);
        assertEquals(userMeals.size(), MealTestData.MEALS.size());
    }

    @Test
    public void getBetween() {
        LocalDateTime start = LocalDateTime.of(2015, Month.MAY, 30, 19, 30);
        LocalDateTime end = LocalDateTime.of(2015, Month.MAY, 31, 11, 30);
        List<Meal> userMeals = repository.getBetween(start, end, UserTestData.USER_ID);
        assertMatch(userMeals, MealTestData.MEAL3, MealTestData.MEAL4);
    }
}