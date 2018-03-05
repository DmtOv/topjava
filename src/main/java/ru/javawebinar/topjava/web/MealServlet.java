package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");
        List<MealWithExceed> meals = getMealWithExceeds();
        forwardToMeals(request, response, meals);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("add meal");
        String description = request.getParameter("description");
        final String CALORIES = "calories";
        int calories;

        try {
            calories = Integer.valueOf(request.getParameter(CALORIES));
        } catch (NumberFormatException e) {
            log.debug("Parametr " + CALORIES + " is not number");
            return;
        }

        MealsUtil.addToMeals(new Meal(LocalDateTime.now(), description, calories));
        List<MealWithExceed> meals = getMealWithExceeds();
        forwardToMeals(request, response, meals);
    }

    private void forwardToMeals(HttpServletRequest request, HttpServletResponse response, List<MealWithExceed> meals) throws ServletException, IOException {
        request.setAttribute("meals", meals);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private List<MealWithExceed> getMealWithExceeds() {
        return MealsUtil.getFilteredWithExceededInOnePass(MealsUtil.getMeals(),
                LocalTime.MIN,
                LocalTime.MAX,
                MealsUtil.getCaloriesPerDay());
    }
}
