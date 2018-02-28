package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStream(final List<UserMeal> mealList,
                                                                         final LocalTime startTime,
                                                                         final LocalTime endTime,
                                                                         final int caloriesPerDay) {

        final Map<LocalDate, List<UserMeal>> groupMeals = mealList.stream()
                .collect(Collectors.groupingBy(o -> o.getDateTime().toLocalDate()));

        return groupMeals.values().stream().map(userMeals -> {
            final int caloriesInDay = userMeals.stream().mapToInt(UserMeal::getCalories).sum();
            final boolean isExceed = caloriesInDay > caloriesPerDay;

            return userMeals.stream()
                    .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                    .map(userMeal -> new UserMealWithExceed(userMeal.getDateTime(),
                            userMeal.getDescription(),
                            userMeal.getCalories(),
                            isExceed))
                    .collect(Collectors.toList());
        }).flatMap(List::stream).collect(Collectors.toList());
    }
}
