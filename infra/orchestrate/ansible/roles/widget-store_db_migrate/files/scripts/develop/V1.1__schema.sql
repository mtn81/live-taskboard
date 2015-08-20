create table widgets (
  category_id varchar(100) not null,
  widget_id varchar(100) not null,
  x integer not null,
  y integer not null,
  width integer not null,
  height integer not null,

  constraint uc_widget_key unique (category_id, widget_id)
);
