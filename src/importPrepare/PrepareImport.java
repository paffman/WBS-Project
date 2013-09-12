package importPrepare;


import java.sql.ResultSet;

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
import java.sql.SQLException;
import java.util.ArrayList;

import functions.CalcOAPBaseline;
import functions.WpManager;

import wpOverview.WPOverview;

import jdbcConnection.SQLExecuter;
public class PrepareImport {
	
	/**
	 * User Konstruktor
	 * @param wpo - WPOverview: WPOverview Objekt, ueber das PrepareImport erzeugt worden ist
	 */

	public PrepareImport(WPOverview wpo){
		prepareAPs();
		new CalcOAPBaseline("Initialisierung einer importierten DB", wpo);
	}

	/**
	 * void prepareAPs()
	 * liest alle normalen Arbeitspakete ein und prüft die Werte, falls noetig werden Aenderungen vorgenommen
	 * 
	 * - wird vom Konstruktor aufgerufen
	 */
	
	public void prepareAPs(){
		try {
			ResultSet rsWPValues = SQLExecuter.executeQuery("SELECT * FROM Arbeitspaket WHERE istOAP = 0");
			while(rsWPValues.next()){
				int lvl1ID = rsWPValues.getInt("LVL1ID");
				int lvl2ID = rsWPValues.getInt("LVL2ID");
				int lvl3ID = rsWPValues.getInt("LVL3ID");
				String lvlxID = rsWPValues.getString("LVLxID");
				
				double dailyRate = getTagessatz(lvl1ID, lvl2ID, lvl3ID, lvlxID);
				double bac = rsWPValues.getDouble("BAC");
				double bacCost = bac*dailyRate;
				double ac = getAufwand(lvl1ID, lvl2ID, lvl3ID, lvlxID);		
				double acCost = getACKosten(lvl1ID, lvl2ID, lvl3ID, lvlxID);
				double etc = rsWPValues.getDouble("ETC");
				
				if(etc < 0){
					etc = 0.;
				}
				
				double percentComplete = WpManager.calcPercentComplete(bac, etc, ac);
				double etcCost = ( etc * dailyRate);
				double eac;
				if(bacCost>0){
					eac = acCost + etcCost;
				}
				else{
					eac = 0.0;
				}
				rsWPValues.updateDouble("AC", ac);
				
				Double cpi;
				if(acCost+etcCost == 0.){
					if(bacCost==0.){
						cpi = 1.0;
					}
					else{
						cpi = 10.0;
					}
				}
				else{
					cpi = bacCost/(acCost+etcCost);
				}
				rsWPValues.updateDouble("CPI", cpi);
				rsWPValues.updateDouble("AC_Kosten", acCost);
				rsWPValues.updateDouble("ETC_Kosten", etcCost);
				rsWPValues.updateDouble("EV", bac*percentComplete/100*dailyRate);
				rsWPValues.updateDouble("BAC_Kosten", bacCost);
				rsWPValues.updateDouble("WP_Tagessatz", dailyRate);
				rsWPValues.updateDouble("EAC", eac);		
				rsWPValues.updateRow();
				WpManager.updateAP(WpManager.getWorkpackage(lvl1ID+"."+lvl2ID+"."+lvl3ID+"."+lvlxID));
			}
			rsWPValues.getStatement().close();
		}
		catch (SQLException e) {
			e.printStackTrace();	
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
		ResultSet rsAPAufwand = SQLExecuter.executeQuery("SELECT * FROM Aufwand " +
				"WHERE LVL1ID = " + lvl1ID +
					"and LVL2ID = " + lvl2ID +
						"and LVL3ID = " + lvl3ID +
							"and LVLxID = '" + lvlxID + "';");
		while(rsAPAufwand.next()){
			summe += rsAPAufwand.getDouble("Aufwand");
		}
		rsAPAufwand.close();
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
		ResultSet rsAPAufwand = SQLExecuter.executeQuery("SELECT * FROM Aufwand WHERE LVL1ID = " + lvl1ID + "AND LVL2ID = " + lvl2ID + "AND LVL3ID = " + lvl3ID + "AND LVLxID = '" + lvlxID + "';");
		while(rsAPAufwand.next()){
			ResultSet rsMitarbeiter = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter WHERE Login= '" + rsAPAufwand.getString("FID_Ma") + "';");
			while(rsMitarbeiter.next()){
				ACKosten += rsAPAufwand.getDouble("Aufwand") * rsMitarbeiter.getDouble("Tagessatz");
			}	
			rsMitarbeiter.close();
		}
		rsAPAufwand.close();
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
		
		try {
			ResultSet rsAllZustaendige = SQLExecuter.executeQuery(getSQLZustaendige(lvl1ID, lvl2ID, lvl3ID, lvlxID));
			while(rsAllZustaendige.next()){
				zustaendige.add(rsAllZustaendige.getString("FID_Ma"));
			}
			rsAllZustaendige.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int j=0; j<zustaendige.size(); j++){
			String name = zustaendige.get(j);
			
			try {
				ResultSet rsCurrentZustaendigWPs = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter " +
						"WHERE Login= '" + name + "';");
				while(rsCurrentZustaendigWPs.next()){
					wptagessatz += rsCurrentZustaendigWPs.getDouble("Tagessatz");
				}				
				rsCurrentZustaendigWPs.close();

			} catch (SQLException e) {
				e.printStackTrace();
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
