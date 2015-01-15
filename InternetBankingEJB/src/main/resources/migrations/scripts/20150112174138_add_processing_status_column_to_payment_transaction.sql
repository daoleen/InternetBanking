-- // add processing_status column to payment_transaction
-- Migration SQL that makes the change goes here.
ALTER TABLE payment_transaction ADD COLUMN processing_status SMALLINT NOT NULL DEFAULT 0;
CREATE INDEX proc_stat_pt_idx ON payment_transaction(processing_status);


-- //@UNDO
-- SQL to undo the change goes here.
DROP INDEX proc_stat_pt_idx;
ALTER TABLE payment_transaction DROP COLUMN processing_status;
