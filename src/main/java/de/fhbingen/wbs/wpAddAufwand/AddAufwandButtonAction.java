package de.fhbingen.wbs.wpAddAufwand;

import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
 * ButtonActions der AddAufwandGUI werden hier verwaltet
 *
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 */
public class AddAufwandButtonAction {

	private AddAufwand addaufwand;
	/**
	 * Konstruktor
	 * @param addaufwand - Objekt vom Typ AddAufwand
	 */
	public AddAufwandButtonAction(AddAufwand addaufwand) {
		this.addaufwand = addaufwand;
		addButtonAction();
	}

	/**
	 * fügt actionListener zum "Schließen", "Editieren" Buttons und den Keylistener zu dem Textfeld "Aufwand" hinzu
	*/
	public void addButtonAction(){
		addaufwand.gui.btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(addaufwand.checkFieldsFilled()){
					try {
						if(!addaufwand.addAufwand())
							JOptionPane.showMessageDialog(addaufwand.gui,
                                    LocalizedStrings.getErrorMessages().checkInputs());
						else {
							//berechnet den neuen ETC und schreibt den Wert in das Textfeld ETC der WPShow damit die Werte mit dem neu errechneten ETC berechnet werden
							double ETC = addaufwand.wpshow.getETCfromGUI() - Double.parseDouble(addaufwand.gui.txfAufwand.getText());
							addaufwand.wpshow.updateETCInGUI(ETC);
							//Methode setChanges der Klasse WPShow aktualisiert die neuen Werte und berechnet alles neu mit dem neuen Aufwand und ETC
							//JOptionPane.showMessageDialog(addaufwand.gui, "Aufwand wurde erfolgreich eingetragen");
							addaufwand.wpshow.save();
							addaufwand.gui.dispose();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					JOptionPane.showMessageDialog(addaufwand.gui,
                            LocalizedStrings.getMessages().fillAllFieldsError(),null,
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		addaufwand.gui.btnSchliessen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addaufwand.gui.dispose();
			}
		});

		addaufwand.gui.txfAufwand.addKeyListener(new KeyAdapter() {
			public void  keyPressed(KeyEvent e) {
				//Kommas werden direkt durch Punkte ersetzt, damit es zu keine Fehlereingaben kommt
				addaufwand.gui.txfAufwand.setText(addaufwand.gui.txfAufwand.getText().replace(",", "."));
			}
			public void keyReleased(KeyEvent e){
				//Es darf nur ein Punkt gesetzt sein, ansonsten wird der letzte Punkt gelöscht
				boolean vorhanden = false;
				StringBuffer test = new StringBuffer(addaufwand.gui.txfAufwand.getText());
				for (int i=0; i<test.length(); i++){
					if(test.charAt(i) == '.'){
						if(vorhanden){
							test.deleteCharAt(i);
						}
						vorhanden = true;

					}
				}
				addaufwand.gui.txfAufwand.setText(test.toString());
				char c = e.getKeyChar();
				//es werden nur Ziffern, Kommas und Punkte bei der Eingabe aktzepiert
				if(!(Character.isDigit(e.getKeyChar()) || c == KeyEvent.VK_COMMA || c== KeyEvent.VK_PERIOD)){
					addaufwand.gui.txfAufwand.setText(addaufwand.gui.txfAufwand.getText().substring(0,addaufwand.gui.txfAufwand.getText().length()-1));
				}
				addaufwand.gui.txfAufwand.setText(addaufwand.gui.txfAufwand.getText().replace(",", "."));
			}

		});
	}
}
