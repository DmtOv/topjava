package ru.javawebinar.topjava.repository.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealsTestData;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
public class JdbcMealRepositoryImplTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }
    
    @Autowired
    JdbcMealRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void save() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void get() {
        Meal meal = repository.get(MealsTestData.USER_MEAL_ID, MealsTestData.USER_ID);
        assertThat(meal).isEqualTo(MealsTestData.mealUser);
        System.out.println(meal);
        System.out.println(MealsTestData.mealUser);
    }

    @Test
    public void getAll() {
        List<Meal> meals = repository.getAll(100000);
        assertThat(meals.stream().findFirst().orElse(null)).isEqualTo(MealsTestData.mealUser);
    }

    @Test
    public void getBetween() {
    }
}