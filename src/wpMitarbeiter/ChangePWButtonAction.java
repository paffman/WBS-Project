package wpMitarbeiter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import wpOverview.WPOverviewGUI;

public class ChangePWButtonAction {

	private ChangePW changepw;
	
	public ChangePWButtonAction(ChangePW changepw) {
		this.changepw = changepw;
		addButtonAction();
	}

	/**
	 * fügt actionListener zum "Ok" und "Cancel" Buttons hinzu und ruft entsprechend notwenidge
	 * Methoden auf
	 */
	public void addButtonAction(){
		changepw.gui.txfUser.setText(changepw.usr.getName());
		
		changepw.gui.btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Prüfung ob alle Felder ausgefüllt
				if(changepw.checkFieldsFilled()){
					//ResultSet mit aktuellem User
					ResultSet user = changepw.sqlExec.executeQuery("SELECT * FROM Mitarbeiter " +
															"WHERE Login= '" + changepw.usr.getLogin() +"';" );
					//Prüfung ob "altes Passwort korrekt"
					if(changepw.checkOldPW(user)){
						if(changepw.checkNewPW()){
							//setzen des neuen Passwortes
							changepw.setNewPassword(user);
							WPOverviewGUI.setStatusbar("Passwort geändert");
							changepw.gui.dispose();
							
						}else{
							JOptionPane.showMessageDialog(changepw.gui, "Passwort und Bestätigung stimmen nicht überein",null,
									JOptionPane.INFORMATION_MESSAGE);
						}
					} else{
						JOptionPane.showMessageDialog(changepw.gui, "altes Passwort inkorrekt",null,
								JOptionPane.INFORMATION_MESSAGE);
					}
					changepw.sqlExec.closeConnection();
				}else{
					JOptionPane.showMessageDialog(changepw.gui, "Felder wurden nicht vollständig eingegeben",null,
							JOptionPane.INFORMATION_MESSAGE);
				}
			
			}
		});
		
		changepw.gui.btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changepw.gui.dispose();
			}
		});
		
	}
}
