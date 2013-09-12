package globals;
/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * ButtonAction fuer die Infobox
 * 
 * @author Peter Lange
 * @version 1.0- 16.02.2011
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoBoxButtonAction {
	
	private InfoBox infobox;
/**
 * Konstruktor
 * @param infobox - das aufrufende Element, das den "schliessen" Button beinhaltet
 */
	public InfoBoxButtonAction(InfoBox infobox){
		this.infobox=infobox;
		addButtonAction();
	}
	/**
	 * methode um dem Schliessen Button seinen ActionListener zuzuweisen
	 */
	public void addButtonAction(){
		infobox.close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				infobox.dispose();
			}
		});
	}

}
