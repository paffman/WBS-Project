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
 * setzt in die Zelle eine JProgressBar und setzt den Wert der ProgressBar anhand des Wertes in der Zelle
 * 
 * @author Daniel Metzler
 * @version 1.9- 17.02.2011
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * wird von dem Konstruktor der Klasse WBSBaseline mit der Fertigstellungs-Spalte aufgerufen
 * fügt eine JProgressBar in die Zelle und setzt den Status auf den in der Zelle abgelegten IntegerWert
 */
public class StatusCellRenderer implements TableCellRenderer {
	private JProgressBar progress;
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	int intValue = Integer.parseInt((String) value);
        if (progress == null) {
        	//JProgressBar wird erzeugt
        	progress = new JProgressBar();
        }
        //Progressbar wird auf den Wert IntegerWert der Zelle gesetzt
        progress.setValue(intValue);
        progress.setBackground(table.getBackground());
        progress.setStringPainted(true);
        return progress;
    }
}