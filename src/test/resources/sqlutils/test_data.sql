START TRANSACTION;

DROP TABLE IF EXISTS planned_value;
DROP TABLE IF EXISTS wp_allocation;
DROP TABLE IF EXISTS dependencies;
DROP TABLE IF EXISTS analyse_data;
DROP TABLE IF EXISTS conflicts;
DROP TABLE IF EXISTS semaphore;
DROP TABLE IF EXISTS work_effort;
DROP TABLE IF EXISTS workpackage;
DROP TABLE IF EXISTS employee_calendar;
DROP TABLE IF EXISTS holidays_calendar;
DROP TABLE IF EXISTS baseline;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS employees;

CREATE TABLE IF NOT EXISTS employees (
	id int(11) NOT NULL AUTO_INCREMENT,
	login varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	first_name varchar(255) DEFAULT NULL,
	project_leader boolean NOT NULL,
	daily_rate double NOT NULL,
	time_preference int(11) NOT NULL DEFAULT 0,
	PRIMARY KEY ( id ),
	UNIQUE ( login )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS project (
	id int(11) NOT NULL AUTO_INCREMENT,
	fid_pl int(11) NOT NULL,
	name varchar(255) NOT NULL,
	levels int(11) NOT NULL, 
	PRIMARY KEY ( id ),
	UNIQUE ( name ),
	FOREIGN KEY (fid_pl) REFERENCES employees(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS baseline (
	id int(11) NOT NULL AUTO_INCREMENT,
	fid_project int(11) NOT NULL,
	bl_date datetime NOT NULL, 
	description varchar(255) DEFAULT NULL,
	PRIMARY KEY ( id ),
	FOREIGN KEY (fid_project) REFERENCES project(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS holidays_calendar (
	id int(11) NOT NULL AUTO_INCREMENT,
	title varchar(255) NOT NULL,
	begin_time datetime NOT NULL,
	end_time datetime NOT NULL,
	availability boolean DEFAULT 0,
	full_time boolean DEFAULT 0,
	PRIMARY KEY ( id ),
	UNIQUE ( title )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS employee_calendar (
	id int(11) NOT NULL AUTO_INCREMENT,
	fid_emp int(11) NOT NULL,
	begin_time datetime NOT NULL,
	end_time datetime NOT NULL,
	description varchar(255) DEFAULT NULL,
	availability boolean DEFAULT 0,
	full_time boolean DEFAULT 0,
	PRIMARY KEY ( id ),
	FOREIGN KEY (fid_emp) REFERENCES employees(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS workpackage (
	id int(11) NOT NULL AUTO_INCREMENT,	
	string_id varchar(255) NOT NULL,
	fid_project int(11) NOT NULL,
	fid_resp_emp int(11) NOT NULL,
	fid_parent int(11) DEFAULT NULL,
	parent_order_id int(11) NOT NULL,
	name varchar(255) NOT NULL,
	description varchar(255) DEFAULT NULL,
	bac double NOT NULL,
	ac double NOT NULL, 
	ev double NOT NULL, 
	etc double NOT NULL, 
	eac double NOT NULL, 
	cpi double NOT NULL, 
	bac_costs double NOT NULL,
	ac_costs double NOT NULL,
	etc_costs double NOT NULL,
	wp_daily_rate double NOT NULL,
	release_date datetime DEFAULT NULL,
	is_toplevel_wp boolean DEFAULT 0,
	is_inactive boolean DEFAULT 0,
	start_date_calc datetime DEFAULT NULL,
	start_date_wish datetime DEFAULT NULL,
	end_date_calc datetime DEFAULT NULL,
	PRIMARY KEY ( id ),
	UNIQUE ( string_id, fid_project ),
	UNIQUE ( fid_parent, parent_order_id ),
	FOREIGN KEY ( fid_project ) REFERENCES project(id),
	FOREIGN KEY ( fid_resp_emp ) REFERENCES employees(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

ALTER TABLE workpackage ADD CONSTRAINT fkc_wp_parent
	CHECK (( fid_parent IS NULL ) OR ( fid_parent = ( SELECT id FROM workpackage AS w WHERE w.id = fid_parent )));

CREATE TABLE IF NOT EXISTS work_effort (
	id int(11) NOT NULL AUTO_INCREMENT,
	fid_wp int(11) NOT NULL,
	fid_emp int(11) NOT NULL,
	rec_date datetime NOT NULL,
	effort double NOT NULL, 
	description varchar(255) DEFAULT NULL,
	PRIMARY KEY ( id ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_emp ) REFERENCES employees( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS semaphore (
	tag varchar(255) NOT NULL,
	fid_emp int(11) NOT NULL,
	PRIMARY KEY ( tag ),
	FOREIGN KEY ( fid_emp ) REFERENCES employees( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS conflicts (
	id int(11) NOT NULL AUTO_INCREMENT,
	fid_wp int(11) NOT NULL,
	fid_wp_affected int(11) DEFAULT NULL,
	fid_emp int(11) NOT NULL,
	reason int(11) NOT NULL,
	occurence_date datetime NOT NULL,
	PRIMARY KEY ( id ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_wp_affected ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_emp ) REFERENCES employees( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS analyse_data (
	id int(11) NOT NULL AUTO_INCREMENT,
	fid_wp int(11) NOT NULL,
	fid_baseline int(11) NOT NULL,
	name varchar(255) NOT NULL,
	bac double NOT NULL,
	ac double NOT NULL,
	ev double NOT NULL,
	etc double NOT NULL,
	eac double NOT NULL,
	cpi double NOT NULL,
	bac_costs double NOT NULL,
	ac_costs double NOT NULL,
	etc_costs double NOT NULL,
	sv int(11) NOT NULL DEFAULT 0,
	spi int(11) NOT NULL DEFAULT 0,
	pv int(11) NOT NULL DEFAULT 0,
	PRIMARY KEY ( id ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_baseline ) REFERENCES baseline( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS dependencies (
	fid_wp_predecessor int(11) NOT NULL,
	fid_wp_successor int(11) NOT NULL,
	PRIMARY KEY ( fid_wp_predecessor, fid_wp_successor ),
	FOREIGN KEY ( fid_wp_predecessor ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_wp_successor ) REFERENCES workpackage( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS wp_allocation (
	fid_wp int(11) NOT NULL,
	fid_emp int(11) NOT NULL,
	PRIMARY KEY ( fid_wp, fid_emp ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_emp ) REFERENCES employees( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS planned_value (
	id int(11) NOT NULL AUTO_INCREMENT,
	fid_wp int(11) NOT NULL,
	pv_date datetime NOT NULL,
	pv int(11) NOT NULL,
	PRIMARY KEY ( id ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

INSERT INTO employees (login, last_name, first_name, project_leader, daily_rate, time_preference) VALUES
('Leiter', 'USE', 'DO NOT', 1, 100, 0),
('h.mueller', 'Müller', 'Hans', 1, 500, 0),
('p.pan', 'Pan', 'Peter', 0, 100, 0),
('b.schmidt', 'Schmidt', 'Bernd', 0, 300, 0);

INSERT INTO project (id, fid_pl, name, levels) VALUES
(1, 1, 'Mauerbau', 4);

INSERT INTO `baseline` (id, fid_project, bl_date, description) VALUES
(1, 1, '2013-12-23 00:00:00', 'Projekt Beginn');

INSERT INTO employee_calendar (id, fid_emp, begin_time, end_time, description, availability, full_time) VALUES
(1, 3, '2014-01-13 00:00:00', '2014-01-14 23:59:00', 'Muss in Berufsschule', 0, 1);

INSERT INTO workpackage (id, string_id, fid_project, fid_resp_emp, fid_parent, parent_order_id, name, description, bac, ac, ev, etc, eac, cpi, bac_costs, ac_costs, etc_costs, wp_daily_rate, release_date, is_toplevel_wp, is_inactive, start_date_calc, start_date_wish, end_date_calc) VALUES
(1, '0.0.0.0', 1, 2, NULL, 1, 'Mauerbau', 'Projekt', 7, 2.625, 750, 4.5, 1350, 0.982456140350877, 2100, 787.5, 1350, 300, NULL, 1, 0, '2014-01-01 08:01:00', '2014-01-01 00:01:00', '2014-01-03 10:01:00'),
(2, '1.0.0.0', 1, 2, 1, 1, 'Vorbereitung', 'Vorbereitende Arbeiten', 2, 2.125, 600, 0, 0, 0.941176470588235, 600, 637.5, 0, 0, NULL, 1, 0, '2014-01-01 08:01:00', '2014-01-01 00:01:00', '2014-01-01 12:01:00'),
(3, '1.1.0.0', 1, 2, 2, 1, 'Inspektion der Baustelle', 'Baustelle muss inspiziert und abgemessen werden', 1, 0.875, 300, 0, 0, 1.14285714285714, 300, 262.5, 0, 300, NULL, 0, 0, '2014-01-01 08:01:00', '2014-01-01 00:01:00', '2014-01-01 12:01:00'),
(4, '1.2.0.0', 1, 4, 2, 2, 'Entwurfszeichnung', 'Es müssen die Grundrisse der Mauer gezeichnet werden', 1, 1.25, 300, 0, 0, 0.8, 300, 375, 0, 300, NULL, 0, 0, '2014-01-01 14:01:00', '2014-01-01 00:01:00', '2014-01-01 12:01:00'),
(5, '2.0.0.0', 1, 2, 1, 2, 'Material Beschaffung', 'Baumaterialien müssen beschaffen werden', 1, 0.5, 150, 0.5, 150, 1, 300, 150, 150, 300, NULL, 0, 0, '2014-01-01 14:01:00', '2014-01-01 00:01:00', '2014-01-01 12:01:00'),
(6, '3.0.0.0', 1, 2, 1, 3, 'Bauen', 'Bauen', 4, 0, 0, 4, 1200, 1, 1200, 0, 1200, 300, NULL, 1, 0, '2014-01-01 14:01:00', '2014-01-01 00:01:00', '2014-01-03 10:01:00'),
(7, '3.1.0.0', 1, 2, 6, 1, 'Zement Mischen', 'Zement Mischen', 1, 0, 0, 1, 300, 1, 300, 0, 300, 300, NULL, 0, 0, '2014-01-01 14:01:00', '2014-01-01 00:01:00', '2014-01-03 10:01:00'),
(8, '3.2.0.0', 1, 2, 6, 2, 'Stein auf Stein', 'Die Mauer wird gebaut', 3, 0, 0, 3, 900, 1, 900, 0, 900, 300, NULL, 0, 0, '2014-01-01 14:01:00', '2014-01-01 00:01:00', '2014-01-02 18:01:00');

INSERT INTO work_effort (id, fid_wp, fid_emp, rec_date, effort, description) VALUES
(1, 3, 2, '2014-01-05 00:00:00', 0.4375, 'Baustelle Inspiziert'),
(2, 3, 3, '2014-01-05 00:00:00', 0.4375, 'Inspektion mit Chef'),
(3, 4, 4, '2014-01-06 00:00:00', 0.375, 'Zeichnungen erstellen'),
(4, 4, 4, '2014-01-07 00:00:00', 0.875, 'Zeichnungen fertig stellen'),
(5, 5, 2, '2014-01-10 00:00:00', 0.25, 'Material Beschaffung'),
(6, 5, 3, '2014-01-10 00:00:00', 0.25, 'Material Beschaffung');

INSERT INTO dependencies (fid_wp_predecessor, fid_wp_successor) VALUES
(2, 5),
(3, 4),
(5, 6);

INSERT INTO wp_allocation (fid_wp, fid_emp) VALUES
(1, 4),
(1, 2),
(1, 3),
(2, 4),
(2, 2),
(2, 3),
(3, 2),
(3, 3),
(4, 4),
(5, 2),
(5, 3),
(6, 4),
(6, 2),
(6, 3),
(7, 4),
(7, 2),
(7, 3),
(8, 4),
(8, 2),
(8, 3);

INSERT INTO planned_value (fid_wp, pv_date, pv ) VALUES
(7, '2014-01-05 00:00:00', 2),
(7, '2014-01-06 00:00:00', 3),
(8, '2014-01-07 00:00:00', 4),
(8, '2014-01-08 00:00:00', 5),
(8, '2014-01-09 00:00:00', 6);

COMMIT;
