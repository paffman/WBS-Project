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
 *  Funktionalität zur GUI ChangePW
 *  prüfung aller notwendigen Bedingungen zum Passwortwechsel
 *  und Änderung des Passwortes in der DB
 *
 * @author Samson von Graevenitz
 * @version 0.5 - 28.12.2010
 */

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import c10n.C10N;
import de.fhbingen.wbs.translation.Messages;
import wpOverview.WPOverviewGUI;

import jdbcConnection.SQLExecuter;


public class ChangePW {

	/**
	 * gui - Hält eine Instanz der GUI
	 * usr - Hält den aktuellen User
	 * sqlExec - stellt die Verknüpfung zur DB her
	 */
	protected ChangePWGUI gui;
	private ChangePW dies;
	protected Worker usr;

    private final Messages messages;
	/**
	 * Konstruktoraufruf zum ändern des Passworts des aktuellen Users
	 * @param usr - aktuell eingeloggter User
	 */
	public ChangePW(Worker usr){
        messages = C10N.get(Messages.class);
        dies = this;
		this.usr = usr;
		gui = new ChangePWGUI();
		new ChangePWButtonAction(dies);
	}



	/**
	 * prüft ob alle Passwortfelder ausgefüllt sind
	 * @return true/false, je nachdem ob Felder gefüllt oder nicht
	 */
	protected boolean checkFieldsFilled(){
		if(gui.txfOldPW.getPassword().length > 0 &&
				gui.txfNewPW.getPassword().length > 0 &&
				gui.txfNewPWConfirm.getPassword().length > 0){
			return true;
		}
		return false;
	}

	/**
	 * prüft, ob das "alte Passwort" des Users mit dem übereinstimmt was
	 * in der Datenbank hinterlegt ist.
	 * @param user - ResultSet welches aktuellen User Hält
	 * @return true/false, je nach dem ob "altes Passwort" korrekt eingegeben wurde
	 */
	protected boolean checkOldPW(ResultSet user){
		String old = String.valueOf(gui.txfOldPW.getPassword());
		try {
			user.first();
			String dbOld = user.getString("Passwort");
			if(dbOld.equals(old)){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * prüft, ob das "Neue Passwort" und "Passwort Confirm" übereinstimmen
	 * @return true/false je nachdem, ob prüfung positiv oder negativ
	 */
	protected boolean checkNewPW(){
		String pw = String.valueOf(gui.txfNewPW.getPassword());
		String pwc = String.valueOf(gui.txfNewPWConfirm.getPassword());
		if(pw.equals(pwc)){
			return true;
		}
		return false;
	}

	/**
	 * Speichert das geänderte Passwort in der Datenbank ab
	 * @param user - ResultSet welches den aktuellen User Hält
	 */
	protected void setNewPassword(ResultSet user){
		String pw = String.valueOf(gui.txfNewPW.getPassword());
		try {
			user.first();
			user.updateString("Passwort", pw);
			user.updateRow();
			user.close();
			WPOverviewGUI.setStatusText(messages.passwordChangeConfirm());
			//JOptionPane.showMessageDialog(gui, "Passwort wurde geändert",null, JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(gui, messages.passwordChangeError(),null,
					JOptionPane.INFORMATION_MESSAGE);			e.printStackTrace();
		}
	}
}
