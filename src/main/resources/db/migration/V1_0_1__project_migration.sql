create table if not exists location (
    "location_id" bigint not null unique generated always as identity,
    "location_city" varchar(14) not null,
    "location_market" varchar(100) not null,
    "location_deleted" boolean default false

);

insert into location(location_city, location_market) values ('San Francisco', 'West');
insert into location(location_city, location_market) values ('New York', 'East');
insert into location(location_city, location_market) VALUES ('Chicago', 'Central');

create table if not exists project (
    "project_id" bigint not null unique generated always as identity,
    "project_name" varchar(140) not null,
    "project_location_id" bigint not null,
    "project_deleted" boolean default false
);

alter table project
    add constraint fk_project_location_id
        foreign key (project_location_id) references location(location_id);

insert into project(project_name, project_location_id) values ('Facebook', 1);
insert into project(project_name, project_location_id) values ('Google', 1);
insert into project(project_name, project_location_id) values ('Trello', 2);
