DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS client_address;
DROP TABLE IF EXISTS city;


CREATE TABLE IF NOT EXISTS city (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(64) NOT NULL);
CREATE TABLE IF NOT EXISTS client_address (
  id               BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  city_id          INTEGER      NOT NULL,
  street           VARCHAR(255) NOT NULL,
  house_number     INTEGER      NOT NULL,
  housing_number   INTEGER      NOT NULL DEFAULT '0',
  apartment_number INTEGER      NOT NULL,
  FOREIGN KEY (city_id) REFERENCES "city" (id)
);
CREATE TABLE IF NOT EXISTS client (
  id                BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name        VARCHAR(32) NOT NULL,
  last_name         VARCHAR(32) NOT NULL,
  patronymic_name   VARCHAR(32) NOT NULL,
  birth_date        DATE        NOT NULL,
  passport_series   CHAR(2)     NOT NULL,
  passport_number   INTEGER     NOT NULL,
  address_id        BIGINT      NOT NULL,
  registration_date DATETIME    NOT NULL DEFAULT CURRENT_TIME(),
  mobile_number     VARCHAR(16) NOT NULL,
  FOREIGN KEY (address_id) REFERENCES client_address (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS user (
  id             BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username       VARCHAR(32) NOT NULL,
  password       CHAR(32)    NOT NULL,
  is_enabled     BOOLEAN     NOT NULL DEFAULT TRUE,
  is_blocked     BOOLEAN     NOT NULL DEFAULT FALSE,
  blocked_reason VARCHAR(255),
  client_id      BIGINT      NOT NULL,
  FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE
);

INSERT INTO city (name) VALUES ('Minsk'), ('Moscow');
INSERT INTO client_address (city_id, street, house_number, housing_number, apartment_number)
VALUES (1, 'Masherova', 12, 0, 17);
INSERT INTO client (first_name, last_name, patronymic_name, birth_date, passport_series, passport_number, address_id, registration_date, mobile_number)
VALUES ('Alexander', 'Kozlov', 'Valerevich', '2010-02-02', 'MP', 100000000, 1, CURRENT_TIME(), '217-20-20'),
  ('Alex', 'Kozlov', 'Petrovich', '2010-07-02', 'MP', 100200000, 1, CURRENT_TIME(), '200-01-02');
INSERT INTO user (username, password, is_enabled, is_blocked, client_id)
VALUES ('alexssource@gmail.com', '11111', TRUE, FALSE, 1), ('admin@localhost', '22222', TRUE, FALSE, 2);