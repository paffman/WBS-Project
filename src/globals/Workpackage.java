package globals;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import dbServices.ValuesService;
import dbServices.WorkpackageService;


import functions.WpManager;
/**
 * Studienprojekt:	WBS<br/>
 * 
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:	
 * 			Andre Paffenholz, <br/>
 * 			Peter Lange, <br/>
 * 			Daniel Metzler,<br/>
 * 			Samson von Graevenitz<br/>
 * 
 * 
 * Studienprojekt:	PSYS WBS 2.0<br/>
 * 
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:	
 * 			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang<br/>
 *
 * Allgemeine InfoBox, wird  über Menü->Hilfe->Info aufgerufen (ggf. erweitern) <br/>
 *
 * @author Samson von Graevenitz,Peter Lange, Marc-Eric Baumgaertner
 * @version 2.0 - 2012-07-05
 */

public class Workpackage {

	private int lvl1ID;
	private int lvl2ID;
	private int lvl3ID;
	private String lvlxID;
	private String name;
	private String beschreibung;
	private double cpi;
	private double bac;
	private boolean oap;
	private boolean inaktiv;
	private String leader;
	private List<String> zustaendige;
	private double ac;
	private double ev;
	private double etc;
	private double wpDailyRate;
	private double eac;
	private double bacCost;
	private double acCost;
	private double etcCost;

	private Date startDateCalc;
	private Date endDateCalc;
	private Date startDateHope;
	private Date endDateHope;
	private Date tmpStartHope;

	/**
	 * Default-Konstruktor zum Erzeugen eines Arbeitspakets
	 * 
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
	 * @deprecated Sollte durch WBS 2.0 Constructor vollstaendig ersetzt werden
	 */
	public Workpackage(int lvl1ID, int lvl2ID, int lvl3ID, String lvlxID, String name, String beschreibung, Double cpi, Double Bac, double ac, double ev,
			double etc, double wptagessatz, double eac, double bac_kosten, double ac_kosten, double etc_kosten, Date release, boolean istOAP,
			boolean istInaktiv, String fid_Leiter, ArrayList<String> zustaendige) {
		this.lvl1ID = lvl1ID;
		this.lvl2ID = lvl2ID;
		this.lvl3ID = lvl3ID;
		this.lvlxID = lvlxID;
		this.name = name;
		this.beschreibung = beschreibung;
		this.cpi = cpi;
		this.bac = Bac;
		this.endDateHope = release;
		this.oap = istOAP;
		this.inaktiv = istInaktiv;
		this.leader = fid_Leiter;
		this.zustaendige = zustaendige;
		this.ac = ac;
		this.bac = Bac;
		this.ev = ev;
		this.etc = etc;
		this.wpDailyRate = wptagessatz;
		this.eac = eac;
		this.bacCost = bac_kosten;
		this.acCost = ac_kosten;
		this.etcCost = etc_kosten;
	}

	/**
	 * WBS 2.0 - Constructor
	 * 
	 * @param lvl1ID
	 * @param lvl2ID
	 * @param lvl3ID
	 * @param lvlxID
	 * @param name
	 * @param beschreibung
	 * @param cpi
	 * @param Bac
	 * @param ac
	 * @param ev
	 * @param etc
	 * @param wptagessatz
	 * @param eac
	 * @param bac_kosten
	 * @param ac_kosten
	 * @param etc_kosten
	 * @param release
	 * @param istOAP
	 * @param istInaktiv
	 * @param fid_Leiter
	 * @param zustaendige
	 * @param sv
	 * @param spi
	 * @param pv
	 * @param startDateCalc
	 * @param startDateHope
	 * @param endDateCalc
	 */
	public Workpackage(int lvl1ID, int lvl2ID, int lvl3ID, String lvlxID, String name, String beschreibung, Double cpi, Double Bac, double ac, double ev,
			double etc, double wptagessatz, double eac, double bac_kosten, double ac_kosten, double etc_kosten, Date release, boolean istOAP,
			boolean istInaktiv, String fid_Leiter, ArrayList<String> zustaendige, Date startDateCalc, Date startDateHope, Date endDateCalc) {

		this(lvl1ID, lvl2ID, lvl3ID, lvlxID, name, beschreibung, cpi, Bac, ac, ev, etc, wptagessatz, eac, bac_kosten, ac_kosten, etc_kosten, release, istOAP,
				istInaktiv, fid_Leiter, zustaendige);

		this.startDateCalc = startDateCalc;
		this.startDateHope = startDateHope;
		this.endDateCalc = endDateCalc;
	}

	/**
	 * Erzeugt ein Default-Arbeitspaket mit Standardwerten
	 */
	public Workpackage() {
		this.lvl1ID = 0;
		this.lvl2ID = 0;
		this.lvl3ID = 0;
		this.lvlxID = null;
		this.name = "";
		this.beschreibung = "";
		this.cpi = 1;
		this.bac = 0;
		this.endDateHope = null;
		this.oap = false;
		this.inaktiv = false;
		this.leader = "";
		this.zustaendige = new ArrayList<String>();
		this.ac = 0;
		this.ev = 0;
		this.etc = 0;
		this.wpDailyRate = 0;
		this.eac = 0;
		this.bacCost = 0;
		this.acCost = 0;
		this.etcCost = 0;
		this.startDateCalc = null;
		this.startDateHope = null;
		this.endDateCalc = null;

		int levels = WpManager.getRootAp().getLvlIDs().length;

		this.lvlxID = "0";
		for (int i = 1; i <= (levels - 4); i++) {
			this.lvlxID += ".0";
		}
	}

	/**
	 * überschriebene toString-Methode, die ein Arbeitspaket eindeutig ausgibt. Diese Methode wird automatisch beim Erzeugen der Bäume aufgerufen, um die AP
	 * Namen darzustellen (in WPOverview.createTree(ArrayList<Workpackage> wpList) )
	 * 
	 * @return String mit der ID und dem Namen des Arbeitspakets
	 */
	public String toString() {
		return lvl1ID + "." + lvl2ID + "." + lvl3ID + "." + lvlxID + " - " + name;
	}
	
	/**
	 * GETTER LVL1ID
	 * 
	 * @return LVL1ID
	 */
	public int getLvl1ID() {
		return lvl1ID;
	}

	/**
	 * Gibt den BAC in Tagen zurueck
	 * 
	 * @return BAC
	 */
	public Double getBac() {
		return bac;
	}

	/**
	 * Gibt den BAC in Stunden zurueck
	 * @return
	 */
	public Double getBacStunden() {
		return bac * 8;
	}

	/**
	 * SETTER BAC - setzt den BAC in Tagen
	 * 
	 * @param Bac neuer BAC
	 */
	public void setBac(Double Bac) {
		this.bac = Bac;
	}

	/**
	 * GETTER AC
	 * 
	 * @return AC
	 */
	public Double getAc() {
		return ac;
	}

	/**
	 * SETTER BAC - setzt den AC
	 * 
	 * @param ac neuer AC
	 */
	public void setAc(Double ac) {
		this.ac = ac;
	}

	/**
	 * GETTER EV
	 * 
	 * @return EV
	 */
	public Double getEv() {
		return ev;
	}

	/**
	 * SETTER EV - setzt den EV
	 * 
	 * @param ev neuer EV
	 */
	public void setEv(Double ev) {
		this.ev = ev;
	}

	/**
	 * Gibt den ETC in Tagen zurueck
	 * 
	 * @return ETC in Tagen
	 */
	public Double getEtc() {
		return etc;
	}

	/**
	 * Gibt den ETC in Stunden zurueck
	 * 
	 * @return ETC in Stunden
	 */
	public Double getEtcStunden() {
		return etc * 8;
	}

	/**
	 * SETTER ETC - setzt den ETC in Tagen
	 * 
	 * @param etc neuer ETC in Tagen
	 */
	public void setEtc(Double etc) {
		this.etc = etc;
	}

	/**
	 * GETTER Tagessatz
	 * 
	 * @return gemittelter Tagessatz
	 */
	public Double getWptagessatz() {
		return wpDailyRate;
	}

	/**
	 * Gibt den durschnittlichen Stundensatz fuer dieses Arbeitspaket zurueck
	 * @return  durschnittlicher Stundensatz
	 */
	public Double getWpStundensatz() {
		return wpDailyRate / 8;
	}

	/**
	 * SETTER Tagessatz - setzt den gemittelten Tagessatz
	 * 
	 * @param wptagessatz neuer Tagessatz
	 */
	public void setwptagessatz(Double wptagessatz) {
		this.wpDailyRate = wptagessatz;
	}

	/**
	 * Uebernimmt Daten aus einem anderen Arbeitspaket
	 * @param other
	 */
	public void update(Workpackage other) {
		this.lvl1ID = other.getLvlID(1);
		this.lvl2ID = other.getLvlID(2);
		this.lvl3ID = other.getLvlID(3);
		this.lvlxID = other.getLvlxID();
		this.name = other.getName();
		this.beschreibung = other.getBeschreibung();
		this.cpi = other.getCpi();
		this.bac = other.getBac();

		this.oap = other.isIstOAP();
		this.inaktiv = other.isIstInaktiv();
		this.leader = other.getFid_Leiter();
		this.zustaendige = other.getWorkers();
		this.ac = other.getAc();
		this.bac = other.getBac();
		this.ev = other.getEv();
		this.etc = other.getEtc();
		this.wpDailyRate = other.getWptagessatz();
		this.eac = other.getEac();
		this.bacCost = other.getBac_kosten();
		this.acCost = other.getAc_kosten();
		this.etcCost = other.getEtc_kosten();
		this.startDateCalc = other.getStartDateCalc();
		this.endDateCalc = other.getEndDateCalc();
		this.startDateHope = other.getStartDateHope();
		this.endDateHope = other.getEndDateHope();

	}

	/**
	 * GETTER EAC
	 * 
	 * @return EAC
	 */
	public Double getEac() {
		return eac;
	}

	/**
	 * SETTER EAC - setzt den EAC
	 * 
	 * @param eac neuer EAC
	 */
	public void setEac(Double eac) {
		this.eac = eac;
	}

	/**
	 * GETTER BAC-Kosten
	 * 
	 * @return BAC in Euro
	 */
	public Double getBac_kosten() {
		return bacCost;
	}

	/**
	 * SETTER BAC-Kosten - setzt die BAC Kosten in Euro
	 * 
	 * @param bac_kosten neuer BAC in Euro
	 */
	public void setbac_kosten(Double bac_kosten) {
		this.bacCost = bac_kosten;
	}

	/**
	 * GETTER AC-Kosten
	 * 
	 * @return AC in Euro
	 */
	public Double getAc_kosten() {
		return acCost;
	}

	/**
	 * SETTER AC-Kosten - setzt die AC Kosten in Euro
	 * 
	 * @param ac_kosten neuer AC in Euro
	 */
	public void setAc_kosten(Double ac_kosten) {
		this.acCost = ac_kosten;
	}

	/**
	 * GETTER ETC-Kosten
	 * 
	 * @return ETC in Euro
	 */
	public double getEtc_kosten() {
		return etcCost;
	}

	/**
	 * SETTER ETC-Kosten - setzt die ETC Kosten in Euro
	 * 
	 * @param etc_kosten neuer ETC in Euro
	 */
	public void setEtc_kosten(double etc_kosten) {
		this.etcCost = etc_kosten;
	}

	/**
	 * SETTER LVL1ID - setzt die LVL1ID
	 * 
	 * @param lvl1id neue LVL1ID
	 */
	public void setLvl1ID(int lvl1id) {
		lvl1ID = lvl1id;
	}

	/**
	 * GETTER LVL2ID
	 * 
	 * @return LVL2ID
	 */
	public int getLvl2ID() {
		return lvl2ID;
	}

	/**
	 * SETTER LVL2ID - setzt die LVL2ID
	 * 
	 * @param lvl2id neue LVL2ID
	 */
	public void setLvl2ID(int lvl2id) {
		lvl2ID = lvl2id;
	}

	/**
	 * GETTER LVL3ID
	 * 
	 * @return LVL32ID
	 */
	public int getLvl3ID() {
		return lvl3ID;
	}

	/**
	 * SETTER LVL3ID - setzt die LVL3ID
	 * 
	 * @param lvl3id neue LVL3ID
	 */
	public void setLvl3ID(int lvl3id) {
		lvl3ID = lvl3id;
	}

	/**
	 * GETTER LVLxID
	 * 
	 * @return LVLxID as String
	 */
	public String getLvlxID() {
		return lvlxID;
	}

	/**
	 * SETTER LVLxID - setzt die LVLxID
	 * 
	 * @param lvlxID neue LVLxID
	 */
	public void setLvlxID(String lvlxID) {
		this.lvlxID = lvlxID;
	}

	/**
	 * GETTER Name
	 * 
	 * @return Name des Arbeitspakets
	 */
	public String getName() {
		return name;
	}

	/**
	 * SETTER Name - setzt einen neuen Namen für das AP
	 * 
	 * @param name neuer Name als String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * GETTER Beschreibung
	 * 
	 * @return liefert die Beschreibung eins APs
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * SETTER Beschreibung - legt eine neue Beschreibung des AP fest
	 * 
	 * @param beschreibung neue Beschreibung des AP
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	/**
	 * GETTER CPI
	 * 
	 * @return CPI
	 */
	public Double getCpi() {
		return cpi;
	}

	/**
	 * SETTER CPI - setzt den neuen CPI
	 * 
	 * @param cpi neuer CPI
	 */
	public void setcpi(Double cpi) {
		this.cpi = cpi;
	}

	/**
	 * GETTER istOAP
	 * 
	 * @return gibt an, ob das Paket ein OAP ist (true = ja, false = nein)
	 */
	public boolean isIstOAP() {
		return oap;
	}

	/**
	 * SETTER istOAP - gibt an, ob es sich um ein OAP handelt
	 * 
	 * @param istOAP handelt es sich um ein OAP?
	 */
	public void setIstOAP(boolean istOAP) {
		this.oap = istOAP;
	}

	/**
	 * GETTER istInaktiv
	 * 
	 * @return gibt an, ob das Paket inaktiv ist (true = ja, false = nein)
	 */
	public boolean isIstInaktiv() {
		return inaktiv;
	}

	/**
	 * SETTER istInaktiv - gibt an, ob das Paket inaktiv ist
	 * 
	 * @param istInaktiv ist das Paket inaktiv zu markieren?
	 */
	public void setIstInaktiv(boolean istInaktiv) {
		this.inaktiv = istInaktiv;
	}

	/**
	 * GETTER Leiter
	 * 
	 * @return gibt den Namen des Leiters als String zurück
	 */
	public String getFid_Leiter() {
		return leader;
	}

	/**
	 * SETTER Leiter - legt den Leiter des AP fest
	 * 
	 * @param fid_Leiter Names des Leiters als String
	 */
	public void setFid_Leiter(String fid_Leiter) {
		this.leader = fid_Leiter;
	}

	/**
	 * GETTER Zuständige
	 * 
	 * @return gibt die zuständigen Mitarbeiter zurück
	 */
	public String getStringZustaendige() {
		String szustaendige = "";
		for (int i = 0; i < zustaendige.size(); i++)
			szustaendige += zustaendige.get(i);
		return szustaendige;
	}
	
	/**
	 * Getter fuer den SV des aktuellen Tages
	 * 
	 * @return SV
	 */
	public double getSv() {
		return ev - getPv();
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public double getSv(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return ev - ValuesService.getApPv(getStringID(), cal);
	}

	/**
	 * 
	 * @return
	 */
	public double getSpi() {
		if (getPv() != 0) {
			if (ev != 0) {
				return ev / getPv() > 10 ? 10.0 : ev / getPv();
			} else {
				return 1.0;
			}
		} else {
			return 0;
		}

	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public double getSpi(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		double pv = ValuesService.getApPv(getStringID(), cal);
		if ((int) pv <= 0) { return 10; }
		if (pv != 0) {
			if (ev != 0) {
				return ev / pv > 10 ? 10.0 : ev / pv;
			} else {
				return 1.0;
			}
		} else {
			return 0;
		}

	}

	/**
	 * GETTER PV
	 * 
	 * @return gibt die Planed Value zurück
	 */
	public double getPv() {
		return ValuesService.getApPv(this.getStringID());
	}

	/**
	 * GETTER startDateCalc
	 * 
	 * @return ausgerechnet Startdatum
	 */
	public Date getStartDateCalc() {
		return startDateCalc;
	}

	/**
	 * SETTER startDateCalc
	 * 
	 * Das Startdatum wird nur gesetzt wenn die PV Analyse zu diesem Zeitpunkt noch
	 * geaendert werden darf, d.h. das bisherige Startdatum liegt in der Zukunft
	 * Dies gilt nicht fuer OAP da ihre Start- und Enddaten 
	 * sowieso durch ihre UAP bestimmt werden.
	 * 
	 * @param startDateCalc als Date von Datenbank
	 */
	public void setStartDateCalc(Date startDateCalc) { 
		if (this.startDateCalc == null) {
			this.startDateCalc = startDateCalc;
		} else {
			if (this.oap || this.startDateCalc.after(new Date(System.currentTimeMillis()))) {
				this.startDateCalc = startDateCalc;
			}
		}
	}

	/**
	 * GETTER endDateCalc
	 * 
	 * @return gerechnete Endedatum
	 */
	public Date getEndDateCalc() {
		return endDateCalc;
	}

	/**
	 * SETTER endDateCalc
	 * 
	 * Das Enddatum wird nur gesetzt wenn die PV Analyse zu diesem Zeitpunkt noch
	 * geaendert werden darf, d.h. das bisherige Enddatum liegt in der Zukunft
	 * Dies gilt nicht fuer OAP da ihre Start- und Enddaten 
	 * sowieso durch ihre UAP bestimmt werden.
	 * 
	 * @param endDateCalc als Date von Datenbank
	 */
	public void setEndDateCalc(Date endDateCalc) {
		if (this.endDateCalc == null || startDateCalc == null) {
			this.endDateCalc = endDateCalc;
		} else {
			if (this.oap || this.endDateCalc.after(new Date(System.currentTimeMillis()))) {
				this.endDateCalc = endDateCalc;
			}
		}
	}

	/**
	 * GETTER startDateHope
	 * 
	 * @return gewünschte Startdatum zurückgeben
	 */
	public Date getStartDateHope() {
		if (startDateHope != null) {
			return startDateHope;
		} else {
			return tmpStartHope;
		}

	}

	/**
	 * SETTER startDateHope
	 * 
	 * @param startDateHope als Date von Datenbank
	 */
	public void setStartDateHope(Date startDateHope) {
		this.startDateHope = startDateHope;
	}

	/**
	 * GETTER Release Datum
	 * 
	 * @return gibt das Release Datum zurück
	 */
	public Date getEndDateHope() {
		return endDateHope;
	}

	/**
	 * SETTER Release - legt ein neues Release-Datum fest
	 * 
	 * @param release neues Datum als String
	 */
	public void setEndDateHope(Date release) {
		this.endDateHope = release;
	}

	/**
	 * WBS2.0 gibt alle Level-IDs in einem Array zurueck, Achtung: index 0 = Level 1!
	 * 
	 * @return Array aller Werte der Levels des AP, beginnend bei Indexwert 0
	 */
	public Integer[] getLvlIDs() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		int position = 1;
		int actualID = getLvlID(position);
		while (actualID != -1) {
			ids.add(position - 1, actualID);
			position++;
			actualID = getLvlID(position);
		}
		return ids.toArray(new Integer[1]);
	}

	/**
	 * WBS2.0 gibt die ID eines gewuenschten Levels zurueck
	 * 
	 * @param level LEvel von dem der Wert benoetigt wird (1 - maximale Ebenenzahl)
	 * @return den entsprechenden Wert der lvlID oder -1 wenn Level nicht vorhanden
	 */
	public String getStringID() {
		return (lvl1ID + "." + lvl2ID + "." + lvl3ID + "." + lvlxID);
	}

	public int getLvlID(int level) {
		int id = 0;

		if (level == 1) {
			id = lvl1ID;
		} else if (level == 2) {
			id = lvl2ID;
		} else if (level == 3) {
			id = lvl3ID;
		} else if (level > 3) {
			String[] xIDs = lvlxID.split("\\.");
			try {
				id = Integer.parseInt(xIDs[level - 4]);
			} catch (ArrayIndexOutOfBoundsException e) {
				id = -1;
			}

		}

		return id;
	}

	/**
	 * WBS2.0 setzt die ID eines gewuenschten Levels
	 * 
	 * @param level
	 * @param value
	 */
	public void setLvlID(int level, int value) {
		if (level == 1) {
			this.lvl1ID = value;
		} else if (level == 2) {
			this.lvl2ID = value;
		} else if (level == 3) {
			this.lvl3ID = value;
		} else if (level > 3) {
			String[] xIDs = lvlxID.split("\\.");
			xIDs[level - 4] = "" + value;
			lvlxID = mergeLvlx(xIDs);
		}
	}

	/**
	 * WBS2.0 gibt das Level des letzten Werts != 0 an
	 * 
	 * @return
	 */
	public int getlastRelevantIndex() {
		int i = 1;
		int actualLvlId = getLvlID(i);
		while (actualLvlId > 0) {
			i++;
			actualLvlId = getLvlID(i);

		}
		return i - 1;
	}

	/**
	 * WBS2.0 fuegt Array von IDs zu String der Form "x.x.x" zusammen
	 * 
	 * @param xIDs
	 * @return
	 */
	private String mergeLvlx(String[] xIDs) {
		String merged = "";
		for (String s : xIDs) {
			if (merged.equals("")) {
				merged += s;
			} else {
				merged += "." + s;
			}
		}
		return merged;
	}

	// getter/setter
	/**
	 * Getter fuer alle Vorgaengern des APListObjects
	 * 
	 * @return Liste mit allen Vorgaengern
	 */
	public Set<Workpackage> getAncestors() {
		return WpManager.getAncestors(this);
	}

	/**
	 * Getter fuer alle Nachfolger des APListObjects
	 * 
	 * @return Liste mit allen Nachfolgern
	 */
	public Set<Workpackage> getFollowers() {
		return WpManager.getFollowers(this);
	}

	public boolean delFollower(Workpackage follower) {
		return WpManager.removeFollower(follower, this);
	}

	public boolean delAncestor(Workpackage ancestor) {
		return WpManager.removeAncestor(ancestor, this);
	}

	public int hashCode() {
		return this.getStringID().hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof Workpackage) {
			Workpackage otherWp = (Workpackage) o;
			return otherWp.getStringID().equals(this.getStringID());
		} else {
			return false;
		}
	}

	public String getOAPID() {
		String oapID;
		if (getLvlID(2) == 0) {
			oapID = "0";
		} else {
			oapID = getLvlID(1) + "";
		}

		for (int i = 2; i <= this.getLvlIDs().length; i++) {
			if (i < this.getlastRelevantIndex()) {
				oapID += "." + getLvlID(i);
			} else {
				oapID += ".0";
			}

		}
		return oapID;
	}

	public boolean addWorker(String workerID) {
		if (!zustaendige.contains(workerID)) {
			zustaendige.add(workerID);
			WorkpackageService.addWpWorker(this, workerID);
			return true;
		} else {
			return false;
		}
	}

	public void removeWorker(String workerID) {
		zustaendige.remove(workerID);
		WorkpackageService.removeWpWorker(this, workerID);
	}

	public List<String> getWorkers() {
		return new ArrayList<String>(zustaendige);
	}

	public boolean canCalc() {
		for (Workpackage actualAncestor : this.getAncestors()) {
			if (actualAncestor.getEndDateCalc() == null) { return false; }
		}
		return true;
	}

	public void setLvlIDs(String[] ids) {
		try {
			int actualLevel = 1;
			for (String actualID : ids) {
				this.setLvlID(actualLevel, Integer.parseInt(actualID));
				actualLevel++;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Bitte geben Sie eine korrekte ID an");
		}
	}

	public void setTempStartHope(Date startHope) {
		this.tmpStartHope = startHope;
	}

	public void setLvlIDs(Integer[] levelIDs) {
		for (int i = 0; i < levelIDs.length; i++) {
			setLvlID(i, levelIDs[i]);
		}
	}

	public String getToolTipString() {

		Set<Workpackage> ancestors = WpManager.getAncestors(this);
		Set<Workpackage> followers = WpManager.getFollowers(this);

		String text = "<html>";

		if (!ancestors.isEmpty()) {
			text += "VG: ";
		}
		for (Workpackage actualAncestor : ancestors) {
			text += actualAncestor.getStringID() + "  ";
		}

		if (!ancestors.isEmpty() && !followers.isEmpty()) {
			text += " | ";
		}

		if (!followers.isEmpty()) {
			text += "NF: ";
		}
		for (Workpackage actualFollower : followers) {
			text += actualFollower.getStringID() + "  ";
		}

		if (!text.equals("<html>")) {
			text += "<br>";
		}
		text += "MA: ";
		for (String actualUser : getWorkers()) {
			text += actualUser + "  ";
		}

		text += "</html>";
		return text;
	}

}
