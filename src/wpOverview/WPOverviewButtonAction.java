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
 * 
 * ButtonActions der WPOverview GUI werden hier verwaltet
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.1 - 30.11.2010
 */


import globals.InfoBox;
import importPrepare.PrepareImport;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import chart.ChartFunctionCPI;
import chart.ChartFunctionComp;

import jdbcConnection.SQLExecuter;
import login.Login;
import wpAddAufwand.AddAufwand;
import wpMitarbeiter.ChangePW;
import wpMitarbeiter.Mitarbeiter;
import wpMitarbeiter.WBSMitarbeiter;
import wpReassign.WPReassign;
import wpShow.WPShow;
import baseline.AddBaseline;
import baseline.WBSBaseline;

public class WPOverviewButtonAction {
	
	WPOverview over;
	ToolBar toolbar;
	
	public WPOverviewButtonAction(WPOverview over){
		this.over = over;
		addButtonAction();
	}
	
	
	/**
	 * void addButtonAction()
	 * fügt actionListener zum "SchließenInaktiv" und "SchließenAktiv" Button hinzu
	 * fügt actionListener für das Menü hinzu (Beenden, Info und Hilfe)
	 * fügt mouseListener zu der Tabelle für inaktive und aktive Arbeitspakete hinzu
	 * 
	 * Schließen-Button/Menü (sowohl aktiv als auch inaktiv):	beendet das Programm
	 * Anmelde-Button/Menü:										Führt Methode checkUser() aus
	 * Info-Menü:												gibt Angaben per JOptionPane über das Projekt aus
	 * Hilfe-Menü:												gibt per JOptionPane eine Hilfe für die WPOverview aus
	 * mouseListener (aktiv und inaktiv): Bei Anklicken des gewünschten Arbeitspaketes
	 * 									  wird die WPShow mit dem angeklickten Arbeitspaketes geöffnet
	*/
	public void addButtonAction(){	
		over.gui.btnSchliessen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);	
			}
		});


		//Menü ActionListener die nur von Projektleitern benötigt werden
		if(over.usr.getProjLeiter()){
			/**
			 * Menü: Arbeitspaket anlegen - ButtonListener
			 * zum Erzeugen eines neuen Arbeitspakets			
			 */
			over.gui.miAP.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					readAP(true);
			}});	
			
			
			/**
			 * Menü: Arbeitspaket löschen - ButtonListener
			 * zum löschen eines Arbeitspakets
			 */
			over.gui.miDelAp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String tmpID[];
					if(over.currentTP != null){
						tmpID = over.currentTP.getLastPathComponent().toString().split("-");
						if(tmpID.length > 0)
							over.deleteWP(tmpID[0]);
					}
					else{
						JOptionPane.showMessageDialog(over.gui, "Bitte markieren Sie das Arbeitspaket das gelöscht werden soll");
					}
			}});	
		}
		
		if(over.usr.getProjLeiter()){
			over.gui.miImportInitial.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new PrepareImport(over);
					over.aktualisieren();
					WPOverviewGUI.setStatusbar("Die Importierte DB wurde berechnet");
					//JOptionPane.showMessageDialog(over.gui, "Berechnung abgeschlossen");
				}
			});
		}
		
		
		//Menü
		over.gui.miAktualisieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				over.resetTrees();
				over.aktualisieren();
				WPOverviewGUI.setStatusbar("Die Ansicht wurde aktualisiert");
			}
		});
		
		over.gui.miOAPAktualisieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddBaseline("", over, false);
			}
		});
		
		over.gui.miChangePW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				new ChangePW(over.usr);
			}
		});
		
		
		over.gui.miAbmelden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Login();
				over.gui.dispose();
			}
		});
		
		over.gui.miBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);	
			}
		});
		
		over.gui.miHilfe.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				JOptionPane.showMessageDialog(over.gui, "Bitte klicken Sie auf das gewünschte Arbeitspaket, um die Details anzuzeigen.");
			}
		});
		
		over.gui.miInfo.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				new InfoBox();		
			}
		});
		
		
		//Mitarbeiter
//		over.gui.btnAddMitarbeiter.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				new WBSMitarbeiter(over);				
//			}
//		});
//		
		
		over.gui.tblMitarbeiter.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1){ 
                	if(over.gui.tblMitarbeiter.getSelectedRow()>=0){
                		SQLExecuter sqlExecMitarbeiter = new SQLExecuter();
                	
	                	String Mitarbeiter = over.gui.tblMitarbeiter.getValueAt(over.gui.tblMitarbeiter.getSelectedRow(),0).toString();    	
	                	ResultSet rsMitarbeiter = sqlExecMitarbeiter.executeQuery("SELECT * FROM Mitarbeiter WHERE Login = '" + Mitarbeiter + "';");
	                	try {
							while(rsMitarbeiter.next()){
								Mitarbeiter mit = new Mitarbeiter(rsMitarbeiter.getString("Login"), rsMitarbeiter.getString("Vorname"), rsMitarbeiter.getString("Name"),
										rsMitarbeiter.getInt("Berechtigung"), rsMitarbeiter.getString("Passwort"), 
										rsMitarbeiter.getDouble("Tagessatz"));
								new WBSMitarbeiter(mit, over);
							}
							rsMitarbeiter.close();
							
						} catch (SQLException e1) {
							e1.printStackTrace();
						} finally{
							sqlExecMitarbeiter.closeConnection();
						}
                	}
                }
        }});
		
		
		//Baseline
		over.gui.btnAddBaseline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddBaseline(over.gui.txfBeschreibung.getText(), over, true);
				over.getBaselines();
			}
		});
		
		over.gui.btnShowBaseline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedBaseline = over.gui.cobChooseBaseline.getSelectedItem().toString();
                int pos = selectedBaseline.indexOf("|");
                String curBaseline = selectedBaseline.substring(0,pos-1);
                new WBSBaseline(Integer.parseInt(curBaseline));
            }
		});
		
		//Bäume
		over.gui.treeInaktiv.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e){
				over.tp= e.getNewLeadSelectionPath();
				if(over.tp != null){
					over.setTreepath(over.tp);
				}
			}
		});	
		
		over.gui.treeFertig.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e){
				over.tp= e.getNewLeadSelectionPath();
				if(over.tp != null){
					over.setTreepath(over.tp);
				}
			}
		});
	
		
	
		over.gui.treeAktiv.addTreeSelectionListener(new TreeSelectionListener(){			
			public void valueChanged(TreeSelectionEvent e){
				over.tp= e.getNewLeadSelectionPath();
				if(over.tp != null){
					over.setTreepath(over.tp);
				}
			}
		});
		
		
		over.gui.treeAlle.addTreeSelectionListener(new TreeSelectionListener(){			
			public void valueChanged(TreeSelectionEvent e){
				over.tp= e.getNewLeadSelectionPath();
				if(over.tp != null){
					over.setTreepath(over.tp);
				}
			}
		});
		
		
		over.gui.treeAktiv.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){ 
                	if(over.currentTP != null){
                    	DefaultMutableTreeNode node=(DefaultMutableTreeNode) over.currentTP.getLastPathComponent();
    					if(node != null){
    						String id=node.toString();
    						if(id.contains(" ")){
    							new WPShow(id.substring(0,id.indexOf(" ")),over.usr,over.dies);
    							over.currentTP = over.gui.treeAktiv.getSelectionPath();
    						}
    						over.gui.treeAktiv.clearSelection();
    					}		
                	}
                }
			}
			
		    public void mouseReleased(MouseEvent e) {
		        if(e.isPopupTrigger()){
		        	over.tp= over.gui.treeAktiv.getPathForLocation(e.getX(), e.getY());
					if(over.tp != null){
						over.setTreepath(over.tp);
						over.gui.treeAktiv.setSelectionPath(over.tp);
					}

                    //Context Menü anzeigen
					over.gui.treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
		        } 
		      }
		});
		
		
		over.gui.treeInaktiv.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){ 
                	if(over.currentTP != null){
                    	DefaultMutableTreeNode node=(DefaultMutableTreeNode) over.currentTP.getLastPathComponent();
    					if(node != null){
    						String id=node.toString();
    						if(id.contains(" ")){
    							new WPShow(id.substring(0,id.indexOf(" ")),over.usr,over.dies);
    							over.currentTP = over.gui.treeInaktiv.getSelectionPath();
    						}
    						over.gui.treeInaktiv.clearSelection();
    					}		
                	}
                }
			}
			
		    public void mouseReleased(MouseEvent e) {
		        if(e.isPopupTrigger()){
		        	over.tp= over.gui.treeInaktiv.getPathForLocation(e.getX(), e.getY());
					if(over.tp != null){
						over.setTreepath(over.tp);
						over.gui.treeInaktiv.setSelectionPath(over.tp);
					}

                    //Context Menü anzeigen
                    over.gui.treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
		        } 
		      }
		});
		
		over.gui.treeFertig.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){ 
                	if(over.currentTP != null){
                    	DefaultMutableTreeNode node=(DefaultMutableTreeNode) over.currentTP.getLastPathComponent();
    					if(node != null){
    						String id=node.toString();
    						if(id.contains(" ")){
    							new WPShow(id.substring(0,id.indexOf(" ")),over.usr,over.dies);
    							over.currentTP = over.gui.treeFertig.getSelectionPath();
    						}
    						over.gui.treeFertig.clearSelection();
    					}		
                	}
                }
			}
				
		    public void mouseReleased(MouseEvent e) {
		        if(e.isPopupTrigger()){
		        	over.tp= over.gui.treeFertig.getPathForLocation(e.getX(), e.getY());
					if(over.tp != null){
						over.setTreepath(over.tp);
						over.gui.treeFertig.setSelectionPath(over.tp);
					}

                    //Context Menü anzeigen
                    over.gui.treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
		        } 
		      }
		});
		
		over.gui.treeAlle.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){ 
                	if(over.currentTP != null){
                    	DefaultMutableTreeNode node=(DefaultMutableTreeNode) over.currentTP.getLastPathComponent();
    					if(node != null){
    						String id=node.toString();
    						if(id.contains(" ")){
    							new WPShow(id.substring(0,id.indexOf(" ")),over.usr,over.dies);
    							over.currentTP = over.gui.treeAlle.getSelectionPath();
    						}
    						over.gui.treeAlle.clearSelection();
    					}		
                	}
                }
			}
			
		    public void mouseReleased(MouseEvent e) {
		        if(e.isPopupTrigger()){
		        	over.tp= over.gui.treeAlle.getPathForLocation(e.getX(), e.getY());
					if(over.tp != null){
						over.setTreepath(over.tp);
						over.gui.treeAlle.setSelectionPath(over.tp);
					}

                    //Context Menü anzeigen
                    over.gui.treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
		        } 
		      }
		});
		
		
		
		//KontextMenüs erstellen
		if(over.usr.getProjLeiter()){
			
			//AP einfügen
			over.gui.miContAPadd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					readAP(true);
				}
			});
			
			
			//AP löschen
			over.gui.miContAPdel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					delAP();
				}
			});
			
			
			//Aufwand eintragen
			over.gui.miContAufw.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					readAP(false);
				}
			});
			
			
			//Unterstruktur einfügen
			over.gui.miContReassign.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new WPReassign(over.tp, over.usr, over);
				}
			});
		}

		
		//ToolBar Hinzufügen
		toolbar = new ToolBar(this, over.usr.getProjLeiter());
		over.gui.add(toolbar, BorderLayout.NORTH);
		
		//Fenster Schließen
		over.gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				over.gui.dispose();
			}
		});
		over.gui.btnChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				new ChartFunctionCPI();
			}
		});
		//Fortschrittsdiagramm anzeigen
		over.gui.btnComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				new ChartFunctionComp();
			}
		});
		
		//setzt die Items der ToolBar je nach Berechtigung und Ansicht
		//setEnabled(true/false)
		over.gui.tabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (over.gui.tabs.getSelectedIndex()) {
				case 0:
					if (over.usr.getProjLeiter())
						toolbar.setAllEnabled(true, true, true, true);
					else
						toolbar.setAllEnabled(false, false, false, true);
					over.gui.legende.setVisible(true);
					break;
				case 1:
					if (over.usr.getProjLeiter())
						toolbar.setAllEnabled(true, true, true, true);
					else
						toolbar.setAllEnabled(false, false, false, true);
					over.gui.legende.setVisible(true);
					break;
				case 2:
					if (over.usr.getProjLeiter())
						toolbar.setAllEnabled(true, true, true, true);
					else
						toolbar.setAllEnabled(false, false, false, true);
					over.gui.legende.setVisible(true);
					break;
				case 3:
					if (over.usr.getProjLeiter())
						toolbar.setAllEnabled(true, true, true, true);
					else
						toolbar.setAllEnabled(false, false, false, true);
					over.gui.legende.setVisible(true);
					break;
				case 4:
					if (over.usr.getProjLeiter())
						toolbar.setAllEnabled(false, false, true, true);
					else
						toolbar.setAllEnabled(false, false, false, true);
					over.gui.legende.setVisible(false);
					break;
				case 5:
					if (over.usr.getProjLeiter())
						toolbar.setAllEnabled(false, false, false, true);
					else
						toolbar.setAllEnabled(false, false, false, true);
					if (over.usr.getProjLeiter())
					over.gui.legende.setVisible(false);
					break;
				}
				
			}
		});
		
	}
	
	/**
	 * Hilfs-/Binde-Funktion zum löschen eines Arbeitspaketes
	 */
	protected void delAP(){
		String tmpID[];
		if(over.currentTP != null){
			tmpID = over.currentTP.getLastPathComponent().toString().split("-");
			if(tmpID.length > 0)
				over.deleteWP(tmpID[0]);
		}
		else{
			JOptionPane.showMessageDialog(over.gui, "Bitte markieren Sie das Arbeitspaket das gelöscht werden soll");
		}
	}
	
	/**
	 * Hilfsfunktion zum Erstellen eines neuen APs und zum Erfassen für Aufwände für bestehende APs
	 * Wird aus dem Menü aufgerufen und aus dem KontextMenü eines Baumeintrags
	 * Um Codezeilen zu reduzieren wurde die Funktionalität in diese Methode gepackt
	 */
	protected void readAP(boolean isNewAp){
		String tmpID[];
		if(over.currentTP != null){
			tmpID = over.currentTP.getLastPathComponent().toString().split("-");
			if(tmpID.length > 0){
				String apID[] = tmpID[0].split("\\.");
				int id1 = Integer.parseInt(apID[0]);
				int id2 = Integer.parseInt(apID[1]);
				int id3 = Integer.parseInt(apID[2]);
				String idx = "";
				for(int i = 3; i < apID.length; i++){
					if(idx.equals(""))
						idx = apID[i];
					else
						idx += "." + apID[i];
				} 
				SQLExecuter sqlExecCurrentIsOAP = new SQLExecuter();
				ResultSet rs = sqlExecCurrentIsOAP.executeQuery("SELECT * FROM Arbeitspaket " +
													"WHERE LVL1ID = " + id1 +
													"AND LVL2ID = " + id2 +
													"AND LVL3ID = " + id3 +
													"AND LVLxID = '" + idx + "';");
				boolean istOAP = false;
				try {
					rs.first();
					istOAP = rs.getBoolean("istOAP");
					rs.close();
					sqlExecCurrentIsOAP.closeConnection();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				//Soll ein Neues AP aufgerufen werden
				if(isNewAp){
					if(istOAP){
						new WPShow(over, over.usr, tmpID[0]);
					} else{
						JOptionPane.showMessageDialog(over.gui, "Bitte markieren Sie ein Oberarbeitspaket!");	
					}					
				}
				//oder ein bestehendes?
				else{
					if(!istOAP){
						WPShow wpshow = new WPShow(tmpID[0], over.usr, over);
						new AddAufwand(tmpID[0], over.usr, wpshow, tmpID[1]);
						
					} else{
						JOptionPane.showMessageDialog(over.gui, "Sie können nur Aufwände zu Arbeitspaketen erfassen, nicht zu OAPs!");
					}
				}		

			}
			else
				JOptionPane.showMessageDialog(over.gui, "Bitte markieren Sie erst ein Arbeitspaket, um dann dort ein neues einzufügen!");
		}
		else{
			JOptionPane.showMessageDialog(over.gui, "Bitte markieren Sie erst ein Arbeitspaket, um dann dort ein neues einzufügen!");
		}
	}
}
