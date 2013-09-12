package importPrepare;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Berechnen der Daten von externen Projekten, die nicht direkt in der WBS Anwendung erzeugt worden sind
 * und als MDB mit dem Programm geladen worden sind.
 * 
 * @author Andre Paffenholz
 * @version 0.3 - .12.2010
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import baseline.AddBaseline;
import wpOverview.WPOverview;

import jdbcConnection.SQLExecuter;

public class PrepareImport {
	
	/**
	 * User Konstruktor
	 * @param wpo - WPOverview: WPOverview Objekt, ueber das PrepareImport erzeugt worden ist
	 */

	public PrepareImport(WPOverview wpo){
		prepareAPs();
		new AddBaseline("Initialisierung einer importierten DB", wpo, true);
	}

	/**
	 * void prepareAPs()
	 * liest alle normalen Arbeitspakete ein und prüft die Werte, falls noetig werden Aenderungen vorgenommen
	 * 
	 * - wird vom Konstruktor aufgerufen
	 */
	
	public void prepareAPs(){
		SQLExecuter sqlExecWPValues = new SQLExecuter();
		try {
			ResultSet rsWPValues = sqlExecWPValues.executeQuery("SELECT * FROM Arbeitspaket WHERE istOAP = 0");
			while(rsWPValues.next()){
				double stat = 0;
				int lvl1ID = rsWPValues.getInt("LVL1ID");
				int lvl2ID = rsWPValues.getInt("LVL2ID");
				int lvl3ID = rsWPValues.getInt("LVL3ID");
				String lvlxID = rsWPValues.getString("LVLxID");
				Double tagessatz = getTagessatz(lvl1ID, lvl2ID, lvl3ID, lvlxID);
				Double BAC = rsWPValues.getDouble("BAC");
				Double BACKosten = BAC*tagessatz;
				Double AC = getAufwand(lvl1ID, lvl2ID, lvl3ID, lvlxID);		
				Double ACKosten = getACKosten(lvl1ID, lvl2ID, lvl3ID, lvlxID);
				Double ETC = rsWPValues.getDouble("ETC");
				
				if(ETC <0){
					ETC = 0.;
				}
				
				if(BAC>0 && ETC==0)
					stat = 100;
				else{
					if(ETC>0 && AC>0){
						stat= (int) (AC*100 /(AC+ETC));
				    }
				    else{
				    	stat= 0;
				    } 
				}	
				Double ETCKosten = ( ETC * tagessatz);
				Double EAC;
				if(BACKosten>0){
					EAC = ACKosten + ETCKosten;
				}
				else{
					EAC = 0.0;
				}
				rsWPValues.updateDouble("AC", AC);
				
				Double cpi;
				if(ACKosten+ETCKosten == 0.){
					if(BACKosten==0.){
						cpi = 1.0;
					}
					else{
						cpi = 999999.9;
					}
				}
				else{
					cpi = BACKosten/(ACKosten+ETCKosten);
				}
				rsWPValues.updateDouble("CPI", cpi);
				rsWPValues.updateDouble("AC_Kosten", ACKosten);
				rsWPValues.updateDouble("ETC_Kosten", ETCKosten);
				rsWPValues.updateDouble("EV", BAC*stat/100*tagessatz);
				rsWPValues.updateDouble("BAC_Kosten", BACKosten);
				rsWPValues.updateDouble("WP_Tagessatz", tagessatz);
				rsWPValues.updateDouble("EAC", EAC);		
				rsWPValues.updateRow();
			}
			rsWPValues.close();
		}
		catch (SQLException e) {
			e.printStackTrace();	
		} finally{
			sqlExecWPValues.closeConnection();
		}
	}
	
	/**
	 * Liefert den Aufwand fuer ein durch die Parameter bestimmtes Paket zurueck
	 * 
	 * @param lvl1ID - Level1 ID als int
	 * @param lvl2ID - Level2 ID als int
	 * @param lvl3ID - Level3 ID als int
	 * @param lvlxID - Levelx ID als String
	 * @return der Aufwand als Double
	 * @throws SQLException
	 */
	
	public Double getAufwand(int lvl1ID, int lvl2ID, int lvl3ID, String lvlxID) throws SQLException{
		Double summe = 0.;
		SQLExecuter sqlExecAPAufwand = new SQLExecuter();
		ResultSet rsAPAufwand = sqlExecAPAufwand.executeQuery("SELECT * FROM Aufwand " +
				"WHERE LVL1ID = " + lvl1ID +
					"and LVL2ID = " + lvl2ID +
						"and LVL3ID = " + lvl3ID +
							"and LVLxID = '" + lvlxID + "';");
		while(rsAPAufwand.next()){
			summe += rsAPAufwand.getDouble("Aufwand");
		}
		rsAPAufwand.close();
		sqlExecAPAufwand.closeConnection();
		return summe;		
	}
	
	/**
	 * Liefert die ACKosten fuer ein durch die Parameter bestimmtes Paket zurueck
	 * 
	 * @param lvl1ID - Level1 ID als int
	 * @param lvl2ID - Level2 ID als int
	 * @param lvl3ID - Level3 ID als int
	 * @param lvlxID - Levelx ID als String
	 * @return ACKosten als Double
	 * @throws SQLException
	 */
	
	public double getACKosten(int lvl1ID, int lvl2ID, int lvl3ID, String lvlxID) throws SQLException{
		double ACKosten =0.;
		SQLExecuter sqlExecAPAufwand = new SQLExecuter();
		ResultSet rsAPAufwand = sqlExecAPAufwand.executeQuery("SELECT * FROM Aufwand WHERE LVL1ID = " + lvl1ID + "AND LVL2ID = " + lvl2ID + "AND LVL3ID = " + lvl3ID + "AND LVLxID = '" + lvlxID + "';");
		while(rsAPAufwand.next()){
			SQLExecuter sqlExecMitarbeiter = new SQLExecuter();
			ResultSet rsMitarbeiter = sqlExecMitarbeiter.executeQuery("SELECT * FROM Mitarbeiter WHERE Login= '" + rsAPAufwand.getString("FID_Ma") + "';");
			while(rsMitarbeiter.next()){
				ACKosten += rsAPAufwand.getDouble("Aufwand") * rsMitarbeiter.getDouble("Tagessatz");
			}	
			rsMitarbeiter.close();
			sqlExecMitarbeiter.closeConnection();
		}
		rsAPAufwand.close();
		sqlExecAPAufwand.closeConnection();
		return ACKosten;
	}
	
	/**
	 * Berechnet den Mittelwert der Tagessätze der Mitarbeiter, die einem AP zugewiesen sind
	 * Wird aufgerufen von:
	 * - WPShow.addButtonAction()
	 * - von beiden Konstruktoren der WPShow
	 * 
	 * @return der gemittelte Tagessatz als Double
	 */
	public double getTagessatz(int lvl1ID, int lvl2ID, int lvl3ID, String lvlxID){

		Double wptagessatz = 0.0;
		ArrayList<String> zustaendige = new ArrayList<String>();
		
		SQLExecuter sqlExecAllZustaendige = new SQLExecuter();
		try {
			ResultSet rsAllZustaendige = sqlExecAllZustaendige.executeQuery(getSQLZustaendige(lvl1ID, lvl2ID, lvl3ID, lvlxID));
			while(rsAllZustaendige.next()){
				zustaendige.add(rsAllZustaendige.getString("FID_Ma"));
			}
			rsAllZustaendige.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			sqlExecAllZustaendige.closeConnection();
		}
		
		for(int j=0; j<zustaendige.size(); j++){
			String name = zustaendige.get(j);
			
			SQLExecuter sqlExecCurrentZustaendigWPs = new SQLExecuter();
			try {
				ResultSet rsCurrentZustaendigWPs = sqlExecCurrentZustaendigWPs.executeQuery("SELECT * FROM Mitarbeiter " +
						"WHERE Login= '" + name + "';");
				while(rsCurrentZustaendigWPs.next()){
					wptagessatz += rsCurrentZustaendigWPs.getDouble("Tagessatz");
				}				
				rsCurrentZustaendigWPs.close();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				sqlExecCurrentZustaendigWPs.closeConnection();
			}
		}	
		wptagessatz /= zustaendige.size();
		if (Double.isNaN(wptagessatz)){
			wptagessatz = 0.0;
		}
		
		return wptagessatz;		
	}
	
	/**
	 * Liefert den SQL String, um die zuständigen auslesen zu können
	 * Wird aufgerufen durch:
	 *  - ActionListener: Zuständige Hinzufügen
	 *  - getAndShowValues()
	 *  - getTagessatz()
	 * @return SQL-String um die zuständigen aus einem AP auslesen zu können
	 */
	public String getSQLZustaendige(int lvl1ID, int lvl2ID, int lvl3ID, String lvlxID){		
		return "SELECT * FROM Paketzuweisung " +		
				"WHERE FID_LVL1ID = " + lvl1ID + " " +
				"AND FID_LVL2ID = " + lvl2ID + " " +
				"AND FID_LVL3ID = " + lvl3ID + " " +
				"AND FID_LVLxID = '" + lvlxID + "';";
	}
}
