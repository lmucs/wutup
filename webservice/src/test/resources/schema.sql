drop table if exists event;
drop table if exists user;

create table user (
  id integer not null,
  firstName varchar(512),
  lastName varchar(512),
  email varchar(512),
  nickname varchar(512),
  primary key(id)
);  

create table event (
  id integer not null,
  name varchar(512),
  description varchar(1024),
  ownerId integer,
  primary key(id),
  foreign key(ownerId) references user(id)
);

create table venue (
  id integer not null,
  name varchar(512),
  address varchar(512),
  latitude double,
  longitude double,
  primary key(id)
);

create table occurrence (
  id integer not null,
  venueId integer,
  start datetime,
  end datetime,
  primary key(id),
  foreign key(venueId) references venue(id)
);
 
