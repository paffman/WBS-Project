package login;


import globals.FilterJTextField;

import java.awt.*;

import javax.swing.*;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * 
 * GUI zur Anzeige des Login Fensters 
 * Verbindet zu einer gegebenen Microsoft Access Database Datei um Benutzer Login zu überprüfen
 * 
 * @author Samson von Graevenitz, Daniel Metzler, Andre Paffenholz
 * @version 0.1 - 30.11.2010
 * 
 * 
 * Die verwendeten Icons stammen von: http://sublink.ca/icons/sweetieplus/
 * sowie: http://http://p.yusukekamiyamane.com/
 * 
 * Diagona Icons
 *
 * Copyright (C) 2007 Yusuke Kamiyamane. All rights reserved.
 * The icons are licensed under a Creative Commons Attribution
 * 3.0 license. <http://creativecommons.org/licenses/by/3.0/>
 * 
 * Sweetieplus:
 * Sie unterliegen der Creative Commons Licence:
 * This licence allows you to use the icons in any client work, or commercial products such as WordPress themes or applications.
 */
public class LoginGUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	protected JLabel lblLogin, lblPassword, lblProjLeiter;
	protected JTextField txfLogin;
	protected JPasswordField txfPassword;
	protected JButton btnAnmelden, btnSchliessen;
	protected JCheckBox chbProjLeiter;
	protected GridBagLayout gbl;
	protected JMenuBar mnbMenu; 
	protected JMenu mnDatei, mnHilfe;
	protected JMenuItem miAnmelden, miDBAendern, miBeenden, miHilfe, miInfo;
	
	//Icons fürs Menü
	private ImageIcon hilfe = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/hilfe.png")));
	private ImageIcon info = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/info.png")));
	private ImageIcon weiter = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/weiter.png")));
	private ImageIcon schlies = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/schliessen.png")));
	private ImageIcon dbaender = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/abmeld.png")));

	/**
	 * Konstuktor DBLoginGUI()
	 * Main-Frame bekommt den Namen "WBS-Login" zugewiesen
	 * es wird das Windows Look and Feel verwendet
	 * die verschiedenen Menüs, Buttons etc. werden initialisiert
	 * und zu dem GridBagLayout hinzugefügt und angeordnet mittels createGbc
	 */
	public LoginGUI(){
		super("WBS-Login");
		initialize();
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Could not load LookAndFeel");
		}
		

        mnbMenu = new JMenuBar();
        mnDatei = new JMenu("Datei");
        mnHilfe = new JMenu("Hilfe");
        miAnmelden = new JMenuItem("Anmelden");
        miAnmelden.setIcon(weiter);
        miDBAendern = new JMenuItem("DB ändern");
        miDBAendern.setIcon(dbaender);
        miBeenden = new JMenuItem("Schließen");
        miBeenden.setIcon(schlies);
        miHilfe = new JMenuItem("Hilfe");
        miHilfe.setIcon(hilfe);
        miInfo = new JMenuItem("Info");
        miInfo.setIcon(info);
        
        mnbMenu.add(mnDatei);
        mnbMenu.add(mnHilfe);
        mnDatei.add(miAnmelden);
        mnDatei.add(miDBAendern);
        mnDatei.addSeparator();
        mnDatei.add(miBeenden);
        mnHilfe.add(miHilfe);
        mnHilfe.addSeparator();
        mnHilfe.add(miInfo);
		
		lblLogin = new JLabel("Login: ");
		txfLogin = new FilterJTextField(20);		
		lblPassword = new JLabel("Password: ");
		txfPassword = new JPasswordField();
		lblProjLeiter = new JLabel("Projektleiter");
		chbProjLeiter = new JCheckBox();
		
		//password.setEditable(false);	
		btnAnmelden = new JButton("Anmelden");
		//ok.setEnabled(false);
		btnSchliessen = new JButton("Schließen");
		
		setJMenuBar(mnbMenu);
		createGBC(lblLogin, 0, 0, 1, 1);
		createGBC(txfLogin, 1, 0, 1, 1);
		createGBC(lblPassword, 0, 1, 1, 1);
		createGBC(txfPassword, 1, 1, 1, 1);
		createGBC(lblProjLeiter, 0, 2, 1, 1);
		createGBC(chbProjLeiter, 1, 2, 1, 1);
		createGBC(btnAnmelden, 0, 3, 1, 1);
		createGBC(btnSchliessen, 1, 3, 1, 1);				
	}
	
	/**
	 * void initialize()
	 * Methode initialize zum erstellen des Layouts
	 * Gui ist 300 Pixel breit und 200 Pixel lang
	 * Es wird ein GridbagLayout verwendet
	 */
	private void initialize() {
		int width = 300;
		int height = 200;
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		setLocation((int)(screenSize.getWidth()/2)-width/2,(int)(screenSize.getHeight()/2)-height/2);
		
		this.setSize(new Dimension(width, height));
        gbl = new GridBagLayout();
		getContentPane().setLayout(gbl);
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
	  	gbc.insets = new Insets(5,5,5,5);
		gbl.setConstraints(c, gbc);
		add(c);
	}

	
}
