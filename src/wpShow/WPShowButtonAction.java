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
 * Funktionalität der WPShowGUI
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.1 - 30.11.2010
 */

import globals.InfoBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import jdbcConnection.SQLExecuter;
import wpAddAufwand.AddAufwand;

public class WPShowButtonAction {

	private WPShow wpShow;
	
	public WPShowButtonAction(WPShow wpShow) {
		this.wpShow = wpShow;
		addButtonAction();
	}

	/**
	 * Belegt die Komponenten in der GUI mit Funktionaltät
	 * Wird von beiden Konstruktoren aufgerufen
	 */
	public void addButtonAction(){
		//Felder und Buttons für Projektleiter
		if(wpShow.usr.getProjLeiter()){
			wpShow.gui.txfName.setEnabled(true);
			wpShow.gui.cobMitarbeiter.setEnabled(true);
			wpShow.gui.txfZustaendige.setVisible(true);
			wpShow.gui.lblZustaendige.setVisible(true);		
			wpShow.gui.btnAddZust.setVisible(true);
			wpShow.gui.btnRemoveZust.setVisible(true);
			wpShow.gui.cobaddZustaendige.setVisible(true);
			wpShow.gui.cobRemoveZustaendige.setVisible(true);			
			wpShow.gui.lblInaktiv.setVisible(true);
			wpShow.gui.chbInaktiv.setVisible(true);
			wpShow.gui.txfRelease.setEnabled(true);
			wpShow.gui.txfBAC.setEnabled(true);
			wpShow.gui.chbInaktiv.setEnabled(true);
			wpShow.gui.btnAddZust.setEnabled(true);
			wpShow.gui.chbIstOAP.setVisible(true);
			wpShow.gui.lblIstOAP.setVisible(true);
		}
		
		//Felder wenn OAP
		if(wpShow.boolIstOAP){
			wpShow.gui.btnAddZust.setVisible(false);
			wpShow.gui.btnRemoveZust.setVisible(false);
			wpShow.gui.cobaddZustaendige.setVisible(false);
			wpShow.gui.cobRemoveZustaendige.setVisible(false);		
			wpShow.gui.txfWPTagessatz.setVisible(false);
			wpShow.gui.lblWPTagessatz.setVisible(false);
			wpShow.gui.chbIstOAP.setEnabled(false);
		}
		
		//Aufwand Hinzufügen
		wpShow.gui.btnAddAufw.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {				
				new AddAufwand(wpShow.wpID, wpShow.usr, wpShow, wpShow.wpname);				
			}
		});
		
	
		
		//Button Action vergeben entsprechend dem Status des Pakets: Neu oder Vorhanden
		if(wpShow.gui.newWP){
			wpShow.gui.btnNewWP.addActionListener(new ActionListener() {			
				public void actionPerformed(ActionEvent arg0) {
					if(wpShow.gui.cobMitarbeiter.getItemCount() == 0){
						JOptionPane.showMessageDialog(wpShow.gui, "Bitte legen Sie erst Mitarbeiter an!");
					}else {
						//Paket einfügen
						if(wpShow.addWp()){	
							WPShowGUI.setStatusbar("Arbeitspaket wurde hinzugefügt");
							wpShow.overgui.aktualisieren();
							
							wpShow.gui.dispose();
						}
						else
							JOptionPane.showMessageDialog(wpShow.gui, "Bitte überprüfen Sie Ihre Eingaben!");
					}
				}
			});
		}
		else {
			//Editiertes Arbeitspaket: Änderungen übernehmen und alles aktualisieren
			wpShow.gui.btnEdit.addActionListener(new ActionListener() {			
				public void actionPerformed(ActionEvent arg0) {
					if(wpShow.gui.cobMitarbeiter.getItemCount() == 0){
						JOptionPane.showMessageDialog(wpShow.gui, "Bitte legen Sie erst Mitarbeiter an!");
					}else {
						if(wpShow.boolIstOAP){
							if(wpShow.istOAPsetChanges()){
								//getAndShowValues();
								wpShow.overgui.aktualisieren();
							}
							else
								JOptionPane.showMessageDialog(wpShow.gui, "Bitte überprüfen Sie Ihre Eingaben!");
						}
						else{
							if(wpShow.setChanges()){				
								//getAndShowValues();
								wpShow.overgui.aktualisieren();
							}
							else
								JOptionPane.showMessageDialog(wpShow.gui, "Bitte überprüfen Sie Ihre Eingaben!");
						}
					}
				}
			});			
		}
		
		
		
		wpShow.gui.btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wpShow.gui.dispose();
			}
		});
		
		//Drop-Down-Listen für zuständigen Hinzufügen und
		//setzt die neue Paketzuweisung in der Datenbank
		wpShow.gui.btnAddZust.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Zuständige Hinzufügen
				
				if(!wpShow.gui.cobaddZustaendige.getSelectedItem().toString().equals("")){
					String selZust = wpShow.gui.cobaddZustaendige.getSelectedItem().toString();
				    int pos = selZust.indexOf("|");
				    String zust = selZust.substring(0,pos-1);
					SQLExecuter sqlExecWPValues = new SQLExecuter(); 
					ResultSet rsWPValues = sqlExecWPValues.executeQuery(wpShow.getSQLWorkpackage());
					
					SQLExecuter sqlExecWPZustaendige = new SQLExecuter();
					ResultSet rsZustaendige = sqlExecWPZustaendige.executeQuery(wpShow.getSQLZustaendige());
					int FID_Proj=0, FID_LVL1ID=0, FID_LVL2ID=0, FID_LVL3ID=0;
					String FID_LVLxID = "";
					boolean vorhanden = false;
					
									
					try {
						while(rsWPValues.next()){
							FID_Proj = rsWPValues.getInt("FID_Proj");
							FID_LVL1ID = rsWPValues.getInt("LVL1ID");
							FID_LVL2ID = rsWPValues.getInt("LVL2ID");
							FID_LVL3ID = rsWPValues.getInt("LVL3ID");
							FID_LVLxID = rsWPValues.getString("LVLxID");
						}
						while(rsZustaendige.next()){
							
							if(zust.equals(rsZustaendige.getString("FID_Ma")))
								vorhanden=true;	
						}
						rsZustaendige.close();
						rsWPValues.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally{
						sqlExecWPZustaendige.closeConnection();
						sqlExecWPValues.closeConnection();
					}
					if(!vorhanden){					
						SQLExecuter sqlExecPaketzuweisung = new SQLExecuter();
						try {
							ResultSet rsPaketzuweisung = sqlExecPaketzuweisung.executeQuery("SELECT * FROM Paketzuweisung ");
							rsPaketzuweisung.moveToInsertRow();
							rsPaketzuweisung.updateInt("FID_Proj", FID_Proj);
							rsPaketzuweisung.updateInt("FID_LVL1ID", FID_LVL1ID);
							rsPaketzuweisung.updateInt("FID_LVL2ID", FID_LVL2ID);
							rsPaketzuweisung.updateInt("FID_LVL3ID", FID_LVL3ID);
							rsPaketzuweisung.updateString("FID_LVLxID", FID_LVLxID);
							rsPaketzuweisung.updateString("FID_Ma", zust);
							rsPaketzuweisung.insertRow();
							rsPaketzuweisung.close();
							WPShowGUI.setStatusbar("Zuständiger wurde hinzugefügt!");
							wpShow.tagessatz= wpShow.getTagessatz();
							if(wpShow.boolIstOAP){
								wpShow.istOAPsetChanges();
							}
							else{
								wpShow.setChanges();
							}							
							
						} catch (SQLException e1) {
							e1.printStackTrace();
						}finally{
							sqlExecPaketzuweisung.closeConnection();
						}
						wpShow.initialRemove();
						wpShow.initialadd();
						wpShow.gui.txfZustaendige.setText(wpShow.gui.txfZustaendige.getText() + zust + ", ");
					}
					else{
						JOptionPane.showMessageDialog(wpShow.gui, "Zuständiger wurde schon eingetragen!");
					}
				}
				else{
					JOptionPane.showMessageDialog(wpShow.gui, "Es wurde kein Zuständiger ausgewählt!");
				}
				
			}
		});
		
		//Zuständige entfernen
		wpShow.gui.btnRemoveZust.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!wpShow.gui.cobRemoveZustaendige.getSelectedItem().toString().equals("")){
				//	ResultSet rsZustaendige = sqlExec.executeQuery(getSQLZustaendige());
					String Test = wpShow.gui.cobRemoveZustaendige.getSelectedItem().toString();   
				    int trullala = Test.indexOf("|");
				    String zust = Test.substring(0,trullala-1);
					SQLExecuter sqlExecAPAufwand = new SQLExecuter();
					SQLExecuter sqlExecAPZuweisung = new SQLExecuter();
					
					try {
						ResultSet rsAPAufwand = sqlExecAPAufwand.executeQuery("SELECT * FROM Aufwand " +
								"WHERE FID_Proj = 1 " + 
									"AND LVL1ID =  " + wpShow.lvl1ID +
										"AND LVL2ID =  " + wpShow.lvl2ID +
											"AND LVL3ID =  " + wpShow.lvl3ID +
												"AND LVLxID =  '" + wpShow.lvlxID +"'" +
														"AND FID_MA = '" + zust + "';");
						if(!rsAPAufwand.next()){
							ResultSet rsAPZuweisung = sqlExecAPZuweisung.executeQuery("SELECT * FROM Paketzuweisung " +
									"WHERE FID_Proj = 1 " + 
											"AND FID_LVL1ID =  " + wpShow.lvl1ID +
													"AND FID_LVL2ID =  " + wpShow.lvl2ID +
														"AND FID_LVL3ID =  " + wpShow.lvl3ID +
															"AND FID_LVLxID =  '" + wpShow.lvlxID +"'" +
																	"AND FID_MA = '" + zust + "';");
							while(rsAPZuweisung.next()){
								rsAPZuweisung.deleteRow();
							}
							rsAPZuweisung.close();
							sqlExecAPZuweisung.closeConnection();
							JOptionPane.showMessageDialog(wpShow.gui, "Zuständiger wurde gelöscht!");
							wpShow.tagessatz= wpShow.getTagessatz();
							
							if(wpShow.boolIstOAP){
								wpShow.istOAPsetChanges();
							}
							else{
								wpShow.setChanges();
							}	
							wpShow.getAndShowValues();
							wpShow.initialRemove();
							wpShow.initialadd();
						}	
						else{
							JOptionPane.showMessageDialog(wpShow.gui, "Der Zuständige hat schon einen Aufwand eingetragen!");
						}
						rsAPAufwand.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally{
						sqlExecAPAufwand.closeConnection();
					}
				}
				else{
					JOptionPane.showMessageDialog(wpShow.gui, "Es wurde kein Zuständiger ausgewählt!");
				}		
			}
		});
		
		//GUI Schließen
		wpShow.gui.miBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wpShow.gui.dispose();
			}
		});
		
		//Entfernt Focus von Beschreibung, wenn Feld per TAB verlassen wird
		//und setzt den Focus aufs Release-Datum
		wpShow.gui.txfSpec.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent k){
				if(k.getKeyCode() == KeyEvent.VK_TAB){
					wpShow.gui.txfRelease.requestFocusInWindow();
				}
			}
		});
		
		
		//Arbeitspaket bearbeiten
		wpShow.gui.miEdit.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				if(wpShow.boolIstOAP){
					if(wpShow.istOAPsetChanges()){
						wpShow.overgui.aktualisieren();
					}
				}
				else{
					if(wpShow.setChanges()){				
						wpShow.overgui.aktualisieren();
					}
				}	
			}
		});
		
		//Hilfe aufrufen
		wpShow.gui.miHilfe.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				JOptionPane.showMessageDialog(wpShow.gui, "Hier können Sie Beschreibung oder Aufwand ändern. Einfach eintragen und danach mit \"Editieren\" bestätigen");
			}
		});
		
		wpShow.gui.miInfo.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				new InfoBox();		
			}
		});
		
		
		//Listener für istOAP -> BAC und ETC werden mit 0 eingetragen
		wpShow.gui.chbIstOAP.addItemListener(new ItemListener() {	
			public void itemStateChanged(ItemEvent arg0) {
				if(wpShow.gui.chbIstOAP.isSelected()){
					wpShow.gui.txfBAC.setText("0.0");
					wpShow.gui.txfBACcost.setText("0.0");
					if(!wpShow.neues){
						wpShow.gui.txfETC.setText("0.0");						
						wpShow.gui.txfETC.setEnabled(false);
					}
					
					wpShow.gui.txfBAC.setEnabled(false);
				}
				else{
					if(wpShow.gui.txfACcost.getText().length()>0){
						wpShow.gui.txfETC.setEnabled(true);					
					}
					wpShow.gui.txfBAC.setEnabled(true);
				}
				
			}
		});
		
		
		//Listener auf dem ETC Feld, der , durch . ersetzt
		wpShow.gui.txfETC.addFocusListener(new FocusAdapter() {
	    
             public void focusGained(FocusEvent e) {
                 JTextField txfTmp = (JTextField) e.getSource();
                 if(txfTmp != null){
                     txfTmp.setSelectionStart(0);
                     txfTmp.setSelectionEnd(txfTmp.getText().length());
                 }
             } 
	    	 
             public void focusLost(FocusEvent e) {
                 JTextField txfTmp = (JTextField) e.getSource();
                 if(txfTmp != null){
                	 if(txfTmp.getText().matches("[a-zA-z]*")) {
                		 JOptionPane.showMessageDialog(wpShow.gui, "Bitte geben Sie nur Zahlen beim ETC ein!");
                		 txfTmp.setText("");
                	 }
                	 txfTmp.setText(txfTmp.getText().replace(",", "."));
                 }
             }

         });
		
		
		
		wpShow.gui.txfBAC.addFocusListener(new FocusAdapter() {
		    
            public void focusGained(FocusEvent e) {
                JTextField txfTmp = (JTextField) e.getSource();
                if(txfTmp != null){
                    txfTmp.setSelectionStart(0);
                    txfTmp.setSelectionEnd(txfTmp.getText().length());
                }
            } 
	    	 
			public void focusLost(FocusEvent e) {
				JTextField txfTmp = (JTextField) e.getSource();
				if (txfTmp != null) {
					if (!wpShow.gui.chbIstOAP.isSelected()) {
						if (txfTmp.getText().length() > 0 && txfTmp.getText().matches("[a-zA-z]*")) {
							JOptionPane.showMessageDialog(wpShow.gui, "Bitte geben Sie nur Zahlen beim BAC ein!");
							txfTmp.setText("");
						}
						txfTmp.setText(txfTmp.getText().replace(",", "."));
					}
				}
			}

        });
		
	}
	
	
	
	
}
