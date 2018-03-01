package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        getFilteredWithExceededCircle(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceededCircle(final List<UserMeal> mealList,
                                                                         final LocalTime startTime,
                                                                         final LocalTime endTime,
                                                                         final int caloriesPerDay) {

        final Map<LocalDate, Integer> mealCalories = new HashMap<>(); // <Дата, Сумма калорий в день>
        final Map<LocalDate, List<Integer>> indexes = new HashMap<>(); // <Дата, Индекс еды в итоговом списке>
        final List<UserMealWithExceed> resultListMeals = new ArrayList<>(); // Итоговый список еды, после всех проверок

        for (UserMeal userMeal : mealList) {

            final LocalDate date = userMeal.getDateTime().toLocalDate();
            final Integer calories = userMeal.getCalories();

            final Integer calInDay = mealCalories.get(date);
            if (calInDay == null) {
                mealCalories.put(date, calories);
                addMealsWithOutExceed(startTime, endTime, indexes, resultListMeals, userMeal, date);
            } else {
                if (calInDay <= caloriesPerDay) {
                    final int totalDataCalories = calInDay + userMeal.getCalories();
                    mealCalories.put(date, totalDataCalories);
                    if (totalDataCalories > caloriesPerDay) { // если калорийность в этот день превысила значение
                        // найти все meals этого дня в итоговом списке и сменить им флаг exceed  на true
                        if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                            UserMealWithExceed umwe = getUserMealWithExceed(userMeal, true);
                            resultListMeals.add(umwe);
                        }
                        if (indexes.get(date) != null) {
                            for (Integer indx : indexes.get(date)) {
                                resultListMeals.get(indx).setExceed(true);
                            }
                        }
                        // продукт с превышенеим калорий в день список индексом не добавляем,
                        // к нему мы точно не вернемся что бы изменить флаг exceed
                    } else { // если калорийность не превышена
                        addMealsWithOutExceed(startTime, endTime, indexes, resultListMeals, userMeal, date);
                    }
                }
            }
        }

        return resultListMeals;
    }

    private static void addMealsWithOutExceed(final LocalTime startTime,
                                              final LocalTime endTime,
                                              final Map<LocalDate, List<Integer>> indexes,
                                              final List<UserMealWithExceed> resultListMeals,
                                              final UserMeal userMeal,
                                              final LocalDate date) {
        if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
            UserMealWithExceed umwe = getUserMealWithExceed(userMeal, false);
            resultListMeals.add(umwe);
            // выясним индекс и добавим в список индексов для текущей даты
            final int indexMeal = resultListMeals.indexOf(umwe);
            saveMealIndex(indexes, date, indexMeal);
        }
    }

    private static void saveMealIndex(Map<LocalDate, List<Integer>> indexes, LocalDate date, int indexMeal) {
        if (indexes.get(date) != null) {
            indexes.get(date).add(indexMeal);
        } else {
            List<Integer> inds = new ArrayList<>();
            inds.add(indexMeal);
            indexes.put(date, inds);
        }
    }

    private static UserMealWithExceed getUserMealWithExceed(UserMeal userMeal, boolean exceed) {
        return new UserMealWithExceed(
                userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                exceed);
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
