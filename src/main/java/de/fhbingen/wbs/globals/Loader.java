package de.fhbingen.wbs.globals;


import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JProgressBar;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.border.LineBorder;
import java.awt.Color;

/**
 * Studienprojekt:	PSYS WBS 2.0<br/>
 *
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 *			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang<br/>
 *
 * Diese Klasse dient zum anzeigen des Ladebalkens waehrend des Startens und waehrend der Dauerberechnung.<br/>
 *
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 2012-08-21
 */
public class Loader extends JFrame {

	private static final long serialVersionUID = 4001816256641483597L;
	private final static JPanel contentPane = new JPanel();
	private final static JLabel lblNewLabel = new JLabel("");

	/**
	 * Konstruktor
	 * @param parent dient zum mittigen platzieren
	 */
	public Loader(JFrame parent) {

		reset();

		this.setUndecorated(true);
		this.setSize(230,50);

		contentPane.removeAll();

		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		final JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.insets = new Insets(10, 10, 10, 10);
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 0;
		contentPane.add(progressBar, gbc_progressBar);

		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 10, 10, 10);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		Controller.centerComponent(parent, this);
		this.requestFocus();
		this.setVisible(true);
	}

	/**
	 * Setzt aktuellen Anzeigetext fuer den Ladebalken
	 * @param text
	 */
	public static void setLoadingText(String text)  {
		lblNewLabel.setText(text);
		contentPane.repaint();
	}

	/**
	 * Setzt den Text auf leer zurueck
	 */
	public static void reset() {
		lblNewLabel.setText("");
	}

}
