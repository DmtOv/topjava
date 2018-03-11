package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;
import java.util.Objects;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));


            //2.3: Проверьте сценарий: авторизованный пользователь пробует изменить чужую еду (id еды ему не принадлежит).
            InMemoryMealRepositoryImpl bean = appCtx.getBean(InMemoryMealRepositoryImpl.class);
            Meal m = bean.get(2, 0);
            m.setDescription("new Description");

            if (Objects.isNull(bean.save(m, AuthorizedUser.id()))) {
                System.out.println("Success. Пользователь не смог сохранить чужую еду");
            } else {
                System.out.println("Error");
            }

            // 5 check we enable meal classes in context of spring
            appCtx.getBean(InMemoryMealRepositoryImpl.class);
            appCtx.getBean(MealServiceImpl.class);
            appCtx.getBean(MealRestController.class);
            System.out.println("We have all Meal classes in context");

        }
    }
}