SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

-- ---------------------------------------------------------------------
-- employees
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS employees (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique Identifier for an employee.',
	login varchar(255) NOT NULL COMMENT 'Unique name for employee, used to login.',
	last_name varchar(255) NOT NULL COMMENT 'The last name of the employee.',
	first_name varchar(255) DEFAULT NULL COMMENT 'The first name of the employee.',
	project_leader boolean NOT NULL COMMENT 'Information if the employee has project leader rights',
	daily_rate double NOT NULL COMMENT 'The daily wage of the employee, needed for earned value analysis.',
	time_preference int(11) NOT NULL DEFAULT 0 COMMENT 'Preference of the employee to use and display times in minutes, hours or days',
	PRIMARY KEY ( id ),
	UNIQUE ( login )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'This table manages the employees who work at the project.';

-- ---------------------------------------------------------------------
-- project
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS project (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique Identifier for a project.',
	fid_pl int(11) NOT NULL COMMENT 'Reference on the employee who is the project leader.',
	name varchar(255) NOT NULL COMMENT 'Name of the project.',
	levels int(11) NOT NULL COMMENT 'This value sets the maximum depth of the work breakdown structure.',
	PRIMARY KEY ( id ),
	UNIQUE ( name ),
	FOREIGN KEY (fid_pl) REFERENCES employees(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'This table manages the projects within the database.';

-- ---------------------------------------------------------------------
-- baseline
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS baseline (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique Identifier for the individual baselines.',
	fid_project int(11) NOT NULL COMMENT 'Reference on the project, to which the baseline belongs.',
	bl_date datetime NOT NULL COMMENT 'Date when Baseline was created.',
	description varchar(255) DEFAULT NULL COMMENT 'Description of the baseline.',
	PRIMARY KEY ( id ),
	FOREIGN KEY (fid_project) REFERENCES project(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'This table manages the baselines of the project.';

-- ---------------------------------------------------------------------
-- holidays_calendar
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS holidays_calendar (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique Identifier for row in the calendar.',
	title varchar(255) NOT NULL COMMENT 'Name of the holiday.',
	begin_time datetime NOT NULL COMMENT 'Begin of the holiday.',
	end_time datetime NOT NULL COMMENT 'End of the holiday.',
	availability boolean DEFAULT 0 COMMENT '',
	full_time boolean DEFAULT 0 COMMENT 'Information if holiday begin/end is in whole days or if the time has to be considered.',
	PRIMARY KEY ( id ),
	UNIQUE ( title )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'A table to track the dates of holidays.';

-- ---------------------------------------------------------------------
-- employee_calendar
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS employee_calendar (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique Identifier for row in the calendar.',
	fid_emp int(11) NOT NULL COMMENT 'Id of the referenced employee.',
	begin_time datetime NOT NULL COMMENT 'Begin of the event in the calendar entry.',
	end_time datetime NOT NULL COMMENT 'End of the event in the calendar entry.',
	description varchar(255) DEFAULT NULL COMMENT 'Description of the event.',
	availability boolean DEFAULT 0 COMMENT 'Availability of the employee during the stated time.',
	full_time boolean DEFAULT 0 COMMENT 'Information if the time has to be considered or only the date is relevant.',
	PRIMARY KEY ( id ),
	FOREIGN KEY (fid_emp) REFERENCES employees(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'A table to track the availability of employees.';

-- ---------------------------------------------------------------------
-- workpackage
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS workpackage (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID for a workpackage. Used to reference a wp within the database.',
	string_id varchar(255) NOT NULL COMMENT 'The complete hierachical ID of a workpackage. Unique within a project.',
	fid_project int(11) NOT NULL COMMENT 'The project, in which the workpackage exists.',
	fid_resp_emp int(11) NOT NULL COMMENT 'The employee who is responsible for this workpackage.',
	fid_parent int(11) DEFAULT NULL COMMENT 'The parent of the workpackage or NULL in case of the package at the top.',
	parent_order_id int(11) NOT NULL COMMENT 'The position of the workpackge within the childs of the parent. Used for the hierachical order.',
	name varchar(255) NOT NULL COMMENT 'Name of the workpackage.',
	description varchar(255) DEFAULT NULL COMMENT 'Description of the workpackage.',
	bac double NOT NULL COMMENT 'Value of the earned value analysis: budget at complete.',
	ac double NOT NULL COMMENT 'Value of the earned value analysis: actual costs.',
	ev double NOT NULL COMMENT 'Value of the earned value analysis: earned value.',
	etc double NOT NULL COMMENT 'Value of the earned value analysis: estimation to complete.',
	eac double NOT NULL COMMENT 'Value of the earned value analysis: estimation at complete.',
	cpi double NOT NULL COMMENT 'Value of the earned value analysis: cost performance index.',
	bac_costs double NOT NULL COMMENT '',
	ac_costs double NOT NULL COMMENT '',
	etc_costs double NOT NULL COMMENT '',
	wp_daily_rate double NOT NULL COMMENT '',
	release_date datetime DEFAULT NULL COMMENT '%%... Was renamed from release, because release is a keyword in SQL.',
	is_toplevel_wp boolean DEFAULT 0 COMMENT 'Information if the wp has child workpackages and cannot have efforts.',
	is_inactive boolean DEFAULT 0 COMMENT 'Information if the wp is inactive.',
	start_date_calc datetime DEFAULT NULL COMMENT 'Expected/Calculated start date for this workpackage.',
	start_date_wish datetime DEFAULT NULL COMMENT '',
	end_date_calc datetime DEFAULT NULL COMMENT 'Expected/Calculated end date for this workpackage.',
	PRIMARY KEY ( id ),
	UNIQUE ( string_id, fid_project ),
	UNIQUE ( fid_parent, parent_order_id ),
	FOREIGN KEY ( fid_project ) REFERENCES project(id),
	FOREIGN KEY ( fid_resp_emp ) REFERENCES employees(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'Table for the workpackages.';
ALTER TABLE workpackage ADD CONSTRAINT fkc_wp_parent
	CHECK (( fid_parent IS NULL ) OR ( fid_parent = ( SELECT id FROM workpackage AS w WHERE w.id = fid_parent )));

-- ---------------------------------------------------------------------
-- work_effort
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS work_effort (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique Identifier for a row. Only exists because table must have a primary and unique key.',
	fid_wp int(11) NOT NULL COMMENT 'ID of the referenced workpackage for which the work was done.',
	fid_emp int(11) NOT NULL COMMENT 'ID of the referenced employee who did the work.',
	rec_date datetime NOT NULL COMMENT 'Date when the work was recorded in the system.',
	effort double NOT NULL COMMENT 'Amount of effort done. Saved in days. One day is equal to 8 hours.',
	description varchar(255) DEFAULT NULL COMMENT 'Description of the effort.',
	PRIMARY KEY ( id ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_emp ) REFERENCES employees( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'Table to track the efforts for workpackages.';

-- ---------------------------------------------------------------------
-- semaphore
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS semaphore (
	tag varchar(255) NOT NULL COMMENT 'A Unique Tag for a semaphore.',
	fid_emp int(11) NOT NULL COMMENT 'The Employee who blocks the semaphore.',
	PRIMARY KEY ( tag ),
	FOREIGN KEY ( fid_emp ) REFERENCES employees( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8
COMMENT 'A table for semaphores. If a tag is not in there or the fid_emp is NULL, the correspondent semaphore is free.';

-- ---------------------------------------------------------------------
-- conflicts
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS conflicts (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for a conflict.',
	fid_wp int(11) DEFAULT NULL COMMENT 'First workpackage involved in the conflict.',
	fid_wp_affected int(11) DEFAULT NULL COMMENT 'Second workpackage involved in the conflict. Not mandatory.',
	fid_emp int(11) NOT NULL COMMENT 'Employee that caused the conflict.',
	reason int(11) NOT NULL COMMENT 'Reason for the conflict.',
	occurence_date datetime NOT NULL COMMENT 'Date when the conflict first appeared.',
	PRIMARY KEY ( id ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_wp_affected ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_emp ) REFERENCES employees( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'This Table manages conflicts among the workpackages.';

-- ---------------------------------------------------------------------
-- analyse_data
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS analyse_data (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier for analyse_data',
	fid_wp int(11) NOT NULL COMMENT 'Workpackage from which the data comes.',
	fid_baseline int(11) NOT NULL COMMENT 'Baseline for which the data is analysed.',
	name varchar(255) NOT NULL COMMENT 'Name of the Workpackage.',
	bac double NOT NULL COMMENT 'BAC of the Workpackage.',
	ac double NOT NULL COMMENT 'AC of the Workpackage.',
	ev double NOT NULL COMMENT 'EV of the Workpackage.',
	etc double NOT NULL COMMENT 'ETC of the Workpackage.',
	eac double NOT NULL COMMENT 'EAC of the Workpackage.',
	cpi double NOT NULL COMMENT 'CPI of the Workpackage.',
	bac_costs double NOT NULL COMMENT 'BAC costs of the Workpackage.',
	ac_costs double NOT NULL COMMENT 'AC costs of the Workpackage.',
	etc_costs double NOT NULL COMMENT 'ETC costs of the Workpackage.',
	sv double NOT NULL DEFAULT 0 COMMENT 'Value of the earned value analysis: Schedule Variance',
	spi double NOT NULL DEFAULT 0 COMMENT 'Value of the earned value analysis: Schedule Performance Index',
	pv double NOT NULL DEFAULT 0 COMMENT 'Value of the earned value analysis: Planned Value',
	PRIMARY KEY ( id ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_baseline ) REFERENCES baseline( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'This table is used to manage the data for analysis of the project performance.';

-- ---------------------------------------------------------------------
-- dependencies
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS dependencies (
	fid_wp_predecessor int(11) NOT NULL COMMENT 'Workpackage which must be done before another workpackage.',
	fid_wp_successor int(11) NOT NULL COMMENT 'Workpackage which has a workpackage that must be done before it.',
	PRIMARY KEY ( fid_wp_predecessor, fid_wp_successor ),
	FOREIGN KEY ( fid_wp_predecessor ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_wp_successor ) REFERENCES workpackage( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8
COMMENT 'This table manages dependencies between workpackages.';

-- ---------------------------------------------------------------------
-- wp_allocation
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS wp_allocation (
	fid_wp int(11) NOT NULL COMMENT 'Id of a workpackage.',
	fid_emp int(11) NOT NULL COMMENT 'Employee who is allocated to the workpackage.',
	PRIMARY KEY ( fid_wp, fid_emp ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id ),
	FOREIGN KEY ( fid_emp ) REFERENCES employees( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8
COMMENT 'This table manages which employees are allocated to which workpackages.';

-- ---------------------------------------------------------------------
-- planned_value
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS planned_value (
	id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique Identifier for planned values.',
	fid_wp int(11) NOT NULL COMMENT 'Id of the workpackage for which the planned value is.',
	pv_date datetime NOT NULL COMMENT 'Date when the planned value should be reached.',
	pv double NOT NULL COMMENT 'The planned Value in work days.',
	PRIMARY KEY ( id ),
	FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'This table manages planned values for the workpackages.';

-- ---------------------------------------------------------------------
COMMIT;