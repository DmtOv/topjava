package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        saveRoles(user);
        return user;
    }

    private void saveRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());

        final Set<Role> roles = user.getRoles();
        if (roles.size() > 0) {
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", roles, roles.size(),
                    (preparedStatement, role) -> {
                        preparedStatement.setInt(1, user.getId());
                        preparedStatement.setString(2, role.name());
                    });
        }
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        User user = DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id));
        user.setRoles(getRoles(id));
        return user;
    }

    private Set<Role> getRoles(int id) {
        List<String> roles = jdbcTemplate.query("SELECT role FROM user_roles WHERE user_id=?",
                BeanPropertyRowMapper.newInstance(String.class), id);
        return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        user.setRoles(getRoles(user.getId()));
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);

        List<Integer> ids = users.stream().map(User::getId).collect(Collectors.toList());

        Map<Integer, Set<Role>> usRoles = new HashMap<>();

        namedParameterJdbcTemplate.query("select * from user_roles WHERE user_id in (:ids)",
                new MapSqlParameterSource("ids", ids),
                rs -> {
                    int id = rs.getInt(1);
                    String role = rs.getString(2);

                    if (usRoles.containsKey(id)) {
                        users.get(id).getRoles().add(Role.valueOf(role));
                    } else {
                        Set<Role> roles = new HashSet<>(Arrays.asList(Role.valueOf(role)));
                        usRoles.put(id, roles);
                    }
                });
        for (User user : users) {
            user.setRoles(usRoles.get(user.getId()));
        }
        return users;
    }
}
