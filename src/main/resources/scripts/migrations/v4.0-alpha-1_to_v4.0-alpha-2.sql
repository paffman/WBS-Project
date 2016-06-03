DROP PROCEDURE IF EXISTS conflicts_delete;

-- --------------------------------------------------------
-- conflicts_delete()
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE conflicts_delete()
  BEGIN
    DELETE
    FROM conflicts
    WHERE reason != 9;
  END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- test_case_select()
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE test_case_select()
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
        ON
          w.id = t.fid_wp;
  END //
DELIMITER ;
-- --------------------------------------------------------

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
    ORDER BY
      t.timestamp
    DESC;
  END //
DELIMITER ;
-- --------------------------------------------------------