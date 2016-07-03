SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

-- ---------------------------------------------------------------------
-- Datenbank: `id_wbs`
-- ---------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `id_wbs` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `id_wbs`;
-- ---------------------------------------------------------------------

-- ---------------------------------------------------------------------
-- db_identifier
-- ---------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS db_identifier (
	db varchar(255) NOT NULL COMMENT 'Name of the DB',
	id int(4) NOT NULL AUTO_INCREMENT COMMENT 'ID of the DB',
	PRIMARY KEY ( id ),
	UNIQUE ( db )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1
COMMENT 'This Table assigns each db an unique, 3 digit ID.';

-- ---------------------------------------------------------------------

-- ---------------------------------------
-- id_as_string view
-- ---------------------------------------
CREATE VIEW id_as_string AS
SELECT db, LPAD(id,4,'0') as id
FROM db_identifier;

-- --------------------------------------------------------
-- db_identifier_new( dbname )
-- rw
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE db_identifier_new(
	IN in_dbname varchar(255))
BEGIN
	INSERT
	INTO id_wbs.db_identifier( db )
	VALUES ( in_dbname );
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- db_identifier_select_by_dbname( dbname )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE db_identifier_select_by_dbname(
	IN in_dbname varchar(255))
BEGIN
	SELECT *
	FROM id_wbs.id_as_string
	WHERE db = in_dbname;
END //
DELIMITER ;
-- --------------------------------------------------------

-- --------------------------------------------------------
-- db_userid_select_by_username( username )
-- r
-- --------------------------------------------------------
DELIMITER //
CREATE PROCEDURE db_userid_select_by_username(
  IN in_username varchar(255))
BEGIN
  SELECT w.id
  FROM auth_user a join wbs_user_wbsuser w on (a.id = w.user_id)
  WHERE a.username = in_username;
END //
DELIMITER ;
-- --------------------------------------------------------

CREATE USER 'idxUser'@'localhost'
 IDENTIFIED BY '1234';
GRANT EXECUTE ON id_wbs.* TO 'idxUser'@'localhost'
WITH MAX_CONNECTIONS_PER_HOUR 60
     MAX_USER_CONNECTIONS 5;

CREATE USER 'idxUser'@'%'
 IDENTIFIED BY '1234';
GRANT EXECUTE ON id_wbs.* TO 'idxUser'@'%'
WITH MAX_CONNECTIONS_PER_HOUR 60
     MAX_USER_CONNECTIONS 5;


