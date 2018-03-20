package ru.javawebinar.topjava.repository.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealsTestData;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealsTestData.assertMatch;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryImplTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    JdbcMealRepositoryImpl repository;

    @Test
    public void save() {
        Meal newMealUser = new Meal(
                LocalDateTime.of(2015, Month.JUNE, 2, 21, 0),
                "user meal 2",
                700);
        Meal created = repository.save(newMealUser, MealsTestData.USER_ID);
        newMealUser.setId(created.getId());
        assertMatch(repository.getAll(MealsTestData.USER_ID), MealsTestData.mealUser, newMealUser);
    }

    @Test
    public void delete() {
        assertThat(repository.delete(MealsTestData.mealUser.getId(), MealsTestData.USER_ID)).isTrue();
        assertThat(repository.getAll(MealsTestData.USER_ID).isEmpty());
    }

    @Test
    public void get() {
        Meal meal = repository.get(MealsTestData.USER_MEAL_ID, MealsTestData.USER_ID);
        assertThat(meal).isEqualTo(MealsTestData.mealUser);
    }

    @Test
    public void getAll() {
        List<Meal> meals = repository.getAll(100000);
        assertThat(meals.stream().findFirst().orElse(null)).isEqualTo(MealsTestData.mealUser);
    }

    @Test
    public void getBetween() {
        LocalDateTime end = LocalDateTime.of(2015, Month.JUNE, 1, 19, 0);
        LocalDateTime start = LocalDateTime.of(2015, Month.JUNE, 1, 12, 0);
        assertMatch(repository.getBetween(start, end, MealsTestData.USER_ID),  MealsTestData.mealUser);
    }
}