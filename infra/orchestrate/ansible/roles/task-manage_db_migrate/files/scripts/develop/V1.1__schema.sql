create table groups (
  group_id varchar(100) unique not null,
  owner_member_id varchar(100) not null,
  name varchar(100) not null,
  description varchar(100),
  state varchar(10) not null
);

create table members (
  member_id varchar(10) unique not null
);

create table groups_members (
  group_id varchar(100) not null,
  member_id varchar(10) not null,

  constraint uc_groups_members unique (group_id, member_id)
);

create table events (
  type varchar(100),
  occurred timestamp,
  body bytea
);

create table event_processes (
  type varchar(100) unique,
  last_event_id integer
);
