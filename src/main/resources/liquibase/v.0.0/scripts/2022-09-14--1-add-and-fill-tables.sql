create table if not exists department
(
    id   integer      not null primary key,
    name varchar(32)
);

insert into department values
    (1, 'department 1'),
    (2, 'department 2'),
    (3, 'department 3'),
    (4, 'department 4'),
    (5, 'department 5'),
    (6, 'department 6'),
    (7, 'department 7'),
    (8, 'department 8'),
    (9, 'department 9'),
    (10, 'department 10')
on conflict(id) do nothing;

create table if not exists person
(
    dtype         varchar(31) not null,
    id            integer     not null primary key,
    card          bytea       check ( length(card) <= 16 ) unique,
    type          integer,
    fired_time    timestamp,
    hire_time     timestamp,
    visit_time    timestamp,
    department_id integer constraint reference_department references department,
);