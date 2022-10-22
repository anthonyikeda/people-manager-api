# Setup

```
createa role test_user with login password 'letmein';
create database test_db with owner test_user;
create table person ( person_id bigint generated always as identity, name varchar(140), age integer);
```
