CREATE TABLE IF NOT EXISTS city (
  id   INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64) NOT NULL
);
CREATE TABLE IF NOT EXISTS client_address (
  id               BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  city_id          INTEGER      NOT NULL,
  street           VARCHAR(255) NOT NULL,
  house_number     INTEGER      NOT NULL,
  housing_number   INTEGER      NOT NULL DEFAULT '0',
  apartment_number INTEGER      NOT NULL,
  FOREIGN KEY (city_id) REFERENCES "city" (id) ON DELETE CASCADE
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

CREATE TABLE IF NOT EXISTS bank (
  bic                 INTEGER      NOT NULL PRIMARY KEY,
  name                VARCHAR(255) NOT NULL,
  bank_account_number BIGINT       NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_card (
  card_number CHAR(19) NOT NULL PRIMARY KEY,
  bank_id INTEGER NOT NULL,
  client_id BIGINT NOT NULL,
  amount DOUBLE NOT NULL DEFAULT '0.0',
  pin_code VARCHAR(32) NOT NULL,
  created_date DATE NOT NULL DEFAULT CURRENT_DATE(),
  FOREIGN KEY (bank_id) REFERENCES bank(bic) ON DELETE CASCADE,
  FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS payment_transaction (
  uuid UUID NOT NULL PRIMARY KEY,
  recepient_bank_id INTEGER,
  recepient_account_number VARCHAR(255),
  recepient_first_name VARCHAR(32),
  recepient_last_name VARCHAR(32),
  recepient_patronymic_name VARCHAR(32),
  recepient_card_number CHAR(19),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  transaction_status SMALLINT NOT NULL DEFAULT '1',
  card_number CHAR(19) NOT NULL,
  completed_at TIMESTAMP,
  money_reservation_id BIGINT NOT NULL,
  FOREIGN KEY (recepient_bank_id) REFERENCES bank(id),
  FOREIGN KEY (recepient_card_number) REFERENCES payment_card(card_number),
  FOREIGN KEY (card_number) REFERENCES payment_card(card_number),
  FOREIGN KEY (money_reservation_id) REFERENCES money_reservation(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS money_reservation(
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  amount DOUBLE NOT NULL DEFAULT '0.00',
  status SMALLINT NOT NULL DEFAULT 1,
  payment_card CHAR(19) NOT NULL,
  created_at TIME NOT NULL DEFAULT CURRENT_TIME(),
  version BIGINT NOT NULL,
  FOREIGN KEY (payment_card) REFERENCES payment_card(card_number) ON DELETE CASCADE
);