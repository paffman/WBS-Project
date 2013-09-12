package baseline;
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
 * 
 * @author Daniel Metzler
 * @version 1.9- 17.02.2011
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.*;

import wpOverview.Legende;

public class WBSBaselineGUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	protected Vector<String> colBaseline;
	protected Vector<Vector<String>> Baseline;	
	protected JScrollPane spBaseline;
	protected JButton btnSchliessen;
	protected JTable tblBaseline;
	private Legende legende;
	
	/**
	 * Konstuktor
	 * Main-Frame bekommt den Namen "Baseline" zugewiesen
	 * es wird das Windows Look and Feel verwendet
	 * die verschiedenen Menüs, Buttons etc. werden initialisiert
	 * und zu dem BorderLayout hinzugefügt
	 */
	public WBSBaselineGUI(){
		super("Baseline");
		initialize();
		setDefaultCloseOperation( DISPOSE_ON_CLOSE);
		//setResizable(false);
		int width = 1024;
		int height = 700;
		//minimale Fenstergröße eintragen
		setSize(1024,700);
		//Fenster mittig platzieren
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		setLocation((int)(screenSize.getWidth()/2)-width/2,(int)(screenSize.getHeight()/2)-height/2);
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Could not load LookAndFeel");
		}
		
		btnSchliessen = new JButton("Schließen");
		
		colBaseline = new Vector<String>();
		colBaseline.add("ID1");
		colBaseline.add("ID2");
		colBaseline.add("ID3");
		colBaseline.add("IDx");
		colBaseline.add("Name");
		colBaseline.add("BAC");
		colBaseline.add("AC");
		colBaseline.add("ETC");
		colBaseline.add("CPI");
		colBaseline.add("BAC_Kosten");
		colBaseline.add("AC_Kosten");
		colBaseline.add("ETC_Kosten");
		colBaseline.add("EAC");
		colBaseline.add("EV");
		colBaseline.add("Trend");
		colBaseline.add("Fertigstellung");
		
		
		Baseline = new Vector<Vector<String>>();
        tblBaseline = new JTable(Baseline, colBaseline); 
        tblBaseline.setPreferredScrollableViewportSize(new Dimension(500, 300));
        tblBaseline.setFillsViewportHeight(true);
        spBaseline = new JScrollPane(tblBaseline);
        
        
        this.add(spBaseline, BorderLayout.CENTER);
        
        JPanel south = new JPanel();
        legende = new Legende();
        south.setLayout(new GridLayout(2,1));
        south.add(legende);
        south.add(btnSchliessen);
        
        this.add(south, BorderLayout.SOUTH);
        setVisible(true);   
	}
	
	/**
	 * Es wird ein BorderLayout verwendet
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

}
