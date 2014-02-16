
package de.fhbingen.wbs.jdbcConnection;

import java.sql.*;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * @deprecated
 * Studienprojekt:	WBS
 *
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz,
 * 						Peter Lange,
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * Studienprojekt:	PSYS WBS 2.0
 *
 * Kunde:		Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Michael Anstatt,
 *			Marc-Eric Baumgärtner,
 *			Jens Eckes,
 *			Sven Seckler,
 *			Lin Yang
 *
 *  Stellt die Verbindung zu einer MDB Datei her
 *
 * @author Samson von Graevenitz / Marc-Eric Baumgärtner
 * @version 2.0 - 13.06.2012
 */
public class MDBConnect {

	//läd den aktuell installierten JdbcOdbcDriver
	private static String driverPackage="sun.jdbc.odbc.JdbcOdbcDriver";
	//selektiert nur MDB Datenbanken
	private static String driver="jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)}";
	//Pfad zum öffnen der MDB
	public static String pathDB;


	/**
	 * Stellt die Verbindung zur MDB her
	 * //WBS 2.0: Fehlerausgabe ueber JOptionPane
	 * @return Connection-Object
	 */
	public static Connection getConnection(){
		try{
			Class.forName(driverPackage).newInstance();
			Properties props = new Properties();
			props.put ("charSet", "ISO-8859-1");
			Connection c = DriverManager.getConnection(driver+";"+pathDB, props);
			return c;
		}catch(InstantiationException e){
			JOptionPane.showMessageDialog(null,e.toString(), "Fehler",
                    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			JOptionPane.showMessageDialog(null,e.toString(), "Fehler",
                    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null,e.toString(), "Fehler",
                    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}catch(IllegalAccessException e){
			JOptionPane.showMessageDialog(null,e.toString(), "Fehler",
                    JOptionPane.ERROR_MESSAGE);
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
		SQLExecuter.closeConnection();
		System.out.println(pathDB);
	}

	public static String getPathDB(){
		return pathDB;
	}
}
