package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExceededInOnePass;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;

    private final Store store = new Store();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("do get");

        if (request.getParameter("action") != null) {

            if (request.getParameter("action").equals("edit")) {

                /// for to add with param
            }

            if (request.getParameter("action").equals("delete")) {
                int id = Integer.valueOf(request.getParameter("id"));
                store.delete(store.findById(id));
                List<MealWithExceed> meals = getMealWithExceeds();
                request.setAttribute("meals", meals);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            }


        } else {

            List<MealWithExceed> meals = getMealWithExceeds();
            request.setAttribute("meals", meals);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
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

        store.save(new Meal(LocalDateTime.now(), description, calories));

        List<MealWithExceed> mealWithExceeds = getMealWithExceeds();
        request.setAttribute("meals", mealWithExceeds);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private List<MealWithExceed> getMealWithExceeds() {
        return getFilteredWithExceededInOnePass(store.findAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
    }
}
