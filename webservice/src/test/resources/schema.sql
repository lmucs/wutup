drop table if exists event;
drop table if exists user;

create table event (
  id number(9) not null,
  name varchar(512),
  primary key(id)
);

create table user(
  id number(9) not null,
  firstName varchar(512),
  lastName varchar(512),
  email varchar(512),
  nickname varchar(512)
);  
