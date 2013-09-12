package chooseDB;

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
 * GUI zur Anzeige des MDBAuswahl Fensters 
 * Verbindet zu einer gegebenen Microsoft Access Database Datei
 * 
 * @author Samson von Graevenitz, Daniel Metzler, Andre Paffenholz
 * @version 0.3- .12.2010
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

import javax.swing.*;

import java.awt.*;

public class DBChooserGUI extends JFrame{

	protected static final long serialVersionUID = 1L;
	protected JLabel lblMDB;									
	protected JButton btnOeffnen, btnWeiter, btnSchliessen;		
	protected JFileChooser fileIn;
	protected JComboBox cobDBAuswahl;
	protected GridBagLayout gbl;
	protected JMenuBar mnbMenu; 
	protected JMenu mnDatei, mnHilfe;
	protected JMenuItem miWeiter, miPfadoeffnen, miBeenden, miHilfe, miInfo;
	
	//Icons fürs Menü
	private ImageIcon hilfe = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/hilfe.png")));
	private ImageIcon info = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/info.png")));
	private ImageIcon weiter = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/weiter.png")));
	private ImageIcon schlies = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/schliessen.png")));
	private ImageIcon oeffnen = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/oeffnen.png")));

	/**
	 * Konstuktor DBChooserGUI()
	 * Main-Frame bekommt den Namen "WBS-File" zugewiesen
	 * es wird das Windows Look and Feel verwendet
	 * die verschiedenen Menüs, Buttons etc. werden initialisiert
	 * und zu dem GridBagLayout hinzugefügt und angeordnet mittels createGbc
	 */
	public DBChooserGUI(){
		super("Datenbank auswählen");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Could not load LookAndFeel");
		}	
		initialize();
		setResizable(false);
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			
		
        /**
         * Menüs
         */
		mnbMenu = new JMenuBar();
        mnDatei = new JMenu("Datei");
        mnHilfe = new JMenu("Hilfe");  
        miWeiter = new JMenuItem("Weiter");
        miWeiter.setIcon(weiter);
        miPfadoeffnen = new JMenuItem("Pfad öffnen");
        miPfadoeffnen.setIcon(oeffnen);
        miBeenden = new JMenuItem("Schließen");
        miBeenden.setIcon(schlies);
        miHilfe = new JMenuItem("Hilfe");
        miHilfe.setIcon(hilfe);
        miInfo = new JMenuItem("Info");
        miInfo.setIcon(info);
       
        mnbMenu.add(mnDatei);
        mnDatei.add(miWeiter);
        mnDatei.add(miPfadoeffnen);
        mnDatei.addSeparator();
        mnDatei.add(miBeenden); 
        mnbMenu.add(mnHilfe);
        mnHilfe.add(miHilfe);
        mnHilfe.addSeparator();
        mnHilfe.add(miInfo);
        
        //MDB-Auswahl
		lblMDB = new JLabel("Pfad der MDB");
		
		cobDBAuswahl = new JComboBox();
		
		cobDBAuswahl.setEditable(true);
		btnOeffnen = new JButton("Öffnen");
		btnSchliessen = new JButton("Schließen");
		btnWeiter = new JButton("Weiter");	
	
		this.setJMenuBar(mnbMenu);
		createGBC(lblMDB, 0, 0, 5, 1);
		createGBC(cobDBAuswahl, 0, 1, 3, 1);
		createGBC(btnOeffnen, 3, 1, 1, 1);
		createGBC(btnWeiter, 0, 2, 3, 1);
		createGBC(btnSchliessen, 3, 2, 1, 1);
		cobDBAuswahl.setPreferredSize(new Dimension(100, 20));
		setVisible(true);
	}
	
	/**
	 * void initialize()
	 * Methode initialize zum erstellen des Layouts
	 * Gui ist 500 Pixel breit und 160 Pixel lang
	 * Es wird ein GridbagLayout verwendet
	 * Gui wird mittig plaziert
	 */
	private void initialize() {
		int width = 500;
		int height = 160;
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		setLocation((int)(screenSize.getWidth()/2)-width/2,(int)(screenSize.getHeight()/2)-height/2);
		
		setSize(new Dimension(width, height));
        gbl = new GridBagLayout();
		getContentPane().setLayout(gbl);
	}
	
	/**
	 * void createGBC(args)
	 * wird am Anordnen der Komponenten auf dem JFrame aufgerufen
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
