package chooseDB;

import javax.swing.*;
import java.awt.*;

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

    protected static final long serialVersionUID = 1L;
    protected JButton okButton, closeButton;
    protected GridBagLayout gbl;
    protected JMenuBar mainMenuBar;
    protected JMenu fileMenu, helpMenu;
    protected JMenuItem okMenuItem, openMenuItem, closeMenuItem, helpMenuItem,
	    infoMenuItem, newDbMenuItem;
    protected JCheckBox plCheckBox;
    protected JTextField hostField, dbNameField, userField;
    protected JPasswordField dbPwPasswordField, pwPasswordField;

    // Icons fürs Menü
    private ImageIcon hilfe = new ImageIcon(Toolkit.getDefaultToolkit()
	    .getImage(this.getClass().getResource("/_icons/hilfe.png")));
    private ImageIcon info = new ImageIcon(Toolkit.getDefaultToolkit()
	    .getImage(this.getClass().getResource("/_icons/info.png")));
    private ImageIcon weiter = new ImageIcon(Toolkit.getDefaultToolkit()
	    .getImage(this.getClass().getResource("/_icons/weiter.png")));
    private ImageIcon schlies = new ImageIcon(Toolkit.getDefaultToolkit()
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

	// Menues
	mainMenuBar = new JMenuBar();
	fileMenu = new JMenu("Datei");
	helpMenu = new JMenu("Hilfe");
	okMenuItem = new JMenuItem("Weiter");
	okMenuItem.setIcon(weiter);
	closeMenuItem = new JMenuItem("Schlie�en");
	closeMenuItem.setIcon(schlies);
	helpMenuItem = new JMenuItem("Hilfe");
	helpMenuItem.setIcon(hilfe);
	infoMenuItem = new JMenuItem("Info");
	infoMenuItem.setIcon(info);
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

	setVisible(true);

	if (!hostField.getText().equals("")) {
	    pwPasswordField.requestFocus();
	}
    }

    /**
     * void initialize() Methode initialize zum erstellen des Layouts Gui ist
     * 500 Pixel breit und 160 Pixel lang Es wird ein GridbagLayout verwendet
     * Gui wird mittig plaziert
     */
    private void initialize() {
	int width = 400;
	int height = 280;
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
     * zum GridBagLayout
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
    private void createGBC(Component c, int x, int y, int width, int height) {
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
}
