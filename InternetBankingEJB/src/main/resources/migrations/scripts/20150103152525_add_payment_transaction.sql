-- // add payment transaction table
-- Migration SQL that makes the change goes here.
CREATE TABLE payment_transaction (
  uuid CHAR(16) FOR BIT DATA NOT NULL PRIMARY KEY,
  recepient_bank_id INTEGER,
  recepient_account_number VARCHAR(255),
  recepient_first_name VARCHAR(32),
  recepient_last_name VARCHAR(32),
  recepient_patronymic_name VARCHAR(32),
  recepient_card_number CHAR(19),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT TIMESTAMP,
  transaction_status SMALLINT NOT NULL DEFAULT '1',
  card_number CHAR(19) NOT NULL,
  completed_at TIMESTAMP,
  money_reservation_id BIGINT NOT NULL,

  FOREIGN KEY (recepient_bank_id) REFERENCES bank(bic),
  FOREIGN KEY (recepient_card_number) REFERENCES payment_card(card_number),
  FOREIGN KEY (card_number) REFERENCES payment_card(card_number),
  FOREIGN KEY (money_reservation_id) REFERENCES money_reservation(id) ON DELETE CASCADE
);


-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE payment_transaction;