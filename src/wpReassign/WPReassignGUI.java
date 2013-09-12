package wpReassign;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * GUI Klasse zum einfügen von Arbeitspaketen in einer Unterstruktur
 * 
 * 
 * @author Andre Paffenholz
 * @version 0.1 - 13.05.2011
 */



import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.MaskFormatter;

public class WPReassignGUI extends JFrame {


	private static final long serialVersionUID = 1L;
	protected GridBagLayout gbl;
	
	protected JLabel lblEinf, lblWPName, lblBAC, lblDatum, lblMa;
	protected JTextField txfEinf, txfName, txfBac;
	protected JComboBox cobUser;
	protected JFormattedTextField txfDatum;
	protected JButton btnSpeichern, btnSchliessen;
	
	
	/**
	 * Konstuktor
	 * es wird das Windows Look and Feel verwendet
	 * die verschiedenen Buttons und Textfelder etc. werden initialisiert 
	 * und zu dem GridBagLayout hinzugefügt und angeordnet mittels createGbc
	 */
	public WPReassignGUI() {
		super("WPReassign");
		initialize();
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Could not load LookAndFeel");
		}
		
		
		
		lblEinf = new JLabel("einfügen ab");
		txfEinf = new JTextField();
		txfEinf.setEnabled(false);
		
        lblWPName = new JLabel("Arbeitspaket Name");
		txfName = new JTextField();

		lblBAC= new JLabel("BAC");
		txfBac = new JTextField();
		
		lblMa = new JLabel("Mitarbeiter");
		cobUser = new JComboBox();
	
		
		
		//Textfeld Datum bekommt das Format dd.mm.yyyy und hat das aktuelle Datum drinnen stehen
		try {
			lblDatum = new JLabel("Datum");
			txfDatum =  new JFormattedTextField(new MaskFormatter("##.##.####"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			String str = dateFormat.format(new Date());
			txfDatum.setText(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		btnSpeichern = new JButton("Speichern");
		btnSchliessen = new JButton("Schließen");
		
		createGBC(lblEinf, 0, 0, 1, 1);
		createGBC(txfEinf, 1, 0, 1, 1);
		createGBC(lblWPName, 0, 1, 1, 1);
		createGBC(txfName, 1, 1, 1, 1);
		createGBC(lblBAC, 0, 2, 1, 1);
		createGBC(txfBac, 1, 2, 1, 1);
		createGBC(lblDatum, 0, 3, 1, 1);
		createGBC(txfDatum, 1, 3, 1, 1);
		createGBC(lblMa, 0, 4, 1, 1);
		createGBC(cobUser, 1, 4, 1, 1);
		createGBC(btnSpeichern, 0, 5, 1, 1);
		createGBC(btnSchliessen, 1, 5, 1, 1);
		
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
		
		setTitle("WP Rekursiv einfügen");
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
