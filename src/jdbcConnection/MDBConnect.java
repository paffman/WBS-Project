/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 *  Stellt die Verbindung zu einer MDB Datei her
 * 
 * @author Samson von Graevenitz
 * @version 0.1 - 30.11.2010
 */

package jdbcConnection;

import java.sql.*;

public class MDBConnect {

	//läd den aktuell installierten JdbcOdbcDriver
	private static String driverPackage="sun.jdbc.odbc.JdbcOdbcDriver";
	//selektiert nur MDB Datenbanken
	private static String driver="jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)}";
	//Pfad zum öffnen der MDB
	private static String pathDB;
	
	
	/**
	 * Stellt die Verbindung zur MDB her
	 * @return Connection-Object
	 */
	public static Connection getConnection(){
		try{
			Class.forName(driverPackage).newInstance();
			Connection c = DriverManager.getConnection(driver+";"+pathDB);
			return c;
		}catch(InstantiationException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Setzt das Datenelement pathDB
	 * @param path Pfadname als String
	 */
	public static void setPathDB(String path){
		path = "DBQ=" + path;
		path = path.replace("\\", "/");
		
		pathDB = path;
		System.out.println(pathDB);
	}
	
	public static String getPathDB(){
		return pathDB;
	}
}
