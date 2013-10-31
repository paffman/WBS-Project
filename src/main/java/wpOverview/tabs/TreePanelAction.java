package wpOverview.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import functions.WpManager;
import globals.Workpackage;

import wpOverview.WPOverview;
import wpShow.WPShow;
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
 * Funktionalität des TreePanel<br/>
 * 
 * @author WBS1.0 Team
 * @version 2.0
 */
public class TreePanelAction {
	private TreePanel gui;
	/**
	 * Konstruktor
	 * @param gui Baum GUI
	 * @param over WPOverview Funktionalität
	 * @param parent ParentFrame
	 */
	public TreePanelAction(final TreePanel gui, final WPOverview over, final JFrame parent) {
		this.gui = gui;
		
		gui.getTree().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) gui.getTree().getLastSelectedPathComponent();
				Workpackage selected = null;
				if (node != null) {
					selected = (Workpackage) node.getUserObject();
					if (e.getClickCount() == 2 && ((Workpackage) node.getUserObject()) != null) {
						new WPShow(over, selected, false, parent);
					}
				}
				
				over.setSelectedWorkpackage(selected);
			}

			public void mouseReleased(MouseEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) gui.getTree().getLastSelectedPathComponent();
				Workpackage selected = null;
				if (node != null) {
					selected = (Workpackage) node.getUserObject();
					if (e.isPopupTrigger()) {
						gui.getTree().setSelectionPath(gui.getTree().getPathForLocation(e.getX(), e.getY()));
						

						// Context Menü anzeigen
						gui.treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
				over.setSelectedWorkpackage(selected);
			}
		});

		if (WPOverview.getUser().getProjLeiter()) {

			// AP einfügen
			gui.miContAPadd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					over.readAP(true);
				}
			});

			// AP löschen
			gui.miContAPdel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WpManager.removeAP(over.getSelectedWorkpackage());
					over.reload();
				}
			});

			// Aufwand eintragen
			gui.miContAufw.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					over.readAP(false);
				}
			});

			// Unterstruktur einfügen
			//WBS 2.0 auskommentiert keine Funktion
//			gui.miContReassign.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					 new WPReassign(over.tp, WPOverview.getUser(), over);
//				}
//			});
			
			gui.miChildrenOut.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode selected = (DefaultMutableTreeNode) gui.getTree().getLastSelectedPathComponent();
					expand(selected);					
				}
				
			});
			
			gui.miChildrenIn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode selected = (DefaultMutableTreeNode) gui.getTree().getLastSelectedPathComponent();
					collapse(selected);					
				}
				
			});
		}
	}
	/**
	 * Klappt den gewaehlten Knoten auf
	 * @param actualNode Aktueller Knoten der aufgeklappt werden soll
	 */
	private void expand(DefaultMutableTreeNode actualNode) {
		gui.getTree().expandPath(new TreePath(((DefaultTreeModel)gui.getTree().getModel()).getPathToRoot(actualNode)));
		for(int i = 0; i<actualNode.getChildCount(); i++) {
			expand((DefaultMutableTreeNode)actualNode.getChildAt(i));
		}
	}
	/**
	 * Klappt den gewaehlten Knoten zu
	 * @param actualNode Aktueller Knoten der zugeklappt werden soll
	 */
	private void collapse(DefaultMutableTreeNode actualNode) {	
		for(int i = 0; i<actualNode.getChildCount(); i++) {
			collapse((DefaultMutableTreeNode)actualNode.getChildAt(i));
		}
		gui.getTree().collapsePath(new TreePath(((DefaultTreeModel)gui.getTree().getModel()).getPathToRoot(actualNode)));
	}
}
