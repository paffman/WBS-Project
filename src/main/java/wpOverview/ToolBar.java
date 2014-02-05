package wpOverview;

import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import chart.ChartCPIView;
import functions.CalcOAPBaseline;
import functions.WpManager;
import globals.Loader;

import wpWorker.WBSUser;

/**
 * Studienprojekt:	WBS
 *
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz,
 * 						Peter Lange,
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 *
 * Erstellt eine freibewegbare ToolLeiste,
 * enthält Symbole zur schnelleren/einfacheren
 * handhabe des Programmes
 *
 * @author Samson von Graevenitz
 * @version 23.01.2011
 */

public class ToolBar extends JToolBar {

    JButton aktualisiereTree, calcDuration, neuesAP, delAP, neuerMa, cpiGraph;

	private static final long serialVersionUID = 1L;

	/**
	 * Fügt der Toolbar Items hinzug
	 *
	 * @param isProjektleiter - wird verwendet, um zu prüfen, ob Felder ausgegraut werden müssen weil keine Berechtigung
	 */
	public ToolBar(final WPOverview over, final WPOverviewGUI gui) {
		super();

        Wbs wbsStrings = LocalizedStrings.getWbs();
        Button buttonStrings = LocalizedStrings.getButton();
        General generalStrings = LocalizedStrings.getGeneralStrings();

		aktualisiereTree = new JButton();
		aktualisiereTree.setIcon(new ImageIcon(getClass().getResource
                ("/_icons/aktualisieren.png"))); //NON-NLS
		aktualisiereTree.setToolTipText(buttonStrings.refresh(generalStrings
                .views()));
		aktualisiereTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				over.reload();
			}
		});

		calcDuration = new JButton();
		calcDuration.setIcon(new ImageIcon(getClass().getResource
                ("/_icons/OAPaktualisieren_gross.png")));
		calcDuration.setToolTipText(buttonStrings.calculate(generalStrings
                .duration()));
		calcDuration.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        gui.setEnabled(false);
                        Loader loader = new Loader(gui);
                        new CalcOAPBaseline(true, over);
                        loader.dispose();
                        Loader.reset();
                        gui.setEnabled(true);
                        gui.requestFocus();
                    }
                }.start();

                over.reload();

            }
        });

		neuesAP = new JButton();
		neuesAP.setIcon(new ImageIcon(getClass().getResource("/_icons/newAP_gross.png")));
		neuesAP.setToolTipText(buttonStrings.addNewNeutral(wbsStrings
                .workPackage()));
		neuesAP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				over.readAP(true);
			}
		});

		delAP = new JButton();
		delAP.setIcon(new ImageIcon(getClass().getResource("/_icons/delAP_gross.png")));
		delAP.setToolTipText(buttonStrings.delete(wbsStrings.workPackage()));
		delAP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WpManager.removeAP(over.getSelectedWorkpackage());
				over.reload();
			}
		});

		neuerMa = new JButton();
		neuerMa.setIcon(new ImageIcon(getClass().getResource("/_icons/newMa_gross.png")));
		neuerMa.setToolTipText(buttonStrings.addNewNeutral(LocalizedStrings
                .getLogin().user()));
		neuerMa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new WBSUser(over);
			}
		});

		cpiGraph = new JButton();
		cpiGraph.setIcon(new ImageIcon(getClass().getResource("/_icons/cpiGraph_gross.png")));
		cpiGraph.setToolTipText(wbsStrings.cpiGraph());
		cpiGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChartCPIView(gui);
			}
		});

		if (WPOverview.getUser().getProjLeiter()) setAllEnabled(true, true, false, true);
		else setAllEnabled(false, false, false, true);

		add(aktualisiereTree);
		add(calcDuration);
		add(new Separator());
		add(neuesAP);
		add(delAP);
		add(new Separator());
		add(neuerMa);
		add(new Separator());
		add(cpiGraph);
	}

	/**
	 * Aktiviert alle Icons in der Toolbar entsprechend den angegebenen Parametern
	 *
	 * @param newAP Boolean für neues AP
	 * @param deleteAP Boolean für AP-löschen
	 * @param newMa Boolean für neuen Mitarbeiter
	 * @param cpi Boolean für CPI
	 */
	public void setAllEnabled(boolean newAP, boolean deleteAP, boolean newMa, boolean cpi) {
		calcDuration.setEnabled(WPOverview.getUser().getProjLeiter());
		neuesAP.setEnabled(newAP);
		delAP.setEnabled(deleteAP);
		neuerMa.setEnabled(newMa);
		cpiGraph.setEnabled(cpi);
	}

}
