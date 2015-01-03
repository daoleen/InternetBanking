-- // Bootstrap.sql

-- This is the only SQL script file that is NOT
-- a valid migration and will not be run or tracked
-- in the changelog.  There is no @UNDO section.

-- // Do I need this file?

-- New projects likely won't need this file.
-- Existing projects will likely need this file.
-- It's unlikely that this bootstrap should be run
-- in the production environment.

-- // Purpose

-- The purpose of this file is to provide a facility
-- to initialize the database to a state before MyBatis
-- SQL migrations were applied.  If you already have
-- a database in production, then you probably have
-- a script that you run on your developer machine
-- to initialize the database.  That script can now
-- be put in this bootstrap file (but does not have
-- to be if you are comfortable with your current process.

-- // Running

-- The bootstrap SQL is run with the "migrate bootstrap"
-- command.  It must be run manually, it's never run as
-- part of the regular migration process and will never
-- be undone. Variables (e.g. ${variable}) are still
-- parsed in the bootstrap SQL.

-- After the boostrap SQL has been run, you can then
-- use the migrations and the changelog for all future
-- database change management.

INSERT INTO city (name) VALUES ('Minsk'), ('Moscow');
INSERT INTO client_address (city_id, street, house_number, housing_number, apartment_number) VALUES
(1, 'Masherova', 12, 0, 17);

INSERT INTO client (first_name, last_name, patronymic_name, birth_date, passport_series, passport_number, address_id, registration_date, mobile_number) VALUES
  ('Alexander', 'Kozlov', 'Valerevich', '2010-02-02', 'MP', 100000000, 1, CURRENT TIME, '217-20-20'),
  ('Alex', 'Kozlov', 'Petrovich', '2010-07-02', 'MP', 100200000, 1, CURRENT TIME, '200-01-02');

INSERT INTO user (username, password, is_enabled, is_blocked, client_id) VALUES
  ('alexssource@gmail.com', '11111', 1, 0, 1),
  ('admin@localhost', '22222', 1, 0, 2);

INSERT INTO bank (bic, name, bank_account_number) VALUES
  (1000000, 'БПС-Банк', 100000000000000001),
  (1000001, 'БСБ-Банк', 100000000000000002),
  (1000002, 'МТ-Банк',  100000000000000003);

INSERT INTO payment_card(card_number, bank_id, client_id, amount, pin_code) VALUES
  ('0000-0000-0000-0001', 1000000, 1, '150.0', '5f4dcc3b5aa765d61d8327deb882cf99'),
  ('0000-0000-0000-0002', 1000000, 2, '90.0', '5f4dcc3b5aa765d61d8327deb882cf99');