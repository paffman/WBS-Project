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
 * zum Anzeigen der Baseline in Tabellenform
 * formatiert die Tabelle korrekt
 * 
 * @author Daniel Metzler und Samson von Graevenitz
 * @version 1.9- 17.02.2011
 */

import java.text.DecimalFormat;
import java.util.Vector;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbcConnection.SQLExecuter;

public class WBSBaseline {

	/**
	 * Variablen für die WBSBaselineGUI und den SQLExecuter
	 * Variablen für den BaselineID und den Decimalformat auf 2 Nachkommastellen
	 */
	protected WBSBaselineGUI gui;
	private WBSBaseline dies;
	protected SQLExecuter sqlExec;
	private int id;
	private final DecimalFormat decform = new DecimalFormat("#0.00");
	private String beschreibung;
	
	/**
	 * Konstruktor
	 * initialisiert die baselineID mit der übergeben Baseline
	 * @param id ID der Baseline
	 */
	public WBSBaseline(int id){	
		dies = this;
		this.id = id;
		gui = new WBSBaselineGUI();	
		sqlExec = new SQLExecuter();
		new WBSBaselineButtonAction(dies);
		try {
			getBaseline();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		
		
		

			
		//Fertigstellunggrad spalte wird duch den StatusCellRenderer mit JProgressBar gefüllt
		StatusCellRenderer pcr=new StatusCellRenderer();
		gui.tblBaseline.getColumnModel().getColumn(15).setCellRenderer(pcr);
		
		//Spaltenbreiten definieren
		gui.tblBaseline.getColumnModel().getColumn(0).setMaxWidth(30);
		gui.tblBaseline.getColumnModel().getColumn(1).setMaxWidth(30);
		gui.tblBaseline.getColumnModel().getColumn(2).setMaxWidth(30);
		gui.tblBaseline.getColumnModel().getColumn(3).setMaxWidth(70);
		gui.tblBaseline.getColumnModel().getColumn(4).setMinWidth(150);
		gui.tblBaseline.getColumnModel().getColumn(5).setMaxWidth(60);
		gui.tblBaseline.getColumnModel().getColumn(6).setMaxWidth(60);
		gui.tblBaseline.getColumnModel().getColumn(7).setMaxWidth(60);
		gui.tblBaseline.getColumnModel().getColumn(8).setMaxWidth(50);
		gui.tblBaseline.getColumnModel().getColumn(9).setMaxWidth(160);
		gui.tblBaseline.getColumnModel().getColumn(10).setMaxWidth(160);
		gui.tblBaseline.getColumnModel().getColumn(11).setMaxWidth(160);
		gui.tblBaseline.getColumnModel().getColumn(12).setMaxWidth(160);
		gui.tblBaseline.getColumnModel().getColumn(13).setMaxWidth(160);
		gui.tblBaseline.getColumnModel().getColumn(14).setMaxWidth(40);		
		gui.tblBaseline.getColumnModel().getColumn(15).setMinWidth(80);
		gui.tblBaseline.getColumnModel().getColumn(15).setMaxWidth(150);
		
		//Werte der Spalte werden rechtsbündig oder linksbündig gesetzt gesetzt (Text rechtsbündig)
		RightCellRenderer rcr = new RightCellRenderer();
		LeftCellRenderer lcr = new LeftCellRenderer();
		
		for (int i=0; i<15;i++){
			if(i!=3 && i!=4){
				gui.tblBaseline.getColumnModel().getColumn(i).setCellRenderer(rcr);		
			}
			else{
				gui.tblBaseline.getColumnModel().getColumn(i).setCellRenderer(lcr);
			}					
		}
		
		
		
		CPICellRenderer ccr = new CPICellRenderer();
		gui.tblBaseline.getColumnModel().getColumn(8).setCellRenderer(ccr);	
		
		//setzt den Titel für die BaselineGUI
		gui.setTitle("Baseline vom " + getBaselineDate() + " | " + beschreibung);
		gui.tblBaseline.repaint();
		sqlExec.closeConnection();
	}
	
	/**
	 * wird von dem Konstruktor aufgerufen
	 * Gibt den Datumswert der entsprechenden ID zurück
	 * @return Datum der Baseline
	 */
	private Date getBaselineDate(){
		Date dte = new Date(1);
		SQLExecuter sqlExecgetbase = new SQLExecuter();
		ResultSet base = sqlExecgetbase.executeQuery("Select * FROM Baseline WHERE ID = " + id);
		try {
			while(base.next()){
				dte = base.getDate("Datum");
				this.beschreibung = base.getString("Beschreibung");
			}
			base.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sqlExecgetbase.closeConnection();
		return dte;
	}
	
	/**
	 * wird vom Konstruktor aufgerufen
	 * holt sich die Daten der ausgewählten Baseline und schreibt diese in die Jtable
	 * @throws SQLException
	 */
	public void getBaseline() throws SQLException{
		Vector<Vector<String>> rows;				
		rows = gui.Baseline;
		//holt sich die Analysedaten der ausgewählten Baseline
		ResultSet rs = sqlExec.executeQuery("SELECT * FROM Analysedaten WHERE FID_Base = " + id + " ORDER BY LVL1ID ASC, LVL2ID ASC, LVL3ID ASC;");
		while(rs.next()){									
			Vector<String> row = new Vector<String>();
			//speichert EV, BAC_Kosten und AC_Kosten als Double ab, da mit ihnen der Trend und der Fertigstellungsgrad berechnet werden
			double EV = rs.getDouble("EV");
			double BAC_Kosten = rs.getDouble("BAC_Kosten");
			double AC_Kosten = rs.getDouble("AC_Kosten");
			double BAC = rs.getDouble("BAC");
			double AC = rs.getDouble("AC");
			double ETC = rs.getDouble("ETC");
			
			
			//Schreibt die Werte in die Tabelle, gerundet auf 2 Dezimalstellen nach dem Komma
			row.addElement(Integer.toString(rs.getInt("LVL1ID")));
			row.addElement(Integer.toString(rs.getInt("LVL2ID")));
			row.addElement(Integer.toString(rs.getInt("LVL3ID")));
			row.addElement(rs.getString("LVLxID"));	
			row.addElement(rs.getString("Name"));
			row.addElement(decform.format(BAC).replace(".", ","));
			row.addElement(decform.format(AC).replace(".", ","));
			row.addElement(decform.format(ETC).replace(".", ","));
			row.addElement(decform.format(rs.getDouble("CPI")));
			row.addElement(decform.format(BAC_Kosten).replace(".", ",") + " €");
			row.addElement(decform.format(AC_Kosten).replace(".", ",") + " €");
			row.addElement(decform.format(rs.getDouble("ETC_Kosten")) + " €");
			row.addElement(decform.format(rs.getDouble("EAC")) + " €");
			row.addElement(decform.format(EV).replace(".", ",") + " €");
			//Berechnung des Trends
			if(AC_Kosten>0){
				row.addElement(decform.format(EV/AC_Kosten));	
			}
			else{
				row.addElement("0,00");
			}	
			//Berechnung des Fertigstellungsgrades

			int stat = 0;
			if(BAC>0 && ETC==0)
				stat = 100;
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
			
			row.addElement(Integer.toString(stat));	
			
			rows.add(row);						
		}
		rs.close();
	}
	
	
}
