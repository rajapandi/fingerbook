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
