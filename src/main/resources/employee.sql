create table employee
(
    id      bigint auto_increment
        primary key,
    grade   varchar(255) null,
    name    varchar(255) null,
    salary  double       null,
    surname varchar(255) null
);

INSERT INTO employee (grade, name, salary, surname)
VALUES ('CHICKEN', 'Pepe', 11000.0, 'Le Pew'),
       ('CHICKEN', 'Playboy', 4000.0, 'Penguin'),
       ('BOSS', 'Matka', 13000.0, 'Boszka'),
       ('CHICKEN', 'Baba', 10000.0, 'Yaga');