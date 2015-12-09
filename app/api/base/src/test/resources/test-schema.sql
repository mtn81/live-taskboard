create table events (
  id integer auto_increment,
  type varchar(100),
  publisher varchar(100),
  occurred timestamp,
  body bytea
);

		
		