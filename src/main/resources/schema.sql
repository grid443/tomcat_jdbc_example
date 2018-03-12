CREATE TABLE IF NOT EXISTS Person (
    id         INTEGER  PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    age        INTEGER  NOT NULL
);

INSERT INTO Person (first_name, age) VALUES ('Иван', 40);

INSERT INTO Person (first_name, age) VALUES ('Пётр', 25)