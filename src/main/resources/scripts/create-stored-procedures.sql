 -- --------------------------------------------------------
-- dependencies_select
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE dependencies_select()
BEGIN
	SELECT *
	FROM dependencies;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- dependencies_delete_by_key
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE dependencies_delete_by_key(
	IN in_pre int(11),
	IN in_suc int(11))
BEGIN
	DELETE
	FROM dependencies
	WHERE
		fid_wp_predecessor = in_pre
		AND
		fid_wp_successor = in_suc;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- dependencies_new
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE dependencies_new(
	IN in_pre int(11),
	IN in_suc int(11))
BEGIN
	INSERT INTO dependencies
	VALUES ( in_pre, in_suc );
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- analyse_data_select_by()
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE analyse_data_select_by(
   IN in_fid_id int(11),
   IN in_use_fid_baseline boolean)
BEGIN
   IF in_use_fid_baseline IS NULL OR in_use_fid_baseline = false
   THEN
      SELECT *
	  FROM analyse_data
	  WHERE fid_wp = in_fid_id;
   ELSE
	  SELECT *
	  FROM analyse_data
	  WHERE fid_baseline = in_fid_id;
   END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- analyse_data_new
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE analyse_data_new(
	IN in_fid_wp int(11),
	IN in_fid_baseline int(11),
	IN in_name varchar(255),
	IN in_bac double,
	IN in_ac double,
	IN in_ev double,
	IN in_etc double,
	IN in_eac double,
	IN in_cpi double,
	IN in_bac_costs double,
	IN in_ac_costs double,
	IN in_etc_costs double,
	IN in_sv double,
	IN in_spi double,
	IN in_pv double)
BEGIN
	INSERT INTO	analyse_data (
		fid_wp,
		fid_baseline,
		name,
		bac,
		ac,
		ev,
		etc,
		eac,
		cpi,
		bac_costs,
		ac_costs,
		etc_costs,
		sv,
		spi,
		pv )
	VALUES (
		in_fid_wp,
		in_fid_baseline,
		in_name,
		in_bac,
		in_ac,
		in_ev,
		in_etc,
		in_eac,
		in_cpi,
		in_bac_costs,
		in_ac_costs,
		in_etc_costs,
		in_sv,
		in_spi,
		in_pv );
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- workpackage_select
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE workpackage_select(
   IN in_no_toplevel boolean)
BEGIN
   IF in_no_toplevel IS NULL OR in_no_toplevel = false
   THEN
      SELECT *
	  FROM workpackage;
   ELSE
	  SELECT *
	  FROM workpackage
	  WHERE is_toplevel_wp = false;
   END IF;
END //
DELIMITER ;

-- --------------------------------------------------------

-- --------------------------------------------------------
-- workpackage_select_by_id
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE workpackage_select_by_id(
	IN in_string_id varchar(255),
	IN in_project_id int(11),
	IN in_id int(11))
BEGIN
	IF in_id IS NULL
	THEN
		SELECT *
		FROM workpackage
		WHERE
			string_id = in_string_id
			AND
			fid_project = in_project_id;
	ELSE
		SELECT *
		FROM workpackage
		WHERE
			id = in_id
			AND
			fid_project = in_project_id;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- workpackage_select_by_date
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE workpackage_select_by_date(
	IN in_date_from datetime,
	IN in_date_to datetime )
BEGIN
	SELECT *
	FROM workpackage
	WHERE
		start_date_calc BETWEEN in_date_from AND in_date_to
		OR
		end_date_calc BETWEEN in_date_from AND in_date_to;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- workpackage_new
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE workpackage_new(
	IN in_string_id varchar(255),
	IN in_fid_project int(11),
	IN in_fid_resp_emp int(11),
	IN in_fid_parent int(11),
	IN in_name varchar(255),
	IN in_description varchar(255),
	IN in_bac double,
	IN in_ac double,
	IN in_ev double,
	IN in_etc double,
	IN in_eac double,
	IN in_cpi double,
	IN in_bac_costs double,
	IN in_ac_costs double,
	IN in_etc_costs double,
	IN in_wp_daily_rate double,
	IN in_release_date datetime,
	IN in_is_toplevel_wp boolean,
	IN in_is_inactive boolean,
	IN in_start_date_calc datetime,
	IN in_start_date_wish datetime,
	IN in_end_date_calc datetime)
BEGIN
	DECLARE parent_order int(11);
    SELECT MAX(parent_order_id) INTO parent_order
    FROM workpackage w
    WHERE w.fid_parent = in_fid_parent;
	IF parent_order IS NULL
	THEN
		SET parent_order = 1;
	ELSE
		SET parent_order = parent_order + 1;
	END IF;

	INSERT INTO workpackage(
		string_id,
		fid_project,
		fid_resp_emp,
		fid_parent,
		parent_order_id,
		name,
		description,
		bac,
		ac,
		ev,
		etc,
		eac,
		cpi,
		bac_costs,
		ac_costs,
		etc_costs,
		wp_daily_rate,
		release_date,
		is_toplevel_wp,
		is_inactive,
		start_date_calc,
		start_date_wish,
		end_date_calc )
	VALUES (
		in_string_id,
		in_fid_project,
		in_fid_resp_emp,
		in_fid_parent,
		parent_order,
		in_name,
		in_description,
		in_bac,
		in_ac,
		in_ev,
		in_etc,
		in_eac,
		in_cpi,
		in_bac_costs,
		in_ac_costs,
		in_etc_costs,
		in_wp_daily_rate,
		in_release_date,
		in_is_toplevel_wp,
		in_is_inactive,
		in_start_date_calc,
		in_start_date_wish,
		in_end_date_calc);
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- workpackage_update_by_id
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE workpackage_update_by_id(
	IN in_string_id varchar(255),
	IN in_project_id int(11),
	IN in_fid_resp_emp int(11),
	IN in_name varchar(255),
	IN in_description varchar(255),
	IN in_bac double,
	IN in_ac double,
	IN in_ev double,
	IN in_etc double,
	IN in_eac double,
	IN in_cpi double,
	IN in_bac_costs double,
	IN in_ac_costs double,
	IN in_etc_costs double,
	IN in_wp_daily_rate double,
	IN in_release_date datetime,
	IN in_is_toplevel_wp boolean,
	IN in_is_inactive boolean,
	IN in_start_date_calc datetime,
	IN in_start_date_wish datetime,
	IN in_end_date_calc datetime)
BEGIN
	DECLARE chk int(11);
	SELECT COUNT(*) INTO chk
	FROM workpackage
	WHERE
		string_id = in_string_id
		AND
		fid_project = in_project_id;
	IF chk = 1
	THEN

		UPDATE workpackage
		SET
			fid_resp_emp = in_fid_resp_emp,
			name = in_name,
			description = in_description,
			bac = in_bac,
			ac = in_ac,
			ev = in_ev,
			etc = in_etc,
			eac = in_eac,
			cpi = in_cpi,
			bac_costs = in_bac_costs,
			ac_costs = in_ac_costs,
			etc_costs = in_etc_costs,
			wp_daily_rate = in_wp_daily_rate,
			release_date = in_release_date,
			is_toplevel_wp = in_is_toplevel_wp,
			is_inactive = in_is_inactive,
			start_date_calc = in_start_date_calc,
			start_date_wish = in_start_date_wish,
			end_date_calc = in_end_date_calc
		WHERE
			string_id = in_string_id
			AND
			fid_project = in_project_id;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- workpackage_delete_by_id( � )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE workpackage_delete_by_id(
	IN in_string_id varchar(255),
	IN in_project_id int(11),
	IN in_id int(11))
BEGIN
	IF in_id IS NULL
	THEN
		DELETE
		FROM workpackage
		WHERE
			string_id = in_string_id
			AND
			fid_project = in_project_id;
	ELSE
		DELETE
		FROM workpackage
		WHERE
			id = in_id
			AND
			fid_project = in_project_id;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- work_effort_select( opt int wp )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE work_effort_select(
	IN in_wp_id int(11))
BEGIN
	IF in_wp_id IS NULL
	THEN
		SELECT *
		FROM work_effort;
	ELSE
		SELECT *
		FROM work_effort
		WHERE fid_wp = in_wp_id;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- work_effort_select_sum( int wp )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE work_effort_select_sum(
	IN in_wp_id int(11))
BEGIN
	SELECT SUM(effort) as "sum"
	FROM work_effort
	WHERE fid_wp = in_wp_id;
END //
DELIMITER ;
-- --------------------------------------------------------


-- --------------------------------------------------------
-- work_effort_new( alle Felder au�er id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE work_effort_new(
	IN in_fid_wp int(11),
	IN in_fid_emp int(11),
	IN in_rec_date datetime,
	IN in_effort double,
	IN in_description varchar(255))
BEGIN
	INSERT
	INTO work_effort(
		fid_wp,
		fid_emp,
		rec_date,
		effort,
		description)
	VALUES (
		in_fid_wp,
		in_fid_emp,
		in_rec_date,
		in_effort,
		in_description);
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- baseline_select( int id )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE baseline_select(
	IN in_id int(11)
)
BEGIN
	IF in_id IS NULL
	THEN
		SELECT *
		FROM baseline;
	ELSE
		SELECT *
		FROM baseline
		WHERE id = in_id;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- baseline_new( alle Felder au�er id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE baseline_new(
	IN in_fid_project int(11),
    IN in_bl_date datetime,
    IN in_description varchar(255))
BEGIN
	INSERT
	INTO baseline(
		fid_project,
	    bl_date,
	    description)
	VALUES (
		in_fid_project,
	    in_bl_date,
	    in_description);
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- holidays_calendar_select( opt int id )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE holidays_calendar_select(
	IN in_id int(11))
BEGIN
	IF in_id IS NULL
	THEN
		SELECT *
		FROM holidays_calendar;
	ELSE
		SELECT *
		FROM holidays_calendar
		WHERE id = in_id;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- holidays_calendar_select_by_date( datetime from, datetime to, opt boolean mode2 )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE holidays_calendar_select_by_date(
	IN in_from datetime,
	IN in_to datetime,
	IN in_alt boolean )
BEGIN
	IF in_alt IS NULL OR in_alt = false
	THEN
		SELECT *
		FROM holidays_calendar
		WHERE
			begin_time BETWEEN in_from AND in_to
			OR
			end_time BETWEEN in_from AND in_to;
	ELSE
		SELECT *
		FROM holidays_calendar
		WHERE
			( begin_time < in_from AND end_time > in_to )
			OR
			((begin_time BETWEEN in_from AND in_to)
			OR
			(end_time BETWEEN in_from AND in_to));
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- holidays_calendar_new( alle Felder au�er id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE holidays_calendar_new(
	IN in_title varchar(255),
	IN in_begin_time datetime,
	IN in_end_time datetime,
	IN in_availability boolean,
	IN in_full_time boolean)
BEGIN
	INSERT
	INTO holidays_calendar(
		title,
	    begin_time,
	    end_time,
	    availability,
	    full_time
	)
	VALUES (
		in_title,
	    in_begin_time,
	    in_end_time,
	    in_availability,
	    in_full_time
	);
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- holidays_calendar_update_by_id( int id, alle Felder au�er id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE holidays_calendar_update_by_id(
	IN in_id int(11),
	IN in_title varchar(255),
	IN in_begin_time datetime,
	IN in_end_time datetime,
	IN in_availability boolean,
    IN in_full_time boolean)
BEGIN
	UPDATE holidays_calendar
	SET
		title = in_title,
	    begin_time = in_begin_time,
	    end_time = in_end_time,
	    availability = in_availability,
	    full_time = in_full_time
	WHERE id = in_id;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- holidays_calendar_delete_by_id( int id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE holidays_calendar_delete_by_id(
	IN in_id int(11))
BEGIN
	DELETE
	FROM holidays_calendar
	WHERE id = in_id;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- conflicts_select()
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE conflicts_select()
BEGIN
	SELECT *
	FROM conflicts;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- conflicts_new( alle Felder au�er id )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE conflicts_new(
	IN in_fid_wp int(11),
	IN in_fid_wp_affected int(11),
	IN in_fid_emp int(11),
	IN in_reason int(11),
	IN in_occurence_date datetime)
BEGIN
	INSERT
	INTO conflicts(
		fid_wp,
	    fid_wp_affected,
	    fid_emp,
	    reason,
	    occurence_date)
	VALUES (
		in_fid_wp,
	    in_fid_wp_affected,
	    in_fid_emp,
	    in_reason,
	    in_occurence_date);
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- conflicts_delete()
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE conflicts_delete()
BEGIN
	DELETE
	FROM conflicts;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- conflicts_delete_by_id( int id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE conflicts_delete_by_id(
	IN in_id int(11))
BEGIN
	DELETE
	FROM conflicts
	WHERE id = in_id;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- semaphore_p( string tag, int user_id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE semaphore_p(
	IN in_tag varchar(255),
	IN in_emp_id int(11))
BEGIN
	INSERT
	INTO semaphore (
		tag,
		fid_emp )
	VALUES (
		in_tag,
		in_emp_id);
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- semaphore_v( string tag, int user_id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE semaphore_v(
	IN in_tag varchar(255),
	IN in_emp_id int(11))
BEGIN
	DELETE
	FROM semaphore
	WHERE
		tag = in_tag
		AND
		fid_emp = in_emp_id;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- semaphore_select( string tag )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE semaphore_select(
	IN in_tag varchar(255))
BEGIN
	SELECT *
	FROM semaphore
	WHERE tag = in_tag;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employee_calendar_select( opt int id, opt boolean is_emp_id )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employee_calendar_select(
	IN in_id int(11),
	IN in_is_emp_id int(11))
BEGIN
	IF in_id IS NULL
	THEN
		SELECT *
		FROM employee_calendar;
	ELSE
		IF in_is_emp_id IS NULL OR in_is_emp_id = false
		THEN
			SELECT *
			FROM employee_calendar
			WHERE id = in_id;
		ELSE
			SELECT *
			FROM employee_calendar
			WHERE fid_emp = in_id;
		END IF;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employee_calendar_select_by_date( datetime from, datetime to )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employee_calendar_select_by_date(
	IN in_from datetime,
	IN in_to datetime )
BEGIN
	SELECT *
	FROM employee_calendar
	WHERE
		begin_time BETWEEN in_from AND in_to
		OR
		end_time BETWEEN in_from AND in_to;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employee_calendar_select_by_date_and_emp( datetime from, datetime to, int fid_emp )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employee_calendar_select_by_date_and_emp(
	IN in_from datetime,
	IN in_to datetime,
	IN in_fid_emp int(11) )
BEGIN
	SELECT *
	FROM(
		SELECT *
		FROM employee_calendar
		WHERE fid_emp = in_fid_emp ) as emp_cal
	WHERE
		( begin_time < in_from AND end_time > in_to )
		OR
		begin_time BETWEEN in_from AND in_to
		OR
		end_time BETWEEN in_from AND in_to;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employee_calendar_new( alle Felder au�er id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employee_calendar_new(
	IN in_fid_emp int(11),
    IN in_begin_time datetime,
    IN in_end_time datetime,
    IN in_description varchar(255),
    IN in_availability boolean,
    IN in_full_time boolean)
BEGIN
	INSERT
	INTO employee_calendar(
		fid_emp,
		begin_time,
		end_time,
		description,
		availability,
		full_time )
	VALUES (
		in_fid_emp,
	    in_begin_time,
	    in_end_time,
	    in_description,
	    in_availability,
	    in_full_time );
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employee_calendar_update_by_id( int id, alle Felder au�er id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employee_calendar_update_by_id(
	IN in_id int(11),
	IN in_fid_emp int(11),
    IN in_begin_time datetime,
    IN in_end_time datetime,
    IN in_description varchar(255),
    IN in_availability boolean,
    IN in_full_time boolean)
BEGIN
	UPDATE employee_calendar
	SET
		fid_emp = in_fid_emp,
	    begin_time = in_begin_time,
	    end_time = in_end_time,
		description = in_description,
	    availability = in_availability,
	    full_time = in_full_time
	WHERE id = in_id;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employee_calendar_delete_by_id( int id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employee_calendar_delete_by_id(
	IN in_id int(11))
BEGIN
	DELETE
	FROM employee_calendar
	WHERE id = in_id;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employees_select( opt boolean notLeiter )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employees_select(
	IN in_not_pl boolean)
BEGIN
	IF in_not_pl IS NULL OR in_not_pl = false 
	THEN
		SELECT *
		FROM employees;
	ELSE
		SELECT *
		FROM employees
		WHERE project_leader = false;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employees_select_by_login( string login )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employees_select_by_login(
	IN in_login varchar(255))
BEGIN
	SELECT *
	FROM employees
	WHERE login = in_login;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employees_select_by_id( int id )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employees_select_by_id(
	IN in_id int(11))
BEGIN
	SELECT *
	FROM employees
	WHERE id = in_id;
END //
DELIMITER ;
-- --------------------------------------------------------

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
	PREPARE grantExec FROM @grantExec;
	EXECUTE grantExec;
	DEALLOCATE PREPARE grantExec;
	
	SET username = CONCAT_WS('_', in_db_id, LEFT(in_login,11));	
	SET @createUsr = CONCAT('CREATE USER \'',username,'\'@"','%','" IDENTIFIED BY "',in_password,'"');
	PREPARE createUsr FROM @createUsr;
	EXECUTE createUsr;
	DEALLOCATE PREPARE createUsr;

	SET @grantExec = CONCAT('GRANT EXECUTE ON ',in_dbname,'.* TO \'',username,'\'@"','%','"');
	PREPARE grantExec FROM @grantExec;
	EXECUTE grantExec;
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

-- --------------------------------------------------------
-- employees_update_password_by_id( int id, string password )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employees_update_password_by_id(
	IN in_id int(11),
	IN in_password varchar(255),
	IN in_db_id varchar(4)) 
BEGIN
	DECLARE username varchar(255);
	SELECT login INTO username
	FROM employees
	WHERE id = in_id;
	SET username = CONCAT_WS('_', in_db_id, LEFT(username,11));	
	SET @changePw = CONCAT('SET PASSWORD FOR \'',username,'\'@"','localhost','" = PASSWORD("',in_password,'")');
	PREPARE changePw FROM @changePw;
	EXECUTE changePw;
	DEALLOCATE PREPARE changePw;
	
	SET @changePw = CONCAT('SET PASSWORD FOR \'',username,'\'@"','%','" = PASSWORD("',in_password,'")');
	PREPARE changePw FROM @changePw;
	EXECUTE changePw;
	DEALLOCATE PREPARE changePw;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employees_update_by_id( alle Felder außer id und login, opt string password )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employees_update_by_id(
	IN in_id int(11),
	IN in_last_name varchar(255),
	IN in_first_name varchar(255),
	IN in_project_leader boolean,
	IN in_daily_rate double,
	IN in_time_preference int(11),
	IN in_password varchar(255),
	IN in_db_id varchar(4)) 
BEGIN
	UPDATE employees
	SET
		last_name = in_last_name,
		first_name = in_first_name,
	    project_leader = in_project_leader,
	    daily_rate = in_daily_rate,
	    time_preference = in_time_preference
	WHERE id = in_id;
	
	IF in_password IS NOT NULL
	THEN
		CALL employees_update_password_by_id(in_id, in_password, in_db_id);
	END IF;		
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- employees_delete_by_id( int id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE employees_delete_by_id(
	IN in_id int(11),
	IN in_db_id varchar(4))
BEGIN
	DECLARE user_login varchar(255);
	DECLARE username varchar(255);
	SELECT login INTO user_login
	FROM employees
	WHERE id = in_id;

	SET username = CONCAT_WS('_', in_db_id, LEFT(user_login,11));	
	SET @dropUser = CONCAT('DROP USER ',username,'@"','%', '"' );
	PREPARE dropUser FROM @dropUser;
	EXECUTE dropUser;
	DEALLOCATE PREPARE dropUser;
	
	SET @dropUser = CONCAT('DROP USER ',username,'@"','localhost', '"' );
	PREPARE dropUser FROM @dropUser;
	EXECUTE dropUser;
	DEALLOCATE PREPARE dropUser;

	DELETE
	FROM employees
	WHERE id = in_id;	
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- wp_allocation_select( int id )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE wp_allocation_select(
	IN in_fid_wp int(11))
BEGIN
	IF in_fid_wp IS NULL
	THEN
		SELECT *
		FROM wp_allocation;
	ELSE
		SELECT *
		FROM wp_allocation
		WHERE fid_wp = in_fid_wp;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- wp_allocation_select_emp_by_id( int id )
-- r
-- --------------------------------------------------------
-- %% deleted, with structure in java wp_allocation_select can be used
-- --------------------------------------------------------

-- --------------------------------------------------------
-- wp_allocation_new( alle Felder )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE wp_allocation_new(
	IN in_fid_wp int(11),
	IN in_fid_emp int(11))
BEGIN
	INSERT
	INTO wp_allocation(
		fid_wp,
		fid_emp )
	VALUES (
		in_fid_wp,
		in_fid_emp );
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- wp_allocation_delete_by_key( int emp, int wp )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE wp_allocation_delete_by_key(
	IN in_emp int(11),
	IN in_wp int(11))
BEGIN
	DELETE
	FROM wp_allocation
	WHERE
		fid_emp = in_emp
		AND
		fid_wp = in_wp;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- wp_alloc_workpackage_select( fid_emp, opt boolean by_date, opt datetime from, opt datetime to )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE wp_alloc_workpackage_select(
	IN in_fid_emp int(11),
	IN in_by_date boolean,
	IN in_from datetime,
	IN in_to datetime)
BEGIN
	IF in_by_date IS NULL OR in_by_date = false
	THEN
		SELECT *
		FROM wp_alloc_workpackage
		WHERE fid_emp = in_fid_emp;
	ELSE
		SELECT *
		FROM wp_alloc_workpackage
		WHERE
			fid_emp = in_fid_emp
			AND
			end_date_calc >= in_from
			AND
			start_date_calc <= in_to;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- planned_value_select_by_date( datetime from, datetime to, opt int wp )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE planned_value_select_by_date(
	IN in_from datetime,
	IN in_to datetime,
	IN in_wp int(11))
BEGIN
	IF in_wp IS NULL
	THEN
		SELECT *
		FROM planned_value
		WHERE
			pv_date BETWEEN in_from AND in_to
		ORDER BY pv_date DESC;
	ELSE
		SELECT *
		FROM planned_value
		WHERE
			pv_date BETWEEN in_from AND in_to
			AND
			fid_wp = in_wp
		ORDER BY pv_date DESC;
	END IF;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- planned_value_select_by_wp_and_date( datetime d, int wp, opt boolean order_desc )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE planned_value_select_by_wp_and_date(
	IN in_date datetime,
	IN in_wp int(11),
	IN in_desc boolean)
BEGIN
	IF in_desc IS NULL OR in_desc = false
	THEN
		SELECT pv
		FROM planned_value
		WHERE
			pv_date = in_date
			AND
			fid_wp = in_wp;
	ELSE
		SELECT pv
		FROM planned_value
		WHERE
			pv_date < in_date
			AND
			fid_wp = in_wp
		ORDER BY pv_date DESC;
	END IF;
END //
DELIMITER ;
-- ------------------------------------------------------

-- --------------------------------------------------------
-- planned_value_new( alle Felder au�er id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE planned_value_new(
	IN in_fid_wp int(11),
    IN in_pv_date datetime,
    IN in_pv double)
BEGIN
	INSERT
	INTO planned_value (
		fid_wp,
	    pv_date,
	    pv)
	VALUE (
		in_fid_wp,
		in_pv_date,
		in_pv);
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- planned_value_update_by_wp_and_date( datetime d, int wp, int pv )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE planned_value_update_by_wp_and_date(
	IN in_fid_wp int(11),
    IN in_pv_date datetime,
    IN in_pv double)
BEGIN
	UPDATE planned_value
	SET pv = in_pv
	WHERE
		fid_wp = in_fid_wp
		AND
		pv_date = in_pv_date;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- planned_value_delete()
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE planned_value_delete()
BEGIN
	DELETE
	FROM planned_value;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- planned_value_delete_by_wp( int wp, opt datetime d )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE planned_value_delete_by_wp(
	IN in_wp int(11),
	IN in_date datetime)
BEGIN
	IF in_date IS NULL
	THEN
		DELETE
		FROM planned_value
		WHERE fid_wp = in_wp;
	ELSE
		DELETE
		FROM planned_value
		WHERE
			fid_wp = in_wp
			AND
			pv_date = in_date;
	END IF;

END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- project_select()
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE project_select()
BEGIN
	SELECT *
	FROM project;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- project_new( alle Felder au�er id )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE project_new(
	IN in_fid_pl int(11),
    IN in_name varchar(255),
    IN in_levels int(11))
BEGIN
	INSERT
	INTO project(
		fid_pl,
	    name,
		levels)
	VALUES (
		in_fid_pl,
	    in_name,
	    in_levels);
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- test_case_select_by_wp(int wp_id)
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE test_case_select_by_wp(
IN in_fid_wp int(11)
 )
BEGIN
	SELECT
		t.id,
		t.fid_wp,
		t.name,
		t.description,
		t.precondition,
		t.expected_result,
		w.string_id
	FROM
		test_cases t
	JOIN
		workpackage w
	WHERE
		t.fid_wp = in_fid_wp
		AND
		t.fid_wp = w.id;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- test_case_new(everything except id)
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE test_case_new(
 IN in_fid_wp int(11),
 IN in_name varchar(255)
)
 BEGIN
	 INSERT INTO test_cases (
		 fid_wp,
		 name,
		 description,
		 precondition,
		 expected_result)
	 VALUES (
		 in_fid_wp,
		 in_name,
		 "",
		 "",
		 ""
	 );
 END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- test_case_update_by_id(everything except id)
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE test_case_update_by_id(
 IN in_id int(11),
 IN in_fid_wp int(11),
 IN in_name varchar(255),
 IN in_description varchar(255),
 IN in_precondition varchar(255),
 IN in_expected_result varchar(255)
)
 BEGIN
	 UPDATE test_cases
	 SET
		 fid_wp = in_fid_wp,
		 name = in_name,
		 description = in_description,
		 precondition = in_precondition,
		 expected_result = in_expected_result
	 WHERE id = in_id;
 END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- test_execution_new(everything except id)
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE test_execution_new(
 IN in_fid_tc int(11),
 IN in_fid_emp int(11),
 IN in_remark varchar(255),
 IN in_timestamp timestamp,
 IN in_status varchar(255)
)
 BEGIN
	 INSERT INTO test_executions (
		 fid_tc,
		 fid_emp,
		 remark,
		 timestamp,
		 status
	 )
	 VALUES (
		 in_fid_tc,
		 in_fid_emp,
		 in_remark,
		 in_timestamp,
		 in_status
	 );
 END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- test_execution_update_by_id(everything except id)
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE test_execution_update_by_id(
 IN in_id int(11),
 IN in_fid_tc int(11),
 IN in_fid_emp int(11),
 IN in_remark varchar(255),
 IN in_timestamp date,
 IN in_status varchar(255)
)
 BEGIN
	 UPDATE test_executions
	 SET
		 fid_tc = in_fid_tc,
		 fid_emp = in_fid_emp,
		 remark = in_remark,
		 timestamp = in_timestamp,
		 status = in_status
	 WHERE id = in_id;
 END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- test_execution_select_by_test_case
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE test_execution_select_by_test_case(
 IN in_fid_tc int(11)
)
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
	WHERE
		t.fid_tc = in_fid_tc
		AND
		t.fid_emp = e.id
	ORDER BY
		t.timestamp
	DESC;
 END //
DELIMITER ;
 -- --------------------------------------------------------