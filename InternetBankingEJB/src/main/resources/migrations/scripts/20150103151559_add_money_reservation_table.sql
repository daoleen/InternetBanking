-- // add money reservation table
-- Migration SQL that makes the change goes here.
CREATE TABLE money_reservation (
  id BIGINT NOT NULL GENERATED AS IDENTITY PRIMARY KEY,
  amount DOUBLE NOT NULL DEFAULT '0.00',
  status SMALLINT NOT NULL DEFAULT 1,
  payment_card CHAR(19) NOT NULL,
  created_at TIME NOT NULL DEFAULT CURRENT TIME,
  version BIGINT NOT NULL,
  FOREIGN KEY (payment_card) REFERENCES payment_card(card_number) ON DELETE CASCADE
);

-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE money_reservation;
