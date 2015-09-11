alter table groups add column id serial primary key;
alter table members add column id serial primary key;
alter table groups_members add column id serial primary key;
alter table events add column id bigserial primary key;
alter table event_processes add column id bigserial primary key;
alter table tasks add column id serial primary key;
alter table task_id_gen add column id serial primary key;
