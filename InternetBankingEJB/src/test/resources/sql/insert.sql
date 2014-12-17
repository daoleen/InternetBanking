INSERT INTO city (name) VALUES ('Minsk'), ('Moscow');
INSERT INTO client_address (city_id, street, house_number, housing_number, apartment_number) VALUES (1, 'Masherova', 12, 0, 17);
INSERT INTO client (first_name, last_name, patronymic_name, birth_date, passport_series, passport_number, address_id, registration_date, mobile_number) VALUES ('Alexander', 'Kozlov', 'Valerevich', '2010-02-02', 'MP', 100000000, 1, CURRENT_TIME(), '217-20-20'), ('Alex', 'Kozlov', 'Petrovich', '2010-07-02', 'MP', 100200000, 1, CURRENT_TIME(), '200-01-02');
INSERT INTO user (username, password, is_enabled, is_blocked, client_id) VALUES ('alexssource@gmail.com', '11111', TRUE, FALSE, 1), ('admin@localhost', '22222', TRUE, FALSE, 2);
INSERT INTO bank (bic, name, bank_account_number) VALUES (1000000, 'БПС', 100000000000000001);
INSERT INTO payment_card(card_number, bank_id, client_id, amount, pin_code) VALUES ('0000-0000-0000-0001', 1000000, 1, '150.0', 'passssssssssssssHASHED');