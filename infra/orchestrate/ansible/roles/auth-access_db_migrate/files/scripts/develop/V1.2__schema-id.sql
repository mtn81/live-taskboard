alter table events add column id bigserial primary key;
alter table event_processes add column id bigserial primary key;
alter table users add column id serial primary key;
alter table auths add column id serial primary key;
alter table user_activations add column id serial primary key;
