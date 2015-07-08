create table groups (
  group_id varchar(100) unique not null,
  owner_member_id varchar(100) not null,
  name varchar(100) not null,
  description varchar(100)
);

create table members (
  member_id varchar(10) unique not null
);

create table groups_members (
  group_id varchar(100) not null,
  member_id varchar(10) not null,

  constraint uc_groups_members unique (group_id, member_id)
);
