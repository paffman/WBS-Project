package wpOverview;

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import baseline.AddBaseline;

import chart.ChartFunctionCPI;

import wpMitarbeiter.WBSMitarbeiter;

public class ToolBar extends JToolBar{
	
	JButton aktualisiereTree, aktualisiereOAP, neuesAP, delAP, neuerMa, cpiGraph;
	
	private static final long serialVersionUID = 1L;

	/**
	 * Fügt der Toolbar Items hinzug
	 * @param over - Hält die Funktionen, die über Items aufgerufen werden
	 * @param isProjektleiter - wird verwendet, um zu prüfen, ob Felder ausgegraut werden müssen
	 * 							weil keine Berechtigung
	 */
	public ToolBar(final WPOverviewButtonAction over, boolean isProjektleiter){
		//super();
		
		aktualisiereTree = new JButton();
		aktualisiereTree.setIcon(new ImageIcon(getClass().getResource("/_icons/aktualisieren.png")));
		aktualisiereTree.setToolTipText("Ansichten aktualisieren");
		aktualisiereTree.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				over.over.aktualisieren();
			}
		});
		
		aktualisiereOAP = new JButton();
		aktualisiereOAP.setIcon(new ImageIcon(getClass().getResource("/_icons/OAPaktualisieren_gross.png")));
		aktualisiereOAP.setToolTipText("Oberarbeitspakete aktualisieren");
		aktualisiereOAP.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new AddBaseline("", over.over, false);
			}
		});
		
		neuesAP = new JButton();
		neuesAP.setIcon(new ImageIcon(getClass().getResource("/_icons/newAP_gross.png")));
		neuesAP.setToolTipText("Neues Arbeitspaket anlegen");
		neuesAP.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				over.readAP(true);
			}
		});
				
		delAP = new JButton();
		delAP.setIcon(new ImageIcon(getClass().getResource("/_icons/delAP_gross.png")));
		delAP.setToolTipText("Arbeitspaket löschen");
		delAP.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				over.delAP();
			}
		});
		
		neuerMa = new JButton();
		neuerMa.setIcon(new ImageIcon(getClass().getResource("/_icons/newMa_gross.png")));
		neuerMa.setToolTipText("Mitarbeiter Hinzufügen");
		neuerMa.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new WBSMitarbeiter(over.over);
			}
		});	
		
		cpiGraph = new JButton();
		cpiGraph.setIcon(new ImageIcon(getClass().getResource("/_icons/cpiGraph_gross.png")));
		cpiGraph.setToolTipText("CPI-Graph");
		cpiGraph.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new ChartFunctionCPI();
			}
		});	
		
		if(isProjektleiter)
			setAllEnabled(true, true, true, true);
		else
			setAllEnabled(false, false, false, true);

		add(aktualisiereTree);
		add(aktualisiereOAP);
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
	 * @param newAP Boolean für neues AP
	 * @param deleteAP Boolean für AP-löschen
	 * @param newMa Boolean für neuen Mitarbeiter
	 * @param cpi Boolean für CPI
	 */
	public void setAllEnabled(boolean newAP, boolean deleteAP, boolean newMa, boolean cpi){
		neuesAP.setEnabled(newAP);
		delAP.setEnabled(deleteAP);
		neuerMa.setEnabled(newMa);
		cpiGraph.setEnabled(cpi);
	}
	
}
