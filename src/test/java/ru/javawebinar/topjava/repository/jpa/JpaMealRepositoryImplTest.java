package ru.javawebinar.topjava.repository.jpa;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JpaMealRepositoryImplTest {

    @Autowired
    JpaMealRepositoryImpl repository;

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
        Meal meal = repository.get(MealTestData.MEAL1.getId(), UserTestData.USER_ID);
        assertEquals(meal.getId(), MealTestData.MEAL1.getId());
        assertEquals(meal.getUser(), UserTestData.USER);
    }

    @Test
    public void getAll() {
        List<Meal> meals = repository.getAll(UserTestData.USER_ID);
        assertEquals(meals.size(), MealTestData.MEALS.size());
    }

    @Test
    public void getBetween() {
    }
}