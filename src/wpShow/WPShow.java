package wpShow;

/**
 * Studienprojekt:	WBS
 *
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 *
 * Funktionen der WPShowGUI
 * ändern, speichern, erstellen von Arbeitspaketen,
 * Daten werden auf die MDB geschrieben
 *
 * @author Samson von Graevenitz, Daniel Metzler
 * @version 0.1 - 30.11.2010
 */

import java.awt.Color;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;

import wpMitarbeiter.Mitarbeiter;
import wpOverview.WPOverview;
import jdbcConnection.SQLExecuter;
import login.User;

public class WPShow{

    public WPShowGUI gui;
    public WPShow dies;
    public WPOverview overgui;
    protected String wpID, wpname;
    protected double tagessatz;
    protected User usr;
    protected int lvl1ID, lvl2ID, lvl3ID, stat;
    protected StringBuilder lvlxID = new StringBuilder();
    final DecimalFormat decform = new DecimalFormat("#0.00");
    boolean neues = false;
    boolean boolIstOAP;
    protected ArrayList<Mitarbeiter> MitarbeiterListe = WPOverview.MitarbeiterListe;


    /**
     *  Konstruktor zum Anzeigen eines neuen Arbeitspaketes
     *  Wird von den TreeSelectionListenern aufgerufen und im Menü - Datei/neues Arbeitspaket
     */
    public WPShow(WPOverview overv, User user, String lastSelectedID) {
        dies = this;

        neues = true;
        this.usr = user;
        getistOAP();
        this.overgui = overv;
        this.gui = new WPShowGUI(true, dies);
        this.wpID = getWpId(lastSelectedID);
        setWPIDs();
        //Datum eintragen
        setUapRelease(lastSelectedID);

        new WPShowButtonAction(dies);
        this.dies = this;
        if(!boolIstOAP){
            this.tagessatz = getTagessatz();
        }
        getInitialValues();
        gui.txfNr.setEnabled(true);
        gui.txfNr.setText(wpID);
        //Elemente verbergen, die nicht benötigt werden
        gui.txfETC.setEnabled(false);
        gui.btnAddAufw.setVisible(false);
        gui.btnAddZust.setVisible(false);
        gui.cobaddZustaendige.setVisible(false);
        gui.btnRemoveZust.setVisible(false);
        gui.cobRemoveZustaendige.setVisible(false);
        gui.setTitle("Neues Arbeitspaket anlegen");
        filltable();
    }


    /**
     * Konstruktor WPShow()
     * initialisiert die WPShowGUI und den SQLExecuter,
     * und beeinhaltet die Listener der WPShowGUI durch die Methode addButtonAction()
     */
    public WPShow(String wpID, User usr, WPOverview overv){
        dies = this;
        this.usr = usr;
        this.wpID = wpID;
        setWPIDs();
        getistOAP();
        this.overgui = overv;
        this.gui = new WPShowGUI(false, dies);

        new WPShowButtonAction(dies);
        this.dies = this;
        if(!boolIstOAP){
            this.tagessatz = getTagessatz();
        }
        getInitialValues();
        getAndShowValues();
        gui.setTitle(Integer.toString(lvl1ID) + "." + Integer.toString(lvl2ID) + "." + Integer.toString(lvl3ID) + "." + lvlxID.toString() + " | " + wpname);
        filltable();
    }


    public void filltable(){
        Vector<Vector<String>> rows;
        rows = gui.aufwand;
        rows.clear();
        SQLExecuter sqlExecistOAP = new SQLExecuter();
        ResultSet aufwand;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        if(usr.getProjLeiter()){
            aufwand = sqlExecistOAP.executeQuery("SELECT * FROM Aufwand " +
                    "WHERE LVL1ID = " + lvl1ID +
                    "and LVL2ID = " + lvl2ID +
                    "and LVL3ID = " + lvl3ID +
                    "and LVLxID = '" + lvlxID + "';");
        }
        else{
            aufwand = sqlExecistOAP.executeQuery("SELECT * FROM Aufwand " +
                    "WHERE LVL1ID = " + lvl1ID +
                    "and LVL2ID = " + lvl2ID +
                    "and LVL3ID = " + lvl3ID +
                    "and LVLxID = '" + lvlxID + "'" +
                    "and FID_Ma = '" + usr.getLogin() + "';");
        }

        try {
            while(aufwand.next()){
                Vector<String> row = new Vector<String>();
                if(usr.getProjLeiter()){
                    row.addElement(aufwand.getString("FID_Ma"));
                }
                row.addElement(decform.format(aufwand.getDouble("Aufwand")));
                row.addElement(formatter.format(aufwand.getDate("Datum")));
                row.addElement(aufwand.getString("Beschreibung"));
                rows.add(row);
            }
            aufwand.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlExecistOAP.closeConnection();
    }

    /**
     * wird aufgerufen vom Konstrukor
     * überprüft ob das AP ein OAP ist
     */
    public void getistOAP(){
        SQLExecuter sqlExecistOAP = new SQLExecuter();
        try {
            ResultSet istOAP = sqlExecistOAP.executeQuery(getSQLWorkpackage());
            while(istOAP.next()){
                if(istOAP.getBoolean("istOAP")){
                    this.boolIstOAP = true;
                }
                else{
                    this.boolIstOAP = false;
                }
            }
            istOAP.close();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        sqlExecistOAP.closeConnection();
    }

    /**
     * Setzt die Datenelemente für die verschienenden Level-IDs
     * Wird von beiden Konstruktoren aufgerufen
     */
    public void setWPIDs(){
        int i = 0;
        StringTokenizer st = new StringTokenizer(wpID, ".");
        lvl1ID = Integer.parseInt(st.nextToken());
        lvl2ID = Integer.parseInt(st.nextToken());
        lvl3ID = Integer.parseInt(st.nextToken());
        while(st.hasMoreElements()){
            lvlxID.insert(i, st.nextToken(""));
            i++;
        }
        lvlxID.deleteCharAt(0);
    }


    /**
     * Berechnet den Mittelwert der Tagessätze der Mitarbeiter, die einem AP zugewiesen sind
     * Wird aufgerufen von:
     * - WPShow.addButtonAction()
     * - von beiden Konstruktoren der WPShow
     *
     * @return der gemittelte Tagessatz als Double
     */
    public double getTagessatz(){

        Double wptagessatz = 0.0;
        ArrayList<String> Zustaendige = new ArrayList<String>();

        SQLExecuter sqlExecAllZustaendige = new SQLExecuter();
        try {
            ResultSet rsAllZustaendige = sqlExecAllZustaendige.executeQuery(getSQLZustaendige());
            while(rsAllZustaendige.next()){
                Zustaendige.add(rsAllZustaendige.getString("FID_Ma"));
            }
            rsAllZustaendige.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            sqlExecAllZustaendige.closeConnection();
        }

        for(int j=0; j<Zustaendige.size(); j++){
            String login = Zustaendige.get(j);

            SQLExecuter sqlExecCurrentZustaendigWPs = new SQLExecuter();
            try {
                ResultSet rsCurrentZustaendigWPs = sqlExecCurrentZustaendigWPs.executeQuery("SELECT * FROM Mitarbeiter " +
                        "WHERE Login = '" + login + "';");
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
        wptagessatz /= Zustaendige.size();
        if (Double.isNaN(wptagessatz)){
            wptagessatz = 0.0;
        }

        return wptagessatz;
    }



    /**
     * Beim Erzeugen eines neuen Arbeitspakets füllt diese Methode die Auswahlfelder für die Benutzer
     * Wird von beiden Konstuktoren aufgerufen
     */
    protected void getInitialValues(){

        SQLExecuter sqlExecMitarbeiter = new SQLExecuter();
        try {
            ResultSet rsMitarbeiter = sqlExecMitarbeiter.executeQuery("SELECT Login, Name, Vorname FROM Mitarbeiter");
            while(rsMitarbeiter.next()){
                String login = rsMitarbeiter.getString("Login");
                String name = rsMitarbeiter.getString("Name");
                String vorname = rsMitarbeiter.getString("Vorname");
                if(!login.equals("Leiter"))
                    gui.cobMitarbeiter.addItem(login + " | " + vorname + " " + name);
            }
            rsMitarbeiter.close();
            if(usr.getProjLeiter()){

                if(!boolIstOAP){
                    initialRemove();
                    initialadd();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlExecMitarbeiter.closeConnection();


    }


    /**
     * Leert die Comboboxen für die zuständigen und fügt nur die zuständigen hinzu, die in der Paketzuweisung eingetragen sind
     * Wird aufgerufen von:
     * - get InitialValues()
     * - und von beiden ActionListeren: Mitarbeiter Hinzufügen und entfernen
     *
     */
    protected void initialRemove(){
        gui.cobRemoveZustaendige.removeAllItems();
        gui.cobRemoveZustaendige.addItem("");
        SQLExecuter sqlExecAPsZuweisung = new SQLExecuter();
        try {
            ResultSet rsAPsZuweisung = sqlExecAPsZuweisung.executeQuery("SELECT DISTINCT FID_MA,FID_Proj FROM Paketzuweisung WHERE FID_LVL1ID = " + lvl1ID + "AND FID_LVL2ID = " + lvl2ID + "AND FID_LVL3ID = " + lvl3ID + "AND FID_LVLxID = '" + lvlxID + "';");
            while(rsAPsZuweisung.next()){
                String name = rsAPsZuweisung.getString("FID_Ma");
                for(int i=0; i<MitarbeiterListe.size(); i++){
                    if(MitarbeiterListe.get(i).getLogin().equals(name)){
                        gui.cobRemoveZustaendige.addItem(MitarbeiterListe.get(i).getLogin() + " | " + MitarbeiterListe.get(i).getVorname() + " " + MitarbeiterListe.get(i).getName());
                        break;
                    }
                }
            }
            rsAPsZuweisung.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlExecAPsZuweisung.closeConnection();
    }

    /**
     * Leert die Comboboxen für die hinzuzufügenden zuständigen und fügt nur die zuständigen hinzu, die noch nicht in der Paketzuweisung eingetragen sind
     * Wird aufgerufen von:
     * - get InitialValues()
     * - und von beiden ActionListeren: Mitarbeiter Hinzufügen und entfernen
     *
     */
    protected void initialadd(){
        gui.cobaddZustaendige.removeAllItems();
        gui.cobaddZustaendige.addItem("");
        SQLExecuter sqlExecAPsZuweisung = new SQLExecuter();
        SQLExecuter sqlExecMitarbeiter = new SQLExecuter();
        try {
            ResultSet rsAPsZuweisung = sqlExecAPsZuweisung.executeQuery("SELECT DISTINCT FID_MA,FID_Proj FROM Paketzuweisung WHERE FID_LVL1ID = " + lvl1ID + "AND FID_LVL2ID = " + lvl2ID + "AND FID_LVL3ID = " + lvl3ID + "AND FID_LVLxID = '" + lvlxID + "';");
            ResultSet rsMitarbeiter = sqlExecMitarbeiter.executeQuery("SELECT * FROM Mitarbeiter");
            ArrayList<String> zugewiesen = new ArrayList<String>();
            while(rsAPsZuweisung.next()){
                String name = rsAPsZuweisung.getString("FID_Ma");
                zugewiesen.add(name);
            }
            ArrayList<String> Mitarbeiter = new ArrayList<String>();
            while(rsMitarbeiter.next()){
                String name = rsMitarbeiter.getString("Login");
                Mitarbeiter.add(name);
            }
            for(int i=0; i<Mitarbeiter.size(); i++){
                boolean darf = true;
                for(int j=0; j<zugewiesen.size(); j++){
                    if(zugewiesen.get(j).equals(Mitarbeiter.get(i))){
                        darf = false;
                        break;
                    }
                }
                if(darf){
                    for(int z=0; z<MitarbeiterListe.size(); z++){
                        if(MitarbeiterListe.get(z).getLogin().equals(Mitarbeiter.get(i))){
                            if(!MitarbeiterListe.get(z).getLogin().equals("Leiter"))
                                gui.cobaddZustaendige.addItem(MitarbeiterListe.get(z).getLogin() + " | " + MitarbeiterListe.get(z).getVorname() + " " + MitarbeiterListe.get(z).getName());
                        }
                    }
                }
            }
            rsAPsZuweisung.close();
            rsMitarbeiter.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            sqlExecMitarbeiter.closeConnection();
            sqlExecAPsZuweisung.closeConnection();
        }

    }



    /**
     * Aktualisiert die Daten in einem Arbeitspaket
     * Wird aufgerufen durch:
     * - Menü / Bearbeiten
     * - Arbeitspaket: Editieren
     * - setChanges()
     * - von Konstruktor der WPShow
     */
    protected void getAndShowValues() {

        String Zustaendige = "";
        SQLExecuter sqlExecWPValues = new SQLExecuter();
        try {
            ResultSet rsWPValues = sqlExecWPValues.executeQuery(getSQLWorkpackage());
            while (rsWPValues.next()) {

                double AC = rsWPValues.getDouble("AC");
                double ETC = rsWPValues.getDouble("ETC");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String str = dateFormat.format(rsWPValues.getDate("Release"));

                gui.chbIstOAP.setSelected(rsWPValues.getBoolean("istOAP"));
                if (rsWPValues.getBoolean("istOAP")) {
                    gui.btnAddAufw.setVisible(false);
                }

                if (getAufwand() > 0) {
                    gui.chbIstOAP.setEnabled(false);
                }

                gui.txfRelease.setText(str);
                double BAC = rsWPValues.getDouble("BAC");

                if(BAC>0 && ETC==0){
                    stat = 100;
                }
                else{
                    if(ETC>0 && AC>0){
                        stat= (int) (AC*100 /(AC+ETC));
                    }
                    else{
                        if(BAC==0 && AC==0 && ETC==0){
                            stat= 100;
                        }
                        else{
                            stat= 0;
                        }
                    }
                }
                double EV = rsWPValues.getDouble("EV");
                gui.txfEV.setText(decform.format(EV).replace(",", ".") + "€");
                wpname = rsWPValues.getString("Name");
                gui.txfName.setText(wpname);
                gui.txfWPTagessatz.setText(decform.format(rsWPValues.getDouble("WP_Tagessatz")).replace(",", ".")+ "€");
                gui.txfETC.setText(decform.format(ETC).replace(",", "."));
                gui.txfETCcost.setText(decform.format(rsWPValues.getDouble("ETC_Kosten")).replace(",", ".")	+ "€");
                double BACKosten = rsWPValues.getDouble("BAC_Kosten");
                gui.txfBACcost.setText(decform.format(BACKosten).replace(",", ".") + "€");
                gui.txfACcost.setText(decform.format(rsWPValues.getDouble("AC_Kosten")).replace(",", ".") + "€");
                gui.txfEAC.setText(decform.format(rsWPValues.getDouble("EAC")).replace(",", ".") + "€");
                gui.chbInaktiv.setSelected(rsWPValues.getBoolean("istInaktiv"));
                gui.txfName.setName(rsWPValues.getString("Name"));
                gui.txfSpec.setText(rsWPValues.getString("Beschreibung"));

                SQLExecuter sqlExecZustaendige = new SQLExecuter();
                ResultSet rsZustaendige = sqlExecZustaendige.executeQuery(getSQLZustaendige());
                while (rsZustaendige.next()) {
                    Zustaendige += rsZustaendige.getString("FID_Ma") + ", ";
                }
                rsZustaendige.close();
                sqlExecZustaendige.closeConnection();


                String leiter = rsWPValues.getString("FID_Leiter");
                for(int i=0; i<gui.cobMitarbeiter.getItemCount(); i++){
                    String strTmp = gui.cobMitarbeiter.getItemAt(i).toString();
                    int tmp = strTmp.indexOf("|");
                    String neu = strTmp.substring(0,tmp-1);
                    if(leiter.equals(neu)){
                        gui.cobMitarbeiter.setSelectedItem(gui.cobMitarbeiter.getItemAt(i).toString());
                    }
                }


                gui.txfAC.setText(decform.format(AC).replace(",", "."));
                gui.txfNr.setText(wpID);
                gui.txfZustaendige.setText(Zustaendige);
                gui.txfBAC.setText(decform.format(BAC).replace(",", "."));
                gui.txfCPI.setText(decform.format(rsWPValues.getDouble("CPI")).replace(",", "."));

                double dcpi = rsWPValues.getDouble("CPI");
                if(AC != 0.){
                    if (dcpi < 0.97) {
                        gui.txfCPI.setDisabledTextColor(Color.black);
                        gui.txfCPI.setBackground(Color.YELLOW);
                        if (dcpi < 0.94) {
                            gui.txfCPI.setDisabledTextColor(Color.black);
                            gui.txfCPI.setBackground(Color.RED);
                        }
                        if (dcpi < 0.6) {
                            gui.txfCPI.setDisabledTextColor(Color.white);
                            gui.txfCPI.setBackground(new Color(80, 00, 00));
                        }

                    } else {
                        if (dcpi > 1.03) {
                            gui.txfCPI.setDisabledTextColor(Color.white);
                            gui.txfCPI.setBackground(new Color(00, 80, 00));
                        } else {
                            gui.txfCPI.setDisabledTextColor(Color.black);
                            gui.txfCPI.setBackground(Color.GREEN);
                        }
                    }
                }
                else{
                    gui.txfCPI.setDisabledTextColor(Color.black);
                    gui.txfCPI.setBackground(Color.WHITE);
                }



                gui.pgbalken.setValue((int) stat);
            }
            rsWPValues.close();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sqlExecWPValues.closeConnection();
        }

    }


    /**
     * Berechnet den Aufwand pro Arbeitspaket
     * Wird aufgerufen durch:
     *  - addWp()
     *  - getAndShowValues()
     *  - setChanges()
     *
     * @return Aufwand des Arbeitspakets als Double
     * @throws SQLException Falls die Abfrage fehlschägt
     */
    public Double getAufwand() throws SQLException{
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
     * Liefert den SQL String, um die zuständigen auslesen zu können
     * Wird aufgerufen durch:
     *  - ActionListener: Zuständige Hinzufügen
     *  - getAndShowValues()
     *  - getTagessatz()
     * @return SQL-String um die zuständigen aus einem AP auslesen zu können
     */
    public String getSQLZustaendige(){
        return "SELECT * FROM Paketzuweisung " +
                "WHERE FID_LVL1ID = " + lvl1ID + " " +
                "AND FID_LVL2ID = " + lvl2ID + " " +
                "AND FID_LVL3ID = " + lvl3ID + " " +
                "AND FID_LVLxID = '" + lvlxID + "';";
    }



    /**
     * Liefert den SQL String, um die zuständigen auslesen zu können
     * Wird aufgerufen durch:
     *  - ActionListener: Zuständige Hinzufügen
     *  - getAndShowValues()
     *  - setChanges()
     * @return SQL-String um ein AP auslesen zu können
     */
    public String getSQLWorkpackage(){
        return "SELECT * FROM Arbeitspaket " +
                "WHERE FID_PROJ = 1" +
                "AND LVL1ID = " + lvl1ID + " " +
                "AND LVL2ID = " + lvl2ID + " " +
                "AND LVL3ID = " + lvl3ID + " " +
                "AND LVLxID = '" + lvlxID + "';";
    }



    /**
     * Ermittelt den höchsten Wert an einer bestimmten Stelle in einem Array aus String-ID
     * Wird von der Methode getWpId(String) aufgerufen
     * @param ids ArrayList mit verschiedenen String ID Nummern
     * @param pos Position in den Strings die über die ganze Arrayliste verglichen wird
     * @return der numerisch höchste Wert an der vorgegebenen Stelle in den String der Arraylist
     */
    protected int getHighest(ArrayList<String> ids, int pos){
        int highest=-1;

        for(String id:ids){
            String[] numbers=id.split("\\.");

            int temp=Integer.valueOf(numbers[pos]);

            if(temp>highest){
                highest=temp;
            }
        }

        return highest;
    }


    /**
     * Liefert eine neue eindeutige ID für ein Arbeitspaket
     * @param currentID aktuelle ID des markieren Arbeitspaketes im Baum -  bzw. leeres Arbeitspaket bei Aufruf über das Menü
     * @return die neue -eindeutige- Nummer des neuen Arbeitspaketes
     */
    public String getWpId(String currentID){

        int lvl1id=0, lvl2id=0, lvl3id=0;
        String lvlxid="";
        String newId="";
        int index0 = currentID.indexOf("0");
        if(index0 > 0) {
            //die einzelnen ebenen aufsprlitten, für die selects
            String[] id = currentID.split("\\.");
            //stelle suchen, die inkrementiert werden muss
            newId = currentID.substring(0,index0-1);
            //zweiten array anlegen, der alle Stellen
            String nId[] = newId.split("\\.");
            currentID = getNewWpIdArray(id, nId.length);

            return currentID;
        }
        else{
            String[] id = currentID.split("\\.");
            currentID = getNewWpIdArray(id, 0);
            return currentID;
        }
    }


    /**
     * Liefert die AC Kosten in Euro für ein AP
     * Wird aufgerufen von:
     * 	- addWp()
     * 	- setChanges()
     *
     * @return die AC Kosten in Euro für ein AP
     * @throws SQLException Falls Abfrage fehlschägt
     */
    public double getACKosten() throws SQLException{
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


    public boolean istOAPsetChanges(){
        boolean changesSet = false;
        SQLExecuter sqlExecWPValues = new SQLExecuter();
        ResultSet rsWPValues = sqlExecWPValues.executeQuery(getSQLWorkpackage());

        //Der AP-Name darf nicht leer sein und es muss ein Mitarbeiter angelegt sein
        if(gui.txfName.getText().length() == 0)
            return false;


        try {
            rsWPValues.first();
            java.sql.Date dte=null;
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date dt = formatter.parse(gui.txfRelease.getText());
            dte=new java.sql.Date(dt.getTime());
            rsWPValues.updateString("Name", gui.txfName.getText());

            String strTmp = gui.cobMitarbeiter.getSelectedItem().toString();
            int tmp = strTmp.indexOf("|");
            String neu = strTmp.substring(0,tmp-1);
            rsWPValues.updateString("FID_Leiter", neu);
            rsWPValues.updateString("Beschreibung", gui.txfSpec.getText());
            rsWPValues.updateBoolean("istInaktiv", gui.chbInaktiv.isSelected());
            rsWPValues.updateDate("Release", dte);
            rsWPValues.updateRow();
            rsWPValues.close();
            WPShowGUI.setStatusbar("Änderungen übernommen");
            changesSet = true;

            //in der Overgui setUapsInaktiv() aufrufen, da dort alle wps als arrayliste vorhanden sind und es eine Funktion getOapId gibt
            overgui.setUapsInaktiv(gui.txfNr.getText(), gui.chbInaktiv.isSelected());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return changesSet;
    }

    /**
     * übernimmt die Änderungen, wenn ein AP editiert wird und speichert diese in der Datenbank
     * Wird aufgerufen durch:
     *  - ActionListern in AddAufwand.gui.btnEdit
     *  - addButtonAction()
     * @return Gibt an, ob die Änderungen erfolgreich gespeichert wurden (true = ja, false = nein)
     */
    public boolean setChanges(){
        boolean changesSet = false;
        SQLExecuter sqlExecWPValues = new SQLExecuter();
        ResultSet rsWPValues = sqlExecWPValues.executeQuery(getSQLWorkpackage());

        //Der AP-Name darf nicht leer sein
        if(gui.txfName.getText().length() == 0 || gui.txfETC.getText().length() == 0 ||  gui.txfBAC.getText().length() == 0)
            return false;

        try {
            rsWPValues.first();
            Double BAC = Double.parseDouble(gui.txfBAC.getText());
            Double BACKosten = BAC*tagessatz;
            Double AC = getAufwand();
            Double ACKosten = getACKosten();
            Double ETC = (Double.parseDouble(gui.txfETC.getText()));
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

            double EV = BAC*stat/100*tagessatz;

            Double ETCKosten = ( ETC * tagessatz);
            Double EAC;
            if(BACKosten>0){
                EAC = ACKosten + ETCKosten;
            }
            else{
                EAC = 0.0;
            }


            if(usr.getProjLeiter()){
                java.sql.Date dte=null;
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date dt = formatter.parse(gui.txfRelease.getText());
                dte=new java.sql.Date(dt.getTime());
                rsWPValues.updateString("Name", gui.txfName.getText());

                String strTmp = gui.cobMitarbeiter.getSelectedItem().toString();
                int tmp = strTmp.indexOf("|");
                String neu = strTmp.substring(0,tmp-1);
                rsWPValues.updateString("FID_Leiter", neu);
                rsWPValues.updateDate("Release", dte);
                rsWPValues.updateDouble("BAC", BAC);
                rsWPValues.updateBoolean("istInaktiv", gui.chbInaktiv.isSelected());
                rsWPValues.updateBoolean("istOAP", gui.chbIstOAP.isSelected());
            }
            rsWPValues.updateString("Beschreibung", gui.txfSpec.getText());
            rsWPValues.updateDouble("AC", AC);
            rsWPValues.updateDouble("WP_Tagessatz", tagessatz);
            Double cpi;
            if(ACKosten+ETCKosten == 0.){
                if(BACKosten==0.){
                    cpi = 1.0;
                }
                else{
                    cpi = 10.0;
                }
            }
            else{
                cpi = BACKosten/(ACKosten+ETCKosten);
                if(cpi>10.0)
                    cpi=10.0;
            }

            rsWPValues.updateDouble("CPI", cpi);
            rsWPValues.updateDouble("ETC", ETC);
            rsWPValues.updateDouble("AC_Kosten", ACKosten);
            rsWPValues.updateDouble("ETC_Kosten", ETCKosten);
            rsWPValues.updateDouble("EV", EV);
            rsWPValues.updateDouble("BAC_Kosten", BACKosten);
            rsWPValues.updateDouble("EAC", EAC);
            rsWPValues.updateRow();
            rsWPValues.close();

            WPShowGUI.setStatusbar("Änderungen wurden übernommen");
            changesSet = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(gui, "Änderung konnte nicht übernommen werden!");
            e.printStackTrace();

        } catch (ParseException e) {
            e.printStackTrace();
        } finally{
            sqlExecWPValues.closeConnection();
            getAndShowValues();
        }
        return changesSet;
    }


    private String getNewWpIdArray(String[] curIdArray, int positionToInc){
        //array mit der ganzen AP Nummer entsprechend anpassen
        int lastNum = Integer.parseInt(curIdArray[positionToInc]);
        curIdArray[positionToInc] = lastNum + 1 + "";

        String currentID="";
        for(int i=0;i<curIdArray.length;i++){
            if(currentID.equals("")){
                currentID = curIdArray[i];
            }
            else {
                currentID += "." + curIdArray[i];
            }
        }

        //prüfen, ob neue Id noch frei ist
        if(!checkWpId(currentID)){
            return getNewWpIdArray(curIdArray, positionToInc);
        }

        return currentID;
    }

    /**
     * Liefert die Anzazhl der Ebenen zurück, für den String der übergeben wurde
     * Wird in der Methode addWp() aufgerufen
     * @param input AP-ID (durch Punkte getrennter String)
     * @return Anzahl der Ebenen
     */
    private int countEbenen(String input){
        int counter = 0;
        for(char c: input.toCharArray()){
            if(c=='.')
                counter++;
        }
        return counter;
    }

    /**
     * wird aufgerufen von der Methode wpadd()
     * überprüft, dass die APNummer richtig benannt ist.
     * überprüft, dass keine 2 Punkte hintereinander gesetzt sind
     * @param input lvlx String
     * @return istkorrekt
     */
    private boolean lvlxPruefen(String input){
        char[] g = input.toCharArray();
        if(g[0]=='.'){
            return false;
        }
        for(int i=0; i<input.toCharArray().length; i++){
            if(g[i]=='.'){
                if(g[i+1]=='.'){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * fügt ein Neues Arbeitspaket in die Datenbank ein
     * Wird vom ActionListener in der WPShowGUI.btnNewWP aufgerufen
     * @return Gibt an, ob die Eingabe in die Datenbank erfolgreich war (true = ja, false = nein)
     */
    public boolean addWp(){

        try{
            int lvl1, lvl2, lvl3;
            String lvlx ="";
            String[] tmp;
            String[] tmpDate;
            int month, day;


            if(gui.txfName.getText().equals(""))
                return false;


            if(!gui.txfRelease.getText().startsWith(".")){
                tmpDate = gui.txfRelease.getText().split("\\.");
                day = Integer.parseInt(tmpDate[0]);
                month = Integer.parseInt(tmpDate[1]);
                if(day < 1 || day > 31 || month < 1 || month > 12)
                    return false;
            }
            else
                return false;

            //Prüfen, ob durchgängige Nummerierung gegeben ist
            boolean numOK = isNumSequential(gui.txfNr.getText());
            if(numOK) {
                //Prüfen, ob ID bereits vorhanden ist
                boolean idEmpty = checkWpId(gui.txfNr.getText());
                if(idEmpty){
                    //ID Zusammensetzen
                    tmp = gui.txfNr.getText().split("\\.");
                    lvl1 = Integer.parseInt(tmp[0]);
                    lvl2 = Integer.parseInt(tmp[1]);
                    lvl3 = Integer.parseInt(tmp[2]);
                    for(int i=3;i<tmp.length;i++){
                        if(lvlx.equals("")){
                            lvlx = tmp[i];
                        }
                        else {
                            lvlx += "." + tmp[i];
                        }
                    }

                    if(countEbenen(lvlx)==(WPOverview.ebenen-4) && lvlxPruefen(lvlx)){
                        String strTmp = gui.cobMitarbeiter.getSelectedItem().toString();
                        int temp = strTmp.indexOf("|");
                        String leiter = strTmp.substring(0,temp-1);

                        SQLExecuter sqlExecMitarbeiter = new SQLExecuter();
                        ResultSet rsMitarbeiter = sqlExecMitarbeiter.executeQuery("SELECT * FROM Mitarbeiter WHERE Login = '" + leiter + "';");
                        while (rsMitarbeiter.next()){
                            tagessatz = rsMitarbeiter.getDouble("Tagessatz");
                        }
                        rsMitarbeiter.close();
                        sqlExecMitarbeiter.closeConnection();
                        Double BAC = Double.parseDouble(gui.txfBAC.getText());
                        Double BACKosten = BAC*tagessatz;
                        Double AC = getAufwand();
                        Double ACKosten = getACKosten();
                        Double ETC = (BAC);
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
                        double EV = BAC*stat/100*tagessatz;

                        System.out.println("EV = " + EV);
                        System.out.println("BACKosten = " + BACKosten);
                        System.out.println("Status = " + stat);

                        Double EAC;
                        if(BACKosten>0){
                            EAC = (AC + ETC) * tagessatz;
                        }
                        else{
                            EAC = 0.0;
                        }
                        Double ETCKosten = ( ETC * tagessatz);



                        Double cpi;
                        if(ACKosten+ETCKosten == 0.){
                            if(BACKosten==0.){
                                cpi = 1.0;
                            }
                            else{
                                cpi = 10.0;
                            }

                        }
                        else{
                            cpi = BACKosten/(ACKosten+ETCKosten);
                            if(cpi>10.0)
                                cpi=10.0;
                        }

                        SQLExecuter sqlExecAPs = new SQLExecuter();
                        ResultSet rsAPs = sqlExecAPs.executeQuery("SELECT * FROM Arbeitspaket ");
                        rsAPs.moveToInsertRow();

                        java.sql.Date dte=null;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                        java.util.Date dt = formatter.parse(gui.txfRelease.getText());
                        dte=new java.sql.Date(dt.getTime());

                        rsAPs.updateInt("FID_Proj", 1);
                        rsAPs.updateInt("LVL1ID", lvl1);
                        rsAPs.updateInt("LVL2ID", lvl2);
                        rsAPs.updateInt("LVL3ID", lvl3);
                        rsAPs.updateString("LVLxID", lvlx);
                        rsAPs.updateString("Name", gui.txfName.getText());
                        rsAPs.updateString("Beschreibung", gui.txfSpec.getText());

                        rsAPs.updateString("FID_Leiter", leiter);
                        rsAPs.updateDouble("BAC", BAC);
                        rsAPs.updateDouble("AC", AC);
                        rsAPs.updateDouble("EV", EV);
                        rsAPs.updateDouble("ETC", ETC);
                        rsAPs.updateDouble("EAC", EAC);
                        rsAPs.updateDouble("CPI", cpi);
                        rsAPs.updateDouble("BAC_Kosten", BACKosten);
                        rsAPs.updateDouble("AC_Kosten", ACKosten);
                        rsAPs.updateDouble("ETC_Kosten", ETCKosten);
                        if(gui.chbIstOAP.isSelected()){
                            rsAPs.updateDouble("WP_Tagessatz", 0);
                        }
                        else{
                            rsAPs.updateDouble("WP_Tagessatz", tagessatz);
                        }

                        rsAPs.updateDate("Release", dte);
                        rsAPs.updateBoolean("istOAP", gui.chbIstOAP.isSelected());
                        rsAPs.updateBoolean("istInaktiv", gui.chbInaktiv.isSelected());
                        rsAPs.insertRow();
                        rsAPs.close();
                        sqlExecAPs.closeConnection();

                        //beim Anlegen eines neuen APs wird direkt der Mitarbeiter in
                        //die Paketzuweisungstabelle übernommen.
                        SQLExecuter sqlExecAPZuweisung = new SQLExecuter();
                        ResultSet rsAPZuweisung = sqlExecAPZuweisung.executeQuery("SELECT * FROM Paketzuweisung");
                        rsAPZuweisung.moveToInsertRow();
                        rsAPZuweisung.updateInt("FID_Proj", 1);
                        rsAPZuweisung.updateInt("FID_LVL1ID", lvl1);
                        rsAPZuweisung.updateInt("FID_LVL2ID", lvl2);
                        rsAPZuweisung.updateInt("FID_LVL3ID", lvl3);
                        rsAPZuweisung.updateString("FID_LVLxID", lvlx);

                        rsAPZuweisung.updateString("FID_Ma", leiter);
                        rsAPZuweisung.insertRow();
                        rsAPZuweisung.close();
                        sqlExecAPZuweisung.closeConnection();

                        return true;
                    }
                    else{
                        JOptionPane.showMessageDialog(gui, "Bitte geben Sie die richtige Anzahl an Ebenen ein \n Ebenenanzahl = " + WPOverview.ebenen + "!");
                        return false;
                    }


                }
                else{
                    JOptionPane.showMessageDialog(gui, "Es existiert bereits ein Arbeitspaket mit dieser ID. Bitte geben Sie eine neue ID ein!");
                    return false;
                }
            }
            else {
                JOptionPane.showMessageDialog(gui, "Die Arbeitspaketnummern müssen fortlaufend sein. Bitte geben Sie eine korrekte ID ein!");
                return false;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }


    /**
     * prüft, ob ein AP mit der ID bereits in der Datenbank existiert
     * Wird von addWp() und aus WPReassign.setRekPakete() aufgerufen
     * @param newId AP-ID des neu zu erstellenden Arbeitspakets
     * @return true = Paket-ID ist noch frei, false = ID ist bereits in der Datenbank vorhanden
     */
    public boolean checkWpId(String newId){
        SQLExecuter sqlExecAP = new SQLExecuter();
        boolean checked = false;
        try{
            String tmp[] = newId.split("\\.");
            int lvl1 = Integer.parseInt(tmp[0]); // LVL1
            int lvl2 = Integer.parseInt(tmp[1]); // LVL2
            int lvl3 = Integer.parseInt(tmp[2]); // LVL3
            String lvlx ="";
            for(int i=3;i<tmp.length;i++){		 // LVLx zusammensetzen
                if(lvlx == "")
                    lvlx = tmp[i];
                else
                    lvlx += "." + tmp[i];
            }

            ResultSet rsAP = sqlExecAP.executeQuery("SELECT * FROM Arbeitspaket WHERE FID_Proj = 1 AND LVL1ID = " + lvl1 + " AND LVL2ID = " + lvl2 + " AND LVL3ID = " + lvl3 + " AND LVLxID = '" + lvlx + "';");
            if(rsAP.next()){
                checked = false;

            }else{
                checked = true;
            }
            rsAP.close();
        }

        catch(Exception ex){
            ex.printStackTrace();
        } finally{
            sqlExecAP.closeConnection();
        }
        return checked;
    }


    /**
     * Liefert das Release Datum des OAPs zu neuen Paket und dieser Wert wird als Default eingetragen
     * @param currentID makrierte Paket ID
     */
    private void setUapRelease(String currentID){
        if(!currentID.startsWith("0")){
            String[] oap = overgui.getOapId(currentID);
            String whereClause ="";

            switch(oap.length){
                case 1:
                    whereClause= " where LVL1ID = " + Integer.parseInt(oap[0]) + " AND istOAP = true";
                    break;
                case 2:
                    whereClause= " where LVL1ID = " + Integer.parseInt(oap[0]) + " AND LVL2ID = " + Integer.parseInt(oap[1]) + " AND istOAP = true";
                    break;
                case 3:
                    whereClause= " where LVL1ID = " + Integer.parseInt(oap[0]) + " AND LVL2ID = " + Integer.parseInt(oap[1]) +
                            " AND LVL3ID = " + Integer.parseInt(oap[2]) + " AND istOAP = true";
                    break;
            }

            SQLExecuter sqlExecAufwand = new SQLExecuter();

            try{
                ResultSet rs = sqlExecAufwand.executeQuery("SELECT * FROM Arbeitspaket "+ whereClause + ";");
                if(rs.next()){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    String str = dateFormat.format(rs.getDate("Release"));
                    gui.txfRelease.setText(str);
                }
                rs.close();
                sqlExecAufwand.closeConnection();
            }
            catch(Exception ex){
                sqlExecAufwand.closeConnection();
                ex.printStackTrace();
            }
        }
    }



    /**
     *  Prüft, ob die Arbeitspakete sequentuell angelegt werden, oder ob ggf. eine falsche AP Nummer eingegeben wurde
     * @param newId die neue ID des zu erstellenden APs
     * @return boolean ist die AP-Nummer korrekt?
     */
    private boolean isNumSequential(String newId){
        SQLExecuter sqlExecAP = new SQLExecuter();
        boolean checked = false;
        try{
            String wpTmpID = newId.toString().split(" - ")[0]; // Nur AP Nummer ohne Namen
            String searchID;
            String[] tmpArr;

            int index0 = wpTmpID.indexOf("0");
            if(index0 < 0) {
                searchID = wpTmpID.substring(0,wpTmpID.length());
            } else {
                searchID = wpTmpID.substring(0, index0);
            }

            tmpArr = searchID.split("\\.");

            int[] wpID = new int[tmpArr.length];
            int lvl1, lvl2, lvl3;
            String lvlx="";

            for(int j=0;j<tmpArr.length;j++)
                wpID[j] = Integer.parseInt(tmpArr[j].trim());

            //Vorgänger AP bestimmen
            switch(wpID.length){
                case 1:
                    lvl1 = wpID[0]-1;
                    lvl2 = 0;
                    lvl3 = 0;
                    lvlx = WPOverview.generateXEbene();
                    break;

                case 2:
                    lvl1 = wpID[0];
                    lvl2 = wpID[1]-1;
                    lvl3 =0;
                    lvlx = WPOverview.generateXEbene();
                    break;

                case 3:
                    lvl1 = wpID[0];
                    lvl2 = wpID[1];
                    lvl3 = wpID[2]-1;
                    lvlx = WPOverview.generateXEbene();
                    break;

                //X-Ebene an entsprechender Stelle um 1 dekrementieren
                default:
                    lvl1 = wpID[0];
                    lvl2 = wpID[1];
                    lvl3 = wpID[2];
                    int j=0;            //counter, um lvlx korrekt zusammen zu setzen

                    //vorgänger entsprechend ekrementieren
                    wpID[wpID.length-1]--;

                    //lvlx zusammensetzen
                    for(int i=3;i<wpID.length;i++){

                        //prüfen, ob noch ein "." angehängt werden muss oder nicht
                        if(lvlx.equals("")){
                            lvlx += ("" + wpID[i]);
                        }
                        else{
                            lvlx += ("." + wpID[i]);
                        }
                    }

                    //lvlx länge anpassen
                    String lvlXOrg = WPOverview.generateXEbene();
                    int lvlxLen = lvlXOrg.split("\\.").length;
                    for(int i= wpID.length-3; i<lvlxLen;i++){   // wpID.length-3 = Anzahl der Stellen ohne 0 in Lvlx
                        lvlx += ".0";
                    }

                    break;
            }

            ResultSet rsAP = sqlExecAP.executeQuery("SELECT * FROM Arbeitspaket WHERE FID_Proj = 1 AND LVL1ID=" + lvl1 + " AND LVL2ID=" + lvl2 + " AND LVL3ID=" + lvl3 + " AND LVLxID='" + lvlx + "';");
            if(rsAP.next()){
                checked = true;

            }else{
                checked = false;
            }
            rsAP.close();
        }

        catch(Exception ex){
            ex.printStackTrace();
        } finally{
            sqlExecAP.closeConnection();
        }
        return checked;
    }
}