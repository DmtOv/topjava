package ru.javawebinar.topjava.web.meal;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class JspMealController extends AbstractMealRestController {

    private static final String MEALS = "meals";
    private static final String MEALS_URL = MEALS;
    private static final String REDIRECT_MEALS = "redirect:" + MEALS;

    private static final String ID = "id";
    private static final String DATETIME = "dateTime";
    private static final String DESCRIPTION = "description";
    private static final String CALORIES = "calories";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";

    @GetMapping("/meals")
    public String getMeals(final Model model) {
        model.addAttribute(MEALS, super.getAll());
        return MEALS_URL;
    }

    @GetMapping("/create")
    public String fillModelCreate(final Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now(), "", 0));
        return "mealForm";
    }

    @GetMapping("/update")
    public String fillModelUpdate(final HttpServletRequest request, final Model model) {
        model.addAttribute("meal", super.get(Integer.valueOf(request.getParameter(ID))));
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(final HttpServletRequest request) {
        super.delete(Integer.valueOf(request.getParameter(ID)));
        return REDIRECT_MEALS;
    }

    @PostMapping("/meals")
    public String create(final HttpServletRequest request) {
        final String id = request.getParameter(ID);
        final Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter(DATETIME)),
                request.getParameter(DESCRIPTION),
                Integer.valueOf(request.getParameter(CALORIES)));

        if (StringUtils.isEmpty(id)) {
            super.create(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            super.update(meal, Integer.parseInt(id));
        }
        return REDIRECT_MEALS;
    }

    @PostMapping("/filter")
    public String getFiltered(final HttpServletRequest request, final Model model) {
        final LocalDate startDate = DateTimeUtil.parseLocalDate(request.getParameter(START_DATE));
        final LocalDate endDate = DateTimeUtil.parseLocalDate(request.getParameter(END_DATE));
        final LocalTime startTime = DateTimeUtil.parseLocalTime(request.getParameter(START_TIME));
        final LocalTime endTime = DateTimeUtil.parseLocalTime(request.getParameter(END_TIME));
        model.addAttribute(MEALS, super.getBetween(startDate, startTime, endDate, endTime));
        return MEALS_URL;
    }
}
