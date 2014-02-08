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
 * GUI ändern des eigenen Passwortes
 *
 * @author Samson von Graevenitz
 * @version 0.5 - 28.11.2010
 */

import c10n.C10N;
import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Login;
import de.fhbingen.wbs.translation.Messages;
import globals.FilterJTextField;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.*;

public class ChangePWGUI extends JFrame{//TODO extend JDialog
    private static final long serialVersionUID = 1L;
    private final Login login;
    private final Messages messages;
    private final Button buttons;
    protected GridBagLayout gbl;

	public JLabel lblUser, lblOldPW, lblNewPW, lblNewPWConfirm;
	public JTextField txfUser;
	public JPasswordField txfOldPW, txfNewPW, txfNewPWConfirm;
	public JButton btnOk, btnCancel;

	/**
	 * Main-Frame bekommt den Namen "Passwort ändern" zugewiesen
	 * es wird das Windows Look and Feel verwendet
	 * die verschiedenen Menüs, Buttons etc. werden initialisiert
	 * und zu dem GridBagLayout hinzugefügt und angeordnet mittels createGbc
	 */
	public ChangePWGUI(){
		super();
        login = LocalizedStrings.getLogin();
        messages = LocalizedStrings.getMessages();
        buttons = LocalizedStrings.getButton();
        setTitle(login.changePassword());
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try { //TODO wird das nicht schon woanders gesetzt?
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
            System.err.println(messages.couldNotLoadLookAndFeel());
        }
		initialize();

		lblUser = new JLabel("Aktueller User");
		txfUser = new FilterJTextField();
		txfUser.setEnabled(false);
		lblOldPW = new JLabel(login.oldPassword());
		txfOldPW = new JPasswordField();
		lblNewPW = new JLabel(login.newPassword());
		txfNewPW = new JPasswordField();
		lblNewPWConfirm = new JLabel(login.repeatPassword());
		txfNewPWConfirm = new JPasswordField();
		btnOk = new JButton(buttons.ok());
		btnCancel = new JButton(buttons.cancel());


		createGBC(lblUser, 0, 0, 1, 1);
		createGBC(txfUser, 1, 0, 1, 1);
		createGBC(lblOldPW, 0, 1, 1, 1);
		createGBC(txfOldPW, 1, 1, 1, 1);
		createGBC(lblNewPW, 0, 2, 1, 1);
		createGBC(txfNewPW, 1, 2, 1, 1);
		createGBC(lblNewPWConfirm, 0, 3, 1, 1);
		createGBC(txfNewPWConfirm, 1, 3, 1, 1);
		createGBC(btnOk, 0, 4, 1, 1);
		createGBC(btnCancel, 1, 4, 1, 1);
	}

	/**
	 * Methode initialize zum Layout erstellen
	 * Es wird ein GridbagLayout verwendet
	 * GUI wird mittig platziert
	 */
	private void initialize() {
		int width = 300;
		int height = 150;
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		setLocation((int)(screenSize.getWidth()/2)-width/2,(int)(screenSize.getHeight()/2)-height/2);

		this.setSize(new Dimension(width, height));
        gbl = new GridBagLayout();
		getContentPane().setLayout(gbl);
		setVisible(true);
	}

	/**
	 * void createGBC(args)
	 * Methode createGBC zum Hinzufügen der einzelnen Komponenten zum GridBagLayout
	 * @param c  		Komponente die zum Layout hinzugefügt wird
	 * @param x	 		Anordnung auf der X-Achse (Breite)
	 * @param y			Anordnung auf der Y-Achse (Länge)
	 * @param width		Breite des Elementes
	 * @param height	Höhe des Elementes
	 */
	private void createGBC(Component c, int x, int y, int width, int height){
	  	GridBagConstraints gbc = new GridBagConstraints();
	  	gbc.fill=GridBagConstraints.HORIZONTAL;
	  	gbc.gridx = x;
	  	gbc.gridy = y;
	  	gbc.gridwidth = width;
	  	gbc.weightx =width;
	  	gbc.weighty = height;
	  	gbc.gridheight = height;
	  	gbc.insets = new Insets(1,1,1,1);
		gbl.setConstraints(c, gbc);
		add(c);
	}
}
