package wpShow;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * GUI zum Anzeigen und ändern der Arbeitspakete
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.1 - 30.11.2010
 */

import java.awt.*;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

public class WPShowGUI extends JFrame{
	
	protected static final long serialVersionUID = 1L;	
	
	protected JLabel lblID, lblName, lblSpec, lblLeiter, lblZustaendige, lblRelease, lblCPI, lblBAC,
	  				lblAC, lblETC, lblStatus, lblWPTagessatz, lblBACcost, lblACcost, lblEV, lblEAC, lblETCcost,
	  				lblInaktiv, lblIstOAP;
	protected static JLabel lblStatusbar;
	
	protected JTextField txfNr, txfName, txfZustaendige, txfRelease, txfCPI, txfBAC, txfAC, txfWPTagessatz,
	  					txfBACcost, txfACcost, txfEV, txfEAC, txfETCcost;
	
	public JTextField txfETC;
	protected JProgressBar pgbalken;
	protected JTextArea txfSpec;
	public JCheckBox chbIstOAP, chbInaktiv;
	protected JComboBox cobMitarbeiter, cobaddZustaendige, cobRemoveZustaendige;
	protected JButton btnEdit, btnCancel, btnAddZust, btnRemoveZust, btnAddAufw, btnNewWP;
	protected JMenuBar mnbMenu; 
	protected JMenu mnDatei, mnHilfe;
	protected JMenuItem miEdit, miBeenden, miHilfe, miInfo;
	protected GridBagLayout gbl;
	protected boolean newWP;
	public JTable tblAufwand;
	protected JScrollPane spAufwand;
	protected Vector<String> colAufwand;
	protected Vector<Vector<String>> aufwand;	
	protected WPShow dies;
	
	//Icons, die in den Menüs / Menübar verwendet werden
	private ImageIcon schlies = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/schliessen.png")));
	private ImageIcon hilfe = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/hilfe.png")));
	private ImageIcon info = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/info.png")));
	private ImageIcon aufw = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/_icons/aufwand.png")));
	
	
	/**
	 * Konstuktor DBShowGUI()
	 * es wird das Windows Look and Feel verwendet
	 * die verschiedenen Menüs, Buttons und Textfelder etc. werden initialisiert
	 * und zu dem GridBagLayout hinzugefügt und angeordnet mittels createGbc
	 */
	public WPShowGUI(boolean isNewWp, WPShow dies){
		
		super("WP-Show");
		this.dies = dies;
		newWP = isNewWp;
		initialize();
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Could not load LookAndFeel");
		}

		//Aufwände in AP-Ansicht
		colAufwand = new Vector<String>();
		if(this.dies.usr.getProjLeiter()){
			colAufwand.add("Name");
		}
		colAufwand.add("Aufwand");
		colAufwand.add("Datum");
		colAufwand.add("Beschreibung");
		
		aufwand = new Vector<Vector<String>>();
		tblAufwand = new JTable(aufwand, colAufwand); 
	    tblAufwand.setPreferredScrollableViewportSize(new Dimension(500, 300));
	    tblAufwand.setFillsViewportHeight(true);
	    spAufwand = new JScrollPane(tblAufwand);
	    
	    if(this.dies.usr.getProjLeiter()){
	    	tblAufwand.getColumnModel().getColumn(0).setMaxWidth(80);
		    tblAufwand.getColumnModel().getColumn(1).setMaxWidth(55);
		    tblAufwand.getColumnModel().getColumn(2).setMaxWidth(65);
	    }
	    else{
	    	 tblAufwand.getColumnModel().getColumn(0).setMaxWidth(55);
			 tblAufwand.getColumnModel().getColumn(1).setMaxWidth(65);
	    }
	    
		//Menü
        mnbMenu = new JMenuBar();
        mnDatei = new JMenu("Datei");
        mnHilfe = new JMenu("Hilfe");
        miEdit = new JMenuItem("Editieren");
        miEdit.setIcon(aufw);
        miBeenden = new JMenuItem("Schließen");
        miBeenden.setIcon(schlies);
        miHilfe = new JMenuItem("Hilfe");
        miHilfe.setIcon(hilfe);
        miInfo = new JMenuItem("Info");
        miInfo.setIcon(info);
        
        mnbMenu.add(mnDatei);
        mnbMenu.add(mnHilfe);
        mnDatei.add(miEdit);
        mnDatei.addSeparator();
        mnDatei.add(miBeenden);
        mnHilfe.add(miHilfe);
        mnHilfe.addSeparator();
        mnHilfe.add(miInfo);
		
        //Arbeitspaket-Details
        lblID = new JLabel("ArbeitspaketID");
		txfNr = new JTextField();
		txfNr.setEnabled(false);
        
		lblName = new JLabel("Arbeitspaket Name");
		txfName = new JTextField();
		txfName.setEnabled(false);
		
		lblSpec = new JLabel("Beschreibung");
		txfSpec = new JTextArea(4,20);
		txfSpec.setLineWrap(true);
		txfSpec.setTabSize(0);
		txfSpec.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txfSpec.setBorder(new LineBorder(Color.LIGHT_GRAY,1));
		txfSpec.setEnabled(true);
		JScrollPane scpSpec = new JScrollPane(txfSpec);
		scpSpec.setMinimumSize(txfSpec.getPreferredScrollableViewportSize());
		
		
		lblLeiter = new JLabel("Mitarbeiter");
		cobMitarbeiter = new JComboBox();
		cobMitarbeiter.setEnabled(false);
		
		lblZustaendige = new JLabel("Zuständige");
		txfZustaendige = new JTextField();
		txfZustaendige.setEditable(false);
		
		cobaddZustaendige = new JComboBox();
		cobaddZustaendige.addItem("");
		cobaddZustaendige.setVisible(false);
		btnAddZust = new JButton("add");
		btnAddZust.setVisible(false);
		cobRemoveZustaendige = new JComboBox();
		cobRemoveZustaendige.addItem("");
		cobRemoveZustaendige.setVisible(false);
		btnRemoveZust = new JButton("remove");
		btnRemoveZust.setVisible(false);
				
		lblRelease = new JLabel("Releasedatum");
		
		try {
			txfRelease =  new JFormattedTextField(new MaskFormatter("##.##.####"));
			txfRelease.setEnabled(false);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		lblIstOAP = new JLabel("Oberarbeitspaket");
		lblIstOAP.setVisible(false);
		chbIstOAP = new JCheckBox();
		chbIstOAP.setVisible(false);
			
		lblCPI = new JLabel("CPI");
		txfCPI = new JTextField();
		txfCPI.setEnabled(false);
		
		lblBAC = new JLabel("BAC");
		txfBAC = new JTextField();
		txfBAC.setEnabled(false);
		
		lblAC = new JLabel("AC");
		txfAC = new JTextField();
		txfAC.setEnabled(false);
		
		lblETC = new JLabel("ETC");
		txfETC = new JTextField();

		lblStatus = new JLabel("Status");
		pgbalken = new JProgressBar();
		pgbalken.setOrientation(JProgressBar.HORIZONTAL);
		pgbalken.setMaximum(100);
		pgbalken.setMinimum(0);
		pgbalken.setStringPainted(true);
		
		lblWPTagessatz = new JLabel("WP Tagessatz");
		txfWPTagessatz = new JTextField();
		txfWPTagessatz.setEnabled(false);
		
		lblBACcost = new JLabel("BAC Kosten");
		txfBACcost = new JTextField();
		txfBACcost.setEnabled(false);
		
		lblACcost = new JLabel("AC Kosten");
		txfACcost = new JTextField();
		txfACcost.setEnabled(false);
		
		lblEV = new JLabel("EV");
		txfEV = new JTextField();
		txfEV.setEnabled(false);
		
		lblEAC = new JLabel("EAC");
		txfEAC = new JTextField();
		txfEAC.setEnabled(false);
		
		lblETCcost = new JLabel("ETC Kosten");
		txfETCcost = new JTextField();
		txfETCcost.setEnabled(false);
		
		lblInaktiv = new JLabel("Inaktiv");
		lblInaktiv.setVisible(false);
		chbInaktiv = new JCheckBox();
		chbInaktiv.setVisible(false);
		
		//Prüfen, ob es sich um ein neues AP handelt oder ein bestehendes
		if(newWP)
			btnNewWP = new JButton("Speichern");
		else
			btnEdit = new JButton("Editieren");
		
		
		btnAddAufw = new JButton("Aufwand eingeben");	
		btnCancel = new JButton("Cancel");
		
		lblStatusbar = new JLabel(" ");
		lblStatusbar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		setJMenuBar(mnbMenu);
		gbl = new GridBagLayout();
		
		JPanel show = new JPanel();
		
		show.setMinimumSize(new Dimension(400, 350));
		show.setLayout(gbl);	
		
		createGBC(show, lblID, 0, 0, 1, 1);
		createGBC(show, txfNr, 1, 0, 1, 1);
		createGBC(show, lblName, 0, 1, 1, 1);
		createGBC(show, txfName, 1, 1, 1, 1);
		createGBC(show, lblSpec, 0, 2, 1, 4);
		createGBC(show, scpSpec, 1, 2, 1, 4);
		createGBC(show, lblLeiter, 0, 7, 1, 1);
		createGBC(show, cobMitarbeiter, 1, 7, 1, 1);
		createGBC(show, lblZustaendige, 0, 8, 1, 1);
		createGBC(show, txfZustaendige, 1, 8, 1, 1);
		createGBC(show, btnAddZust, 0, 9, 1, 1);
		createGBC(show, cobaddZustaendige, 1, 9, 1, 1);
		createGBC(show, btnRemoveZust, 0, 10, 1, 1);
		createGBC(show, cobRemoveZustaendige, 1, 10, 1, 1);	
		createGBC(show, lblRelease, 0, 11, 1, 1);
		createGBC(show, txfRelease, 1, 11, 1, 1);
		createGBC(show, lblCPI, 0, 12, 1, 1);
		createGBC(show, txfCPI, 1, 12, 1, 1);	
		createGBC(show, lblBAC, 0, 13, 1, 1);
		createGBC(show, txfBAC, 1, 13, 1, 1);				
		createGBC(show, lblAC, 0, 14, 1, 1);
		createGBC(show, txfAC, 1, 14, 1, 1);
		createGBC(show, lblETC, 0, 15, 1, 1);
		createGBC(show, txfETC, 1, 15, 1, 1);	
		createGBC(show, lblStatus, 0, 16, 1, 1);
		createGBC(show, pgbalken, 1, 16, 1, 1);	
		createGBC(show, lblWPTagessatz, 0, 17, 1, 1);
		createGBC(show, txfWPTagessatz, 1, 17, 1, 1);	
		createGBC(show, lblBACcost, 0, 18, 1, 1);
		createGBC(show, txfBACcost, 1, 18, 1, 1);		
		createGBC(show, lblACcost, 0, 19, 1, 1);
		createGBC(show, txfACcost, 1, 19, 1, 1);		
		createGBC(show, lblEV, 0, 20, 1, 1);
		createGBC(show, txfEV, 1, 20, 1, 1);	
		createGBC(show, lblEAC, 0, 21, 1, 1);
		createGBC(show, txfEAC, 1, 21, 1, 1);		
		createGBC(show, lblETCcost, 0, 22, 1, 1);
		createGBC(show, txfETCcost, 1, 22, 1, 1);	
		createGBC(show, lblInaktiv, 0, 23, 1, 1);
		createGBC(show, chbInaktiv, 1, 23, 1, 1);
		createGBC(show, lblIstOAP, 0, 24, 1, 1);
		createGBC(show, chbIstOAP, 1, 24, 1, 1);
		createGBC(show, btnCancel, 1, 25, 1, 1);
		
		if(newWP)
			createGBC(show, btnNewWP, 0, 25, 1, 1);
		else		
			createGBC(show, btnEdit, 0, 25, 1, 1);
		
		
		createGBC(show, btnAddAufw, 0, 26, 1, 1);
		createGBC(show, lblStatusbar, 0, 27, 2, 1);
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(show);
		if(!this.dies.boolIstOAP){
			split.setRightComponent(spAufwand);
			this.setSize(800, 650);
		}
		else{
			this.setSize(400, 650);
		}
		add(split);
		setVisible(true);
	}
	
	/**
	 * Methode initialize zum Layout erstellen
	 * Gui ist 800 Pixel breit und 600 Pixel lang
	 * Es wird ein GridbagLayout verwendet
	 */
	private void initialize() {
		int width = 800;
		int height = 600;
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		setLocation((int)(screenSize.getWidth()/2)-width/2,(int)(screenSize.getHeight()/2)-height/2);
		
		this.setSize(new Dimension(width, height));
        
		getContentPane().setLayout(new BorderLayout());		
	}
	
	/**
	 * void createGBC(args)
	 * Methode createGBC zum Hinzufügen der einzelnen Komponenten zum GridBagLayout
	 * Wird beim Aufbau der GUI im Konstruktor aufgerufen
	 * @param c  		Komponente die zum Layout hinzugefügt wird
	 * @param x	 		Anordnung auf der X-Achse (Breite)
	 * @param y			Anordnung auf der Y-Achse (Laenge)
	 * @param width		Breite des Elementes
	 * @param height	Höhe des Elementes
	 */
	private void createGBC(JPanel frame, Component c, int x, int y, int width, int height){
	  	GridBagConstraints gbc = new GridBagConstraints();
	  	gbc.fill=GridBagConstraints.BOTH;
	  	gbc.gridx = x;                        
	  	gbc.gridy = y;  
	  	gbc.gridwidth = width;  
	  	gbc.weightx =width;
	  	gbc.weighty = height;
	  	gbc.gridheight = height;
	  	gbc.insets = new Insets(1,1,1,1);
		gbl.setConstraints(c, gbc);
		frame.add(c);
	}
	
	public static void setStatusbar(String str){
		lblStatusbar.setText(str);
	}
}