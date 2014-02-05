package wpWorker;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * ButtonAction der WBSMitarbeiter
 * 
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 * Aenderungen by Marc-Eric Baumgaertner
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WBSUserButtonAction {

	private WBSUser mitarbeiter;

	public WBSUserButtonAction(WBSUser Mitarbeiter) {
		this.mitarbeiter = Mitarbeiter;
		addButtonAction();
	}

	/**
	 * fügt actionListener zum "Schließen", "Reset", "Hinzufügen", "Editieren" Buttons und den Keylistener zu dem Textfeld "Tagessatz" hinzu
	 */
	public void addButtonAction(){
		mitarbeiter.gui.btnedit.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {				
				if(mitarbeiter.check()) {
					int rights; 
					if(mitarbeiter.gui.cbBerechtigung.isSelected()){
						rights = 1;
					}
					else{
						rights = 0;
					}
					Worker actualWorker = mitarbeiter.getActualWorker();
					actualWorker.setName(mitarbeiter.gui.txfName.getText());
					actualWorker.setVorname(mitarbeiter.gui.txfVorname.getText());
					actualWorker.setBerechtigung(rights);
					actualWorker.setTagessatz(Double.parseDouble(mitarbeiter.gui.txfTagessatz.getText()));
					mitarbeiter.changeMitarbeiter(actualWorker);
					mitarbeiter.gui.dispose();		
				}				
			}
		});
		
		mitarbeiter.gui.btnschliessen.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {	
				mitarbeiter.gui.dispose();
			}
		});
		
		mitarbeiter.gui.btnPwReset.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {	
				//Mitarbeiter wird aus der Datenbank geholt
				mitarbeiter.passwordReset();
				mitarbeiter.gui.dispose();
			}
		});
		
		mitarbeiter.gui.btnhinzufuegen.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				if(mitarbeiter.check()) {
					int rights; 
					if(mitarbeiter.gui.cbBerechtigung.isSelected()){
						rights = 1;
					}
					else{
						rights = 0;
					}
					mitarbeiter.addMitarbeiter(new Worker(mitarbeiter.gui.txfLogin.getText(), mitarbeiter.gui.txfVorname.getText(), mitarbeiter.gui.txfName.getText(), rights, Double.parseDouble(mitarbeiter.gui.txfTagessatz.getText())));
					mitarbeiter.gui.dispose();	
				}
			}
		});
		
	}}
