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
 * CellRenderer zum Einfürben der Baseline
 * 
 * @author Daniel Metzler
 */

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;


class ColorCellRenderer extends DefaultTableCellRenderer{ 
		  	 
	private static final long serialVersionUID = 1L;
 	
	/**
	 * wird von dem Konstruktor der Klasse WBSBaseline mit der CPI-Spalte aufgerufen
	 * färbt die Zelle der Tabelle entsprechend des dort eingebenen Double Wertes in die richtigen Farben
	 */
	public Component getTableCellRendererComponent(JTable aTable, Object CPI, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn){  
		Component renderer = super.getTableCellRendererComponent(aTable, CPI, aIsSelected, aHasFocus, aRow, aColumn);
	    renderer.setBackground(Color.LIGHT_GRAY);
	    return renderer;
	}		  		  
} 
