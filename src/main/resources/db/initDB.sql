DROP TABLE IF EXISTS votes;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name                  VARCHAR                 NOT NULL,
    email                 VARCHAR                 NOT NULL,
    password              VARCHAR                 NOT NULL,
    registered            TIMESTAMP DEFAULT now() NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id               INTEGER NOT NULL,
    role                  VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name      VARCHAR  NOT NULL
);

CREATE TABLE dishes
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name           TEXT         NOT NULL,
    date           TIMESTAMP    NOT NULL,
    price          DECIMAL(5,2) NOT NULL,
    restaurant_id  INTEGER      NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

CREATE TABLE votes
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    date               TIMESTAMP NOT NULL,
    restaurant_id      INTEGER   NOT NULL,
    user_id            INTEGER   NOT NULL,
    CONSTRAINT votes_idx UNIQUE (date, user_id),
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES USERS (id)
);
