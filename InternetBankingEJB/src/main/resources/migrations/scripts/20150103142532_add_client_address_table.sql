-- // add client_address table
-- Migration SQL that makes the change goes here.
CREATE TABLE client_address (
  id BIGINT NOT NULL GENERATED AS IDENTITY PRIMARY KEY,
  city_id INTEGER NOT NULL,
  street VARCHAR(255) NOT NULL,
  house_number     INTEGER      NOT NULL,
  housing_number   INTEGER,
  apartment_number INTEGER      NOT NULL,
  FOREIGN KEY (city_id) REFERENCES city(id) ON DELETE NO ACTION
);


-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE client_address;