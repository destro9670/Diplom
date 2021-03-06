create sequence user_id_seq
  increment 1
  start 6
  no cycle;
  
create table users(
  id bigint primary key default nextval('user_id_seq'),
  name varchar(45) not null,
  surname varchar(45) not null,
  nick varchar(15) not null,
  login varchar(15) not null,
  password varchar(256) not null,
  last_seen varchar(20) not null
);

create sequence room_id_seq
  increment 1
  start 5
  no cycle;

create table rooms(
  id bigint primary key default nextval('room_id_seq'),
  name varchar(31) not null
);

create sequence users_in_rooms_id_seq
  increment 1
  start 5
  no cycle;

create table users_in_rooms(
  id bigint primary key default nextval('users_in_rooms_id_seq'),
  user_id bigint references users(id),
  room_id bigint references rooms(id)
);

create sequence messages_id_seq
  increment 1
  start 1
  no cycle;

create table messages(
  id bigint primary key default nextval('messages_id_seq'),
  user_from_id bigint references users(id),
  user_to_id bigint references users(id),
  room_id bigint references rooms(id),
  content text,
  read_status boolean not null,
  send_status boolean not null,
  send_date varchar(20) not null
);