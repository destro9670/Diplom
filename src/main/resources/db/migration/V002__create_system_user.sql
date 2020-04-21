insert into users (id, name, surname, nick, login, password, last_seen) values(1, 'system', 'system', 'system', 'system', 'NuN',   'Nun');
insert into users (id, name, surname, nick, login, password, last_seen) values(2, 'user1',  'user1',  'user1',  'user1',  'user1', 'user1');
insert into users (id, name, surname, nick, login, password, last_seen) values(3, 'user2',  'user2',  'user2',  'user2',  'user2', 'user2');
insert into users (id, name, surname, nick, login, password, last_seen) values(4, 'user3',  'user3',  'user3',  'user3',  'user3', 'user3');
insert into users (id, name, surname, nick, login, password, last_seen) values(5, 'user4',  'user4',  'user4',  'user4',  'user4', 'user4');

insert  into rooms(id, name) values (1,'system_user1');
insert  into rooms(id, name) values (2,'system_user2');
insert  into rooms(id, name) values (3,'system_user3');
insert  into rooms(id, name) values (4,'system_user4');

insert into users_in_rooms (id,user_id, room_id) values (1,1,1);
insert into users_in_rooms (id,user_id, room_id) values (2,1,2);
insert into users_in_rooms (id,user_id, room_id) values (3,1,3);
insert into users_in_rooms (id,user_id, room_id) values (4,1,4);
