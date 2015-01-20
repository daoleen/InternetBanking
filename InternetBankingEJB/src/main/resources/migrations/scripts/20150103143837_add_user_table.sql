-- // add user table
-- Migration SQL that makes the change goes here.
CREATE TABLE user (
  id BIGINT NOT NULL GENERATED AS IDENTITY PRIMARY KEY,
  username       VARCHAR(32) NOT NULL,
  password       CHAR(80) FOR BIT DATA NOT NULL,
  is_enabled     SMALLINT NOT NULL DEFAULT 1,
  is_blocked     SMALLINT NOT NULL DEFAULT 0,
  blocked_reason VARCHAR(255),
  client_id      BIGINT      NOT NULL,

  FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE NO ACTION
);
CREATE UNIQUE INDEX user_username_idx ON user(username);


-- //@UNDO
-- SQL to undo the change goes here.
DROP INDEX user_username_idx;
DROP TABLE user;
