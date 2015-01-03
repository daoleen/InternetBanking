-- // add client table
-- Migration SQL that makes the change goes here.
CREATE TABLE client (
  id BIGINT NOT NULL GENERATED AS IDENTITY PRIMARY KEY,
  first_name        VARCHAR(32) NOT NULL,
  last_name         VARCHAR(32) NOT NULL,
  patronymic_name   VARCHAR(32) NOT NULL,
  birth_date        DATE        NOT NULL,
  passport_series   CHAR(2)     NOT NULL,
  passport_number   INTEGER     NOT NULL,
  address_id        BIGINT      NOT NULL,
  registration_date TIME    NOT NULL DEFAULT CURRENT_TIME,
  mobile_number     VARCHAR(16) NOT NULL,

  FOREIGN KEY (address_id) REFERENCES client_address(id) ON DELETE NO ACTION
);
CREATE INDEX client_fullname_idx ON client(first_name, last_name, patronymic_name);
CREATE UNIQUE INDEX client_passp_idx ON client(passport_series, passport_number);
CREATE UNIQUE INDEX client_mobile_idx ON client(mobile_number);

-- //@UNDO
-- SQL to undo the change goes here.
DROP INDEX client_mobile_idx;
DROP INDEX client_passp_idx;
DROP INDEX client_fullname_idx;
DROP TABLE client;