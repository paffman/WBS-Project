package wpOverview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import functions.WpManager;
import globals.Workpackage;

/**
 * Studienprojekt:	WBS
 *
 * Kunde:				Pentasys AG, Jens von Gersdorff
 *
 * Projektmitglieder:	Andre Paffenholz,
 * 						Peter Lange,
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 *
 *
 * TreeRenderer, der die einzelnen Einträge in einem Baum erstellt
 * Die verwendeten Icons stammen von: http://sublink.ca/icons/sweetieplus/
 * und unterliegen der Creative Commons Licence:
 * This licence allows you to use the icons in any client work, or commercial products such as WordPress themes or applications.
 *
 * @author Andre Paffenholz, Daniel Metzler
 */

public class TreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 5446711829460583776L;
	//Icons festlegen
	 private ImageIcon std, std_oap, fertig_oap, fertig_uap;


	private ImageIcon std_no_dep = new ImageIcon(Toolkit.getDefaultToolkit().
            getImage(this.getClass().getResource("/_icons/std.png"))); //NON-NLS
	private ImageIcon std_oap_no_dep = new ImageIcon(
            Toolkit.getDefaultToolkit().getImage(this.getClass().
                    getResource("/_icons/std_oap.png"))); //NON-NLS
	private ImageIcon fertig_oap_no_dep = new ImageIcon(
            Toolkit.getDefaultToolkit().getImage(this.getClass().
                    getResource("/_icons/fertige_oap.png"))); //NON-NLS
	private ImageIcon fertig_uap_no_dep = new ImageIcon(
            Toolkit.getDefaultToolkit().getImage(this.getClass().
                    getResource("/_icons/fertige_uap.png"))); //NON-NLS


	private ImageIcon std_dep = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().
            getResource("/_icons/std_dep.png"))); //NON-NLS
	private ImageIcon std_oap_dep = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().
            getResource("/_icons/std_oap_dep.png"))); //NON-NLS
	private ImageIcon fertig_oap_dep = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().
            getResource("/_icons/fertige_oap_dep.png"))); //NON-NLS
	private ImageIcon fertig_uap_dep = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().
            getResource("/_icons/fertige_uap_dep.png"))); //NON-NLS


	private ImageIcon inaktiv = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().
            getResource("/_icons/inaktiv.png"))); //NON-NLS


	/**
	 * Methode zum Rendern eines Eintrags in einem JTree
	 * Diese Methode wird beim Erstellen und Aktualisieren des Baums automatisch (!!) aufgerufen
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		//Prüfen ob es sich um eine TreeNode handelt
	    if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
	      Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

	      //Prüfen ob es sich um ein valies Arbeitspaket oder einfach um eine Nummer im Baum handelt
	      if (userObject instanceof Workpackage) {
	        Workpackage wp = (Workpackage) userObject;


	        if(!WpManager.getFollowers(wp).isEmpty() || !WpManager.getAncestors(wp).isEmpty()) {
	        	std = std_dep;
	        	std_oap = std_oap_dep;
	        	fertig_oap = fertig_oap_dep;
	        	fertig_uap = fertig_uap_dep;
	        } else {
	        	std = std_no_dep;
	        	std_oap = std_oap_no_dep;
	        	fertig_oap = fertig_oap_no_dep;
	        	fertig_uap = fertig_uap_no_dep;
	        }

	        double cpi = wp.getCpi();

	        //hier wird die fürbung der Schrift und des Hintergrunds vorgenommen

	        //schrift Kursiv darstellen, falls Paket inaktiv
	        if(wp.getLvl1ID()>0 && wp.isIstInaktiv()){
		        Font aktFont = getFont();
		        aktFont = aktFont.deriveFont(Font.ITALIC) ;
		        setFont(aktFont);
		        //ImageIcon vergeben


		        if(!expanded)
		        	super.setClosedIcon(inaktiv);
		        else
		        	super.setOpenIcon(inaktiv);


		        if(leaf)
		        	super.setLeafIcon(inaktiv);
		        else
		        	super.setIcon(inaktiv);

	        }
	        else {
	        	//Schrift Fett darstellen, falls Paket abgeschlossen
	        	if(wp.getLvl1ID()>0 && WpManager.calcPercentComplete(wp.getBac(), wp.getEtc(), wp.getAc())==100 && wp.getAc() > 0){
			        Font aktFont = getFont();
			        if(aktFont == null) {
			        	setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
			        	aktFont = getFont();
			        }
			        aktFont = aktFont.deriveFont(Font.BOLD);
			        setFont(aktFont);

			        if(!expanded)
			        	super.setClosedIcon(fertig_oap);
			        else
			        	super.setOpenIcon(fertig_oap);

			        if(leaf && wp.isIstOAP())
			        	super.setLeafIcon(fertig_oap);
			        else
			        	if(leaf)
			        		super.setLeafIcon(fertig_uap);
			        	else
			        		super.setIcon(fertig_uap);

	        	}
	        	else {
			        Font aktFont = getFont();
			        if(aktFont != null){
				        aktFont = aktFont.deriveFont(Font.PLAIN) ;
				        setFont(aktFont);
			        }

			        if(!expanded)
			        	super.setClosedIcon(std_oap);
			        else
			        	super.setOpenIcon(std_oap);


			        if(leaf && wp.isIstOAP())
			        	super.setLeafIcon(std_oap);
			        else
			        	if(leaf)
			        		super.setLeafIcon(std);
			        	else
			        		super.setIcon(std_oap);
	        	}
	        }


	        //Treefaerbungen vornehmen
	        if(wp.getAc() > 0){
		        if(cpi<=0.97){
		        	this.setBackgroundNonSelectionColor(Color.yellow);
		        	this.setBackgroundSelectionColor(Color.yellow);
		        	this.setTextSelectionColor(Color.black);
			        this.setTextNonSelectionColor(Color.black);

					if(cpi<0.94){
						this.setBackgroundNonSelectionColor(Color.red);
						this.setBackgroundSelectionColor(Color.red);
						this.setTextSelectionColor(Color.black);
				        this.setTextNonSelectionColor(Color.black);
					}

					if(cpi<0.6){
						this.setBackgroundNonSelectionColor(new Color(80, 00, 00));
						this.setBackgroundSelectionColor(new Color(80, 00, 00));
						this.setTextSelectionColor(Color.white);
						this.setTextNonSelectionColor(Color.white);
					}

				}
				else{
					if(cpi>1.03){
						this.setBackgroundNonSelectionColor(new Color(00, 80, 00));
						this.setBackgroundSelectionColor(new Color(00, 80, 00));
						this.setTextSelectionColor(Color.white);
						this.setForeground(Color.white);
						this.setTextNonSelectionColor(Color.white);
					}
					else{
						this.setTextSelectionColor(Color.black);
				        this.setTextNonSelectionColor(Color.black);
						this.setBackgroundNonSelectionColor(Color.green);
						this.setBackgroundSelectionColor(Color.green);
					}
				}
	        }
	        else {
	        	this.setBackgroundNonSelectionColor(Color.white);
	        	this.setBackgroundSelectionColor(Color.white);
	        	this.setTextSelectionColor(Color.black);
		        this.setTextNonSelectionColor(Color.black);
	        }

	      }
	    }

	    //als Rückgabe erfolgt der Aufruf und die Rückgabe der Methode in der Superklasse
	    return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
	  }
}