package wpOverview.tabs;

import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


import functions.WpManager;
import globals.ToolTipTree;
import globals.Workpackage;


import wpOverview.TreeCellRenderer;
import wpOverview.WPOverview;
import wpOverview.WPOverviewGUI;
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
 * Panel der Baumansicht<br/>
 *
 * @author WBS1.0 Team
 * @version 2.0
 */
public class TreePanel extends JPanel {

	private static final long serialVersionUID = -7713839856873070570L;
	JMenuItem miContAufw;
	JMenuItem miContAPadd;
	JMenuItem miContAPdel;
//	JMenuItem miContReassign;
	JMenuItem miChildrenOut;

	// JTree CellRenderer - ist für die einzelnen Einträge im Baum verantwortlich z.B. bei der faerbung
	private TreeCellRenderer renderer = new TreeCellRenderer();
	private JTree treeAll;
	private WPOverview over;
	JPopupMenu treeContextMenu;

	private ArrayList<Workpackage> nodes;

	private JFrame parent;
	public JMenuItem miChildrenIn;
	private ArrayList<Workpackage> onlyThese;
	/**
	 * Konstruktor
	 * Baut Baum der alle Knoten enthaelt
	 * @param nodes Liste mit Knoten
	 * @param over WPOverview Funktionalität
	 * @param parent ParentFrame
	 */
	public TreePanel(ArrayList<Workpackage> nodes, WPOverview over, JFrame parent) {
		this(nodes, nodes, over,parent);
	}
	/**
	 * Konstruktor
	 * Baut Baum der nur bestimmte Knoten enthaelt
	 * @param nodes Liste mit Knoten
	 * @param onlyThese Liste mit interessanten Knoten
	 * @param over WPOverview Funktionalität
	 * @param parent ParentFrame
	 */
	public TreePanel(ArrayList<Workpackage> nodes, ArrayList<Workpackage> onlyThese, WPOverview over, JFrame parent) {
		this.over = over;
		this.parent = parent;
		this.nodes = nodes;
		this.onlyThese = onlyThese;
		init();
	}
	/**
	 * Tree KontextMenue anlegen
	 */
	private void init() {
        Button buttonStrings = LocalizedStrings.getButton();
        Wbs wbsStrings = LocalizedStrings.getWbs();

		treeContextMenu = new JPopupMenu();
		miContAufw = new JMenuItem(buttonStrings.register(wbsStrings.workEffort()));
		miContAufw.setIcon(WPOverviewGUI.aufw);
		treeContextMenu.add(miContAufw);
		treeContextMenu.addSeparator();
		miChildrenOut = new JMenuItem(buttonStrings.expand());
		miChildrenIn = new JMenuItem(buttonStrings.collapse());

		miChildrenOut.setIcon(WPOverviewGUI.childrenOut);
		miChildrenIn.setIcon(WPOverviewGUI.childrenIn);

		treeContextMenu.add(miChildrenOut);
		treeContextMenu.add(miChildrenIn);

		if (WPOverview.getUser().getProjLeiter()) {
			treeContextMenu.addSeparator();
			miContAPadd = new JMenuItem(buttonStrings.
                    addNewNeutral(wbsStrings.workPackage()));
			miContAPadd.setIcon(WPOverviewGUI.newAp);
			treeContextMenu.add(miContAPadd);
			treeContextMenu.addSeparator();
			miContAPdel = new JMenuItem(buttonStrings.delete(
                    wbsStrings.workPackage()));
			miContAPdel.setIcon(WPOverviewGUI.delAP);
			treeContextMenu.add(miContAPdel);
			treeContextMenu.addSeparator();
// WBS 2.0 auskommentiert keine Funktion
//			miContReassign = new JMenuItem("Arbeitspaket rekursiv einfügen");
//			miContReassign.setIcon(WPOverviewGUI.reass);
//			treeContextMenu.add(miContReassign);
		}

		this.removeAll();

		DefaultMutableTreeNode root;

		root = WPOverview.createTree(nodes, onlyThese);

		treeAll = new ToolTipTree(root);
		treeAll.setToolTipText("");
		treeAll.setCellRenderer(renderer);
		treeAll.setModel(new DefaultTreeModel(root));

		// JPanel panel = new JPanel();
		this.setLayout(new BorderLayout());
		this.add(treeAll, BorderLayout.CENTER);
		// this.add(panel);
		new TreePanelAction(this, over, parent);

		TreePath path = find((DefaultMutableTreeNode) treeAll.getModel().getRoot(), over.getSelectedWorkpackage());

		if (path != null) {
			treeAll.expandPath(path);
			treeAll.setSelectionPath(path);
		} else {
			if (over.getSelectedWorkpackage() != null) {
				Workpackage parentWP = WpManager.getWorkpackage(over.getSelectedWorkpackage().getOAPID());
				// Arbetispaket nicht mehr vorhanden, statdessen OAP suchen
				if (parent != null) {
					path = find((DefaultMutableTreeNode) treeAll.getModel().getRoot(), parentWP);
					if (path != null) {
						treeAll.expandPath(path);
						treeAll.setSelectionPath(path);
					}
				}
			} else {
				over.setSelectedWorkpackage(null);
			}

		}
	}
	/**
	 * Liefert Pfad zu einem gesuchten Node
	 * @param root Start der suche
	 * @param wp gesuchtes Workpackage
	 * @return TreePath zum Knoten
	 */
	private TreePath find(DefaultMutableTreeNode root, Workpackage wp) {
		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			if(node.getUserObject() != null) {
				if (((Workpackage) node.getUserObject()).equals(wp)) { return new TreePath(node.getPath()); }
			}

		}
		return null;
	}
	/**
	 * Liefert den kompletten Baum zurueck
	 * @return JTree des Baums
	 */
	public JTree getTree() {
		return treeAll;
	}
	/**
	 * Laedt den Baum neu
	 */
	public void reload() {
		init();
	}
	/**
	 * Aendert den kompletten Baum
	 * @param nodes alle Knoten des Baums
	 * @param onlyThese angezeigte Knoten des Baums
	 */
	public void setNodes(ArrayList<Workpackage> nodes, ArrayList<Workpackage> onlyThese) {
		this.nodes = nodes;
		this.onlyThese = onlyThese;
	}

}
