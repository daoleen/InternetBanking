-- // add bank table
-- Migration SQL that makes the change goes here.
CREATE TABLE bank (
  bic INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(32) NOT NULL,
  bank_account_number BIGINT NOT NULL
);
CREATE UNIQUE INDEX bank_name_idx ON bank(name);
CREATE UNIQUE INDEX bank_account_idx ON bank(bank_account_number);


-- //@UNDO
-- SQL to undo the change goes here.
DROP INDEX bank_account_idx;
DROP INDEX bank_name_idx;
DROP TABLE bank;