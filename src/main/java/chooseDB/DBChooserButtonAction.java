package chooseDB;

import globals.InfoBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * fügt Funktionalitäten zur DBChooserGUI hinzu
 * 
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.3 - 09.12.2010
 */
public class DBChooserButtonAction {

	private DBChooser dbchooser;
	
	/**
	 * Konstruktor
	 * @param dbchooser
	 */
	public DBChooserButtonAction(DBChooser dbchooser) {
		this.dbchooser = dbchooser;
		addButtonAction();
		dbchooser.fillCobDB();
	}

	/**
	 * void addButtonAction()
	 * fügt actionListener zum "Schließen" und "Weiter" Buttons hinzu
	 * fügt actionListener für das Menü hinzu (Weiter, Oeffnen, Schließen, Info und Hilfe
	 * 
	 * Schließen-Button/Menü:	beendet das Programm
	 * Weiter-Button/Menü:		führt Methode weiter() aus
	 * Oeffnen-Button/Menü:		führt Methode oeffnen() aus
	 * Info-Menü:				gibt Angaben per JOptionPane über das Projekt aus
	 * Hilfe-Menü:				gibt per JOptionPane eine Hilfe für die DBChooserGUI aus
	*/
	public void addButtonAction(){
		dbchooser.gui.btnSchliessen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		dbchooser.gui.btnWeiter.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				dbchooser.next();
			}
		});
		
		dbchooser.gui.btnOeffnen.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				dbchooser.open();				
			}
		});
		
		dbchooser.gui.miPfadoeffnen.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				dbchooser.open();				
			}
		});
		
		dbchooser.gui.miWeiter.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				dbchooser.next();
			}
		});
		
		dbchooser.gui.miBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		dbchooser.gui.miHilfe.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				JOptionPane.showMessageDialog(dbchooser.gui, "Zuerst muss der richtige Pfad angegeben werden. Dann gelangen Sie mit \"Weiter\" zum Login");				
			}
		});
		
		dbchooser.gui.miInfo.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				new InfoBox();		
			}
		});
	}
}
