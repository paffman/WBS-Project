-- --------------------------------------------------------
-- this script migrates a database running version v3.0-beta-3 to version v4.0-alpha-1
-- --------------------------------------------------------

-- ---------------------------------------------------------------------
-- TABLES
-- ---------------------------------------------------------------------

-- ---------------------------------------------------------------------
-- test_cases
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS test_cases (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique Identifier for test cases.',
  fid_wp int(11) NOT NULL COMMENT 'Id of the workpackage for which this test case is.',
  name varchar(255) NOT NULL COMMENT 'Name of the test case.',
  description varchar(255) COMMENT 'Description of the test case.',
  precondition varchar(255) COMMENT 'Precondition of the test case.',
  expected_result varchar(255) COMMENT 'Measurable definition of the testcases expected result.',
  PRIMARY KEY ( id ),
  FOREIGN KEY ( fid_wp ) REFERENCES workpackage( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
  COMMENT 'This table manages test cases which belong to exactly one workpackage.';
-- ---------------------------------------------------------------------

-- ---------------------------------------------------------------------
-- test_executions
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS test_executions (
  id int(10) NOT NULL AUTO_INCREMENT COMMENT 'Unique Identifier for test executions.',
  fid_tc int(11) NOT NULL COMMENT 'Id of the testcase for which the test case execution is.',
  fid_emp int(11) NOT NULL COMMENT 'Id of the employee which executed the test execution.',
  remark varchar(255) COMMENT 'test execution remark',
  timestamp TIMESTAMP,
  status ENUM('failed', 'neutral', 'succeeded'),
  PRIMARY KEY ( id ),
  FOREIGN KEY ( fid_emp ) REFERENCES employees( id ),
  FOREIGN KEY ( fid_tc ) REFERENCES test_cases( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
  COMMENT 'This table manages test executions which belong to exactly one workpackage and one test case.';
-- ---------------------------------------------------------------------

-- ---------------------------------------------------------------------
-- STORED PROCEDURES
-- ---------------------------------------------------------------------

-- --------------------------------------------------------
-- workpackage_update_by_id
-- rw
-- --------------------------------------------------------
DROP PROCEDURE IF EXISTS workpackage_update_by_id;

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
  IN in_end_date_calc datetime,
  IN in_fid_parent int(11),
  IN in_parent_order_id int(11))
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
        end_date_calc = in_end_date_calc,
        fid_parent = in_fid_parent,
        parent_order_id = in_parent_order_id
      WHERE
        string_id = in_string_id
        AND
        fid_project = in_project_id;
    END IF;
  END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- workpackage_update_string_id
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE workpackage_update_string_id(
  IN in_pk int(11),
  IN in_string_id varchar(255)
)
  BEGIN
    UPDATE workpackage
    SET
      string_id = in_string_id
    WHERE
      id = in_pk;
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