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

package wpAddAufwand;

import java.awt.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.text.MaskFormatter;



public class AddAufwandGui extends JFrame{

	private static final long serialVersionUID = 1L;
	protected GridBagLayout gbl;
	
	protected JLabel lblID, lblName, lblUser,lblBeschreibung, lblAufwand, lblDatum;	
	protected JTextField txfNr, txfName, txfBeschreibung, txfAufwand;
	protected JComboBox cobUser;
	protected JFormattedTextField txfDatum;
	protected JButton btnEdit, btnSchliessen;	

	
	/**
	 * Main-Frame bekommt den Namen "WPAufwand" zugewiesen
	 * es wird das Windows Look and Feel verwendet
	 * die verschiedenen Menüs, Buttons etc. werden initialisiert
	 * und zu dem GridBagLayout hinzugefügt und angeordnet mittels createGbc
	 */
	public AddAufwandGui(){
		super("WPAufwand");
		initialize();
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Could not load LookAndFeel");
		}
		
		
        lblID = new JLabel("ArbeitspaketID");
		txfNr = new JTextField();
		txfNr.setEnabled(false);
        
		lblName = new JLabel("Arbeitspaket Name");
		txfName = new JTextField();
		txfName.setEnabled(false);
		
		lblUser = new JLabel("Benutzer");
		cobUser = new JComboBox();
			
		lblBeschreibung = new JLabel("Beschreibung");
		txfBeschreibung = new JTextField();
		
		lblAufwand = new JLabel("Aufwand");
		txfAufwand = new JTextField();
		
		lblDatum = new JLabel("Datum");
		//Textfeld Datum bekommt das Format dd.mm.yyyy und hat das aktuelle Datum drinnen stehen
		try {
			txfDatum =  new JFormattedTextField(new MaskFormatter("##.##.####"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			String str = dateFormat.format(new Date());
			txfDatum.setText(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		btnEdit = new JButton("Editieren");
		btnSchliessen = new JButton("Schließen");
		
		createGBC(lblID, 0, 0, 1, 1);
		createGBC(txfNr, 1, 0, 1, 1);
		createGBC(lblName, 0, 1, 1, 1);
		createGBC(txfName, 1, 1, 1, 1);
		createGBC(lblUser, 0, 2, 1, 1);
		createGBC(cobUser, 1, 2, 1, 1);
		createGBC(lblBeschreibung, 0, 3, 1, 1);
		createGBC(txfBeschreibung, 1, 3, 1, 1);
		createGBC(lblAufwand, 0, 4, 1, 1);
		createGBC(txfAufwand, 1, 4, 1, 1);
		createGBC(lblDatum, 0, 5, 1, 1);
		createGBC(txfDatum, 1, 5, 1, 1);
		createGBC(btnEdit, 0, 6, 1, 1);
		createGBC(btnSchliessen, 1, 6, 1, 1);
		
		setVisible(true);
	}
		
		
		/**
		 * Methode initialize zum erstellen des Layouts
		 * Gui ist 300 Pixel breit und 220 Pixel lang
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
