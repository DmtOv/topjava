package ru.javawebinar.topjava.web.meal;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

@Controller
public class JspMealController extends AbstractMealRestController {

    private static final String MEALS = "meals";

    @Autowired
    MealService mealService;

    @GetMapping("/meals")
    public String getMeals(Model model) {
        model.addAttribute(MEALS, super.getAll());
        return MEALS;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal());
        return "mealForm";
    }




}
