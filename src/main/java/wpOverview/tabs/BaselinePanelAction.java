package wpOverview.tabs;

import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import chart.ChartCPIView;
import chart.ChartCompleteView;
import dbServices.ConflictService;
import functions.CalcOAPBaseline;
import globals.Loader;

import wpBaseline.BaselineView;
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
 * Funktionalitaet des BaselinePanel<br/>
 *
 * @author WBS1.0 Team
 * @version 2.0
 */
public class BaselinePanelAction {
	/**
	 * Konstruktor
	 * @param gui Baseline Panel
	 * @param over WPOverview Funktionalität
	 * @param parent ParentFrame
	 */
	BaselinePanelAction(final BaselinePanel gui, final WPOverview over, final JFrame parent) {
		gui.btnAddBaseline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {

						boolean calc;
						if (ConflictService.getAllConflicts().isEmpty()) {
							calc = true;
						} else {
							if (JOptionPane.showConfirmDialog(null,
                                    LocalizedStrings.getMessages()
                                            .durationCalculationNotUpToDate(),
                                    LocalizedStrings.getWbs()
                                            .baselineCalculation(),
									JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								calc = true;
							} else {
								calc = false;
							}
						}

						if (calc) {
							gui.setEnabled(false);
							Loader loader = new Loader(parent);
							new CalcOAPBaseline(gui.txfBeschreibung.getText(), over);
							loader.dispose();
							Loader.reset();
							gui.cobChooseBaseline.setSelectedIndex(gui.cobChooseBaseline.getItemCount() - 1);
							gui.setEnabled(true);
							gui.requestFocus();
							over.reload();
						}

					}
				}.start();
			}
		});

		gui.btnShowBaseline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedBaseline = gui.cobChooseBaseline.getSelectedItem().toString();
				int pos = selectedBaseline.indexOf("|");
				String curBaseline = selectedBaseline.substring(0, pos - 1);
				new BaselineView(Integer.parseInt(curBaseline), parent);
			}
		});

		gui.btnChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChartCPIView(parent);
			}
		});

		gui.btnComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChartCompleteView(parent);
			}
		});
	}

}
