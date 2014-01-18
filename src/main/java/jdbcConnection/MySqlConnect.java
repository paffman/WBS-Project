package jdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ...
 * 
 * @author Hendrik Mainzer
 * @version 3.0 - 2014-01-17
 */
public final class MySqlConnect {

	/**
	 * Constant which holds the name of the jdbc driver.
	 */
	private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	/**
	 * address of the host.
	 */
	private static String host;
	/**
	 * name of the connected database.
	 */
	private static String dbName;
	/**
	 * username/login of the connected database.
	 */
	private static String login;
	/**
	 * password of login.
	 */
	private static String password;
	/**
	 * unique id of the database
	 */
	private static String id;

	/**
	 * private constructor to make it impossible to create an instance of
	 * MySqlConnect.
	 */
	private MySqlConnect() {
	};

	/**
	 * Returns an Connection-Object which can be used to submit queries to the
	 * MySql-Database.
	 * 
	 * @return Connection-Object
	 */
	public static Connection getConnection() {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(MYSQL_DRIVER);
			Connection connect = DriverManager.getConnection("jdbc:mysql://"
					+ host + "/" + dbName + "?user=" + login + "&password="
					+ password);
			return connect;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.err.println("Could not establish connection to database: "
				+ dbName);
		return null;
	}

	/**
	 * Sets the parameters for the access of the MySql-DB.
	 * 
	 * @param aHost
	 *            : address of the host
	 * @param aDbName
	 *            : name of the db
	 * @param aId
	 *            : index of the database
	 * @param aLogin
	 *            : login of the user, actual db-user is dbId_login, wbs-tool
	 *            user is login
	 * @param aPassword
	 *            : password of the user
	 */
	public static void setDbConncetion(final String aHost,
			final String aDbName, final String aId, final String aLogin,
			final String aPassword) {
		MySqlConnect.host = aHost;
		MySqlConnect.dbName = aDbName;
		MySqlConnect.id = aId;
		MySqlConnect.login = aLogin;
		MySqlConnect.password = aPassword;

		// since the DB may have changed existing connections are closed.
		SQLExecuter.closeConnection();
	}

	/**
	 * @return the host
	 */
	public static String getHost() {
		return host;
	}

	/**
	 * @return the dbName
	 */
	public static String getDbName() {
		return dbName;
	}

	/**
	 * @return the id
	 */
	public static String getId() {
		return id;
	}

	/**
	 * @return the login
	 */
	public static String getLogin() {
		return login;
	}
}
