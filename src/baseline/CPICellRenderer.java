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
 * färbt die Zelle des CPI anhand des Wertes ein
 * setzt den richtigen Font der Zelle, anhand der Ebene des Oberarbeitspaketes
 * 
 * @author Daniel Metzler
 * @version 1.9- 17.02.2011
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;


class CPICellRenderer extends DefaultTableCellRenderer{ 
		  	 
	private static final long serialVersionUID = 1L;
 	
	/**
	 * wird von dem Konstruktor der Klasse WBSBaseline mit der CPI-Spalte aufgerufen
	 * färbt die Zelle der Tabelle entsprechend des dort eingebenen Double Wertes in die richtigen Farben
	 */
	public Component getTableCellRendererComponent(JTable aTable, Object CPI, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn){  
		Component renderer = super.getTableCellRendererComponent(aTable, CPI, aIsSelected, aHasFocus, aRow, aColumn);
	    String replace = CPI.toString().replace(",", ".");
	    
	    //prüft ob es ein Oberarbeitspaket von mind. der Ebene 1 ist (oder höher) --> Schrift wird dick
	    if(aTable.getValueAt(aRow, 1).toString().equals("0") && aTable.getValueAt(aRow, 2).toString().equals("0")){
	    	renderer.setFont(new Font("Times New Roman", Font.BOLD, 14));
	    }
	    else{
	    	renderer.setFont(new Font("Times New Roman", Font.ITALIC, 12));
	    }
	    if(Double.parseDouble(aTable.getValueAt(aRow, 6).toString().replace(",", "."))==0.){
	    	renderer.setForeground(Color.black);
	    	renderer.setBackground(Color.WHITE);
	    }
	    else{
	    	double dcpi = Double.parseDouble(replace);
		    if(dcpi<0.97){	
		    	//von 0.97 bis 0.94 wird die Zelle gelb gefärbt und die Schrift schwarz
		    	renderer.setForeground(Color.black);
		    	renderer.setBackground(Color.YELLOW);
				if(dcpi<0.94){
					//von 0.6 bis 0.94 wird die Zelle rot gefärbt und die Schrift schwarz
					renderer.setForeground(Color.black);
					renderer.setBackground(Color.RED);
				}
				if(dcpi<0.6){
					//von 0 bis 0.6 wird die Zelle dunkelrot gefärbt und die Schrift weiß
					renderer.setForeground(Color.white);
					renderer.setBackground(new Color(80, 00, 00));
				}
							
			}
			else{
				if(dcpi>1.03){
					//von 1.3 bis unendlich wird die Zelle dunkelgrün gefärbt und die Schrift weiß
					renderer.setForeground(Color.white);
					renderer.setBackground(new Color(00, 80, 00));
				}
				else{
					//von 0.97 bis 1.03 wird die Zelle grün gefärbt und die Schrift schwarz
					renderer.setForeground(Color.black);
					renderer.setBackground(Color.GREEN);
				}
			}
		    if (renderer instanceof JLabel) {
	            JLabel l = (JLabel) renderer;
	                l.setHorizontalAlignment(JLabel.RIGHT);
	        }
	    }

	    return renderer;
	}
}
