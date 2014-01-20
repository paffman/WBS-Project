package chooseDB;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Studienprojekt: WBS<br/>
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: Andre Paffenholz, <br/>
 * Peter Lange, <br/>
 * Daniel Metzler,<br/>
 * Samson von Graevenitz<br/>
 * 
 * 
 * GUI zur Anzeige des MDBAuswahl Fensters Verbindet zu einer gegebenen
 * Microsoft Access Database Datei
 * 
 * Studienprojekt: PSYS WBS 2.0<br/>
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: <br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * 
 * @author Samson von Graevenitz, Daniel Metzler, Andre Paffenholz, Lin Yang
 * @version 2.0 - 2012-08-22
 * 
 *          Die verwendeten Icons stammen von:
 *          http://sublink.ca/icons/sweetieplus/ sowie:
 *          http://http://p.yusukekamiyamane.com/
 * 
 *          Diagona Icons
 * 
 *          Copyright (C) 2007 Yusuke Kamiyamane. All rights reserved. The icons
 *          are licensed under a Creative Commons Attribution 3.0 license.
 *          <http://creativecommons.org/licenses/by/3.0/>
 * 
 *          Sweetieplus: Sie unterliegen der Creative Commons Licence: This
 *          licence allows you to use the icons in any client work, or
 *          commercial products such as WordPress themes or applications.
 */
public class DBChooserGUI extends JFrame {

    /**
     * generated serialVersionUID.
     */
    protected static final long serialVersionUID = 1L;
    /**
     * Height of the window.
     */
    private static final int WINDOW_HEIGHT = 280;
    /**
     * Width of the window.
     */
    private static final int WINDOW_WIDTH = 400;
    /**
     * Button to confirm db connection entries and start the wbs-tool.
     */
    private JButton okButton;
    /**
     * Button to close the window.
     */
    private JButton closeButton;
    /**
     * The GridBagLayout for the window.
     */
    private GridBagLayout gbl;
    /**
     * The MenuBar for the window.
     */
    private JMenuBar mainMenuBar;
    /**
     * File-Menu of the MenuBar.
     */
    private JMenu fileMenu;
    /**
     * Help-Menu of the MenuBar.
     */
    private JMenu helpMenu;
    /**
     * Items of the Menues.
     */
    private JMenuItem okMenuItem, openMenuItem, closeMenuItem, helpMenuItem,
	    infoMenuItem, newDbMenuItem;
    /**
     * Checkbox for project leader login.
     */
    private JCheckBox plCheckBox;
    /**
     * Fields for the database connection.
     */
    private JTextField hostField, dbNameField, userField;
    /**
     * PasswordFields for the passwords needed for a database connection.
     */
    private JPasswordField dbPwPasswordField, pwPasswordField;

    /**
     * Icon for help menu.
     */
    private ImageIcon helpIcon = new ImageIcon(Toolkit.getDefaultToolkit()
	    .getImage(this.getClass().getResource("/_icons/hilfe.png")));
    /**
     * Icon for info menu.
     */
    private ImageIcon infoIcon = new ImageIcon(Toolkit.getDefaultToolkit()
	    .getImage(this.getClass().getResource("/_icons/info.png")));
    /**
     * Icon for ok menu.
     */
    private ImageIcon okIcon = new ImageIcon(Toolkit.getDefaultToolkit()
	    .getImage(this.getClass().getResource("/_icons/weiter.png")));
    /**
     * Icon for close menu.
     */
    private ImageIcon closeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
	    .getImage(this.getClass().getResource("/_icons/schliessen.png")));

    /**
     * Konstuktor DBChooserGUI() Main-Frame bekommt den Namen "WBS-File"
     * zugewiesen es wird das Windows Look and Feel verwendet die verschiedenen
     * Menüs, Buttons etc. werden initialisiert und zu dem GridBagLayout
     * hinzugefügt und angeordnet mittels createGbc
     * 
     * @param dbChooser
     *            callin dbChooser, used to fill login data with data from last
     *            used db.
     */
    public DBChooserGUI(final DBChooser dbChooser) {
	super("Login");
	try {
	    UIManager
		    .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    SwingUtilities.updateComponentTreeUI(this);
	} catch (Exception e) {
	    System.err.println("Could not load LookAndFeel");
	}
	initialize();
	setResizable(false);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// menus
	mainMenuBar = new JMenuBar();
	fileMenu = new JMenu("Datei");
	helpMenu = new JMenu("Hilfe");
	okMenuItem = new JMenuItem("Weiter");
	okMenuItem.setIcon(okIcon);
	closeMenuItem = new JMenuItem("Schlie�en");
	closeMenuItem.setIcon(closeIcon);
	helpMenuItem = new JMenuItem("Hilfe");
	helpMenuItem.setIcon(helpIcon);
	infoMenuItem = new JMenuItem("Info");
	infoMenuItem.setIcon(infoIcon);
	newDbMenuItem = new JMenuItem("DB Einrichtungsassistent");

	mainMenuBar.add(fileMenu);
	fileMenu.add(newDbMenuItem);
	fileMenu.add(okMenuItem);
	fileMenu.addSeparator();
	fileMenu.add(closeMenuItem);
	mainMenuBar.add(helpMenu);
	helpMenu.add(helpMenuItem);
	helpMenu.addSeparator();
	helpMenu.add(infoMenuItem);

	// labels and input elements
	JLabel titleLabel = new JLabel("Datenbank:");
	JLabel hostLabel = new JLabel("Host:");
	JLabel dbNameLabel = new JLabel("DB-Name:");
	JLabel dbPwLabel = new JLabel("Index Password:");
	JLabel titleUserLabel = new JLabel("Benutzer:");
	JLabel userLabel = new JLabel("Benutzername:");
	JLabel pwLabel = new JLabel("Password:");
	plCheckBox = new JCheckBox("Projektleiter");
	hostField = new JTextField();
	dbNameField = new JTextField();
	userField = new JTextField();
	dbPwPasswordField = new JPasswordField();
	pwPasswordField = new JPasswordField();
	closeButton = new JButton("Schlie�en");
	okButton = new JButton("Ok");

	// load saved database into fields
	if (dbChooser.getLastDbHost() != null) {
	    hostField.setText(dbChooser.getLastDbHost());
	}
	if (dbChooser.getLastDbName() != null) {
	    dbNameField.setText(dbChooser.getLastDbName());
	}
	if (dbChooser.getLastDbIndexPw() != null) {
	    dbPwPasswordField.setText(dbChooser.getLastDbIndexPw());
	}
	if (dbChooser.getLastDbUser() != null) {
	    userField.setText(dbChooser.getLastDbUser());
	}

	// place all elements in the window.
	this.setJMenuBar(mainMenuBar);
	createGBC(titleLabel, 0, 0, 4, 1);
	createGBC(hostLabel, 0, 1, 1, 1);
	createGBC(hostField, 1, 1, 3, 1);
	createGBC(dbNameLabel, 0, 2, 1, 1);
	createGBC(dbNameField, 1, 2, 3, 1);
	createGBC(dbPwLabel, 0, 3, 1, 1);
	createGBC(dbPwPasswordField, 1, 3, 3, 1);
	createGBC(titleUserLabel, 0, 4, 4, 1);
	createGBC(userLabel, 0, 5, 1, 1);
	createGBC(userField, 1, 5, 3, 1);
	createGBC(pwLabel, 0, 6, 1, 1);
	createGBC(pwPasswordField, 1, 6, 3, 1);
	createGBC(plCheckBox, 0, 7, 1, 1);
	createGBC(okButton, 2, 8, 1, 1);
	createGBC(closeButton, 3, 8, 1, 1);

	// show gui
	setVisible(true);

	// set Focus to password field if the host is already set and therefore
	// a saved database has been loaded.
	if (!hostField.getText().equals("")) {
	    pwPasswordField.requestFocus();
	}
    }

    /**
     * Method to create the gui. A GridBagLayout is used for the positioning of
     * the elements.
     */
    private void initialize() {
	int width = WINDOW_WIDTH;
	int height = WINDOW_HEIGHT;
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension screenSize = tk.getScreenSize();
	setLocation((int) (screenSize.getWidth() / 2) - width / 2,
		(int) (screenSize.getHeight() / 2) - height / 2);

	setSize(new Dimension(width, height));
	gbl = new GridBagLayout();
	getContentPane().setLayout(gbl);
    }

    /**
     * void createGBC(args) wird am Anordnen der Komponenten auf dem JFrame
     * aufgerufen Methode createGBC zum Hinzufügen der einzelnen Komponenten
     * zum GridBagLayout.
     * 
     * @param c
     *            Komponente die zum Layout hinzugefügt wird
     * @param x
     *            Anordnung auf der X-Achse (Breite)
     * @param y
     *            Anordnung auf der Y-Achse (Länge)
     * @param width
     *            Breite des Elementes
     * @param height
     *            Höhe des Elementes
     */
    private void createGBC(final Component c, final int x, final int y,
	    final int width, final int height) {
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.gridx = x;
	gbc.gridy = y;
	gbc.gridwidth = width;
	gbc.weightx = width;
	gbc.weighty = height;
	gbc.gridheight = height;
	gbc.insets = new Insets(5, 5, 5, 5);
	gbl.setConstraints(c, gbc);
	add(c);
    }

    /**
     * @return the okButton
     */
    protected final JButton getOkButton() {
	return okButton;
    }

    /**
     * @return the closeButton
     */
    protected final JButton getCloseButton() {
	return closeButton;
    }

    /**
     * @return the okMenuItem
     */
    protected final JMenuItem getOkMenuItem() {
	return okMenuItem;
    }

    /**
     * @return the openMenuItem
     */
    protected final JMenuItem getOpenMenuItem() {
	return openMenuItem;
    }

    /**
     * @return the closeMenuItem
     */
    protected final JMenuItem getCloseMenuItem() {
	return closeMenuItem;
    }

    /**
     * @return the helpMenuItem
     */
    protected final JMenuItem getHelpMenuItem() {
	return helpMenuItem;
    }

    /**
     * @return the infoMenuItem
     */
    protected final JMenuItem getInfoMenuItem() {
	return infoMenuItem;
    }

    /**
     * @return the newDbMenuItem
     */
    protected final JMenuItem getNewDbMenuItem() {
	return newDbMenuItem;
    }

    /**
     * @return the plCheckBox
     */
    protected final JCheckBox getPlCheckBox() {
	return plCheckBox;
    }

    /**
     * @return the hostField
     */
    protected final JTextField getHostField() {
	return hostField;
    }

    /**
     * @return the dbNameField
     */
    protected final JTextField getDbNameField() {
	return dbNameField;
    }

    /**
     * @return the userField
     */
    protected final JTextField getUserField() {
	return userField;
    }

    /**
     * @return the dbPwPasswordField
     */
    protected final JPasswordField getDbPwPasswordField() {
	return dbPwPasswordField;
    }

    /**
     * @return the pwPasswordField
     */
    protected final JPasswordField getPwPasswordField() {
	return pwPasswordField;
    }
}
