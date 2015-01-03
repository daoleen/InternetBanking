-- // add city table
-- Migration SQL that makes the change goes here.
CREATE TABLE city (
  id INT NOT NULL GENERATED AS IDENTITY,
  name VARCHAR(64) NOT NULL,
  PRIMARY KEY (id)
);
CREATE UNIQUE INDEX city_name_idx ON city(name);


-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE city;