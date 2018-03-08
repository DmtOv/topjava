package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.store.MemoryCrud;

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

    private MemoryCrud memoryCrud;

    @Override
    public void init() {
        memoryCrud = new MemoryCrud();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("do get");
        request.setCharacterEncoding("UTF-8");

        if (request.getParameter("action") != null
                && (!request.getParameter("action").equals(""))) { // update
            if (request.getParameter("action").equals("edit")) {
                int id = Integer.valueOf(request.getParameter("id"));
                Meal meal = memoryCrud.findById(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/add.jsp").forward(request, response);
            } else if (request.getParameter("action").equals("delete")) {  // delete
                int id = Integer.valueOf(request.getParameter("id"));
                Meal m = memoryCrud.findById(id);
                memoryCrud.delete(m);
                fwdToListMeals(request, response);
            }
        } else {
            fwdToListMeals(request, response);
        }
    }

    private void fwdToListMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealWithExceed> mealWithExceeds = getMealWithExceeds();
        request.setAttribute("meals", mealWithExceeds);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("add meal");
        request.setCharacterEncoding("UTF-8");

        if (request.getParameter("id") != null
                && (!request.getParameter("id").equals(""))) { // update
            String description = request.getParameter("description");
            int calories = Integer.valueOf(request.getParameter("calories"));
            int id = Integer.valueOf(request.getParameter("id"));

            Meal meal = memoryCrud.findById(id);
            meal.setDescription(description);
            meal.setCalories(calories);
            memoryCrud.update(meal);
            fwdToListMeals(request, response);
        } else {  // add
            String description = request.getParameter("description");
            int calories = Integer.valueOf(request.getParameter("calories"));
            memoryCrud.save(new Meal(LocalDateTime.now(), description, calories));
            fwdToListMeals(request, response);
        }
    }

    private List<MealWithExceed> getMealWithExceeds() {
        return getFilteredWithExceededInOnePass(memoryCrud.findAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
    }
}
