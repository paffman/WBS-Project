package wpOverview;



import de.fhbingen.wbs.translation.LocalizedStrings;
import functions.CalcOAPBaseline;
import functions.WpManager;
import globals.Controller;
import globals.InfoBox;
import globals.Loader;
import importPrepare.PrepareImport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import chooseDB.DBChooser;
import dbaccess.DBModelManager;
import wpWorker.ChangePW;

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
 * ButtonActions der WPOverview GUI werden hier verwaltet
 *
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.1 - 30.11.2010
 */
public class WPOverviewButtonAction {

	private WPOverview over;
	private WPOverviewGUI gui;
	/**
	 * Konstruktor
	 * @param over WPOverview Funktionalitaet
	 * @param gui WPOverview GUI
	 */
	public WPOverviewButtonAction(WPOverview over, WPOverviewGUI gui) {
		this.over = over;
		this.gui = gui;
		addButtonAction();
	}
	/**
	 * Fuegt die ActionListener den WPOverwiev Buttons hinzu
	 */
	public void addButtonAction() {
		gui.btnSchliessen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller.leaveDB();
				System.exit(0);
			}
		});

		if (WPOverview.getUser().getProjLeiter()) {

			gui.miAP.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					over.readAP(true);
				}
			});

			gui.miDelAp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (over.getSelectedWorkpackage() != null) {
						WpManager.removeAP(over.getSelectedWorkpackage());
						over.reload();
					} else {
						JOptionPane.showMessageDialog(gui,
                                LocalizedStrings.getErrorMessages().markWorkpackageToDelete());
					}
				}
			});

			gui.miImportInitial.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new PrepareImport(over);
					over.reload();
					WPOverviewGUI.setStatusText(LocalizedStrings.getMessages
                            ().importedDbWasCalculated());
					// JOptionPane.showMessageDialog(gui, "Berechnung abgeschlossen");
				}
			});
		}

		gui.miAktualisieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				over.reload();
				WPOverviewGUI.setStatusText(LocalizedStrings.getMessages()
                        .viewWasRefreshed());
			}
		});

		gui.miCalcDuration.addActionListener(new ActionListener() {
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

		gui.miChangePW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChangePW(WPOverview.getUser());
			}
		});

		gui.miAbmelden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DBChooser();
				gui.dispose();
				Controller.leaveDB();
			}
		});

		gui.miBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller.leaveDB();
				System.exit(0);

			}
		});

		gui.miHilfe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(gui, LocalizedStrings
                        .getMessages().clickOnWorkpackageToShowDetails());
			}
		});

		gui.miInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new InfoBox();
			}
		});

		// Fenster Schließen
		gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				gui.dispose();
				if ( WPOverview.getUser().getProjLeiter()){
				    DBModelManager.getSemaphoreModel().leaveSemaphore("pl", WPOverview.getUser().getId());
				}
//				System.out.println("Benutzer wurde abgemeldet");
			}
		});

		// setzt die Items der ToolBar je nach Berechtigung und Ansicht
		// setEnabled(true/false)
		gui.tabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (gui.tabs.getSelectedIndex()) {
				case 0: // Alle AP
					if (WPOverview.getUser().getProjLeiter()) gui.toolbar.setAllEnabled(true, true, false, true);
					else gui.toolbar.setAllEnabled(false, false, false, true);
					gui.showLegende(true);
					break;
				case 1: // Offene AP
					if (WPOverview.getUser().getProjLeiter()) gui.toolbar.setAllEnabled(true, true, false, true);
					else gui.toolbar.setAllEnabled(false, false, false, true);
					gui.showLegende(true);
					break;
				case 2: // Fertige AP
					if (WPOverview.getUser().getProjLeiter()) gui.toolbar.setAllEnabled(true, true, false, true);
					else gui.toolbar.setAllEnabled(false, false, false, true);
					gui.showLegende(true);
					break;
				case 3: //Mitarbeiter
					if (WPOverview.getUser().getProjLeiter()) gui.toolbar.setAllEnabled(false, false, true, true);
					else gui.toolbar.setAllEnabled(false, false, false, true);
					gui.showLegende(false);
					break;
				case 4: // Baseline
					if (WPOverview.getUser().getProjLeiter()) gui.toolbar.setAllEnabled(false, false, false, true);
					else gui.toolbar.setAllEnabled(false, false, false, true);
					gui.showLegende(false);
					break;
				case 5: // Avaialbilities
					if (WPOverview.getUser().getProjLeiter()) gui.toolbar.setAllEnabled(false, false, false, true);
					else gui.toolbar.setAllEnabled(false, false, false, true);
					gui.showLegende(false);
					break;
				case 6: // Timeline
					if (WPOverview.getUser().getProjLeiter()) gui.toolbar.setAllEnabled(false, false, false, true);
					else gui.toolbar.setAllEnabled(false, false, false, true);
					gui.showLegende(false);
					break;
				case 7: // Konflikte
					if (WPOverview.getUser().getProjLeiter()) gui.toolbar.setAllEnabled(false, false, false, true);
					else gui.toolbar.setAllEnabled(false, false, false, true);
					if (WPOverview.getUser().getProjLeiter()) gui.showLegende(false);
					break;
				}

			}
		});

	}

}
