drop table if exists event;

create table event (
  id number(9) not null,
  name varchar(512),
  primary key(id)
);
