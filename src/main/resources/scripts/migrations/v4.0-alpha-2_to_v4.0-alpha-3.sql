DROP PROCEDURE IF EXISTS employees_new;

-- --------------------------------------------------------
-- employees_new( alle Felder außer id, string password )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employees_new(
	IN in_login varchar(255),
	IN in_last_name varchar(255),
	IN in_first_name varchar(255),
	IN in_project_leader boolean,
	IN in_daily_rate double,
	IN in_time_preference int(11),
	IN in_password varchar(255),
	IN in_dbname varchar(255),
	IN in_db_id varchar(4))
BEGIN
	DECLARE username varchar(255);
	DECLARE stmt varchar(255);

	SET username = CONCAT_WS('_', in_db_id, LEFT(in_login,11));
	SET @createUsr = CONCAT('CREATE USER \'',username,'\'@"','localhost','" IDENTIFIED BY "',in_password,'"');
	PREPARE createUsr FROM @createUsr;
	EXECUTE createUsr;
	DEALLOCATE PREPARE createUsr;

	SET @grantExec = CONCAT('GRANT EXECUTE ON ',in_dbname,'.* TO \'',username,'\'@"','localhost','"');
	SET @grantExec2 = CONCAT('GRANT EXECUTE ON ','id_wbs','.* TO \'',username,'\'@"','localhost','"');
	PREPARE grantExec FROM @grantExec;
	PREPARE grantExec2 FROM @grantExec2;
	EXECUTE grantExec;
	EXECUTE grantExec2;
	DEALLOCATE PREPARE grantExec;

	SET username = CONCAT_WS('_', in_db_id, LEFT(in_login,11));
	SET @createUsr = CONCAT('CREATE USER \'',username,'\'@"','%','" IDENTIFIED BY "',in_password,'"');
	PREPARE createUsr FROM @createUsr;
	EXECUTE createUsr;
	DEALLOCATE PREPARE createUsr;

	SET @grantExec = CONCAT('GRANT EXECUTE ON ',in_dbname,'.* TO \'',username,'\'@"','%','"');
	SET @grantExec2 = CONCAT('GRANT EXECUTE ON ','id_wbs','.* TO \'',username,'\'@"','%','"');
	PREPARE grantExec FROM @grantExec;
	PREPARE grantExec2 FROM @grantExec2;
	EXECUTE grantExec;
	EXECUTE grantExec2;
	DEALLOCATE PREPARE grantExec;

	INSERT
	INTO employees(
		login,
		last_name,
	    first_name,
	    project_leader,
	    daily_rate,
	    time_preference)
	VALUES (
		in_login,
	    in_last_name,
	    in_first_name,
	    in_project_leader,
	    in_daily_rate,
	    in_time_preference);
END //
DELIMITER ;
-- --------------------------------------------------------

DROP PROCEDURE IF EXISTS test_execution_select;

-- --------------------------------------------------------
-- test_execution_select()
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE test_execution_select()
 BEGIN
	 SELECT
		 t.id,
		 t.fid_tc,
		 t.fid_emp,
		 t.remark,
		 t.timestamp,
		 t.status,
		 e.login
	 FROM
		 test_executions t
		 JOIN
		 employees e
		 ON t.fid_emp = e.id
	 ORDER BY
		 t.timestamp
	 DESC;
 END //
DELIMITER ;
-- --------------------------------------------------------