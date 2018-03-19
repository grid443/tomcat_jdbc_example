CREATE TABLE IF NOT EXISTS Person (
    id          INTEGER  PRIMARY KEY AUTO_INCREMENT,
    first_name  VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name   VARCHAR(50) NOT NULL,
    age         INTEGER  NOT NULL,
);

INSERT INTO Person (first_name, middle_name, last_name, age) VALUES ('Иван', 'Петрович', 'Сидоров', 40);

INSERT INTO Person (first_name, last_name, age) VALUES ('Пётр', 'Иванов', 25)