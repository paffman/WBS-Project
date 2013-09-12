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
 * Listener, der das Ziel des Drag&Drop Vorgangs markiert
 * 
 * @author Andre Paffenholz
 * @version 0.1 - 21.01.2011
 */

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import jdbcConnection.SQLExecuter;
import wpShow.WPShow;

class TreeDropTarget implements DropTargetListener {

	  //private DropTarget target;
	  private JTree targetTree;
	  private WPOverview overview;
	  private WPShow wpshow;
	  public static String oldWpId;
	  private String newWpId;

	  /**
	   * Default-Konstruktor
	   * @param tree JTree auf dem der Listener arbeiten soll
	   * @param overview WPOverview wird benötigt, um eine Instanz von WPShow aufrufen zu können
	   */
	  public TreeDropTarget(JTree tree, WPOverview overview) {
		  this.overview = overview;
		  targetTree = tree;
		  new DropTarget(targetTree, this);
	  }

	/**
	 * Drop Handler - liefert eine TreeNode zu einem Event zurück
	 * @param dtde DropTargetDragEvent das ausgelöst wurde
	 * @return aktuelle TreeNode
	 */
	  private TreeNode getNodeForEvent(DropTargetDragEvent dtde) {
	    Point p = dtde.getLocation();
	    DropTargetContext dtc = dtde.getDropTargetContext();
	    JTree tree = (JTree) dtc.getComponent();
	    TreePath path = tree.getClosestPathForLocation(p.x, p.y);
	    return (TreeNode) path.getLastPathComponent();
	  }

	  
	  /**
	   * Drag-Handler - wird beim Drag&Drop Vorgang ausgelöst
	   */
	  public void dragEnter(DropTargetDragEvent dtde) {
	    TreeNode node = getNodeForEvent(dtde);
	    if (node.isLeaf()) {
	    	dtde.rejectDrag();
	    } else {
	      //dtde.acceptDrag(DnDConstants.ACTION_MOVE);
	      dtde.acceptDrag(dtde.getDropAction());      
	    }
	  }

	  
	  
	  /**
	   * Drag-Handler - wird beim Drag&Drop Vorgang ausgelöst
	   */
	  public void dragOver(DropTargetDragEvent dtde) {
	    TreeNode node = getNodeForEvent(dtde);
	    if (node.isLeaf()) {
	    	WPOverviewGUI.lblStatusbar.setText("Bitte wählen Sie als Ziel ein Oberarbeitpaket!");
	    	dtde.rejectDrag();
	    } else {
	    	WPOverviewGUI.lblStatusbar.setText("");
	      //dtde.acceptDrag(DnDConstants.ACTION_MOVE);
	      dtde.acceptDrag(dtde.getDropAction());
	    }
	  }

	  
	  /**
	   * Unbenutzer Drag-Handler
	   */
	  public void dragExit(DropTargetEvent dte) {
	  }

	  
	  /**
	   * Unbenutzer Drag-Handler
	   */ 
	  public void dropActionChanged(DropTargetDragEvent dtde) {
	  }

	  
	  /**
	   * Drag-Handler zum eigentlichen Ablegen
	   */
	  public void drop(DropTargetDropEvent dtde) {
	    Point pt = dtde.getLocation();
	    DropTargetContext dtc = dtde.getDropTargetContext();
	    JTree tree = (JTree) dtc.getComponent();
	    TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
	    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) parentpath
	        .getLastPathComponent();
	    if (parent.isLeaf()) {
	      dtde.rejectDrop();
	      WPOverviewGUI.lblStatusbar.setText("Bitte wählen Sie als Ziel ein Oberarbeitpaket!");
	      return;
	    }
	    
	    
	    //System.out.println("ID: " + parent.toString());
	    //aktuell markierte ID auslesen
	    boolean isOap = false;	    
	    String[] tmpID = parent.toString().split("-");
	    if(tmpID.length > 0){
		    //WPShow aufrufen
		    wpshow = new WPShow(tmpID[0].trim(), overview.usr, overview);
		    
		    //Prüfen ob OAP -> für später
		    if(wpshow.gui.chbIstOAP.isSelected())
		    	isOap = true;
		    
		    //...und unsichtbar lassen
		    wpshow.gui.setVisible(false);
		    
		    //Unsichbares Fenster Schließen
		    wpshow.gui.dispose();
		    
		    //neue id auslesen
		    newWpId = wpshow.getWpId(tmpID[0].trim());

		    
		    //alte id zusammnensetzen zum Auslesen
		    String[] oldID = oldWpId.split("\\.");
		    if(oldID.length > 0){
		    	 int olvl1 = Integer.valueOf(oldID[0]);
		    	 int olvl2 = Integer.valueOf(oldID[1]);
		    	 int olvl3 = Integer.valueOf(oldID[2]);
		    	 String olvlx ="";
		    	 for(int i=3;i<oldID.length;i++){
		    		 if(olvlx.equals(""))
		    			 olvlx = oldID[i];
		    		 else
		    			 olvlx += "." + oldID[i];
		    		 
		    	 }
		    	 
		    	//neue ID zusammensetzen zum einfügen
				    String[] newID = newWpId.trim().split("\\.");
				    if(newID.length > 0){
				    	 int nlvl1 = Integer.valueOf(newID[0]);
				    	 int nlvl2 = Integer.valueOf(newID[1]);
				    	 int nlvl3 = Integer.valueOf(newID[2]);
				    	 String nlvlx ="";
				    	 for(int i=3;i<newID.length;i++){
				    		 if(nlvlx.equals(""))
				    			 nlvlx = newID[i];
				    		 else
				    			 nlvlx += "." + newID[i];
				    		 
				    	 }
				    	 
				    	 
					    //Update auf das bestehende Arbeitspaket ausführen
					    try{					    	
					    	//Dummy Insert, damit die ID in der DB vorhanden ist, danach Daten entprechend aus dem "alten" Paket kopieren
					    	//Problem: Datensatz kann nur geändert werden, wenn der "neue" in den Arbeitspaketen besteht - daher erst ein Dummy Insert
					    	SQLExecuter sqlExeDummySel = new SQLExecuter();
					    	ResultSet rs = sqlExeDummySel.executeQuery("SELECT * FROM Arbeitspaket WHERE LVL1ID = " + olvl1 + " AND LVL2ID = " + olvl2 + " AND LVL3ID =  " + olvl3 + " AND LVLxID = '" + olvlx + "'");
					    	int dlvl1=0;
					    	double bac =0, ac=0, ev=0, etc=0, eac=0, cpi=0, back=0, ack=0, etck=0, tag=0;
					    	String leiter ="", name ="", beschr ="";
					    	boolean oap=false, inak=false;
					    	

							SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
							String dstr = "";
					    	
					    	if(rs.next()){					    	
						    	dlvl1 = rs.getInt("LVL1ID");	
						    	leiter = rs.getString("FID_Leiter");
						    	name = rs.getString("Name");
						    	beschr = rs.getString("Beschreibung");
						    	bac = rs.getDouble("BAC");
						    	ac = rs.getDouble("AC");
						    	ev = rs.getDouble("EV");
						    	etc = rs.getDouble("ETC");
						    	eac = rs.getDouble("EAC");
						    	cpi = rs.getDouble("CPI");
						    	back = rs.getDouble("BAC_Kosten");
						    	ack = rs.getDouble("AC_Kosten");
						    	etck = rs.getDouble("ETC_Kosten");
						    	tag = rs.getDouble("WP_Tagessatz");
						    	dstr = dateFormat.format(rs.getDate("Release"));
						    	oap = rs.getBoolean("istOAP");
						    	inak = rs.getBoolean("istInaktiv");
					    	}
					    	
					    	//neues AP anlegen und werte übernhemen - außer dem Projekt
					    	if(dlvl1 > 0){
						    	rs.moveToInsertRow();
						    	rs.updateInt("FID_Proj", 1);
								rs.updateInt("LVL1ID", nlvl1);
								rs.updateInt("LVL2ID", nlvl2);
								rs.updateInt("LVL3ID", nlvl3);
								rs.updateString("LVLxID", nlvlx);
								rs.updateString("Name", name);
								rs.updateString("Beschreibung", beschr);
								rs.updateString("FID_Leiter", leiter);
								rs.updateDouble("BAC", bac);
								rs.updateDouble("AC", ac);
								rs.updateDouble("EV", ev);
								rs.updateDouble("ETC", etc);
								rs.updateDouble("EAC", eac);
								rs.updateDouble("CPI", cpi);
								rs.updateDouble("BAC_Kosten", back);
								rs.updateDouble("AC_Kosten", ack);
								rs.updateDouble("ETC_Kosten", etck);
								rs.updateDouble("WP_Tagessatz", tag);
								
								java.util.Date dt = dateFormat.parse(dstr);
								java.sql.Date dte =new java.sql.Date(dt.getTime());
								
								rs.updateDate("Release", dte);
								rs.updateBoolean("istOAP", oap);
								rs.updateBoolean("istInaktiv", inak);
								rs.insertRow();
						    						    		
					    	}
					    	else {
					    		dtde.rejectDrop();
					    		return;
					    	}

					    	//Verbindung Schließen
					    	rs.close();
						    sqlExeDummySel.closeConnection();
						   						    
					    				    	
						    //Paketzuweisungen anpassen
					    	SQLExecuter sqlExePaket = new SQLExecuter();
						    sqlExePaket.executeUpdate("UPDATE Paketzuweisung SET FID_LVL1ID = " + nlvl1 + ", FID_LVL2ID = " + nlvl2 + ", FID_LVL3ID =  " + nlvl3 + ", FID_LVLxID = '" + nlvlx + "' " 
						    	+	" WHERE FID_LVL1ID = " + olvl1 + " AND FID_LVL2ID = " + olvl2 + " AND FID_LVL3ID =  " + olvl3 + " AND FID_LVLxID = '" + olvlx + "'");
						    sqlExePaket.closeConnection();
						    
						    
						    //Aufwände anpassen
					    	SQLExecuter sqlExeAufw = new SQLExecuter();
						    sqlExeAufw.executeUpdate("UPDATE Aufwand SET LVL1ID = " + nlvl1 + ", LVL2ID = " + nlvl2 + ", LVL3ID =  " + nlvl3 + ", LVLxID = '" + nlvlx + "' " 
						    	+	" WHERE LVL1ID = " + olvl1 + " AND LVL2ID = " + olvl2 + " AND LVL3ID =  " + olvl3 + " AND LVLxID = '" + olvlx + "'");
						    sqlExeAufw.closeConnection();
						    
						    
					    	
					    	//Arbeitspaket löschen
					    	SQLExecuter sqlExePak = new SQLExecuter();
						    sqlExePak.executeUpdate("DELETE * FROM Arbeitspaket WHERE LVL1ID = " + olvl1 + " AND LVL2ID = " + olvl2 + " AND LVL3ID =  " + olvl3 + " AND LVLxID = '" + olvlx + "'");
						    sqlExePak.closeConnection();
						    						    
						    //Prüfen oh OAP --> alle UAPs verschieben
						    if(isOap)
						    	dropUaps();
						    
						    //alles Aktualisieren
						    overview.aktualisieren();
							WPOverviewGUI.lblStatusbar.setText("Paket(e) erfolgreich verschoben");
						    
					    }
					    catch(Exception ex){
					    	ex.printStackTrace();
					    }
				    } 
		    }
		   

		    try {
		      Transferable tr = dtde.getTransferable();
		      DataFlavor[] flavors = tr.getTransferDataFlavors();
		      for (int i = 0; i < flavors.length; i++) {
		        if (tr.isDataFlavorSupported(flavors[i])) {
		          dtde.acceptDrop(dtde.getDropAction());
		          TreePath p = (TreePath) tr.getTransferData(flavors[i]);
		          DefaultMutableTreeNode node = (DefaultMutableTreeNode) p
		              .getLastPathComponent();
		          DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		          model.insertNodeInto(node, parent, 0);
		          dtde.dropComplete(true);
		          return;
		        }
		      }
		      dtde.rejectDrop();
		    } catch (Exception e) {
		      e.printStackTrace();
		      dtde.rejectDrop();
		    }	
	    }

	  }
	  

	  /**
		 * Hängt alle UAPs zu einem OAP um
		 * Wird per Drag&Drop durch drop(DropTargetDropEvent dtde) aufgerufen
		 */
		private void dropUaps(){
			
			String[] oap = overview.getOapId(oldWpId);
			String whereCl = "";
			
			switch(oap.length){
				case 1: whereCl = " where LVL1ID = " + oap[0] + ";";
					break;
				case 2: whereCl = " where LVL1ID = " + oap[0] + " AND LVL2ID = " + oap[1] + ";";
					break;
				case 3: whereCl = " where LVL1ID = " + oap[0] + " AND LVL2ID = " + oap[1] + " AND LVL3ID = " + oap[2] +";";
					break;
				case 4: whereCl = " where LVL1ID = " + oap[0] + " AND LVL2ID = " + oap[1] + " AND LVL3ID = " + oap[2] + " AND LVLxID = '" + oap[3] +"';";
				break;
			}
						
			SQLExecuter sqlExec= new SQLExecuter(); 
			ResultSet rs = sqlExec.executeQuery("SELECT * FROM Arbeitspaket " + whereCl);
			
	    	int nlvl1=0, nlvl2=0, nlvl3=0;
	    	String nlvlx="";
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	    	//OAP einlesen
	    	String[] oapNeu = overview.getOapId(newWpId);
	    	//Counter für unterpakete beim Einordnen
	    	int levelcount = 1;
	    	
	    	ArrayList<Workpackage> alleWp = new ArrayList<Workpackage>();
	    	
			try {
				while(rs.next()){
					alleWp.add(new Workpackage(rs.getInt("LVL1ID"), rs.getInt("LVL2ID"), rs.getInt("LVL3ID"), rs.getString("LVLxID"), rs.getString("Name"), 
							rs.getString("Beschreibung"), rs.getDouble("CPI"), rs.getDouble("BAC"), rs.getDouble("AC"), rs.getDouble("EV"), rs.getDouble("ETC"), 
							rs.getDouble("WP_Tagessatz"), rs.getDouble("EAC"), rs.getDouble("BAC_Kosten"), rs.getDouble("AC_Kosten"), rs.getDouble("ETC_Kosten"), 
							dateFormat.format(rs.getDate("Release")), rs.getBoolean("istOAP"), rs.getBoolean("istInaktiv"), rs.getString("FID_Leiter"), null));			    		
				}
				rs.close();
				sqlExec.closeConnection();
				
				//jedes Arbeitspaket umbauen	
				for(Workpackage wp : alleWp) {
				    	
				    	//Nur bei APS - nicht beim Projekt
				    	if(wp.getLvl1ID() > 0){

				    		//neue ID zusammensetzen
				    		switch(oapNeu.length){
				    		case 1:
								nlvl1 = Integer.valueOf(oapNeu[0]);
								nlvl2 = levelcount;
								nlvl3 = 0;
								nlvlx = "0";
				    			break;
				    			
				    		case 2:
				    			nlvl1 = Integer.valueOf(oapNeu[0]);
				    			nlvl2 = Integer.valueOf(oapNeu[1]);
				    			nlvl3 =levelcount;
				    			nlvlx = "0";
				    			break;
				    			
				    		case 3:
				    			nlvl1 = Integer.valueOf(oapNeu[0]);
								nlvl2 = Integer.valueOf(oapNeu[1]);
								nlvl3 = Integer.valueOf(oapNeu[2]);
								nlvlx = ""+levelcount;
				    			break;
				    		}
					    	
				    		//neues AP anlegen und werte übernhemen
				    		sqlExec = new SQLExecuter();
				    		ResultSet rsIns = sqlExec.executeQuery("SELECT * FROM Arbeitspaket " + whereCl);
				    		rsIns.moveToInsertRow();
				    		rsIns.updateInt("FID_Proj", 1);
							rsIns.updateInt("LVL1ID", nlvl1);
							rsIns.updateInt("LVL2ID", nlvl2);
							rsIns.updateInt("LVL3ID", nlvl3);
							rsIns.updateString("LVLxID", nlvlx);
							rsIns.updateString("Name", wp.getName());
							rsIns.updateString("Beschreibung", wp.getBeschreibung());
							rsIns.updateString("FID_Leiter", wp.getfid_Leiter());
							rsIns.updateDouble("BAC", wp.getBac());
							rsIns.updateDouble("AC", wp.getAc());
							rsIns.updateDouble("EV", wp.getEv());
							rsIns.updateDouble("ETC", wp.getEtc());
							rsIns.updateDouble("EAC", wp.getEac());
							rsIns.updateDouble("CPI", wp.getcpi());
							rsIns.updateDouble("BAC_Kosten", wp.getbac_kosten());
							rsIns.updateDouble("AC_Kosten", wp.getAc_kosten());
							rsIns.updateDouble("ETC_Kosten",wp.getEtc_kosten());
							rsIns.updateDouble("WP_Tagessatz", wp.getwptagessatz());
							
							java.util.Date dt = dateFormat.parse(wp.getRelease());
							java.sql.Date dte =new java.sql.Date(dt.getTime());
							
							rsIns.updateDate("Release", dte);
							rsIns.updateBoolean("istOAP", wp.isIstOAP());
							rsIns.updateBoolean("istInaktiv", wp.isIstInaktiv());
							rsIns.insertRow();
							rsIns.close();
							sqlExec.closeConnection();
							
							//Counter erHöhen
							levelcount++;
					    			
							
							//System.out.printf("Paket: %d %d %d %s\n",nlvl1,nlvl2,nlvl3,nlvlx);
							
							  //Paketzuweisungen anpassen
					    	SQLExecuter sqlExePaket = new SQLExecuter();
						    sqlExePaket.executeUpdate("UPDATE Paketzuweisung SET FID_LVL1ID = " + nlvl1 + ", FID_LVL2ID = " + nlvl2 + ", FID_LVL3ID =  " + nlvl3 + ", FID_LVLxID = '" + nlvlx + "' " 
						    	+	" WHERE FID_LVL1ID = " + wp.getLvl1ID() + " AND FID_LVL2ID = " + wp.getLvl2ID() + " AND FID_LVL3ID =  " + wp.getLvl3ID() + " AND FID_LVLxID = '" + wp.getLvlxID() + "'");
						    sqlExePaket.closeConnection();
						    
						    
						    //Aufwände anpassen
					    	SQLExecuter sqlExeAufw = new SQLExecuter();
						    sqlExeAufw.executeUpdate("UPDATE Aufwand SET LVL1ID = " + nlvl1 + ", LVL2ID = " + nlvl2 + ", LVL3ID =  " + nlvl3 + ", LVLxID = '" + nlvlx + "' " 
						    	+	" WHERE LVL1ID = " + wp.getLvl1ID() + " AND LVL2ID = " + wp.getLvl2ID() + " AND LVL3ID =  " + wp.getLvl3ID() + " AND LVLxID = '" + wp.getLvlxID() + "'");
						    sqlExeAufw.closeConnection();
						    
						   
					    	//Arbeitspaket löschen
					    	SQLExecuter sqlExePak = new SQLExecuter();
						    sqlExePak.executeUpdate("DELETE * FROM Arbeitspaket WHERE LVL1ID = " + wp.getLvl1ID() + " AND LVL2ID = " + wp.getLvl2ID() + " AND LVL3ID =  " + wp.getLvl3ID() + " AND LVLxID = '" + wp.getLvlxID() + "'");
						    sqlExePak.closeConnection();
							
				    	}
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			sqlExec.closeConnection();
			
		}	  
	}