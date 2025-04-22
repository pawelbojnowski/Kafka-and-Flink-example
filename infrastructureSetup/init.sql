CREATE EXTENSION pgcrypto WITH SCHEMA public;
create schema if not exists public;
-- use public;

create table public.user
(
    id           int not null primary key,
    firstname    text,
    lastname     text,
    phone_number int
);

INSERT INTO public.user(id, firstname, lastname, phone_number)
VALUES (1, 'John', 'Wick', 100100100),
       (2, 'Jack', 'Sparrow', 200200200),
       (3, 'Brian', 'O’Conner', 300300300);

--
create table public.client
(
    client_id     uuid DEFAULT gen_random_uuid() not null PRIMARY KEY,
    id           int,
    firstname    text,
    lastname     text,
    phone_number int
)
