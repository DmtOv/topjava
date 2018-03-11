package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    //4.4: конвертацию в MealWithExceeded можно делать как в слое web, так и в service
    //5.1: Отдать свою еду (для отображения в таблице, формат List<MealWithExceed>), запрос БЕЗ параметров
    //5.5: Сервлет мы удалим, а контроллер останется,
    // поэтому возвращать List<MealWithExceed> надо из контроллера.
    // И userId принимать в контроллере НЕЛЬЗЯ (иначе - для чего аторизация?).
    // Подмену MIX/MAX для Date/Time также сделайте здесь.
    //5.7: Сделайте отдельный getAll без применения фильтра

    public List<MealWithExceed> getAll() {
        return MealsUtil.getWithExceeded(
                service.getAll(AuthorizedUser.id(), LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX),
                AuthorizedUser.getCaloriesPerDay());
    }

    //5.2: Отдать свою еду, отфильтрованную по startDate, startTime, endDate, endTime
    public List<MealWithExceed> getAllFiltered(LocalDate startDate, LocalDate endDate,
                                               LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getWithExceeded(
                service.getAll(AuthorizedUser.id(), startDate, endDate, startTime, endTime),
                AuthorizedUser.getCaloriesPerDay());
    }

    //5.3: Отдать/удалить свою еду по id, параметр запроса - id еды.
    // Если еда с этим id чужая или отсутствует - NotFoundException
    public MealWithExceed get(int id) {
        Meal meal = service.get(id, AuthorizedUser.id());
        if (meal != null) {
            return MealsUtil.getWithExceeded(
                    service.getAll(AuthorizedUser.id(), meal.getDate(), meal.getDate(), LocalTime.MIN, LocalTime.MAX),
                    AuthorizedUser.getCaloriesPerDay()).stream()
                    .filter(m -> (m.getId() == id)).findFirst()
                    .orElseThrow(() -> new NotFoundException("Not found meal"));
        } else {
            throw new NotFoundException("Not found meal");
        }
    }

    //5.4: Сохранить/обновить еду, параметр запроса - Meal. Если обновляемая еда с этим id чужая или отсутствует - NotFoundException
    public Meal save(Meal meal, Integer userId) {
        Meal updatedMeal = service.save(meal, userId);
        if (updatedMeal != null) {
            return meal;
        } else {
            throw new NotFoundException("Not found meal for update");
        }
    }

    //5.6: В REST при update принято передавать id (см. AdminRestController.update)
    public Meal update(Meal meal, Integer userId) {
        return save(meal, AuthorizedUser.id());
    }
}
