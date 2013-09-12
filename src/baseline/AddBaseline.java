package baseline;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Berechnet eine neue Baseline oder nur die Oberarbeitspakete und schreibt diese in die Datenbank 
 * 
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 */

import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import wpOverview.WPOverview;
import jdbcConnection.SQLExecuter;

public class AddBaseline {

	/**
	 * Variablen für die baseID (ID der Baseline)
	 * Variablen für die Beschreibung der Baseline und der WPOverview
	 * Arrayliste zuweisungsliste in der alle Mitarbeiterzuweisungen stehen werden
	 * Arrayliste arbeitspaketliste in der alle arbeitspaket des Projektes stehen werden 
	 */
	private int baseID;
	private String beschreibung;
	private WPOverview wpOverview;
	private boolean baseline;
	private ArrayList<Aufwand> zuweisungsliste = new ArrayList<Aufwand>();
	ArrayList<Arbeitspaket> arbeitspaketliste = new ArrayList<Arbeitspaket>();	
	
	/**
	 * Konstruktor
	 * Werte aus der WPShow werden übergeben
	 * wenn der wert baseline auf true steht, wird eine komplette Baseline gezogen
	 * ansonsten werden nur die Oberarbeitspakete berechnet
	 */
	public AddBaseline(String beschreibung, WPOverview wpOverview, boolean baseline){
		this.wpOverview = wpOverview;
		this.beschreibung = beschreibung;
		this.baseline = baseline;
		getArbeitspakete();
		getPaketzuweisung();
		if(baseline){
			createBaseline();
		}
		generateAnalyse();	
		this.wpOverview.aktualisieren();
	}
	
	/**
	 * wird von dem KOnstruktor aufgerufen
	 * schreibt alle Arbeitspakete in die ArrayListe arbeitspaket 
	 */
	public void getArbeitspakete(){
		//ArrayListe mit allen Arbeitspaketen
		try {
			SQLExecuter sqlArbeit = new SQLExecuter();
			ResultSet arbeit = sqlArbeit.executeQuery("SELECT * FROM Arbeitspaket");
			while(arbeit.next()){
				int id1 = arbeit.getInt("LVL1ID");
				int id2 = arbeit.getInt("LVL2ID");
				int id3 = arbeit.getInt("LVL3ID");
				String idx = arbeit.getString("LVLxID");
				String leiter = arbeit.getString("Name");
				double bac = arbeit.getDouble("BAC");
				double ac = arbeit.getDouble("AC");
				double ev = arbeit.getDouble("EV");
				double etc = arbeit.getDouble("ETC");
				double eac = arbeit.getDouble("EAC");
				double bac_kosten = arbeit.getDouble("BAC_Kosten");
				double ac_kosten = arbeit.getDouble("AC_Kosten");
				double etc_kosten = arbeit.getDouble("ETC_Kosten");
				boolean istOAP = arbeit.getBoolean("istOAP");
				boolean inaktiv = arbeit.getBoolean("istInaktiv");			
				arbeitspaketliste.add(new Arbeitspaket(id1, id2, id3, idx, leiter, bac, ac, ev, etc, eac, bac_kosten, ac_kosten, etc_kosten, istOAP, inaktiv));
			}
			arbeit.close();
			sqlArbeit.closeConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * wird von dem KOnstruktor aufgerufen
	 * schreibt alle Zuweisungen der Mitarbeiter in die Arbeitspakete in die ArrayListe zuweisungstabelle 
	 */
	public void getPaketzuweisung(){
		//ArrayList in der alle Aufwände stehen
		
		try {
			SQLExecuter sqlExecAna = new SQLExecuter();
			ResultSet zuweisung = sqlExecAna.executeQuery("SELECT * FROM Paketzuweisung");
			while(zuweisung.next()){
				int id1 = zuweisung.getInt("FID_LVL1ID");
				int id2 = zuweisung.getInt("FID_LVL2ID");
				int id3 = zuweisung.getInt("FID_LVL3ID");
				String idx = zuweisung.getString("FID_LVLxID");
				String name = zuweisung.getString("FID_Ma");			
				zuweisungsliste.add(new Aufwand(id1, id2, id3, idx, name));
			}
			zuweisung.close();
			sqlExecAna.closeConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
		
	/**
	 * wird im Konstruktor aufgerufen
	 * erstellt eine neue Baseline in der Datenbank
	 * Tabelle wird mit der übergebenen Beschreibung und dem aktuellen Datum gefüllt
	 */
	public void createBaseline(){
		SQLExecuter sqlExecBaseline=new SQLExecuter();
		SQLExecuter sqlExecUpdateBaseline = new SQLExecuter();
		try{
			java.util.Date dt = new Date();
			java.sql.Date dte=new java.sql.Date(dt.getTime());
			int highest=0;
			
			sqlExecUpdateBaseline.executeUpdate("INSERT INTO Baseline(FID_Proj,Datum, Beschreibung) VALUES (1,'"+dte+"','"+beschreibung+"');");
			ResultSet rsBaseline= sqlExecBaseline.executeQuery("Select * from Baseline;");
			while(rsBaseline.next()){
				int wert=rsBaseline.getInt(1);
				//System.out.println(wert+" "+sz.getInt(2)+" "+sz.getDate(3)+" "+sz.getString(4));
				if(wert>highest){
					highest=wert;
				}
			}
			//baseID wird mit der neu generierten Baseline-ID initialisiert
			baseID=highest;
			rsBaseline.close();
			sqlExecBaseline.closeConnection();
			sqlExecUpdateBaseline.closeConnection();
		}catch (BatchUpdateException e) { 
			System.out.println("Update Fehler"); 
			sqlExecBaseline.closeConnection();
			sqlExecUpdateBaseline.closeConnection();
			e.printStackTrace();
		}
		catch(SQLException e){	
			System.out.println("Fehler beim anlegen der Baseline");
			sqlExecBaseline.closeConnection();
			sqlExecUpdateBaseline.closeConnection();
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * wird im Konstruktor aufgerufen
	 * berechnet alle Werte der OAPs für die ersten 3 Ebenen und schreibt sie in die Analysedaten zu einer Baseline
	 * die Werte des Projektes an sich werden auch berechnet und in die Analysedaten geschrieben
	 */
	public void generateAnalyse(){
		//ProjektDaten
		Double ProjektBAC = 0.0;
		Double ProjektAC = 0.0;
		Double ProjektETC = 0.0;			
		Double ProjektEV = 0.0;
		Double ProjektEAC = 0.0;
		Double ProjektCPI = 0.0;
		Double ProjektBACKosten = 0.0;
		Double ProjektACKosten = 0.0;
		Double ProjektETCKosten = 0.0;	
		ArrayList<String> ProjMit = new ArrayList<String>();
		//geht alle Arbeitspakete durch
		for(int paket=0; paket<arbeitspaketliste.size(); paket++){
			//nimmt alle UAPs
			if(!arbeitspaketliste.get(paket).getistOAP()){				
				int id1 = arbeitspaketliste.get(paket).getId1();
				int id2 = arbeitspaketliste.get(paket).getId2();
				int id3 = arbeitspaketliste.get(paket).getId3();
				String idx = arbeitspaketliste.get(paket).getIdx();
				boolean inaktiv = arbeitspaketliste.get(paket).getInaktiv();
				
				//prüft ob Mitarbeiter in Zuweisungstabelle steht
				for(int zuw=0; zuw<zuweisungsliste.size(); zuw++){
					if(zuweisungsliste.get(zuw).getId1()==id1 && 
							zuweisungsliste.get(zuw).getId2()==id2 && 
							zuweisungsliste.get(zuw).getId3()==id3 && 
							zuweisungsliste.get(zuw).getIdx().equals(idx)){
						boolean vorhanden = false;
						for(int mit=0; mit<ProjMit.size(); mit++){
							if(ProjMit.get(mit).equals(zuweisungsliste.get(zuw).getName()))
								vorhanden = true;
						}
						if(!vorhanden){
							ProjMit.add(zuweisungsliste.get(zuw).getName());
						}	
					}				
				}
				//bei inaktiven Paketen wird nur der AC mitgezählt
				if(inaktiv){
					ProjektAC += arbeitspaketliste.get(paket).getAc();
					ProjektACKosten += arbeitspaketliste.get(paket).getAc_kosten();
				}
				else{
					//berechnet die Werte des Projektes, indem die Werte aller Unterarbeitspakete aufsummiert werden	
					ProjektBAC += arbeitspaketliste.get(paket).getBac();
					ProjektAC += arbeitspaketliste.get(paket).getAc();
					ProjektETC += arbeitspaketliste.get(paket).getEtc();
					ProjektEV += arbeitspaketliste.get(paket).getEv();
					ProjektEAC += arbeitspaketliste.get(paket).getEac();
					ProjektBACKosten += arbeitspaketliste.get(paket).getBac_kosten();
					ProjektACKosten += arbeitspaketliste.get(paket).getAc_kosten();
					ProjektETCKosten += arbeitspaketliste.get(paket).getEtc_kosten();
				}		
			}
		}
		
		
		//Berechnung des ProjektCPI
		if(ProjektACKosten+ProjektETCKosten == 0.){
			if(ProjektBACKosten==0.){
				ProjektCPI = 1.0;
			}
			else{
				ProjektCPI = 10.0;
			}
		}
		else{
			ProjektCPI = ProjektBACKosten/(ProjektACKosten+ProjektETCKosten);
			if(ProjektCPI>10.0)
				ProjektCPI=10.0;
		}
		//Projektwerte in die Analysedaten schreiben
		updateRSana(1, 0, 0, 0, WPOverview.generateXEbene(), baseID, "Projekt", ProjektBAC, ProjektAC,
				ProjektEV, ProjektETC, ProjektEAC, ProjektCPI, ProjektBACKosten, ProjektACKosten, ProjektETCKosten, ProjMit);			
		
		System.out.println("Projekt fertig");
		
		
		
		//Ebene 1				
		for(int paket=0; paket<arbeitspaketliste.size(); paket++){
			
			if(arbeitspaketliste.get(paket).getistOAP() && arbeitspaketliste.get(paket).getId1()>0 
					&& arbeitspaketliste.get(paket).getId2()==0 && arbeitspaketliste.get(paket).getId3()==0
						&& arbeitspaketliste.get(paket).getIdx().equals(WPOverview.generateXEbene())){				
				int id1 = arbeitspaketliste.get(paket).getId1();
				int id2 = arbeitspaketliste.get(paket).getId2();
				int id3 = arbeitspaketliste.get(paket).getId3();
				String idx = arbeitspaketliste.get(paket).getIdx();
				String name= arbeitspaketliste.get(paket).getName();
				Double BAC = 0.0;
				Double AC = 0.0;
				Double ETC = 0.0;			
				Double EV = 0.0;
				Double EAC = 0.0;
				Double CPI = 0.0;
				Double BACKosten = 0.0;
				Double ACKosten = 0.0;
				Double ETCKosten = 0.0;	
				boolean e1inaktiv = arbeitspaketliste.get(paket).getInaktiv();
				ArrayList<String> Ebene1Mit = new ArrayList<String>();
				if(!e1inaktiv){
					for(int uap=0; uap<arbeitspaketliste.size(); uap++){
						if(!arbeitspaketliste.get(uap).getistOAP() && arbeitspaketliste.get(uap).getId1()==id1){
							//Es werden alle Mitarbeiter geholt, die in den Unterarbeitspaketen einen Aufwand eingegeben haben			
							int aid1 = arbeitspaketliste.get(uap).getId1();
							int aid2 = arbeitspaketliste.get(uap).getId2();
							int aid3 = arbeitspaketliste.get(uap).getId3();
							String aidx = arbeitspaketliste.get(uap).getIdx();
							boolean inaktiv = arbeitspaketliste.get(uap).getInaktiv();
							for(int i=0; i<zuweisungsliste.size(); i++){				
								if(zuweisungsliste.get(i).getId1()==aid1 && 
										zuweisungsliste.get(i).getId2()==aid2 && 
										zuweisungsliste.get(i).getId3()==aid3 && 
										zuweisungsliste.get(i).getIdx().equals(aidx)){
									boolean vorhanden = false;
									for(int j=0; j<Ebene1Mit.size(); j++){
										if(Ebene1Mit.get(j).equals(zuweisungsliste.get(i).getName()))
											vorhanden = true;
									}
									if(!vorhanden){
										Ebene1Mit.add(zuweisungsliste.get(i).getName());
									}
									
								}				
							}
							if(inaktiv){
								AC += arbeitspaketliste.get(uap).getAc();
								ACKosten += arbeitspaketliste.get(uap).getAc_kosten();
							}
							else{
								//berechnet für die einzelnen Ebene1 Pakete die Werte, indem alle Werte Unterarbeitspaket des zu berechnenten OAP's aufsummiert werden
								BAC += arbeitspaketliste.get(uap).getBac();
								AC += arbeitspaketliste.get(uap).getAc();
								ETC += arbeitspaketliste.get(uap).getEtc();
								EV += arbeitspaketliste.get(uap).getEv();
								EAC += arbeitspaketliste.get(uap).getEac();
								BACKosten += arbeitspaketliste.get(uap).getBac_kosten();
								ACKosten += arbeitspaketliste.get(uap).getAc_kosten();
								ETCKosten += arbeitspaketliste.get(uap).getEtc_kosten();
							}	
						}
					}	
					
					if(ACKosten+ETCKosten == 0.){
						if(BACKosten==0.){
							CPI = 1.0;
						}
						else{
							CPI = 10.0;
						}
					}
					else{
						CPI = BACKosten/(ACKosten+ETCKosten);
						if(CPI>10.0)
							CPI=10.0;
					}
				}
				//Projektwerte in die Analysedaten schreiben
				updateRSana(1, id1, id2, id3, idx, baseID, name, BAC, AC, EV, ETC, EAC, 
							CPI, BACKosten, ACKosten, ETCKosten, Ebene1Mit);
					
			}			
		}
		
		System.out.println("Ebene 1 fertig");
		
	
		//Ebene 2
		for(int paket=0; paket<arbeitspaketliste.size(); paket++){
			
			if(arbeitspaketliste.get(paket).getistOAP() && arbeitspaketliste.get(paket).getId1()>0 
					&& arbeitspaketliste.get(paket).getId2()>0 && arbeitspaketliste.get(paket).getId3()==0
						&& arbeitspaketliste.get(paket).getIdx().equals(WPOverview.generateXEbene())){
				int id1 = arbeitspaketliste.get(paket).getId1();
				int id2 = arbeitspaketliste.get(paket).getId2();
				int id3 = arbeitspaketliste.get(paket).getId3();
				String idx = arbeitspaketliste.get(paket).getIdx();
				String name= arbeitspaketliste.get(paket).getName();
				Double BAC = 0.0;
				Double AC = 0.0;
				Double ETC = 0.0;			
				Double EV = 0.0;
				Double EAC = 0.0;
				Double CPI = 0.0;
				Double BACKosten = 0.0;
				Double ACKosten = 0.0;
				Double ETCKosten = 0.0;	
				boolean e2inaktiv = arbeitspaketliste.get(paket).getInaktiv();
				ArrayList<String> Ebene2Mit = new ArrayList<String>();
				if(!e2inaktiv){
					for(int uap=0; uap<arbeitspaketliste.size(); uap++){
						if(!arbeitspaketliste.get(uap).getistOAP() && arbeitspaketliste.get(uap).getId1()==id1 && arbeitspaketliste.get(uap).getId2()==id2){
							//Es werden alle Mitarbeiter geholt, die in den Unterarbeitspaketen einen Aufwand eingegeben haben			
							int aid1 = arbeitspaketliste.get(uap).getId1();
							int aid2 = arbeitspaketliste.get(uap).getId2();
							int aid3 = arbeitspaketliste.get(uap).getId3();
							String aidx = arbeitspaketliste.get(uap).getIdx();
							boolean inaktiv = arbeitspaketliste.get(uap).getInaktiv();
							for(int i=0; i<zuweisungsliste.size(); i++){				
								if(zuweisungsliste.get(i).getId1()==aid1 && 
										zuweisungsliste.get(i).getId2()==aid2 && 
										zuweisungsliste.get(i).getId3()==aid3 && 
										zuweisungsliste.get(i).getIdx().equals(aidx)){
									boolean vorhanden = false;
									for(int j=0; j<Ebene2Mit.size(); j++){
										if(Ebene2Mit.get(j).equals(zuweisungsliste.get(i).getName()))
											vorhanden = true;
									}
									if(!vorhanden){
										Ebene2Mit.add(zuweisungsliste.get(i).getName());
									}
									
								}				
							}
							if(inaktiv){
								AC += arbeitspaketliste.get(uap).getAc();
								ACKosten += arbeitspaketliste.get(uap).getAc_kosten();
							}
							else{
								//berechnet für die einzelnen Ebene1 Pakete die Werte, indem alle Werte Unterarbeitspaket des zu berechnenten OAP's aufsummiert werden
								BAC += arbeitspaketliste.get(uap).getBac();
								AC += arbeitspaketliste.get(uap).getAc();
								ETC += arbeitspaketliste.get(uap).getEtc();
								EV += arbeitspaketliste.get(uap).getEv();
								EAC += arbeitspaketliste.get(uap).getEac();
								BACKosten += arbeitspaketliste.get(uap).getBac_kosten();
								ACKosten += arbeitspaketliste.get(uap).getAc_kosten();
								ETCKosten += arbeitspaketliste.get(uap).getEtc_kosten();
							}	
						}
					}	
					
					if(ACKosten+ETCKosten == 0.){
						if(BACKosten==0.){
							CPI = 1.0;
						}
						else{
							CPI = 10.0;
						}
					}
					else{
						CPI = BACKosten/(ACKosten+ETCKosten);
						if(CPI>10.0)
							CPI=10.0;
					}
				}
				//Projektwerte in die Analysedaten schreiben
				updateRSana(1, id1, id2, id3, idx, baseID, name, BAC, AC, EV, ETC, EAC, 
							CPI, BACKosten, ACKosten, ETCKosten, Ebene2Mit);			
			}
		}
				
		System.out.println("Ebene 2 fertig");
		
		
		
		
		//Ebene 3
		for(int paket=0; paket<arbeitspaketliste.size(); paket++){
			if(arbeitspaketliste.get(paket).getistOAP() && arbeitspaketliste.get(paket).getId1()>0 
					&& arbeitspaketliste.get(paket).getId2()>0 && arbeitspaketliste.get(paket).getId3()>0
						&& arbeitspaketliste.get(paket).getIdx().equals(WPOverview.generateXEbene())){
				int id1 = arbeitspaketliste.get(paket).getId1();
				int id2 = arbeitspaketliste.get(paket).getId2();
				int id3 = arbeitspaketliste.get(paket).getId3();
				String idx = arbeitspaketliste.get(paket).getIdx();
				String name= arbeitspaketliste.get(paket).getName();
				Double BAC = 0.0;
				Double AC = 0.0;
				Double ETC = 0.0;			
				Double EV = 0.0;
				Double EAC = 0.0;
				Double CPI = 0.0;
				Double BACKosten = 0.0;
				Double ACKosten = 0.0;
				Double ETCKosten = 0.0;	
				boolean e3inaktiv = arbeitspaketliste.get(paket).getInaktiv();
				ArrayList<String> Ebene3Mit = new ArrayList<String>();
				if(!e3inaktiv){
					for(int uap=0; uap<arbeitspaketliste.size(); uap++){
						if(!arbeitspaketliste.get(uap).getistOAP() && arbeitspaketliste.get(uap).getId1()==id1 
								&& arbeitspaketliste.get(uap).getId2()==id2 && arbeitspaketliste.get(uap).getId3()==id3){
							//Es werden alle Mitarbeiter geholt, die in den Unterarbeitspaketen einen Aufwand eingegeben haben			
							int aid1 = arbeitspaketliste.get(uap).getId1();
							int aid2 = arbeitspaketliste.get(uap).getId2();
							int aid3 = arbeitspaketliste.get(uap).getId3();
							String aidx = arbeitspaketliste.get(uap).getIdx();
							boolean inaktiv = arbeitspaketliste.get(uap).getInaktiv();
							for(int i=0; i<zuweisungsliste.size(); i++){				
								if(zuweisungsliste.get(i).getId1()==aid1 && 
										zuweisungsliste.get(i).getId2()==aid2 && 
										zuweisungsliste.get(i).getId3()==aid3 && 
										zuweisungsliste.get(i).getIdx().equals(aidx)){
									boolean vorhanden = false;
									for(int j=0; j<Ebene3Mit.size(); j++){
										if(Ebene3Mit.get(j).equals(zuweisungsliste.get(i).getName()))
											vorhanden = true;
									}
									if(!vorhanden){
										Ebene3Mit.add(zuweisungsliste.get(i).getName());
									}
									
								}				
							}
							if(inaktiv){
								AC += arbeitspaketliste.get(uap).getAc();
								ACKosten += arbeitspaketliste.get(uap).getAc_kosten();
							}
							else{
								//berechnet für die einzelnen Ebene1 Pakete die Werte, indem alle Werte Unterarbeitspaket des zu berechnenten OAP's aufsummiert werden
								BAC += arbeitspaketliste.get(uap).getBac();
								AC += arbeitspaketliste.get(uap).getAc();
								ETC += arbeitspaketliste.get(uap).getEtc();
								EV += arbeitspaketliste.get(uap).getEv();
								EAC += arbeitspaketliste.get(uap).getEac();
								BACKosten += arbeitspaketliste.get(uap).getBac_kosten();
								ACKosten += arbeitspaketliste.get(uap).getAc_kosten();
								ETCKosten += arbeitspaketliste.get(uap).getEtc_kosten();
							}	
						}
					}	
					
					if(ACKosten+ETCKosten == 0.){
						if(BACKosten==0.){
							CPI = 1.0;
						}
						else{
							CPI = 10.0;
						}
					}
					else{
						CPI = BACKosten/(ACKosten+ETCKosten);
						if(CPI>10.0)
							CPI=10.0;
					}
				}
				//Projektwerte in die Analysedaten schreiben
				updateRSana(1, id1, id2, id3, idx, baseID, name, BAC, AC, EV, ETC, EAC, 
							CPI, BACKosten, ACKosten, ETCKosten, Ebene3Mit);
					
			}
		}
		System.out.println("Ebene 3 da");
	}
	
	
	/**
	 * wird von der Methode generateAnalyse() aufgerufen
	 * schreibt die übergebenen Werte in die Tabelle der Datenbanktabelle Analysedaten
	 * @param projID ProjektID
	 * @param id1 LVL1ID
	 * @param id2 LVL2ID
	 * @param id3 LVL3ID
	 * @param idx LVLXID
	 * @param baseID BaselineID
	 * @param name WPName
	 * @param BAC WPBAC
	 * @param AC WPAC
	 * @param EV WPEV
	 * @param ETC WPETC
	 * @param EAC WPEAC
	 * @param CPI WPCPI
	 * @param BACKosten WPBACKosten
	 * @param ACKosten WPACKosten
	 * @param ETCKosten WPETCKosten
	 */
	private void updateRSana(int projID, int id1, int id2, int id3, String idx, int baseID, String name,
			double BAC, double AC, double EV, double ETC, double EAC, double CPI, double BACKosten,
			double ACKosten, double ETCKosten, ArrayList<String> Mitarbeiter){
		if(baseline){
			SQLExecuter sqlExecUpdateWP = new SQLExecuter();
			try {			
					sqlExecUpdateWP.executeUpdate("INSERT INTO Analysedaten (Proj, LVL1ID, LVL2ID, LVL3ID, LVLxID, FID_Base, Name, BAC, AC, EV, ETC, EAC, CPI, BAC_Kosten, AC_Kosten, ETC_Kosten) " +
							"VALUES ("+ projID +", "+ id1 +", "+ id2 +", "+ id3 +", '" + idx + "', " + baseID + ", '" + name + "', " + BAC + ", " + AC + ", " + EV + ", " + ETC + ", " +
							EAC + ", " + CPI + ", " + BACKosten + ", " + ACKosten + ", " + ETCKosten + ");");												
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			sqlExecUpdateWP.closeConnection();
		}
		
		updateWP(projID, id1, id2, id3, idx, name, BAC, AC, EV, ETC, EAC, CPI, BACKosten, ACKosten, ETCKosten, Mitarbeiter);
	}
	
	/**
	 * wird von der Methode generateAnalyse() aufgerufen
	 * schreibt die übergebenen Werte in die Tabelle der Datenbanktabelle Arbeitspaket
	 * @param projID ProjektID
	 * @param id1 LVL1ID
	 * @param id2 LVL2ID
	 * @param id3 LVL3ID
	 * @param idx LVLXID
	 * @param name WPName
	 * @param BAC WPBAC
	 * @param AC WPAC
	 * @param EV WPEV
	 * @param ETC WPETC
	 * @param EAC WPEAC
	 * @param CPI WPCPI
	 * @param BACKosten WPBACKosten
	 * @param ACKosten WPACKosten
	 * @param ETCKosten WPETCKosten
	 */
	
	private void updateWP(int projID, int id1, int id2, int id3, String idx, String name,
			double BAC, double AC, double EV, double ETC, double EAC, double CPI, double BACKosten,
			double ACKosten, double ETCKosten, ArrayList<String> Mitarbeiter){
			SQLExecuter sqlExecUpdateWP= new SQLExecuter();
		
			try {
				sqlExecUpdateWP.executeUpdate("UPDATE Arbeitspaket " +
											"SET BAC = " + BAC + ", " +
											"AC = " + AC + ", " +
											"EV = " + EV + ", " +
											"ETC = " + ETC + ", " +
											"EAC = " + EAC + ", " +
											"CPI = " + CPI + ", " +
											"BAC_Kosten = " + BACKosten + ", " +
											"AC_Kosten = " + ACKosten + ", " +
											"ETC_Kosten = " + ETCKosten + " " +
											"WHERE " +
											"FID_Proj = " + projID + " AND " +	
											"LVL1ID = " + id1 + " AND " +
											"LVL2ID = " + id2 + " AND " +
											"LVL3ID = " + id3 + " AND " +
											"LVLxID = '" + idx + "';" );
			
			
			//Es werden alle zuständigen Mitarbeiter des aktuellen OAPs geholt
			ArrayList<String> Vorhanden = new ArrayList<String>();
			for(int zust=0; zust<zuweisungsliste.size(); zust++){
				if(zuweisungsliste.get(zust).getId1()==id1 && zuweisungsliste.get(zust).getId2()==id2
						&& zuweisungsliste.get(zust).getId3()==id3 && zuweisungsliste.get(zust).getIdx().equals(idx)){
					String Vor = zuweisungsliste.get(zust).getName();		
					Vorhanden.add(Vor);
				}
				
			}
			
			
			//Es werden die Indizes gespeichert, die schon vorhanden sind und nicht gespeichert werden müssen
			ArrayList<Integer> nichtspeichern = new ArrayList<Integer>();
			for(int i=0; i<Mitarbeiter.size(); i++){
				for(int j=0; j<Vorhanden.size(); j++){
					if(Mitarbeiter.get(i).equals(Vorhanden.get(j))){
						nichtspeichern.add(i);					
					}
				}
			}
			
			
			//Es werden die neuen zuständigen in die Tabelle Paketzuweisung geschrieben
			for(int i=0; i<Mitarbeiter.size(); i++){
				boolean darf = true;
				for(int j=0; j<nichtspeichern.size(); j++){
					if(nichtspeichern.get(j)==i){
						darf = false;
					}
				}
				if(darf){				
					sqlExecUpdateWP.executeUpdate("INSERT INTO Paketzuweisung (FID_Proj, FID_LVL1ID, FID_LVL2ID, FID_LVL3ID, FID_LVLxID, FID_Ma) " +
							"VALUES ("+ projID +", "+ id1 +", "+ id2 +", "+ id3 +", '" + idx + "', '" + Mitarbeiter.get(i) + "' );");					
				}			
			}	
			
			
			//Es werden zuständigen gelöscht die in keine AP mehr des OAPs tätig sind
			for(int i=0; i<Vorhanden.size(); i++){
				boolean loeschen = true;
				for(int j=0; j<Mitarbeiter.size(); j++){
					if(Vorhanden.get(i).equals(Mitarbeiter.get(j))){
						loeschen = false;
					}
				}
				if(loeschen){				
					sqlExecUpdateWP.executeUpdate("DELETE FROM Paketzuweisung WHERE FID_Proj = " + projID + " AND FID_LVL1ID = " + id1 + " AND FID_LVL2ID = " + id2 + " AND FID_LVL3ID = " + id3 + "" +
							" AND FID_LVLxID = '" + idx + "' AND FID_Ma = '" + Vorhanden.get(i) + "';");					
				}			
			}	
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		sqlExecUpdateWP.closeConnection();
		
		
	}	
	
}