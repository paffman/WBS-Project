-- --------------------------------------------------------
-- db_identifier_with_application_server( dbname )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE db_identifier_with_application_server(
  IN in_dbname varchar(255))
BEGIN
  SELECT with_application_server
  FROM id_wbs.db_identifier
  WHERE db = in_dbname;
END //
DELIMITER ;
-- --------------------------------------------------------

DROP PROCEDURE IF EXISTS db_identifier_new;

-- --------------------------------------------------------
-- db_identifier_new( dbname )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE db_identifier_new(
	IN in_dbname varchar(255),
	IN in_with_application_server BIT)
BEGIN
	INSERT
	INTO id_wbs.db_identifier( db, with_application_server )
	VALUES ( in_dbname, in_with_application_server );
END //
DELIMITER ;
-- --------------------------------------------------------

ALTER TABLE id_wbs.db_identifier ADD with_application_server BIT NOT NULL;