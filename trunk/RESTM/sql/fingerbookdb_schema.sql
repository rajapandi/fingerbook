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

CREATE TABLE `pig_script` (
  `script_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `script_name` varchar(30) NOT NULL,
  `script_desc` varchar(255) DEFAULT NULL,
  `script_file_path` varchar(255) NOT NULL,
  `deleted` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`script_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


CREATE TABLE `pig_script_result` (
  `script_result_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `script_id` int(10) unsigned NOT NULL,
  `return_code` int(11) DEFAULT NULL,
  `return_message` varchar(255) DEFAULT NULL,
  `successful` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `error_code` int(11) DEFAULT NULL,
  `error_message` varchar(500) DEFAULT NULL,
  `duration` int(10) NOT NULL DEFAULT '0',
  `exec_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`script_result_id`),
  KEY `script_id` (`script_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- Filtros para la tabla `pig_script_result`
ALTER TABLE `pig_script_result`
  ADD CONSTRAINT `pig_script_result_ibfk_1` FOREIGN KEY (`script_id`) REFERENCES `pig_script` (`script_id`);

-- http://sha1-hash-online.waraxe.us/ to compute SHA-1 hash
-- scott - wombat
insert into users(username, password, enabled) values ('scott', '74baa3bc21a1c85ef7f3d7efe7d75d297e2bc57e', true);
-- anonymous - anonymous
insert into users(username, password, enabled) values ('anonymous', '0a92fab3230134cca6eadd9898325b9b2ae67998', true);
-- admin - admin
insert into users(username, password, enabled) values ('admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', true);

insert into authorities(username, authority) values ('scott', 'ROLE_USER');
insert into authorities(username, authority) values ('anonymous', 'ANONYMOUS');
insert into authorities(username, authority) values ('admin', 'ROLE_ADMIN');

INSERT INTO `pig_script` (`script_id`, `script_name`, `script_desc`, `script_file_path`, `deleted`) VALUES(1, 'script', 'Copy tfinger to file', '/Users/nahu/STS/workspace/PigExecutor/scripts/script.pig', NULL);
INSERT INTO `pig_script` (`script_id`, `script_name`, `script_desc`, `script_file_path`, `deleted`) VALUES(2, 'script2', 'Test: Unexistent', 'bin/script2.pig', NULL);
