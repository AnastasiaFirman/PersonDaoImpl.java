drop table if exists books;
drop table if exists person;

create table person (
id serial primary key,
first_name text,
last_name text,
age int,
date_of_birth date
);

create table books (
id serial primary key,
book_title text,
author text,
person_id bigint,
foreign key (person_id) references person (id)
);
