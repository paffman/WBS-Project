package wpOverview;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 *  Diese Klasse bildet ein Arbeitspaket ab
 * 
 * @author Peter Lange, Andre Paffenholz
 * @version 0.2 - 8.12.2010
 */


import java.util.ArrayList;

public class Workpackage {
	
	//private int fid_proj;
	private int lvl1ID;
	private int lvl2ID;
	private int lvl3ID;
	private String lvlxID;
	private String name;
	private String beschreibung;
	private double cpi;
	private double Bac;
	private String release;
	private boolean istOAP;
	private boolean istInaktiv;
	private String fid_Leiter;
	private ArrayList<String> zustaendige;
	private double ac;
	private double ev;
	private double etc;
	private double wptagessatz;
	private double eac;
	private double bac_kosten;
	private double ac_kosten;
	private double etc_kosten;
	private double status;

	
	/**
	 * Default-Konstruktor zum Erzeugen eines Arbeitspakets
	 * @param lvl1ID LVL1ID
	 * @param lvl2ID LVL2ID
	 * @param lvl3ID LVL3ID
	 * @param lvlxID LVLxID
	 * @param name Names des Pakets
	 * @param beschreibung Beschreibung
	 * @param cpi CPI
	 * @param Bac BAC
	 * @param ac AC
	 * @param ev EV
	 * @param etc ETC
	 * @param wptagessatz gemittelter Tagessatz
	 * @param eac EAC
	 * @param bac_kosten BAC in Euro
	 * @param ac_kosten AC in Euro
	 * @param etc_kosten ETC in Euro
	 * @param release geplantes Datum der Fertigstellung
	 * @param istOAP Handelt es sich um ein Oberarbeitspaket?
	 * @param istInaktiv Ist das Paket inaktiv?
	 * @param fid_Leiter Der Zuständige Leiter dieses Arbeitspakets
	 * @param zustaendige ArrayList mit allen Mitarbeitern, die AUfwand zu diesem Paket erfasst haben
	 */
	public Workpackage(int lvl1ID, int lvl2ID, int lvl3ID, String lvlxID, String name, String beschreibung, 
			Double cpi, Double Bac, double ac, double ev, double etc, double wptagessatz, double eac, double bac_kosten, double ac_kosten, 
			double etc_kosten, String release, boolean istOAP, boolean istInaktiv, String fid_Leiter, ArrayList<String> zustaendige){		
				//this.fid_proj=fid_proj;
				this.lvl1ID=lvl1ID;
				this.lvl2ID=lvl2ID;
				this.lvl3ID=lvl3ID;
				this.lvlxID=lvlxID;
				this.name = name;
				this.beschreibung=beschreibung;
				this.cpi = cpi;
				this.Bac=Bac;
				this.release=release;
				this.istOAP=istOAP;
				this.istInaktiv=istInaktiv;
				this.fid_Leiter=fid_Leiter;
				this.zustaendige=zustaendige;
				this.ac=ac;
				this.Bac=Bac;				
				this.ev=ev;
				this.etc=etc;
				this.wptagessatz=wptagessatz;
				this.eac=eac;
				this.bac_kosten=bac_kosten;
				this.ac_kosten=ac_kosten;
				this.etc_kosten=etc_kosten;
				if(etc==0)
					this.status = 100;
				else{
					if(etc>0 && ac>0){
						this.status= (ac/(ac+etc))*100;
				    }
				    else{
				    	this.status= 0;
				    } 
				}
				
	}
	
	/**
	 * überschriebene toString-Methode, die ein Arbeitspaket eindeutig ausgibt.
	 * Diese Methode wird automatisch beim Erzeugen der Bäume aufgerufen, um die AP Namen darzustellen
	 * (in WPOverview.createTree(ArrayList<Workpackage> wpList) )
	 * @return String mit der ID und dem Namen des Arbeitspakets
	 */
	public String toString(){
		return lvl1ID + "." + lvl2ID + "." + lvl3ID + "." + lvlxID + " - " + name;
	}
	


//Nur Multiprojekt wichtig	
	/*
	public int getFid_proj() {
		return fid_proj;
	}

	public void setFid_proj(int fidProj) {
		fid_proj = fidProj;
	}*/


	/**
	 * GETTER LVL1ID
	 * @return LVL1ID
	 */
	public int getLvl1ID() {
		return lvl1ID;
	}

	

	/**
	 * GETTER BAC
	 * @return BAC
	 */
	public Double getBac() {
		return Bac;
	}
	
	

	/**
	 * SETTER BAC - setzt den BAC
	 * @param Bac neuer BAC
	 */
	public void setBac(Double Bac) {
		this.Bac = Bac;
	}

	

	/**
	 * GETTER Status
	 * @return Status
	 */
	public double getStatus() {
		return status;
	}

	
	/**
	 * SETTER Status
	 * @param status neuer Statuswert
	 */
	public void setStatus(double status) {
		this.status = status;
	}

	

	/**
	 * GETTER AC
	 * @return AC
	 */
	public Double getAc() {
		return ac;
	}

	
	/**
	 * SETTER BAC - setzt den AC
	 * @param ac neuer AC
	 */
	public void setAc(Double ac) {
		this.ac = ac;
	}

	

	/**
	 * GETTER EV
	 * @return EV
	 */
	public Double getEv() {
		return ev;
	}

	
	/**
	 * SETTER EV - setzt den EV
	 * @param ev neuer EV
	 */
	public void setEv(Double ev) {
		this.ev = ev;
	}

	

	/**
	 * GETTER ETC
	 * @return ETC
	 */
	public Double getEtc() {
		return etc;
	}

	
	/**
	 * SETTER ETC - setzt den ETC
	 * @param etc neuer ETC
	 */
	public void setEtc(Double etc) {
		this.etc = etc;
	}

	

	/**
	 * GETTER Tagessatz
	 * @return gemittelter Tagessatz
	 */
	public Double getwptagessatz() {
		return wptagessatz;
	}

	
	/**
	 * SETTER Tagessatz - setzt den gemittelten Tagessatz
	 * @param wptagessatz neuer Tagessatz
	 */
	public void setwptagessatz(Double wptagessatz) {
		this.wptagessatz = wptagessatz;
	}
	
	

	/**
	 * GETTER EAC
	 * @return EAC
	 */
	public Double getEac() {
		return eac;
	}

	
	/**
	 * SETTER EAC - setzt den EAC
	 * @param eac neuer EAC
	 */
	public void setEac(Double eac) {
		this.eac = eac;
	}

	
	

	/**
	 * GETTER BAC-Kosten
	 * @return BAC in Euro
	 */
	public Double getbac_kosten() {
		return bac_kosten;
	}

	
	/**
	 * SETTER BAC-Kosten - setzt die BAC Kosten in Euro
	 * @param bac_kosten neuer BAC in Euro
	 */
	public void setbac_kosten(Double bac_kosten) {
		this.bac_kosten = bac_kosten;
	}

	
	/**
	 * GETTER AC-Kosten
	 * @return AC in Euro
	 */
	public Double getAc_kosten() {
		return ac_kosten;
	}

	
	/**
	 * SETTER AC-Kosten - setzt die AC Kosten in Euro
	 * @param ac_kosten neuer AC in Euro
	 */
	public void setAc_kosten(Double ac_kosten) {
		this.ac_kosten = ac_kosten;
	}
	
	
	/**
	 * GETTER ETC-Kosten
	 * @return ETC in Euro
	 */
	public double getEtc_kosten() {
		return etc_kosten;
	}

	
	/**
	 * SETTER ETC-Kosten - setzt die ETC Kosten in Euro
	 * @param etc_kosten neuer ETC in Euro
	 */
	public void setEtc_kosten(double etc_kosten) {
		this.etc_kosten = etc_kosten;
	}

	
	/**
	 * SETTER LVL1ID - setzt die LVL1ID
	 * @param lvl1id neue LVL1ID
	 */
	public void setLvl1ID(int lvl1id) {
		lvl1ID = lvl1id;
	}

	
	/**
	 * GETTER LVL2ID
	 * @return LVL2ID
	 */
	public int getLvl2ID() {
		return lvl2ID;
	}

	
	/**
	 * SETTER LVL2ID - setzt die LVL2ID
	 * @param lvl2id neue LVL2ID
	 */
	public void setLvl2ID(int lvl2id) {
		lvl2ID = lvl2id;
	}

	
	/**
	 * GETTER LVL3ID
	 * @return LVL32ID
	 */
	public int getLvl3ID() {
		return lvl3ID;
	}

	
	/**
	 * SETTER LVL3ID - setzt die LVL3ID
	 * @param lvl3id neue LVL3ID
	 */
	public void setLvl3ID(int lvl3id) {
		lvl3ID = lvl3id;
	}

	
	/**
	 * GETTER LVLxID
	 * @return LVLxID as String
	 */
	public String getLvlxID() {
		return lvlxID;
	}

	
	/**
	 * SETTER LVLxID - setzt die LVLxID
	 * @param lvlxID neue LVLxID
	 */
	public void setLvlxID(String lvlxID) {
		this.lvlxID = lvlxID;
	}

	
	/**
	 * GETTER Name
	 * @return Name des Arbeitspakets
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * SETTER Name - setzt einen neuen Namen für das AP
	 * @param name neuer Name als String
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	
	/**
	 * GETTER Beschreibung
	 * @return liefert die Beschreibung eins APs
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	
	/**
	 * SETTER Beschreibung - legt eine neue Beschreibung des AP fest
	 * @param beschreibung neue Beschreibung des AP
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	
	/**
	 * GETTER CPI
	 * @return CPI
	 */
	public Double getcpi() {
		return cpi;
	}

	
	/**
	 * SETTER CPI - setzt den neuen CPI
	 * @param cpi neuer CPI
	 */
	public void setcpi(Double cpi) {
		this.cpi = cpi;
	}

	
	/**
	 * GETTER Release Datum
	 * @return gibt das Release Datum zurück
	 */
	public String getRelease() {
		return release;
	}

	
	/**
	 * SETTER Release - legt ein neues Release-Datum fest
	 * @param release neues Datum als String
	 */
	public void setRelease(String release) {
		this.release = release;
	}

	
	/**
	 * GETTER istOAP
	 * @return gibt an, ob das Paket ein OAP ist (true = ja, false = nein)
	 */
	public boolean isIstOAP() {
		return istOAP;
	}

	
	/**
	 * SETTER istOAP - gibt an, ob es sich um ein OAP handelt
	 * @param istOAP handelt es sich um ein OAP?
	 */
	public void setIstOAP(boolean istOAP) {
		this.istOAP = istOAP;
	}

	
	
	/**
	 * GETTER istInaktiv
	 * @return gibt an, ob das Paket inaktiv ist (true = ja, false = nein)
	 */
	public boolean isIstInaktiv() {
		return istInaktiv;
	}

	
	/**
	 * SETTER istInaktiv - gibt an, ob das Paket inaktiv ist
	 * @param istInaktiv ist das Paket inaktiv zu markieren?
	 */
	public void setIstInaktiv(boolean istInaktiv) {
		this.istInaktiv = istInaktiv;
	}

	
	/**
	 * GETTER Leiter
	 * @return gibt den Namen des Leiters als String zurück
	 */
	public String getfid_Leiter() {
		return fid_Leiter;
	}

	
	/**
	 * SETTER Leiter - legt den Leiter des AP fest
	 * @param fid_Leiter Names des Leiters als String
	 */
	public void setfid_Leiter(String fid_Leiter) {
		this.fid_Leiter = fid_Leiter;
	}
	
	
	/**
	 * GETTER Zuständige
	 * @return gibt die zuständigen Mitarbeiter zurück
	 */
	public String getzustaendige() {
		String szustaendige = "";
		for(int i=0; i<zustaendige.size(); i++)
			szustaendige+=zustaendige.get(i);
		return szustaendige;
	}

	
	/**
	 * SETTER Zuständige - legt alle zuständigen fest
	 * @param zustaendige ArrayList mit allen zuständigen Mitarbeitern
	 */
	public void setzustaendige(ArrayList<String> zustaendige) {
		this.zustaendige = zustaendige;
	}
}
