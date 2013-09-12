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
 * ButtonAction fuer die Baselinedetailansicht
 * 
 * @author Andre Paffenholz und Daniel Metzler
 * @version 1.9- 17.02.2011
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WBSBaselineButtonAction {

	private WBSBaseline wbsBaseline;
	/**
	 * Konstruktor
	 * @param wbsBaseline - WBSBaseline Objekt, ueber das die WBSBaselineGUI erzeugt worden ist
	 */
	public WBSBaselineButtonAction(WBSBaseline wbsBaseline) {
		this.wbsBaseline = wbsBaseline;
		addButtonAction();
	}

	/**
	 * void addButtonAction()
	 * fügt actionListener zum "Schließen" Button hinzu
	 */
	public void addButtonAction(){
		wbsBaseline.gui.btnSchliessen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wbsBaseline.gui.dispose();	
			}
		});			
	}
		
}
