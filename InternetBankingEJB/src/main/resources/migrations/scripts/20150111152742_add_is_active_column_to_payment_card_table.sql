-- // add is_active column to payment card table
-- Migration SQL that makes the change goes here.
ALTER TABLE payment_card ADD COLUMN is_active SMALLINT NOT NULL DEFAULT 0;


-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE payment_card DROP COLUMN is_active;
