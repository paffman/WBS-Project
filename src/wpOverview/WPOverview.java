package wpOverview;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import wpAddAufwand.AddAufwand;
import wpComparators.APLevelComparator;
import wpConflict.Conflict;
import wpShow.WPShow;
import wpWorker.User;

import functions.WpManager;
import globals.Controller;
import globals.Workpackage;
import jdbcConnection.SQLExecuter;


/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
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
 * GUI zum auswählen und Anzeigen der Arbeitspakete der User
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 2.0 - 30.11.2010
 */
public class WPOverview {

	public static int levelCount = getEbenen();

	private static WPOverviewGUI gui;
	private static User usr;

	private Workpackage selectedWorkpackage;

	/**
	 * Konstruktor WPOverview() initialisiert die WPOverviewGUI, und beeinhaltet die Listener der WPOverviewGUI durch die Methode addButtonAction()
	 */
	public WPOverview(User usr, JFrame parent) {

		WPOverview.usr = usr;

		gui = new WPOverviewGUI(this, parent);
		gui.setTitle("Übersicht Projekt " + getProjektName());
		gui.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent arg0) {
				Controller.leaveDB();
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				Controller.leaveDB();
			}
		});

		new WPOverviewButtonAction(this, gui);

		reload();

	}

	/**
	 * Gibt den Namen des Projekts zurück
	 * 
	 * @return Projektname
	 */
	private String getProjektName() {
		ResultSet proj = SQLExecuter.executeQuery("Select * FROM Projekt");
		String name = "";
		try {
			while (proj.next()) {
				name = proj.getString("Name");
			}
			proj.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return name;
	}

	/**
	 * Liefert einen String zurück mit der Anzahl der passenden Stellen für LVLxID anhand der definierten Ebenen im Projekt Wird von generateAnalyse() in der
	 * Baseline aufgerufen
	 * 
	 * @return String der LVLxID
	 */
	public static String generateXEbene() {
		String Nullen = "0";
		for (int i = 4; i < levelCount; i++) {
			Nullen += ".0";
		}
		return Nullen;

	}

	public Workpackage getSelectedWorkpackage() {
		return this.selectedWorkpackage;
	}

	public void setSelectedWorkpackage(Workpackage wp) {
		this.selectedWorkpackage = wp;
	}

	/**
	 * Liefert die Anzahl der Festgelegten Ebenen im Projekt zurück Wird vom statischen Datenelement ebenen aufgerufen, um dies zu instanziieren
	 * 
	 * @return Anzahl der Ebenen in einem Projekt
	 */
	public static int getEbenen() {
		ResultSet rsProjekt = SQLExecuter.executeQuery("SELECT * FROM Projekt");
		try {
			if (rsProjekt.next()) {
				levelCount = rsProjekt.getInt("Ebenen");
			}
			rsProjekt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return levelCount;
	}


	public static User getUser() {
		if(usr != null) {
			return usr;
		} else {
			return new User("Leiter", true);
		}
		
	}

	/**
	 * Hilfsfunktion zum Erstellen eines neuen APs und zum Erfassen für Aufwände für bestehende APs Wird aus dem Menü aufgerufen und aus dem KontextMenü eines
	 * Baumeintrags Um Codezeilen zu reduzieren wurde die Funktionalität in diese Methode gepackt
	 */
	public void readAP(boolean isNewAp) {
		if (getSelectedWorkpackage() != null) {

			Workpackage selectedAP = getSelectedWorkpackage();

			boolean istOAP = false;
			istOAP = selectedAP.isIstOAP();

			// Soll ein Neues AP aufgerufen werden
			if (isNewAp) {
				if (istOAP) {
					new WPShow(this, selectedAP, true, gui);

				} else {
					JOptionPane.showMessageDialog(WPOverview.gui, "Bitte markieren Sie ein Oberarbeitspaket!");
				}
			}
			// oder ein bestehendes?
			else {
				if (!istOAP) {
					WPShow wpshow = new WPShow(this, selectedAP, false, gui);
					new AddAufwand(wpshow, selectedAP);

				} else {
					JOptionPane.showMessageDialog(WPOverview.gui, "Sie können nur Aufwände zu Arbeitspaketen erfassen, nicht zu OAPs!");
				}
			}

		} else {
			JOptionPane.showMessageDialog(WPOverview.gui, "Bitte markieren Sie erst ein Arbeitspaket, um dann dort ein neues einzufügen!");
		}
	}
	/**
	 * Aktualisiert die Haupt-GUI 
	 */
	public void reload() {
		gui.reloadTrees();
		gui.reloadWorkers();
		gui.reloadTimeline();
		gui.reloadAvailabilities();
		gui.reloadConflicts();
		gui.reloadBaseline();
		gui.repaint();
		WPOverviewGUI.setStatusText("Ansicht aktualisiert");
	}
	/**
	 * Erstellt den WP-Baum
	 * @return RootNode des Baums
	 */
	public static DefaultMutableTreeNode createTree() {
		return createTree(new ArrayList<Workpackage>(WpManager.getAllAp()));
	}

	public static DefaultMutableTreeNode createTree(ArrayList<Workpackage> wpList) {
		return createTree(wpList, wpList);
	}

	public static DefaultMutableTreeNode createTree(ArrayList<Workpackage> wpList, List<Workpackage> onlyThese) {
		wpList = new ArrayList<Workpackage>(wpList);
		onlyThese = new ArrayList<Workpackage>(onlyThese);
		if (!wpList.isEmpty()) {
			Collections.sort(wpList, new APLevelComparator());
	
			int levels = WpManager.getRootAp().getLvlIDs().length;
			DefaultMutableTreeNode[] parents = new DefaultMutableTreeNode[levels + 1];
	
			parents[0] = new DefaultMutableTreeNode(wpList.remove(0));
	
			int lastRelevantIndex;
			for (Workpackage actualWP : wpList) {
				lastRelevantIndex = actualWP.getlastRelevantIndex();
				if (onlyThese.contains(actualWP)) {
					try {
						parents[lastRelevantIndex] = new DefaultMutableTreeNode(actualWP);
						parents[lastRelevantIndex - 1].add(parents[actualWP.getlastRelevantIndex()]);
					} catch (ArrayIndexOutOfBoundsException e) {
						System.err.println("Problem mit der Baumstruktur bei " + actualWP);
					} catch (NullPointerException e){
						System.err.println("Problem mit der Baumstruktur bei " + actualWP);
					}
				}
	
			}
			return parents[0];
		}
		return new DefaultMutableTreeNode();
	}
	/**
	 * Fuegt einen Conflict zur Conflicttable und setzt das Icon des Konflikt-Tabs auf Warning
	 * @param conflict Konflikt der geworfen werden soll
	 */
	public static void throwConflict(Conflict conflict) {
		if(gui!= null) {
			gui.throwConflict(conflict);
		}
	
	}
	/**
	 * Setzt das Icon des Konflikt-Tabs auf noWarning
	 */
	public static void releaseAllConflicts() {
		if(gui!= null) {
			gui.releaseConflicts();
		}
	}
	/**
	 * Liefert ein Color Objekt mit der dem cpi/ac Entsprechenden Farbwert
	 * @param cpi Wert deren Color-Objekt gebraucht wirt
	 * @param ac Wert deren Color-Objekt gebraucht wirt
	 * @return Color Objekt mit den Werten Entsprechenden Farbwert
	 */
	public static Color[] getCPIColor(double cpi, double ac) {
		Color[] colors = new Color[2];
		if (ac > 0) {
			if (cpi < 0.97) {
				colors[0] = Color.black;
				colors[1] = Color.YELLOW;
				if (cpi < 0.94) {
					colors[0] = Color.black;
					colors[1] = Color.RED;
				}
				if (cpi < 0.6) {
					colors[0] = Color.white;
					colors[1] = new Color(80, 00, 00);
				}

			} else {
				if (cpi > 1.03) {
					colors[0] = Color.white;
					colors[1] = new Color(00, 80, 00);
				} else {
					colors[0] = Color.black;
					colors[1] = Color.GREEN;
				}
			}
		} else {
			colors[0] = Color.black;
			colors[1] = Color.WHITE;
		}
		
		return colors;

	}
	/**
	 * Liefert ein Color Objekt mit der dem spi/ac Entsprechenden Farbwert
	 * @param spi Wert deren Color-Objekt gebraucht wirt
	 * @param ac Wert deren Color-Objekt gebraucht wirt
	 * @return Color Objekt mit den Werten Entsprechenden Farbwert
	 */
	public static Color[] getSPIColor(double spi, double ac) {
		return getCPIColor(spi, ac);
	}
}
