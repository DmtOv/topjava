package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealsTestData {

    public static final int USER_MEAL_ID = 100002;
    public static final int ADMIN_MEAL_ID = 100003;

    public static final int USER_ID = 100000;
    public static final int ADMIN_ID = 100001;

    public static final Meal mealUser = new Meal(USER_MEAL_ID,
            LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "user meal", 500);

    public static final Meal mealAdmin = new Meal(ADMIN_MEAL_ID,
            LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "admin meal", 1500);

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }

}
