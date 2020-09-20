DELETE FROM votes;
DELETE FROM user_roles;
DELETE FROM restaurants;
DELETE FROM dishes;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password, registered)
VALUES ('Admin', 'admin@gmail.com', '{noop}admin', '2020-01-30 8:00:00'),
       ('User', 'user@yandex.ru', '{noop}password', '2020-01-30 8:00:00');

INSERT INTO user_roles (role, user_id)
VALUES ('ADMIN', 100000),
       ('USER', 100001);

INSERT INTO restaurants (name)
VALUES ('Венеция'),
       ('Прага');

INSERT INTO dishes (name, local_date, price, restaurant_id)
VALUES ('Суп','2020-06-29', 0.1, 100002),
       ('Чай','2020-06-29', 0.2,  100002),
       ('Борщ','2020-07-29', 1.1, 100002),
       ('Компот','2020-07-29', 1.2,  100002),
       ('еда','2020-07-30', 1.1, 100002),
       ('вода','2020-07-30', 0.2, 100002),
       ('граничная еда','2020-07-30', 2.1, 100003),
       ('Отбивная','2020-07-30', 2.2, 100003),
       ('Фрукты','2020-07-30', 2.3, 100003),
       ('Сладости','2020-07-30', 2.4, 100003),
       ('сок','2020-07-30', 2.5, 100003);

INSERT INTO votes (local_date, restaurant_id, user_id)
VALUES ('2020-06-28', 100002, 100000),
       ('2020-06-29', 100002, 100000),
       ('2020-06-29', 100002, 100001),
       ('2020-06-30', 100002, 100000),
       ('2020-06-30', 100003, 100001),
       ('2020-07-29', 100003, 100001),
       ('2020-07-30', 100002, 100000);
