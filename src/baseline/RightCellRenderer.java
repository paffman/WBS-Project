/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * setzt die Zellen die mit dieser Methode aufgerufen werden rechtsbündig
 * setzt den richtigen Font der Zelle, anhand der Ebene des Oberarbeitspaketes
 * 
 * @author Daniel Metzler
 * @version 1.9- 17.02.2011
 */

package baseline;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class RightCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * wird von dem Konstruktor der Klasse WBSBaseline mit der CPI-Spalte aufgerufen
	 * färbt die Zelle der Tabelle entsprechend der Ebene und rückt die Werte rechtsbündig
	 */
	public Component getTableCellRendererComponent(JTable aTable, Object CPI, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn){  
		Component renderer = super.getTableCellRendererComponent(aTable, CPI, aIsSelected, aHasFocus, aRow, aColumn);

		if (renderer instanceof JLabel) {
			JLabel l = (JLabel) renderer;          
        	l.setHorizontalAlignment(JLabel.RIGHT);          
		}
		
		if(aTable.getValueAt(aRow, 1).toString().equals("0") && aTable.getValueAt(aRow, 2).toString().equals("0")){
	    	renderer.setFont(new Font("Times New Roman", Font.BOLD, 14));
	    	renderer.setBackground(Color.GRAY);
	    }
	    else{
	    	renderer.setFont(new Font("Times New Roman", Font.ITALIC, 12));
	    	renderer.setBackground(Color.LIGHT_GRAY);
	    }
		
				
			

        return this;
    }
}