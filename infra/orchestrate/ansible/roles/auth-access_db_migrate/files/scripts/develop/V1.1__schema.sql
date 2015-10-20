create table events (
  type varchar(100),
  occurred timestamp,
  body bytea
);

create table event_processes (
  type varchar(100) unique,
  last_event_id integer
);


create table users (
  user_id varchar(100),
  email varchar(100),
  name varchar(100),
  password varchar(100),
  status varchar(10)
);

create table auths (
  auth_id varchar(100),
  user_id varchar(100)
);

create table user_activations (
  activation_id varchar(100) not null,
  user_id varchar(100) not null,
  expire timestamp not null,
  constraint uc_user_activations unique (activation_id, user_id)
);
