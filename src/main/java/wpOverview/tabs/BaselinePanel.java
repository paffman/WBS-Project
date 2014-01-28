package wpOverview.tabs;

import globals.FilterJTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dbaccess.DBModelManager;
import dbaccess.data.Baseline;
import wpOverview.WPOverview;
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
 * Panel des Baseline Fensters<br/>
 * 
 * @author WBS1.0 Team
 * @version 2.0
 */
public class BaselinePanel extends JPanel {

	private static final long serialVersionUID = 7264492119053048051L;
	JButton btnAddBaseline;
	JButton btnShowBaseline;
	JButton btnChart;
	JButton btnComp;
	JComboBox<String> cobChooseBaseline;
	private JLabel lblBaseline;
	JTextField txfBeschreibung;
	private JLabel lblBeschreibung;
	/**
	 * Konstruktor
	 * @param over WPOverwiev Funktionalität
	 * @param parent ParentFrame
	 */
	public BaselinePanel(WPOverview over, JFrame parent) {

		lblBaseline = new JLabel("Bitte Baseline auswählen");

		btnAddBaseline = new JButton("Neue Baseline anlegen");
		btnShowBaseline = new JButton("Baseline anzeigen");
		btnChart = new JButton("CPI-Diagramm anzeigen");
		btnComp = new JButton("Fertigstellung anzeigen");

		cobChooseBaseline = new JComboBox<String>();

		txfBeschreibung = new FilterJTextField(70);
		txfBeschreibung.setMinimumSize(new Dimension(250, 18));
		lblBeschreibung = new JLabel("Baseline Beschreibung");

		JPanel panel = new JPanel(false);
		panel.setSize(new Dimension(400, 200));
		GridBagLayout gbl = new GridBagLayout();
		panel.setLayout(gbl);

		addComponent(panel, gbl, lblBaseline, 0, 0, 1, 1);
		addComponent(panel, gbl, cobChooseBaseline, 1, 0, 1, 1);
		addComponent(panel, gbl, btnShowBaseline, 2, 0, 1, 1);
		addComponent(panel, gbl, lblBeschreibung, 0, 1, 1, 1);
		addComponent(panel, gbl, txfBeschreibung, 1, 1, 1, 1);
		addComponent(panel, gbl, btnAddBaseline, 2, 1, 1, 1);
		addComponent(panel, gbl, btnChart, 1, 2, 1, 1);
		addComponent(panel, gbl, btnComp, 1, 3, 1, 1);

		this.setLayout(new BorderLayout(10, 10));
		this.add(BorderLayout.BEFORE_FIRST_LINE, panel);
		
		new BaselinePanelAction(this, over, parent);

		getBaselines(WPOverview.getUser().getProjLeiter());
	}
	
	
	/**
	 * Laedt Baseline in ComboBox
	 * @param leiter Boolean ob angemeldeter User ist Leiter
	 */
	protected void getBaselines(boolean leiter) {
		if (leiter) {			
			cobChooseBaseline.removeAllItems();
            List<Baseline> baselines =
                    DBModelManager.getBaselineModel().getBaseline();
            for (Baseline b : baselines) {
                String Beschreibung = b.getDescription();
                Date dte = b.getBl_date();
                int number = b.getId();
                cobChooseBaseline.addItem(number + " | " + dte + " | "
                        + Beschreibung);
            }
            cobChooseBaseline
                    .setSelectedIndex(cobChooseBaseline.getItemCount() - 1);
            cobChooseBaseline
                    .setMinimumSize(new Dimension((int) cobChooseBaseline
                            .getPreferredSize().getHeight(), 400));
            if (cobChooseBaseline.getItemCount() == 0) {
                btnShowBaseline.setEnabled(false);
                btnChart.setEnabled(false);
                btnComp.setEnabled(false);
            } else {
                btnShowBaseline.setEnabled(true);
                btnChart.setEnabled(true);
                btnComp.setEnabled(true);
            }
		}
	}
	

	/**
	 * addComponent(args) wird am Anordnen der Komponenten auf dem JPanel aufgerufen Methode addComponent 
	 * zum Hinzufügen der einzelnen Komponenten zum GridBagLayout
	 * 
	 * @param cont Container - aktueller Container auf den das Gridlayout hinzugefügt werden soll
	 * @param gbl Aktuelles Gridlayout
	 * @param c Komponente die zum Layout hinzugefügt wird
	 * @param x Anordnung auf der X-Achse (Breite)
	 * @param y Anordnung auf der Y-Achse (Länge)
	 * @param width Breite des Elementes
	 * @param height Höhe des Elementes
	 */
	private void addComponent(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(2, 2, 2, 2);
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}



	public void reload() {
		getBaselines(WPOverview.getUser().getProjLeiter());
	}
}
