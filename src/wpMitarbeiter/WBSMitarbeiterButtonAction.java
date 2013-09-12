package wpMitarbeiter;

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
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class WBSMitarbeiterButtonAction {
	
	private WBSMitarbeiter mitarbeiter;

	public WBSMitarbeiterButtonAction(WBSMitarbeiter Mitarbeiter){
		this.mitarbeiter = Mitarbeiter;
		addButtonAction();
	}
	
	/**
	 * fügt actionListener zum "Schließen", "Reset", "Hinzufügen", "Editieren" Buttons und den Keylistener zu dem Textfeld "Tagessatz" hinzu
	*/
	public void addButtonAction(){
		mitarbeiter.gui.btnedit.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {				
				if(mitarbeiter.changeMitarbeiter()){
					//OverviewGUI wird mit den aktuellen Daten des geänderten Mitarbeiter aktuallisiert, wird jetzt in der Tabelle angezeigt
					mitarbeiter.Overview.mitarbeiterAktualisieren();
					mitarbeiter.sqlExec.closeConnection();
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
				ResultSet rs = mitarbeiter.sqlExec.executeQuery("SELECT * FROM Mitarbeiter WHERE Login = '" + mitarbeiter.mitarbeiter.getLogin() + "';");
				try {
					//Passwort wird auf "1234" zurückgesetzt
					while(rs.next()){
						rs.updateString("Passwort", "1234");
						rs.updateRow();
						JOptionPane.showMessageDialog(mitarbeiter.gui, "Das Passwort wurde auf \"1234\" zurückgesetzt!");
					}
					rs.close();
				} catch (SQLException e) {				
					e.printStackTrace();
				}finally{
					mitarbeiter.sqlExec.closeConnection();
				}
				mitarbeiter.gui.dispose();
			}
		});
		
		
		mitarbeiter.gui.txfTagessatz.addKeyListener(new KeyAdapter() {			
			public void  keyPressed(KeyEvent e) {	
				//Kommas werden direkt durch Punkte ersetzt, damit es zu keine Fehlereingaben kommt
				mitarbeiter.gui.txfTagessatz.setText(mitarbeiter.gui.txfTagessatz.getText().replace(",", "."));		
			}
			public void keyReleased(KeyEvent e){
				//Es darf nur ein Punkt gesetzt sein, ansonsten wird der letzte Punkt gelöscht
				boolean vorhanden = false;
				StringBuffer test = new StringBuffer(mitarbeiter.gui.txfTagessatz.getText());
				for (int i=0; i<test.length(); i++){	
					if(test.charAt(i) == '.'){
						if(vorhanden){
							test.deleteCharAt(i);
						}
						vorhanden = true;		
						
					}
				}
				mitarbeiter.gui.txfTagessatz.setText(test.toString());				
				char c = e.getKeyChar();
				//es werden nur Ziffern, Kommas und Punkte bei der Eingabe aktzepiert
				if(!(Character.isDigit(e.getKeyChar()) || c == KeyEvent.VK_COMMA || c== KeyEvent.VK_PERIOD)){
					mitarbeiter.gui.txfTagessatz.setText(mitarbeiter.gui.txfTagessatz.getText().substring(0,mitarbeiter.gui.txfTagessatz.getText().length()-1));							
				}
				mitarbeiter.gui.txfTagessatz.setText(mitarbeiter.gui.txfTagessatz.getText().replace(",", "."));
			}
					
		});		

		
		mitarbeiter.gui.btnhinzufuegen.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {			
				if(!mitarbeiter.gui.txfTagessatz.getText().equals("")){
					if(Double.parseDouble(mitarbeiter.gui.txfTagessatz.getText()) > 0){
						if(mitarbeiter.addMitarbeiter()){
							//JOptionPane.showMessageDialog(mitarbeiter.gui, "Mitarbeiter wurde eingetragen!!");
							//OverviewGUI wird dem neuen Mitarbeiter aktuallisiert, wird jetzt in der Tabelle angezeigt
							mitarbeiter.Overview.mitarbeiterAktualisieren();
							mitarbeiter.gui.dispose();
						}
					} else{
						JOptionPane.showMessageDialog(mitarbeiter.gui, "Tagessatz darf nicht 0.00 € sein.");
					}
				}else{
					JOptionPane.showMessageDialog(mitarbeiter.gui, "Tagessatz darf nicht leer sein.");
				}
			}
		});
		
	}	
}
