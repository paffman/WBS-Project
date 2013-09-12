package login;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Funktionalität für LoginGUI
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.1 - 30.11.2010
 */

import globals.InfoBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import jdbcConnection.MDBConnect;
import chooseDB.DBChooser;

public class LoginButtonAction {

	private Login login;
	
	public LoginButtonAction(Login login){
		this.login = login;
		addButtonAction();
	}
	
	/**
	 * void addButtonAction()
	 * fügt actionListener zum "Schließen" und "Anmelden" Button hinzu
	 * fügt actionListener für das Menü hinzu (Anmelden, Schließen, Info und Hilfe
	 * 
	 * Schließen-Button/Menü:	beendet das Programm
	 * Anmelde-Button/Menü:		Führt Methode checkUser() aus
	 * Info-Menü:				gibt Angaben per JOptionPane über das Projekt aus
	 * Hilfe-Menü:				gibt per JOptionPane eine Hilfe für die DBLoginGUI aus
	*/
	public void addButtonAction(){
		login.gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				login.gui.dispose();
			}
		});
		
		login.gui.btnSchliessen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		login.gui.miBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		login.gui.btnAnmelden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login.checkUser();
			}
		});
		
		login.gui.miAnmelden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login.checkUser();
			}
		});
		
		login.gui.miDBAendern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				MDBConnect.setPathDB(" ");
				new DBChooser();
				login.gui.dispose();
			}
		});
		
		login.gui.miHilfe.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				JOptionPane.showMessageDialog(login.gui, "Bitte Benutzernamen und Passwort angeben. Dann gelangen Sie mit \"Anmelden\" zur Übersicht der Arbeitspakete");
			}
		});
		
		login.gui.miInfo.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {			
				new InfoBox();		
			}
		});
	}
}
