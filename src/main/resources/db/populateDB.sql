DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);


INSERT INTO meals (date_time, description, calories, user_id) VALUES
  (TIMESTAMP '2015-06-01 14:00:00', 'user meal', 500, 100000);


INSERT INTO meals (date_time, description, calories, user_id) VALUES
  (TIMESTAMP '2015-06-01 21:00:00', 'admin meal', 1500, 100001);