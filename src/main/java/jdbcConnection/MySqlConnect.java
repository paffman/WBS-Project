package jdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ...
 * 
 * @author Hendrik Mainzer
 * @version
 */
public final class MySqlConnect {

    private static final String mySqlDriver = "com.mysql.jdbc.Driver";
    private static String host, dbName, login, pw;

    /**
     * private constructor to make it impossible to create an instance of
     * MySqlConnect.
     */
    private MySqlConnect() {
    };

    /**
     * getConnection() returns an Connection-Object which can be used to submit
     * queries to the MySql-Database.
     * 
     * @return Connection-Object
     */
    public static Connection getConnection() {
	try {
	    // This will load the MySQL driver, each DB has its own driver
	    Class.forName(mySqlDriver);
	    Connection connect = DriverManager.getConnection("jdbc:mysql://"
		    + host + "/" + dbName + "?user=" + login
		    + "&password=" + pw);
	    return connect;
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Sets the parameters for the access of the MySql-DB.
     * 
     * @param aHost
     *            : address of the host
     * @param aDbName
     *            : name of the db
     * @param aLogin
     *            : login of the user, actual db-user is dbname_login, wbs-tool
     *            user is login
     * @param aPassword
     *            : password of the user
     */
    public static void setDbConncetion(final String aHost,
	    final String aDbName, final String aLogin, final String aPassword) {
	MySqlConnect.host = aHost;
	MySqlConnect.dbName = aDbName;
	MySqlConnect.login = aLogin;
	MySqlConnect.pw = aPassword;
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
     * @return the login
     */
    public static String getLogin() {
	return login;
    }
}
