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

-- http://sha1-hash-online.waraxe.us/ to compute SHA-1 hash
-- scott - wombat
insert into users(username, password, enabled) values ('scott', '74baa3bc21a1c85ef7f3d7efe7d75d297e2bc57e', true);
-- anonymous - anonymous
insert into users(username, password, enabled) values ('anonymous', '0a92fab3230134cca6eadd9898325b9b2ae67998', true);

insert into authorities(username, authority) values ('scott', 'ROLE_USER');
insert into authorities(username, authority) values ('anonymous', 'ANONYMOUS');
