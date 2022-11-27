create table if not exists "person" (
    person_id bigint not null unique generated always as identity,
    name varchar(140) not null,
    age integer not null default 0,
    person_deleted boolean default false
);

INSERT INTO person(name, age) VALUES('Belinda', 23);
insert into person (name, age) values('Mark', 29);
insert into person(name, age) values('Justine', 32);
insert into person(name, age) values('Justin', 22);
insert into person (name, age) values ('Passion', 22);
insert into person(name, age) values ('Jenny', 19);
insert into person (name, age) values ('Jack', 32);
insert into person(name, age) values ('Andy', 34);
insert into person (name, age) values ('Anne', 34);
insert into person(name, age) values ('Amber', 27);
insert into person (name, age) values ('Christopher', 27);
insert into person(name, age) values ('Frank', 36);
insert into person (name, age) values ('Danielle', 28);

