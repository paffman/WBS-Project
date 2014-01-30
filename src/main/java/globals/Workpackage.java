package globals;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import wpWorker.Worker;
import dbServices.ValuesService;
import dbServices.WorkpackageService;
import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import functions.WpManager;

/**
 * Studienprojekt: WBS<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: Andre Paffenholz, <br/>
 * Peter Lange, <br/>
 * Daniel Metzler,<br/>
 * Samson von Graevenitz<br/>
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Allgemeine InfoBox, wird über Menü->Hilfe->Info aufgerufen (ggf. erweitern) <br/>
 * 
 * @author Samson von Graevenitz,Peter Lange, Marc-Eric Baumgaertner
 * @version 2.0 - 2012-07-05
 */

public class Workpackage {

    private dbaccess.data.Workpackage thisWp;

    private List<Employee> respEmployees;

    private final static int HOURS_PER_DAY = 8;

    /*
     * private int id; private int lvl1ID; private int lvl2ID; private int
     * lvl3ID; private String lvlxID; private String name; private String
     * beschreibung; private double cpi; private double bac; private boolean
     * oap; private boolean inaktiv; private String leader; private double ac;
     * private double ev; private double etc; private double wpDailyRate;
     * private double eac; private double bacCost; private double acCost;
     * private double etcCost;
     */
    private Date tmpStartHope;

    /**
     * Constructor for the Workpackage.
     * 
     * @param wp
     *            A workpackage this is wrapped in this class.
     * @param respEmployees
     *            The employees working on this workpackage.
     */
    public Workpackage(final dbaccess.data.Workpackage wp,
            final ArrayList<Employee> respEmployees) {
        thisWp = wp;
        this.respEmployees = respEmployees;
    }

    /**
     * Erzeugt ein Default-Arbeitspaket mit Standardwerten
     */
    public Workpackage() {
        thisWp = new dbaccess.data.Workpackage();

        int levels = WpManager.getRootAp().getLvlIDs().length;
        String lvlxID = "0";
        for (int i = 1; i <= (levels - 4); i++) {
            lvlxID += ".0";
        }
        thisWp.setStringID("0.0.0" + lvlxID);
        thisWp.setName("");
        thisWp.setDescription("");
        thisWp.setCpi(1);
        thisWp.setBac(0);
        thisWp.setReleaseDate(null);
        thisWp.setTopLevel(false);
        thisWp.setInactive(false);
        this.respEmployees = new ArrayList<Employee>();
        thisWp.setAc(0);
        thisWp.setEv(0);
        thisWp.setEtc(0);
        thisWp.setDailyRate(0);
        thisWp.setEac(0);
        thisWp.setBacCosts(0);
        thisWp.setAcCosts(0);
        thisWp.setEtcCosts(0);
        thisWp.setStartDateCalc(null);
        thisWp.setStartDateWish(null);
        thisWp.setEndDateCalc(null);
    }

    /**
     * überschriebene toString-Methode, die ein Arbeitspaket eindeutig ausgibt.
     * Diese Methode wird automatisch beim Erzeugen der Bäume aufgerufen, um
     * die AP Namen darzustellen (in
     * WPOverview.createTree(ArrayList<Workpackage> wpList) )
     * 
     * @return String mit der ID und dem Namen des Arbeitspakets
     */
    public final String toString() {
        return thisWp.getStringID() + " - " + thisWp.getName();
    }

    /**
     * Gibt den BAC in Tagen zurueck
     * 
     * @return BAC
     */
    public final Double getBac() {
        return thisWp.getBac();
    }

    /**
     * Gibt den BAC in Stunden zurueck
     * 
     * @return
     */
    public final Double getBacStunden() {
        return thisWp.getBac() * HOURS_PER_DAY;
    }

    /**
     * SETTER BAC - setzt den BAC in Tagen
     * 
     * @param Bac
     *            neuer BAC
     */
    public final void setBac(final Double bac) {
        thisWp.setBac(bac);
    }

    /**
     * GETTER AC
     * 
     * @return AC
     */
    public final Double getAc() {
        return thisWp.getAc();
    }

    /**
     * SETTER BAC - setzt den AC
     * 
     * @param ac
     *            neuer AC
     */
    public final void setAc(final Double ac) {
        thisWp.setAc(ac);
    }

    /**
     * GETTER EV
     * 
     * @return EV
     */
    public final Double getEv() {
        return thisWp.getEv();
    }

    /**
     * SETTER EV - setzt den EV
     * 
     * @param ev
     *            neuer EV
     */
    public final void setEv(final Double ev) {
        thisWp.setEv(ev);
    }

    /**
     * Gibt den ETC in Tagen zurueck
     * 
     * @return ETC in Tagen
     */
    public final Double getEtc() {
        return thisWp.getEtc();
    }

    /**
     * Gibt den ETC in Stunden zurueck
     * 
     * @return ETC in Stunden
     */
    public final Double getEtcStunden() {
        return thisWp.getEtc() * HOURS_PER_DAY;
    }

    /**
     * SETTER ETC - setzt den ETC in Tagen
     * 
     * @param etc
     *            neuer ETC in Tagen
     */
    public final void setEtc(final Double etc) {
        thisWp.setEtc(etc);
    }

    /**
     * GETTER Tagessatz
     * 
     * @return gemittelter Tagessatz
     */
    public final Double getWptagessatz() {
        return thisWp.getDailyRate();
    }

    /**
     * Gibt den durschnittlichen Stundensatz fuer dieses Arbeitspaket zurueck
     * 
     * @return durschnittlicher Stundensatz
     */
    public final Double getWpStundensatz() {
        return thisWp.getDailyRate() / HOURS_PER_DAY;
    }

    /**
     * SETTER Tagessatz - setzt den gemittelten Tagessatz
     * 
     * @param wptagessatz
     *            neuer Tagessatz
     */
    public final void setwptagessatz(final Double wptagessatz) {
        thisWp.setDailyRate(wptagessatz);
    }

    /**
     * Uebernimmt Daten aus einem anderen Arbeitspaket
     * 
     * @param other
     */
    public void update(Workpackage other) {
        thisWp.setStringID(other.getStringID());
        thisWp.setName(other.getName());
        thisWp.setDescription(other.getBeschreibung());
        thisWp.setCpi(other.getCpi());
        thisWp.setBac(other.getBac());
        thisWp.setTopLevel(other.isIstOAP());
        thisWp.setInactive(other.isIstInaktiv());
        this.respEmployees = other.getWorkers();
        thisWp.setAc(other.getAc());
        thisWp.setEv(other.getEv());
        thisWp.setEtc(other.getEtc());
        thisWp.setDailyRate(other.getWptagessatz());
        thisWp.setEac(other.getEac());
        thisWp.setBacCosts(other.getBac_kosten());
        thisWp.setAcCosts(other.getAc_kosten());
        thisWp.setEtcCosts(other.getEtc_kosten());
        thisWp.setStartDateCalc(other.getStartDateCalc());
        thisWp.setStartDateWish(other.getStartDateHope());
        thisWp.setEndDateCalc(other.getEndDateCalc());
        thisWp.setReleaseDate(other.getEndDateHope());
    }

    /**
     * GETTER EAC
     * 
     * @return EAC
     */
    public final Double getEac() {
        return thisWp.getEac();
    }

    /**
     * SETTER EAC - setzt den EAC
     * 
     * @param eac
     *            neuer EAC
     */
    public final void setEac(final Double eac) {
        thisWp.setEac(eac);
    }

    /**
     * GETTER BAC-Kosten
     * 
     * @return BAC in Euro
     */
    public final Double getBac_kosten() {
        return thisWp.getBacCosts();
    }

    /**
     * SETTER BAC-Kosten - setzt die BAC Kosten in Euro
     * 
     * @param bac_kosten
     *            neuer BAC in Euro
     */
    public final void setbac_kosten(final Double bac_kosten) {
        thisWp.setBacCosts(bac_kosten);
    }

    /**
     * GETTER AC-Kosten
     * 
     * @return AC in Euro
     */
    public final Double getAc_kosten() {
        return thisWp.getAcCosts();
    }

    /**
     * SETTER AC-Kosten - setzt die AC Kosten in Euro
     * 
     * @param ac_kosten
     *            neuer AC in Euro
     */
    public final void setAc_kosten(final Double ac_kosten) {
        thisWp.setAcCosts(ac_kosten);
    }

    /**
     * GETTER ETC-Kosten
     * 
     * @return ETC in Euro
     */
    public final double getEtc_kosten() {
        return thisWp.getEtcCosts();
    }

    /**
     * SETTER ETC-Kosten - setzt die ETC Kosten in Euro
     * 
     * @param etc_kosten
     *            neuer ETC in Euro
     */
    public final void setEtc_kosten(final double etc_kosten) {
        thisWp.setEtcCosts(etc_kosten);
    }

    /**
     * GETTER LVL1ID
     * 
     * @return LVL1ID
     */
    public final int getLvl1ID() {
        return getLvlIDs()[0];
    }

    /**
     * SETTER LVL1ID - setzt die LVL1ID
     * 
     * @param lvl1id
     *            neue LVL1ID
     */
    public void setLvl1ID(int lvl1id) {
        setLvlID(1, lvl1id);
    }

    /**
     * GETTER LVL2ID
     * 
     * @return LVL2ID
     */
    public final int getLvl2ID() {
        return getLvlIDs()[1];
    }

    /**
     * SETTER LVL2ID - setzt die LVL2ID
     * 
     * @param lvl2id
     *            neue LVL2ID
     */
    public final void setLvl2ID(final int lvl2id) {
        setLvlID(2, lvl2id);
    }

    /**
     * GETTER LVL3ID
     * 
     * @return LVL32ID
     */
    public int getLvl3ID() {
        return getLvlIDs()[2];
    }

    /**
     * SETTER LVL3ID - setzt die LVL3ID
     * 
     * @param lvl3id
     *            neue LVL3ID
     */
    public final void setLvl3ID(final int lvl3id) {
        setLvlID(3, lvl3id);
    }

    /**
     * GETTER LVLxID
     * 
     * @return LVLxID as String
     */
    public final String getLvlxID() {
        Integer[] ids = getLvlIDs();
        String ret = "";
        for (int i = 3; i < ids.length; i++) {
            ret = ret + ids[i];
            if (i <= ids.length - 2) {
                ret = ret + ".";
            }
        }
        return ret;
    }

    /**
     * SETTER LVLxID - setzt die LVLxID
     * 
     * @param lvlxID
     *            neue LVLxID
     */
    public final void setLvlxID(final String lvlxID) {
        thisWp.setStringID(getLvl1ID() + "." + getLvl2ID() + "." + getLvl3ID()
                + "." + lvlxID);
    }

    /**
     * WBS2.0 gibt alle Level-IDs in einem Array zurueck, Achtung: index 0 =
     * Level 1!
     * 
     * @return Array aller Werte der Levels des AP, beginnend bei Indexwert 0
     */
    public final Integer[] getLvlIDs() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        String[] idStrings = thisWp.getStringID().split("\\.");
        for (int i = 0; i < idStrings.length; i++) {
            ids.add(Integer.parseInt(idStrings[i]));
        }
        return ids.toArray(new Integer[1]);
    }

    /**
     * WBS2.0 gibt die ID eines gewuenschten Levels zurueck
     * 
     * @param level
     *            LEvel von dem der Wert benoetigt wird (1 - maximale
     *            Ebenenzahl)
     * @return den entsprechenden Wert der lvlID oder -1 wenn Level nicht
     *         vorhanden
     */
    public final String getStringID() {
        return thisWp.getStringID();
    }

    public final int getLvlID(final int level) {
        try {
            return getLvlIDs()[level - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    /**
     * WBS2.0 setzt die ID eines gewuenschten Levels
     * 
     * @param level
     * @param value
     */
    public final void setLvlID(final int level, final int value) {
        Integer[] ids = getLvlIDs();
        try {
            ids[level - 1] = value;
        } catch (ArrayIndexOutOfBoundsException e) {
            Integer[] ids2 = new Integer[level];
            for (int i = 0; i < ids.length; i++) {
                ids2[i] = ids[i];
            }
            ids2[level - 1] = value;
            ids = ids2;
        }
        String newStringId = "";
        for (int i = 0; i < ids.length; i++) {
            newStringId = newStringId + ids[i];
            if (i <= ids.length - 2) {
                newStringId = newStringId + ".";
            }
        }
        thisWp.setStringID(newStringId);
    }

    /**
     * WBS2.0 gibt das Level des letzten Werts != 0 an
     * 
     * @return
     */
    public final int getlastRelevantIndex() {
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

    /**
     * GETTER Name
     * 
     * @return Name des Arbeitspakets
     */
    public final String getName() {
        return thisWp.getName();
    }

    /**
     * SETTER Name - setzt einen neuen Namen für das AP
     * 
     * @param name
     *            neuer Name als String
     */
    public final void setName(final String name) {
        thisWp.setName(name);
    }

    /**
     * GETTER Beschreibung
     * 
     * @return liefert die Beschreibung eins APs
     */
    public final String getBeschreibung() {
        return thisWp.getDescription();
    }

    /**
     * SETTER Beschreibung - legt eine neue Beschreibung des AP fest
     * 
     * @param beschreibung
     *            neue Beschreibung des AP
     */
    public final void setBeschreibung(final String beschreibung) {
        thisWp.setDescription(beschreibung);
    }

    /**
     * GETTER CPI
     * 
     * @return CPI
     */
    public final Double getCpi() {
        return thisWp.getCpi();
    }

    /**
     * SETTER CPI - setzt den neuen CPI
     * 
     * @param cpi
     *            neuer CPI
     */
    public final void setcpi(final Double cpi) {
        thisWp.setCpi(cpi);
    }

    /**
     * GETTER istOAP
     * 
     * @return gibt an, ob das Paket ein OAP ist (true = ja, false = nein)
     */
    public final boolean isIstOAP() {
        return thisWp.isTopLevel();
    }

    /**
     * SETTER istOAP - gibt an, ob es sich um ein OAP handelt
     * 
     * @param istOAP
     *            handelt es sich um ein OAP?
     */
    public final void setIstOAP(final boolean istOAP) {
        thisWp.setTopLevel(istOAP);
    }

    /**
     * GETTER istInaktiv
     * 
     * @return gibt an, ob das Paket inaktiv ist (true = ja, false = nein)
     */
    public final boolean isIstInaktiv() {
        return thisWp.isInactive();
    }

    /**
     * SETTER istInaktiv - gibt an, ob das Paket inaktiv ist
     * 
     * @param istInaktiv
     *            ist das Paket inaktiv zu markieren?
     */
    public final void setIstInaktiv(final boolean istInaktiv) {
        thisWp.setInactive(istInaktiv);
    }

    /**
     * GETTER Leiter
     * 
     * @return gibt den Namen des Leiters als String zurück
     */
    public final int getFid_Leiter() {
        return thisWp.getEmployeeID();
    }

    /**
     * SETTER Leiter - legt den Leiter des AP fest
     * 
     * @param fid_Leiter
     *            Names des Leiters als String
     */
    public final void setFid_Leiter(final int fidLeiter) {
        thisWp.setEmployeeID(fidLeiter);
    }

    /**
     * GETTER Zuständige
     * 
     * @return gibt die zuständigen Mitarbeiter zurück
     */
    public final String getStringZustaendige() {
        String szustaendige = "";
        for (int i = 0; i < respEmployees.size(); i++) {
            szustaendige += respEmployees.get(i);
        }
        return szustaendige;
    }

    /**
     * Getter fuer den SV des aktuellen Tages
     * 
     * @return SV
     */
    public final double getSv() {
        return thisWp.getEv() - getPv();
    }

    /**
     * @param date
     * @return
     */
    public final double getSv(final Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return thisWp.getEv() - ValuesService.getApPv(getWpId(), cal);
    }

    /**
     * @return Returns the SPI of the workpackage.
     */
    public final double getSpi() {
        final double ev = thisWp.getEv();
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
     * @param date
     * @return Returns the SPI of the workpackage.
     */
    public final double getSpi(final Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        double pv = ValuesService.getApPv(getWpId(), cal);
        if ((int) pv <= 0) {
            return 10;
        }
        final double ev = thisWp.getEv();
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
    public final double getPv() {
        return ValuesService.getApPv(this.getWpId());
    }

    /**
     * GETTER startDateCalc
     * 
     * @return ausgerechnet Startdatum
     */
    public final Date getStartDateCalc() {
        return thisWp.getStartDateCalc();
    }

    /**
     * SETTER startDateCalc Das Startdatum wird nur gesetzt wenn die PV Analyse
     * zu diesem Zeitpunkt noch geaendert werden darf, d.h. das bisherige
     * Startdatum liegt in der Zukunft Dies gilt nicht fuer OAP da ihre Start-
     * und Enddaten sowieso durch ihre UAP bestimmt werden.
     * 
     * @param startDateCalc
     *            als Date von Datenbank
     */
    public final void setStartDateCalc(final Date startDateCalc) {
        if (thisWp.getStartDateCalc() == null) {
            thisWp.setStartDateCalc(startDateCalc);
        } else {
            if (thisWp.isTopLevel()
                    || thisWp.getStartDateCalc().after(
                            new Date(System.currentTimeMillis()))) {
                thisWp.setStartDateCalc(startDateCalc);
            }
        }
    }

    /**
     * GETTER endDateCalc
     * 
     * @return gerechnete Endedatum
     */
    public final Date getEndDateCalc() {
        return thisWp.getEndDateCalc();
    }

    /**
     * SETTER endDateCalc Das Enddatum wird nur gesetzt wenn die PV Analyse zu
     * diesem Zeitpunkt noch geaendert werden darf, d.h. das bisherige Enddatum
     * liegt in der Zukunft Dies gilt nicht fuer OAP da ihre Start- und Enddaten
     * sowieso durch ihre UAP bestimmt werden.
     * 
     * @param endDateCalc
     *            als Date von Datenbank
     */
    public final void setEndDateCalc(final Date endDateCalc) {
        if (thisWp.getEndDateCalc() == null
                || thisWp.getStartDateCalc() == null) {
            thisWp.setEndDateCalc(endDateCalc);
        } else {
            if (thisWp.isTopLevel()
                    || thisWp.getEndDateCalc().after(
                            new Date(System.currentTimeMillis()))) {
                thisWp.setEndDateCalc(endDateCalc);
            }
        }
    }

    /**
     * GETTER startDateHope
     * 
     * @return gewünschte Startdatum zurückgeben
     */
    public final Date getStartDateHope() {
        final Date startDateWish = thisWp.getStartDateWish();
        if (startDateWish != null) {
            return startDateWish;
        } else {
            return tmpStartHope;
        }

    }

    /**
     * SETTER startDateHope
     * 
     * @param startDateWish
     *            als Date von Datenbank
     */
    public final void setStartDateHope(final Date startDateWish) {
        thisWp.setStartDateWish(startDateWish);
    }

    /**
     * GETTER Release Datum
     * 
     * @return gibt das Release Datum zurück
     */
    public final Date getEndDateHope() {
        return thisWp.getReleaseDate();
    }

    /**
     * SETTER Release - legt ein neues Release-Datum fest
     * 
     * @param release
     *            neues Datum als String
     */
    public final void setEndDateHope(final Date release) {
        thisWp.setReleaseDate(release);
    }

    // getter/setter
    /**
     * Getter fuer alle Vorgaengern des APListObjects
     * 
     * @return Liste mit allen Vorgaengern
     */
    public final Set<Workpackage> getAncestors() {
        return WpManager.getAncestors(this);
    }

    /**
     * Getter fuer alle Nachfolger des APListObjects
     * 
     * @return Liste mit allen Nachfolgern
     */
    public final Set<Workpackage> getFollowers() {
        return WpManager.getFollowers(this);
    }

    public final boolean delFollower(final Workpackage follower) {
        return WpManager.removeFollower(follower, this);
    }

    public final boolean delAncestor(final Workpackage ancestor) {
        return WpManager.removeAncestor(ancestor, this);
    }

    public final int hashCode() {
        return this.getStringID().hashCode();
    }

    public final boolean equals(final Object o) {
        if (o instanceof Workpackage) {
            Workpackage otherWp = (Workpackage) o;
            return otherWp.getStringID().equals(this.getStringID());
        } else {
            return false;
        }
    }

    public final String getOAPID() {
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

    public final boolean addWorker(final Employee worker) {
        System.out.println("addWorker: " + worker.getId());//%%
        if (!respEmployees.contains(worker)) {            
            WorkpackageService.addWpWorker(this, worker.getId());
            respEmployees.add(worker);
            return true;
        } else {
            return false;
        }
    }

    public final void removeWorker(final Employee worker) {
        System.out.println("removeWorker: " + worker.getId());//%%
        respEmployees.remove(worker);
        WorkpackageService.removeWpWorker(this, worker.getId());
    }

    public final List<Employee> getWorkers() {
        return new ArrayList<Employee>(respEmployees);
    }

    public final List<Integer> getWorkersIds() {
        List<Integer> ids = new ArrayList<Integer>();
        for (Employee emp : respEmployees) {
            ids.add(emp.getId());
        }
        return ids;
    }

    public final List<String> getWorkerLogins() {
        List<String> logins = new ArrayList<String>();
        for (Employee emp : respEmployees) {
            logins.add(emp.getLogin());
        }
        return logins;
    }

    public final boolean canCalc() {
        for (Workpackage actualAncestor : this.getAncestors()) {
            if (actualAncestor.getEndDateCalc() == null) {
                return false;
            }
        }
        return true;
    }

    public final void setLvlIDs(String[] ids) {
        try {
            int actualLevel = 1;
            for (String actualID : ids) {
                this.setLvlID(actualLevel, Integer.parseInt(actualID));
                actualLevel++;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Bitte geben Sie eine korrekte ID an");
        }
    }

    public final void setTempStartHope(final Date startHope) {
        this.tmpStartHope = startHope;
    }

    public final void setLvlIDs(final Integer[] levelIDs) {
        for (int i = 0; i < levelIDs.length; i++) {
            setLvlID(i, levelIDs[i]);
        }
    }

    public final String getToolTipString() {

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
        for (Employee actualUser : getWorkers()) {
            text += actualUser.getLogin() + "  ";
        }

        text += "</html>";
        return text;
    }

    /**
     * Returns the ID of the wrapped workpackage.
     * 
     * @return The ID of the wrapped Workpackage.
     */
    public final int getWpId() {
        return thisWp.getId();
    }

    /**
     * @return The wrapped dbaccess.data.Workpackage
     */
    public final dbaccess.data.Workpackage getWp() {
        return thisWp;
    }

    /**
     * Finds out and sets the Parent ID
     */
    public void setParentID() {
        Integer[] id = getLvlIDs();
        int lastRelevant = getlastRelevantIndex();
        System.out.println(lastRelevant);
        if (lastRelevant > 0) {
            lastRelevant--;
        }
        id[lastRelevant] = 0;

        String parentStringId = id[0].toString();
        for (int i = 1; i < id.length; i++) {
            parentStringId = parentStringId + "." + id[i].toString();
        }
        System.out.println(parentStringId); // %%
        System.out.println(DBModelManager.getWorkpackageModel()
                .getWorkpackage(parentStringId).getId());
        thisWp.setParentID((DBModelManager.getWorkpackageModel()
                .getWorkpackage(parentStringId).getId()));
    }

    /**
     * Reloads thisWp from the Database.
     */
    public void reloadFromDB() {
        thisWp =
                DBModelManager.getWorkpackageModel().getWorkpackage(
                        getStringID());
    }
}
