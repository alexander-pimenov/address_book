-- Создавать отдельную БД для проекта не будем.
-- Подключимся к БД postgres, создадим отдельную схему
-- address_app для работы и соответствующие таблицы в схеме
-- persons.

--//запускаем командную строку и сначала устанавливаем кодировку кириллицы:

--chcp 1251

--//в консоли подключаемся к БД postgres

--psql --username=postgres

--//При запросе пароля вводим: qwerty
--//устанавливаем кодировку для кириллицы в postgres

--set client_encoding='win1251';

--//можем проверить все имеющиеся Базы Данных

--\l

--//Переключаться между базами данных можно с помощью команды

--\с или \connect имя_базы_данных
--\connect postgres

--//Создаем новую схему в БД postgres: address_app

--create schema address_app;

--//Переключаемся из схемы public на эту схему:

--set search_path=address_app;

--//Создаем таблицу для хранения данных Person:

create table if not exists persons (
    id serial primary key not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    street varchar(255) not null,
    postal_code int not null,
    city varchar(255) not null,
    birthday date not null,
    created timestamp(0) without time zone default now() not null,
	updated timestamp(0) without time zone
);

--//Добавим некоторые данные в таблицу Persons:

insert into persons (first_name, last_name, street,
    postal_code, city, birthday) values ('Alex', 'Pim', 'West', 117222,
    'Kiev','1990-10-05');

insert into persons (first_name, last_name, street,
    postal_code, city, birthday) values ('Dora', 'Hon', 'East', 112255,
    'Lvov','1985-04-08');

insert into persons (first_name, last_name, street,
    postal_code, city, birthday) values ('Maria', 'Kosaa', 'Central', 123456,
    'Krakov','1988-11-18');

--изменим дату updated поста JS-developer
update post set updated='2020-10-10 15:14:13' where id=2;
--если указать дату так, то чч:мм:сс будут в нулях
--update post set updated='2020-10-10' where id=2;


--create table if not exists users (
--   id serial primary key,
--   name varchar(255),
--   role_id int references roles(id)
--);

--create table if not exists role_rule (
--   id serial primary key,
--   role_id int references roles(id),
--   rule_id int references rules(id)
--);

--create table if not exists items (
--     id serial primary key,
--     name varchar(255),
--	 user_id int references users(id),
--	 category_id int references category(id),
--	 state_id int references state(id)
--);