package wpWorker;

import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import c10n.C10N;
import de.fhbingen.wbs.translation.Messages;
import jdbcConnection.SQLExecuter;

import wpOverview.WPOverviewGUI;

public class ChangePWButtonAction {

	private ChangePW changepw;
    private final Messages messages;

    public ChangePWButtonAction(ChangePW changepw) {
        messages = LocalizedStrings.getMessages();
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
				/*if(changepw.checkFieldsFilled()){
					//ResultSet mit aktuellem User
					ResultSet user = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter " +
															"WHERE Login= '" + changepw.usr.getLogin() +"';" );
					//Prüfung ob "altes Passwort korrekt"
					if(changepw.checkOldPW(user)){
						if(changepw.checkNewPW()){
							//setzen des neuen Passwortes
							changepw.setNewPassword(user);
							WPOverviewGUI.setStatusText(messages.passwordChangeConfirm());
							changepw.gui.dispose();

						}else{
							JOptionPane.showMessageDialog(changepw.gui,
                                    messages.passwordsNotMatchingError(),
                                    null,
									JOptionPane.INFORMATION_MESSAGE);
						}
					} else{
						JOptionPane.showMessageDialog(changepw.gui,
                                messages.passwordOldWrong(),null,
								JOptionPane.INFORMATION_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(changepw.gui, messages.fillAllFieldsError(),
                            null,
							JOptionPane.INFORMATION_MESSAGE);
				}*/

			}
		});

		changepw.gui.btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changepw.gui.dispose();
			}
		});

	}
}
