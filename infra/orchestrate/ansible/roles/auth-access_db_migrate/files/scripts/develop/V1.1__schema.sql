create table users (
  user_id varchar(100),
  email varchar(100),
  name varchar(100),
  password varchar(100)
);

create table auths (
  auth_id varchar(100),
  user_id varchar(100)
);

create table events (
  type varchar(100),
  occurred timestamp,
  body bytea
);