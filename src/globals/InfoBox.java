package globals;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Allgemeine InfoBox, wird über Menü->Hilfe->Info aufgerufen
 * 
 * @author Samson von Graevenitz/Peter Lange
 * @version 1.0- 16.02.2011
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.*;

public class InfoBox extends JDialog{

	private final String version = "1.6";
	private static final long serialVersionUID = 1L;
	Image pictInfo = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/globals/info.jpg"));
	JButton close;

	/**
	 * erzeugt ein Infofenster
	 * 	 
	 * */

	public InfoBox(){
	
		setTitle("WBS-Advanced - Version: " + version);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Could not load LookAndFeel");
		}
		
		int width = 500;
		int height = 550;
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		setLocation((int)(screenSize.getWidth()/2)-width/2,(int)(screenSize.getHeight()/2)-height/2);
		setSize(new Dimension(width, height));
		close=new JButton("Schliessen");
		
		add(createInfoBox());
		new InfoBoxButtonAction(this);
		setVisible(true);
		
	}
	/**
	 * Erzeugt das Panel mit dem Infobild und dem SchliessenButton -  Wird vom Konstruktor aufgerufen
	 * @return gibt den Panel für das Infofeld zurück
	 */
	
	protected JComponent createInfoBox() {

		JPanel anzeige=new JPanel();
	
		JPanel panel = new JPanel(false);
		panel.setSize(new Dimension(400,200));
		GridBagLayout gbl = new GridBagLayout();
		panel.setLayout(gbl);
				
		addComponent(panel,gbl,new JLabel(new ImageIcon(pictInfo)),0, 0, 1, 1);
		addComponent(panel,gbl,close,0, 1, 1, 1);

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
}
