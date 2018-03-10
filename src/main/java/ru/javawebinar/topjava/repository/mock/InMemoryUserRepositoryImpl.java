package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    private Map<Integer, User> repository = new ConcurrentHashMap<>();

    private AtomicInteger counter = new AtomicInteger(0);

    {
        Stream.of(
                new User(null, "userName1", "email1", "password1", Role.ROLE_ADMIN),
                new User(null, "userName2", "email2", "password2", Role.ROLE_USER),
                new User(null, "userName3", "email3", "password3", Role.ROLE_USER),
                new User(null, "userName4", "email4", "password4", Role.ROLE_USER)
        ).forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return Objects.nonNull(repository.remove(id));
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);

        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return Optional.ofNullable(repository.get(id)).orElse(null);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values().stream().sorted().collect(Collectors.toList());
    }

    @Override
    public User getByEmail(final String email) {
        log.info("getByEmail {}", email);

        return repository.values().stream()
                .filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }
}
