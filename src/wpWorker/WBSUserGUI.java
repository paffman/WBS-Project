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
 * GUI zum Hinzufügen eines Aufwandes zum Arbeitspaket
 * 
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 */

import globals.FilterJTextField;

import java.awt.*;
import javax.swing.*;

public class WBSUserGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	protected GridBagLayout gbl;
	protected JLabel lblLogin, lblVorname, lblName, lblBerechtigung, lblTagessatz;	
	protected JTextField txfLogin, txfVorname, txfName, txfTagessatz;	
	protected JButton btnedit, btnhinzufuegen, btnschliessen, btnPwReset;	
	protected JCheckBox cbBerechtigung;
	
	/**
	 * Main-Frame bekommt den Namen "WPMitarbeiter" zugewiesen
	 * es wird das Windows Look and Feel verwendet
	 * die verschiedenen Menüs, Buttons etc. werden initialisiert
	 * und zu dem GridBagLayout hinzugefügt und angeordnet mittels createGbc
	 */
	public WBSUserGUI() {
		super("WPMitarbeiter");
		initialize();
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Could not load LookAndFeel");
		}
		
		lblLogin = new JLabel("Loginname:");
		txfLogin = new FilterJTextField();
		
		lblVorname = new JLabel("Vorname");
		txfVorname = new FilterJTextField();
		
		lblName = new JLabel("Nachname:");
		txfName = new FilterJTextField();
		
		lblBerechtigung = new JLabel("Projektleiterberechtigung: ");
		cbBerechtigung = new JCheckBox();			
		
		lblTagessatz = new JLabel("Tagessatz:");
		txfTagessatz = new FilterJTextField();
		
		btnedit = new JButton("ändern");
		btnhinzufuegen = new JButton("Hinzufügen");
		btnschliessen = new JButton("Schließen");
		btnPwReset = new JButton("Passwort zurücksetzen");
		
		createGBC(lblLogin, 			0, 0, 1, 1);
		createGBC(txfLogin, 			1, 0, 1, 1);
		createGBC(lblVorname,			0, 1, 1, 1);
		createGBC(txfVorname,			1, 1, 1, 1);
		createGBC(lblName, 				0, 2, 1, 1);
		createGBC(txfName, 				1, 2, 1, 1);
		createGBC(lblBerechtigung, 		0, 3, 1, 1);
		createGBC(cbBerechtigung,	 	1, 3, 1, 1);
		createGBC(lblTagessatz, 		0, 4, 1, 1);
		createGBC(txfTagessatz, 		1, 4, 1, 1);
		createGBC(btnedit, 				0, 5, 1, 1);
		createGBC(btnhinzufuegen, 		0, 5, 1, 1);
		createGBC(btnschliessen, 		1, 5, 1, 1);
		createGBC(btnPwReset, 			0, 6, 1, 1);
		
		setVisible(true);
	}
	
	
	/**
	 * Methode initialize zum erstellen des Layouts
	 * Gui ist 300 Pixel breit und 200 Pixel lang
	 * Es wird ein GridbagLayout verwendet
	 * Gui wird mittig plaziert
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
	  	gbc.insets = new Insets(1,1,1,1);
		gbl.setConstraints(c, gbc);
		add(c);
	}
}
