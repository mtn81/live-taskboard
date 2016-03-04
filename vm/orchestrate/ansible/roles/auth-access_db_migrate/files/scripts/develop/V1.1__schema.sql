create table events (
  type varchar(100),
  publisher varchar(100),
  occurred timestamp,
  body bytea
);

create table event_processes (
  type varchar(100) unique,
  last_event_id integer
);

create table event_tracks (
  category varchar(50),
  key varchar(100),
  occurred timestamp,
  state varchar(10),

  constraint uc_track_key unique (category, key)
);

create table users (
  user_id varchar(100),
  email varchar(100),
  name varchar(100),
  password varchar(100),
  status varchar(10),
  notify_email boolean not null,
  activation_id varchar(100) not null,
  activation_expire timestamp not null
);

create table social_users (
  user_id varchar(100) not null unique,
  orig_email varchar(100),
  orig_name varchar(100) not null,
  email varchar(100),
  name varchar(100),
  notify_email boolean not null,
  social_id varchar(100) not null,
  type varchar(10) not null
);

create table auths (
  auth_id varchar(100),
  user_id varchar(100)
);

