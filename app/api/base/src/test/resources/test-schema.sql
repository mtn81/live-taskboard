create table events (
  id integer auto_increment,
  type varchar(100),
  publisher varchar(100),
  occurred timestamp,
  body bytea
);

create table event_tracks (
  id integer auto_increment,
  category varchar(50),
  key varchar(100),
  occurred timestamp,
  state varchar(10)
)

		
		