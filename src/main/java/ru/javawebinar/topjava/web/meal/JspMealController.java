package ru.javawebinar.topjava.web.meal;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
public class JspMealController extends AbstractMealRestController {

    private static final String MEALS = "meals";
    private static final String REDIRECT = "redirect:";

    @GetMapping("/meals")
    public String getMeals(Model model) {
        model.addAttribute(MEALS, super.getAll());
        return MEALS;
    }

    @GetMapping("/create")
    public String fillModel(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now(), "", 0));
        return "mealForm";
    }

    @PostMapping("/meals")
    public String create(HttpServletRequest request, Model model) {
        final String id = request.getParameter("id");
        final Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter(Meal.DATETIME)),
                request.getParameter(Meal.DESCRIPTION),
                Integer.valueOf(request.getParameter(Meal.CALORIES)));

        if (StringUtils.isEmpty(id)) {
            super.create(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            super.update(meal, Integer.parseInt(id));
        }
        return REDIRECT + MEALS;
    }


}
