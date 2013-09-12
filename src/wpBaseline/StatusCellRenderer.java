package wpBaseline;


import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Studienprojekt:	WBS<br/>
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:	Andre Paffenholz, <br/>
 * 						Peter Lange, <br/>
 * 						Daniel Metzler,<br/>
 * 						Samson von Graevenitz<br/>
 * 
 * setzt in die Zelle eine JProgressBar und setzt den Wert der ProgressBar anhand des Wertes in der Zelle<br/>
 * wird von dem Konstruktor der Klasse WBSBaseline mit der Fertigstellungs-Spalte aufgerufen<br/>
 * fügt eine JProgressBar in die Zelle und setzt den Status auf den in der Zelle abgelegten IntegerWert<br/>
 * 
 * @author Daniel Metzler
 * @version 1.9- 17.02.2011
 */
public class StatusCellRenderer implements TableCellRenderer {
	private JProgressBar progress;
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	int intValue = Integer.parseInt(value.toString().replace(',','.'));
        if (progress == null) {
        	//JProgressBar wird erzeugt
        	progress = new JProgressBar();
        }

        //Progressbar wird auf den Wert IntegerWert der Zelle gesetzt
        progress.setValue(intValue);
        return progress;
    }
}