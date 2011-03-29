CREATE DATABASE fingerbook;

USE fingerbook;

CREATE TABLE WebUser
(
	username varchar(32) not null PRIMARY KEY,
	password varchar(32) not null,
	email varchar(64) not null,
	usertype CHAR(1) not null,
	birthdate DATETIME,
	lastLogindate DATETIME,
	registrationdate DATETIME 
) ENGINE=InnoDB;


create table users
(
	username varchar(50) not null primary key, 
	password varchar(50) not null,
	enabled boolean not null
) ENGINE=InnoDB;


create table authorities 
(
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
) ENGINE=InnoDB;

create unique index ix_auth_username on authorities (username,authority);

insert into users(username, password, enabled) values ('scott', 'wombat', true);
insert into authorities(username, authority) values ('scott', 'ROLE_USER');
