package wpOverview.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.jfree.chart.ChartPanel;

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
 * WindowBuilder autogeneriert<br/>
 * 
 * @author WBS1.0 Team
 * @version 2.0
 */
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
 * GUI des Verguegbarkeitsgraphen<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 2012-08-21
 */
public class AvailabilityGraphGUI extends javax.swing.JPanel {

	private static final long serialVersionUID = 602668733483664542L;

	public AvailabilityGraph function;

	protected ChartPanel pnlGraph;
	protected JToggleButton[] buttons;
	protected JButton btnNext;
	protected JButton btnPrev;
	protected JToggleButton btnManualAv;
	/**
	 * 
	 * @param over WPOverview Funktionalität
	 * @param parent ParentFrame
	 */
	public AvailabilityGraphGUI(WPOverview over, JFrame parent) {
		super();
		initGUI();
		new AvailabilityGraphAction(this, parent);
		function = new AvailabilityGraph(this, over);
		buttons[2].doClick();
	}
	/**
	 * Laedt die GUI
	 */
	protected void initGUI() {
		this.setLayout(new BorderLayout());
		this.add(createOptionPanel(), BorderLayout.NORTH);
		
		pnlGraph = new ChartPanel(null);	
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		panel.add(pnlGraph, constraints);
		panel.setBackground(Color.white);
		this.add(panel, BorderLayout.CENTER);
	}
	
	/**
	 * Erstellt das JPanel fuer due GUI
	 * @return JPanel der GUI
	 */
	protected JPanel createOptionPanel() {
		
		JPanel optionPanel = new JPanel();
		
		buttons = new JToggleButton[4];
		buttons[AvailabilityGraph.DAY] = new JToggleButton("Tag");
		buttons[AvailabilityGraph.WEEK] = new JToggleButton("Woche");
		buttons[AvailabilityGraph.MONTH] = new JToggleButton("Monat");
		buttons[AvailabilityGraph.YEAR] = new JToggleButton("Jahr");
		for (int i = 0; i < buttons.length; i++) {
			optionPanel.add(buttons[i]);
		}
		optionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnPrev = new JButton("<");
		optionPanel.add(btnPrev);
		
		btnNext = new JButton(">");
		optionPanel.add(btnNext);
		
		btnManualAv = new JToggleButton("manuelle Verfügbarkeiten einblenden");
		optionPanel.add(btnManualAv);
		
		return optionPanel;
	}
	/**
	 * Berechnet den Graph neu
	 */
	public void reload() {
		function.remake();
	}

}
