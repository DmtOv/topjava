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

    @GetMapping("/meals")
    public String getMeals(final Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/create")
    public String fillModelCreate(final Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now(), "", 0));
        return "mealForm";
    }

    @GetMapping("/update")
    public String fillModelUpdate(final HttpServletRequest request, final Model model) {
        model.addAttribute("meal", super.get(Integer.valueOf(request.getParameter("id"))));
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(final HttpServletRequest request) {
        super.delete(Integer.valueOf(request.getParameter("id")));
        return "redirect:meals";
    }

    @PostMapping("/meals")
    public String create(final HttpServletRequest request) {
        final String id = request.getParameter("id");
        final Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        if (StringUtils.isEmpty(id)) {
            super.create(meal);
        } else {
            int intId = Integer.parseInt(id);
            meal.setId(intId);
            super.update(meal, intId);
        }
        return "redirect:meals";
    }

    @PostMapping("/filter")
    public String getFiltered(final HttpServletRequest request, final Model model) {
        final LocalDate startDate = DateTimeUtil.parseLocalDate(request.getParameter("startDate"));
        final LocalDate endDate = DateTimeUtil.parseLocalDate(request.getParameter("endDate"));
        final LocalTime startTime = DateTimeUtil.parseLocalTime(request.getParameter("startTime"));
        final LocalTime endTime = DateTimeUtil.parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
