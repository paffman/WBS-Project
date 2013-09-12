package wpReassign;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Klasse zum einfügen von Arbeitspaketen in einer Unterstruktur
 * 
 * 
 * @author Andre Paffenholz
 * @version 0.1 - 13.05.2011
 */


import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import baseline.AddBaseline;

import wpMitarbeiter.Mitarbeiter;
import wpOverview.WPOverview;
import wpShow.WPShow;
import jdbcConnection.SQLExecuter;

import login.User;


public class WPReassign {

	public WPReassignGUI gui;
	private String wpid;
	private TreePath treepath;
	private User user;
	private ArrayList<Mitarbeiter> alleMa = WPOverview.MitarbeiterListe;
	private WPOverview over;
			
	public WPReassign(TreePath Tpath, User user, WPOverview over){
		this.user = user;
		this.treepath = Tpath;
		this.over = over;
		setWpID();
		gui = new WPReassignGUI();
		gui.txfEinf.setText(wpid);
		new WPReassignButtonAction(this);
		fillCobMa();
	}
	
	
	/**
	 * prüft ob alle Eingabe ausgefüllt sind
	 * @return true/false, je nachdem ob Felder gefüllt oder nicht
	 */
	public boolean checkFieldsFilled(){
		if(gui.txfBac.getText().length() > 0 &&
				gui.txfDatum.getText().length() > 0 &&
				gui.txfName.getText().length() > 0){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Arbeitspaket ID aus aktuellen Baumpfad auslesen
	 * wird aufgerufen vom Konstruktor WPReassign
	 */
	private void setWpID(){
		String[] tmpID;
		if(treepath != null){
			tmpID = treepath.getLastPathComponent().toString().split("-");
			if(tmpID.length > 0){
				wpid = tmpID[0];
			}
		}
		else{
			wpid= "";
		}

	}
	
	
	/**
	 * Methode zum füllen der Combobox mit allen Mitarbeitern
	 */
	private void fillCobMa() {
		for(int j=0; j<alleMa.size(); j++){					
			gui.cobUser.addItem(alleMa.get(j).getLogin() + " | " + alleMa.get(j).getVorname() + " " + alleMa.get(j).getName());
		}
	}
	
	/**
	 * Methode zum einfügen des Arbeitspakets in der ersten Unteren Ebene (OAP) des in wpid angegebenen OAPs
	 * wird durch die ButtonAction beim Speichern aufgerufen
	 */
	protected void setRekPakete() {
		if(user.getProjLeiter()){
			//OAP Id auslesen
			String[] oapID = over.getOapId(wpid);
			String where =" WHERE ";
			String lvlx="";
			String[] neuid = new String[4];
			System.arraycopy(oapID, 0, neuid, 0, oapID.length);
			for(int j=0;j<neuid.length;j++){
				if(neuid[j] == null){
					neuid[j] = "0";
				}
			}
			neuid[neuid.length-1] = "1";
			
			//Level zusammensetzen
			if(oapID.length > 3) { //LVLx OAPs enthalten !
				for(int j=3;j<oapID.length;j++){
					lvlx = lvlx + oapID[j] + ".";
				}
				if(lvlx.endsWith("."))
					lvlx = lvlx.substring(0, lvlx.length()-1);
				
				//System.out.println("X: " + lvlx);
			}
			else{
				lvlx = WPOverview.generateXEbene();
			}
			
			neuid[3]=lvlx;
				
			
				
			//WHERE Klausel zusammensetzen
			switch(oapID.length){
			case 1:
				where += "LVL1ID = " + neuid[0] + " AND LVL2ID > 0  AND LVL3ID = 0";
				break;
				
			case 2:
				where += "LVL1ID = " + neuid[0] + " AND LVL2ID = " + neuid[1]  + " AND LVL3ID > 0 AND LVLxID = '" + lvlx + "'";
				break;
				
			case 3:
				where += "LVL1ID = " + neuid[0] + " AND LVL2ID = " + neuid[1] + " AND LVL3ID = " + neuid[2]  + " AND LVLxID = '" + lvlx + "'";
				break;
				
			}
			
			
			//Alle unterarbeitspakete auslesen, die selbst OAPs sind
			SQLExecuter sqlExecistOAP = new SQLExecuter();
			try {
				String query = "SELECT * FROM Arbeitspaket " + where;
				ResultSet alleUnterOAPs = sqlExecistOAP.executeQuery(query);
				String[] oldOap = new String[4];
				double bac = Double.parseDouble(gui.txfBac.getText());
				double bac_k =  bac*user.gettagessatz();
				String[] fullname = gui.cobUser.getSelectedItem().toString().split(" | ");
				String login = fullname[0].trim();
								
				while(alleUnterOAPs.next()){
					oldOap[0] = "" + alleUnterOAPs.getInt("LVL1ID");
					oldOap[1] = "" +  alleUnterOAPs.getInt("LVL2ID");
					oldOap[2] = "" +  alleUnterOAPs.getInt("LVL3ID");
					oldOap[3] =  alleUnterOAPs.getString("LVLxID");
					
					//Paket erstellen
					//System.out.println("GEFUNDEN: " + oldOap[0] + "." + oldOap[1] +  "." + oldOap[2] + "." + oldOap[3]);
					//erste .0. suchen und erHöhen
					for(int j=0;j<oldOap.length-1;j++){
						if(oldOap[j].equals("0")){
							oldOap[j] = "1";
							break;
						}
					}
					
					String[] tmp = oldOap[3].split("\\."); //lvlx
					for(int k=0;k<tmp.length;k++){
						if(tmp[k].equals("0")){
							tmp[k]= "1";
							break;
						}	
					}
					//Zusammensetzen
					for(int k=0;k<tmp.length;k++){
						if(k==0)
							oldOap[3] = tmp[k];
						else
							oldOap[3] += ("." + tmp[k]);
					}

					String curID = oldOap[0] + "." + oldOap[1] + "." + oldOap[2] + "." + oldOap[3];
					WPShow wps = new WPShow(curID, user, over);
					wps.gui.setVisible(false);
					if(!wps.checkWpId(curID))
						curID = wps.getWpId(curID);	
				
					wps.gui.dispose();
					createDBEntry(curID, login, bac, bac_k);
				
				}	
	

				alleUnterOAPs.close();
				JOptionPane.showMessageDialog(gui, "einfügen erfolgreich beendet.",null,
						JOptionPane.INFORMATION_MESSAGE);
				gui.dispose();
				//OAPs aktualisieren
				new AddBaseline("", over, false);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			sqlExecistOAP.closeConnection();
		}
	}
	
	
	
	private void createDBEntry(String id, String login, double bac, double bac_k){
		try{
			
			String[] tmp = id.split("\\.");
			String[] neuid = new String[4];
			neuid[0] = tmp[0];
			neuid[1] = tmp[1];
			neuid[2] = tmp[2];
			for(int i=3;i<tmp.length;i++){
				if(i==3)
					neuid[3] = tmp[i];
				else
					neuid[3] += ("." + tmp[i]);
			}
				
			
			SQLExecuter sqlExecAPs = new SQLExecuter();
			ResultSet rsAPs = sqlExecAPs.executeQuery("SELECT * FROM Arbeitspaket ");
			rsAPs.moveToInsertRow();
			
			java.sql.Date dte=null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			java.util.Date dt = formatter.parse(gui.txfDatum.getText());
			dte=new java.sql.Date(dt.getTime());
							
			rsAPs.updateInt("FID_Proj", 1);
			rsAPs.updateInt("LVL1ID", Integer.parseInt(neuid[0]));
			rsAPs.updateInt("LVL2ID",Integer.parseInt(neuid[1]));
			rsAPs.updateInt("LVL3ID", Integer.parseInt(neuid[2]));
			rsAPs.updateString("LVLxID", neuid[3].trim());
			rsAPs.updateString("Name", gui.txfName.getText());
			rsAPs.updateString("Beschreibung", "test");
			
			rsAPs.updateString("FID_Leiter", login);
			rsAPs.updateDouble("BAC", bac);
			rsAPs.updateDouble("AC", 0.0);
			rsAPs.updateDouble("EV", 0.0);
			rsAPs.updateDouble("ETC", bac);
			rsAPs.updateDouble("EAC", 0.0);
			rsAPs.updateDouble("CPI", 1.0);
			rsAPs.updateDouble("BAC_Kosten", bac_k);
			rsAPs.updateDouble("AC_Kosten", 0.0);
			rsAPs.updateDouble("ETC_Kosten", bac_k);
			rsAPs.updateDouble("WP_Tagessatz", 0.0);
								
			rsAPs.updateDate("Release", dte);
			rsAPs.updateBoolean("istOAP", true);
			rsAPs.updateBoolean("istInaktiv", false);
			rsAPs.insertRow();
			rsAPs.close();
			sqlExecAPs.closeConnection();	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
