
package jdbcConnection;
import java.sql.*;


/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Führt SQL-Statements aus.
 * Hält ein Connection-Objekt auf dem SQL-Updates und SQL-Querys
 * ausgeführt werden können.
 * 
 * @author Samson von Graevenitz
 * @version - 0.1 30.11.2010
 */
public class SQLExecuter {

	//Datenelement fürs Konnektor-Objekt
	private static Connection theConn = null; 
	
	/**
	 * Default-Konstruktor 
	 */
	public SQLExecuter(){
	}
	
	/**
	 * Stellt die Verbindung zur MDB her und speichert das Connection-Objekt
	 *
	 * @exception Exception Es wird ein Fehler verursacht, falls die Verbindung fehlschägt.
	 * Dabei wird "no Connection" auf der Konsole ausgegeben
	 */
	private static void getConnection(){
		try{
		if(theConn == null) {
			theConn = MDBConnect.getConnection();
		}
			
		}catch(Exception e){
			System.out.println("no Connection");
			
		}
	}
	
	/**
	 * Schließt die Verbindung zur MDB
	 */
	public static void closeConnection(){
		try{
			if(theConn != null) {
				theConn.close();
			}
		}catch(SQLException e){
            e.printStackTrace();
        }
		theConn = null;
	}
	
	/**
	 * Führt ein SQL-Statement aus - welches kein ResultSet benötigt
	 * z.B. CREATE TABLE, INSERT INTO
	 * @param sql SQL Statement als String
	 */
	public static void executeUpdate(String sql) throws SQLException{
		getConnection();
		Statement stmt;
		try {
			stmt = theConn.createStatement();
			//stmt = theConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			stmt.executeUpdate(sql);
			theConn.commit();
			stmt.close();
		} catch (SQLException e) {
			/*
			 * Falls ein Arbeitspaket gelöscht wird und die Kaskadierung dies Verhindert,
			 * so wird eine entsprechende SQLException geworfen. Diese wird abgefangen und der Fehler wird
			 * an die WPOverview durchgereicht, wo er dann verarbeitet wird.
			 * 
			 * Bei anderen Exceptions soll der Fehler nach wie vor ausgegeben werden.
			 */
			
			if(e.getErrorCode() == -1612){
				throw new SQLException(e);
			}
			else{
//				e.printStackTrace();
				throw e;
			}
		}finally{
//			closeConnection();
		}
	}
	
	

	/**
	 * Führt ein SQL-Statement aus, das ein ResultSet zurückgibt z.B.:	SELECT FROM
	 * 
	 * @param sql SQL Statement als String
	 * @return liefert ein ResultSet zurück
	 * 
	 * @exception Exception ein leeres ResultSet wird zurückgegeben und die Fehlermeldung ausgegeben
	 * 
	 */
    public static ResultSet executeQuery(String sql){
        getConnection();
        Statement stmt;
        ResultSet rs;
        try {
            stmt = theConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
//            this.closeConnection();
        }
        return null;
    }
}
