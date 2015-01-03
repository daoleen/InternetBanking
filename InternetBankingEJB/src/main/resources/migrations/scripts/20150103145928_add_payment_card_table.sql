-- // add payment card table
-- Migration SQL that makes the change goes here.
CREATE TABLE payment_card (
  card_number CHAR(19) NOT NULL PRIMARY KEY,
  bank_id INTEGER NOT NULL,
  client_id BIGINT NOT NULL,
  amount DOUBLE NOT NULL DEFAULT '0.0',
  pin_code CHAR(32) FOR BIT DATA NOT NULL,
  created_date DATE NOT NULL DEFAULT CURRENT DATE,

  FOREIGN KEY (bank_id) REFERENCES bank(bic) ON DELETE NO ACTION,
  FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE NO ACTION
);


-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE payment_card;
