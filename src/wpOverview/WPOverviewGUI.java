package wpOverview;
/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * GUI zum auswählen der Arbeitspakete des User
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
 * 
 */


import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class WPOverviewGUI extends JFrame{

	protected static final long serialVersionUID = 1L;
	protected BorderLayout bl;
	protected JTabbedPane tabs;
	protected JLabel lblBaseline;
	protected Vector<String> colMitarbeiter;
	protected Vector<Vector<String>> Mitarbeiter;	
	protected JScrollPane spInaktiv, spAktiv, spfertig, spAlle, spMitarbeiter;
	protected JButton btnChart,btnSchliessen, btnAddBaseline, btnShowBaseline,btnComp;
	protected JMenuBar mnbMenu; 
	protected JMenu mnDatei, mnHilfe;
	protected JMenuItem miAktualisieren, miAbmelden, miBeenden, miHilfe, miInfo, miAP, miChangePW, miDelAp, 
						miImportInitial, miOAPAktualisieren;
	protected JTree treeAktiv,treeInaktiv,treeFertig,treeAlle;
	protected boolean projektleiter;
	public JTable tblMitarbeiter;
	protected JComboBox cobChooseBaseline;
	protected JLabel lblBeschreibung;
	protected JTextField txfBeschreibung;
	//Variablen für das KontextMenü
	protected JPopupMenu treeContextMenu;
	protected JMenuItem miContAPdel, miContAPadd, miContAufw, miContReassign;
	protected static JLabel lblStatusbar;
	protected Legende legende;
	
	//Icons, die in den Menüs / Menübar verwendet werden
	private ImageIcon newAp = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/newAP.png")));
	private ImageIcon delAP = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/delAP.png")));
	private ImageIcon akt = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/aktualisieren.png")));
	private ImageIcon aktoap = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/OAPaktualisieren.png")));
	private ImageIcon dbcalc = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/DBberechnen.png")));
	private ImageIcon schlies = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/schliessen.png")));
	private ImageIcon pw = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/pw.png")));
	private ImageIcon abmeld = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/abmeld.png")));
	private ImageIcon hilfe = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/hilfe.png")));
	private ImageIcon info = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/info.png")));
	private ImageIcon aufw = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/aufwand.png")));
	private ImageIcon reass = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/reassign.png")));
	
	//JTree CellRenderer - ist für die einzelnen Einträge im Baum verantwortlich z.B. bei der fürbung
	private TreeCellRenderer renderer = new TreeCellRenderer();
	protected TreeDragSource dsAktiv, dsAlle;
	protected TreeDropTarget dtAktiv, dtAlle;
	
	
	/**
	 * Konstuktor DBOverviewGUI()
	 * Main-Frame bekommt den Namen "WP-Overview" zugewiesen
	 * es wird das Windows Look and Feel verwendet
	 * die verschiedenen Menüs, Buttons etc. werden initialisiert
	 * es wird ein TabbedPane verwendet um die aktiven und inaktiven Arbeitspaket in verschieden Tabs anzuzeigen
	 * und jedes zu TabbedPane wird das GridBagLayout hinzugefügt
	 */
	public WPOverviewGUI(boolean projektleiter, DefaultMutableTreeNode rootAktiv, DefaultMutableTreeNode rootInaktiv, DefaultMutableTreeNode rootFertig, DefaultMutableTreeNode rootAlle){
		super("WP-Overview");
		this.projektleiter = projektleiter;
		initialize();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE );
		//setResizable(false);
		int width = 1024;
		int height = 700;
		//minimale Fenstergröße eintragen
		setSize(1024,700);
		//Fenster mittig platzieren
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		setLocation((int)(screenSize.getWidth()/2)-width/2,(int)(screenSize.getHeight()/2)-height/2);
		//Verhalten beim Maximieren festlegen
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Could not load LookAndFeel");
		}
		
		lblBaseline = new JLabel("Bitte Baseline auswählen");
		
        mnbMenu = new JMenuBar();
        mnDatei = new JMenu("Datei");
        mnHilfe = new JMenu("Hilfe");
        miAktualisieren = new JMenuItem("Aktualisieren");
        miAktualisieren.setIcon(akt);
        miOAPAktualisieren = new JMenuItem("OAPAktualisieren");
        miOAPAktualisieren.setIcon(aktoap);
        miChangePW = new JMenuItem("Passwort ändern");
        miChangePW.setIcon(pw);
        miAbmelden = new JMenuItem("Abmelden");
        miAbmelden.setIcon(abmeld);
        miBeenden = new JMenuItem("Schließen");
        miBeenden.setIcon(schlies);
        miHilfe = new JMenuItem("Hilfe");
        miHilfe.setIcon(hilfe);
        miInfo = new JMenuItem("Info");
        miInfo.setIcon(info);
        
        //Menüeinträge für Projektleiter
        if(projektleiter) {
        	miAP = new JMenuItem("neues Arbeitspaket");
        	miAP.setIcon(newAp);
        	miDelAp = new JMenuItem("Arbeitspaket löschen");
        	miDelAp.setIcon(newAp);
        	miImportInitial =  new JMenuItem("Importierte DB berechnen");
        	miImportInitial.setIcon(dbcalc);
        }
        	
        
        mnbMenu.add(mnDatei);
        mnbMenu.add(mnHilfe);
        
        if(projektleiter) {
        	//mnDatei.add(miAP);	
        	//mnDatei.add(miDelAp);
        	mnDatei.add(miImportInitial);
        }
        
        //mnDatei.add(miAktualisieren);
        mnDatei.add(miOAPAktualisieren);
        mnDatei.add(miChangePW);
        mnDatei.add(miAbmelden);
        mnDatei.addSeparator();
        mnDatei.add(miBeenden);
        mnHilfe.add(miHilfe);
        mnHilfe.addSeparator();
        mnHilfe.add(miInfo);
        setJMenuBar(mnbMenu);		
		
        txfBeschreibung = new JTextField(70);
        txfBeschreibung.setMinimumSize(new Dimension(250,18));
        lblBeschreibung = new JLabel("Baseline Beschreibung");
        
		//btnAddMitarbeiter = new JButton("Mitarbeiter Hinzufügen");
		btnAddBaseline = new JButton("Neue Baseline anlegen");
		btnShowBaseline = new JButton("Baseline anzeigen");
		btnChart=new JButton("CPI-Diagramm anzeigen");
		btnComp=new JButton("Fertigstellung anzeigen");
		
		cobChooseBaseline = new JComboBox();
			
		colMitarbeiter = new Vector<String>();		
		colMitarbeiter.add("Login");
		colMitarbeiter.add("Vorname");
		colMitarbeiter.add("Name");
		colMitarbeiter.add("Berechtigung");
		colMitarbeiter.add("Tagessatz");
		
		
		//Tree KontextMenü anlegen
        treeContextMenu = new JPopupMenu();
        miContAufw = new JMenuItem("Aufwand eintragen");
        treeContextMenu.add(miContAufw);
        miContAufw.setIcon(aufw);

		if(projektleiter){
			treeContextMenu.addSeparator();
			miContAPadd = new JMenuItem("neues Arbeitspaket einfügen");
			miContAPadd.setIcon(newAp);
			treeContextMenu.add(miContAPadd);
			treeContextMenu.addSeparator();
			miContAPdel = new JMenuItem("Arbeitspaket löschen");
			miContAPdel.setIcon(delAP);
			treeContextMenu.add(miContAPdel);
			treeContextMenu.addSeparator();
			miContReassign = new JMenuItem("Arbeitspaket rekursiv einfügen");
			miContReassign.setIcon(reass);
			treeContextMenu.add(miContReassign);
		}

		
		
		//Bäume anlegen
		treeInaktiv=new JTree(rootInaktiv);
		treeInaktiv.setCellRenderer(renderer);
		treeInaktiv.setModel(new DefaultTreeModel(rootInaktiv));
	    spInaktiv = new JScrollPane(treeInaktiv);
		 
        treeAktiv=new JTree(rootAktiv);
        treeAktiv.setCellRenderer(renderer);
        treeAktiv.setModel(new DefaultTreeModel(rootAktiv));
        spAktiv = new JScrollPane(treeAktiv);
              
        treeFertig=new JTree(rootFertig);
        treeFertig.setCellRenderer(renderer);
        treeFertig.setModel(new DefaultTreeModel(rootFertig));
        spfertig = new JScrollPane(treeFertig);
        
        treeAlle=new JTree(rootAlle);
        treeAlle.setCellRenderer(renderer);
        treeAlle.setModel(new DefaultTreeModel(rootFertig));
        spAlle = new JScrollPane(treeAlle);
        
        lblStatusbar = new JLabel(" ");
        lblStatusbar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        
        //Panel, welches Schließen Button und Statusleiste entHält, wird BorderLayout.SOUTH gesetzt
        JPanel south = new JPanel();
        legende = new Legende();
        btnSchliessen = new JButton("Schließen");
        south.setLayout(new GridLayout(3,1));
        south.add(legende);
        south.add(btnSchliessen);
        south.add(lblStatusbar);
        
        //Mitarbeiter Tabelle definieren
        Mitarbeiter = new Vector<Vector<String>>();
        tblMitarbeiter = new JTable(Mitarbeiter, colMitarbeiter); 
        tblMitarbeiter.setPreferredScrollableViewportSize(new Dimension(500, 300));
        tblMitarbeiter.setFillsViewportHeight(true);
        spMitarbeiter = new JScrollPane(tblMitarbeiter);
        
		
        
        //Alle Tabs definieren
        tabs = new JTabbedPane();
        tabs.addTab("offene Arbeitspakete", createPanelAktiv());
        tabs.addTab("inaktive Arbeitspakete", createPanelInaktiv());
        tabs.addTab("fertige Arbeitspakete", createPanelFertig());
        tabs.addTab("alle Arbeitspakete", createPanelAlle());
        
        //Tabs anlegen, die nur Projektleiter sehen können
        if(projektleiter){
        	tabs.addTab("Mitarbeiter", createPanelNeueMitarbeiter());
        	tabs.addTab("Baseline", createPanelNeueBaseline());
        }
        
       
        
        this.add(tabs, BorderLayout.CENTER);
        this.add(south, BorderLayout.SOUTH);
        
		setVisible(true);   
	}
	
	
	/**
	 * Legt das Tab "Inaktiv" an -  Wird vom Konstruktor aufgerufen
	 * @return gibt den Panel für die inaktive Arbeitspakettabelle zurück
	 */
	protected JComponent createPanelInaktiv() {
        JPanel panel = new JPanel(false);
        bl = new BorderLayout(20,20);
        panel.setLayout(bl);
        panel.add(spInaktiv,BorderLayout.CENTER);
        //panel.add(btnSchliessen, BorderLayout.SOUTH);
        return panel;
    }
	
	/**
	 * Legt das Tab "Aktiv" an -  Wird vom Konstruktor aufgerufen
	 * @return gibt den Panel für die aktive Arbeitspakettabelle zurück
	 */
	protected JComponent createPanelAktiv() {
        JPanel panel = new JPanel(false);
        bl = new BorderLayout(20,20);
        panel.setLayout(bl);
        panel.add(spAktiv,BorderLayout.CENTER);
        //panel.add(btnSchliessen, BorderLayout.SOUTH);
        return panel;
    }
	
	
	/**
	 * Legt das Tab "Aktiv" an -  Wird vom Konstruktor aufgerufen
	 * @return gibt den Panel für die fertige Arbeitspakettabelle zurück
	 */
	protected JComponent createPanelFertig() {
        JPanel panel = new JPanel(false);
        bl = new BorderLayout(20,20);
        panel.setLayout(bl);
        panel.add(spfertig,BorderLayout.CENTER);
        //panel.add(btnSchliessen, BorderLayout.SOUTH);
        return panel;
    }
	
	
	/**
	 * Legt das Tab "Alle AP" an -  Wird vom Konstruktor aufgerufen
	 * @return gibt den Panel für die fertige Arbeitspakettabelle zurück
	 */
	protected JComponent createPanelAlle() {
        JPanel panel = new JPanel(false);
        bl = new BorderLayout(20,20);
        panel.setLayout(bl);
        panel.add(spAlle,BorderLayout.CENTER);
       // panel.add(btnSchliessen, BorderLayout.SOUTH);
        return panel;
    }

	
	
	/**
	 * Legt das Tab "Mitarbeiter" an -  Wird vom Konstruktor aufgerufen
	 * @return gibt den Panel für die Mitarbeitertabelle zurück
	 */	
	protected JComponent createPanelNeueMitarbeiter() {
		JPanel panel = new JPanel(false);
		bl = new BorderLayout(2,2);
		panel.setLayout(bl);
        panel.add(spMitarbeiter,BorderLayout.CENTER);
        //panel.add(btnAddMitarbeiter,BorderLayout.SOUTH);
        return panel;
    }

	/**
	 * Legt das Tab "Neue Baseline" an -  Wird vom Konstruktor aufgerufen
	 * @return gibt den Panel für die Baseline zurück
	 */
	protected JComponent createPanelNeueBaseline() {

		JPanel anzeige=new JPanel();
	
		JPanel panel = new JPanel(false);
		panel.setSize(new Dimension(400,200));
		GridBagLayout gbl = new GridBagLayout();
		panel.setLayout(gbl);
				
		addComponent(panel,gbl,lblBaseline, 		0, 0, 1, 1);
		addComponent(panel,gbl,cobChooseBaseline, 	1, 0, 1, 1);
		addComponent(panel,gbl,btnShowBaseline, 	2, 0, 1, 1);
		addComponent(panel,gbl,lblBeschreibung, 	0, 1, 1, 1);
		addComponent(panel,gbl,txfBeschreibung, 	1, 1, 1, 1);
		addComponent(panel,gbl,btnAddBaseline, 		2, 1, 1, 1);
		addComponent(panel,gbl,btnChart, 			1, 2, 1, 1);
		addComponent(panel,gbl,btnComp, 			1, 3, 1, 1);
		
		anzeige.setLayout(new BorderLayout(10,10));
		anzeige.add(BorderLayout.BEFORE_FIRST_LINE,panel);
		
        return anzeige;
    }
	/**
	 * void addComponent(args)
	 * wird am Anordnen der Komponenten auf dem JPanel aufgerufen
	 * Methode addComponent zum Hinzufügen der einzelnen Komponenten zum GridBagLayout
	 * @param cont		Container - aktueller Container auf den das Gridlayout hinzugefügt werden soll
	 * @param gbl		Aktuelles Gridlayout
	 * @param c  		Komponente die zum Layout hinzugefügt wird
	 * @param x	 		Anordnung auf der X-Achse (Breite)
	 * @param y			Anordnung auf der Y-Achse (Länge)
	 * @param width		Breite des Elementes
	 * @param height	Höhe des Elementes
	 */
	private void addComponent( Container cont,GridBagLayout gbl,
             Component c,int x, int y,int width, int height){
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.NONE;
				gbc.anchor = GridBagConstraints.CENTER;
				gbc.gridx = x; 
				gbc.gridy = y;
				gbc.gridwidth = width; 
				gbc.gridheight = height;
				gbc.insets = new Insets(2,2,2,2);  
				gbl.setConstraints( c, gbc );
				cont.add( c );
	 }


	/**
	 * void initialize()
	 * Methode initialize zum erstellen des Layouts
	 * Gui ist 610 Pixel breit und 500 Pixel lang
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	
	/**
	 * ändert den Text in der Statusbar
	 * @param str String zur Anzeige in der Statusbar
	 */
	public static void setStatusbar(String str){
		lblStatusbar.setText(str);
	}
}
