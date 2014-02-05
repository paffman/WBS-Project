package wpOverview.tabs;

import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


import functions.WpManager;
import globals.ToolTipTree;
import globals.Workpackage;


import wpComparators.APLevelComparator;
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
 * WindowBuilder autogeneriert<br/>
 *
 * @author WBS1.0 Team
 * @version 2.0
 */
public class FollowerPanel extends JPanel {
	private static final long serialVersionUID = -6543342094372254375L;
	JMenuItem miContAufw;
	JMenuItem miContAPadd;
	JMenuItem miContAPdel;
//	JMenuItem miContReassign;

	// JTree CellRenderer - ist für die einzelnen Einträge im Baum verantwortlich z.B. bei der fürbung
	private TreeCellRenderer renderer = new TreeCellRenderer();
	private JTree treeAlle;
	JPopupMenu treeContextMenu;
	/**
	 * Konstruktor
	 * Ruft init() auf
	 */
	public FollowerPanel() {
		init();
	}
	/**
	 * Tree KontextMenue anlegen
	 */
	private void init() {
        Wbs wbsStrings = LocalizedStrings.getWbs();
        Button buttonStrings = LocalizedStrings.getButton();
		treeContextMenu = new JPopupMenu();
		miContAufw = new JMenuItem(wbsStrings.insertWorkEffort());
		treeContextMenu.add(miContAufw);
		miContAufw.setIcon(WPOverviewGUI.aufw);

		if (WPOverview.getUser().getProjLeiter()) {
			treeContextMenu.addSeparator();
			miContAPadd = new JMenuItem(buttonStrings.
                    addNewNeutral(wbsStrings.workPackage()));
			miContAPadd.setIcon(WPOverviewGUI.newAp);
			treeContextMenu.add(miContAPadd);
			treeContextMenu.addSeparator();
			miContAPdel = new JMenuItem(buttonStrings.delete(wbsStrings.workPackage()));
			miContAPdel.setIcon(WPOverviewGUI.delAP);
			treeContextMenu.add(miContAPdel);
			treeContextMenu.addSeparator();
// WBS 2.0 auskommentiert keine Funktion
//			miContReassign = new JMenuItem("Arbeitspaket rekursiv einfügen");
//			miContReassign.setIcon(WPOverviewGUI.reass);
//			treeContextMenu.add(miContReassign);
		}

		this.removeAll();

		List<Workpackage> iterator = new ArrayList<Workpackage>(WpManager.getNoAncestorWps());
		DefaultMutableTreeNode root = createTree(new DefaultMutableTreeNode(""), new ArrayList<Workpackage>(iterator));

		treeAlle = new ToolTipTree(root);
		treeAlle.setCellRenderer(renderer);
		treeAlle.setModel(new DefaultTreeModel(root));

		expandAll(treeAlle, true);

		// JPanel panel = new JPanel();
		this.setLayout(new BorderLayout());
		this.add(treeAlle, BorderLayout.CENTER);
	}

	private DefaultMutableTreeNode createTree(DefaultMutableTreeNode actualNode, List<Workpackage> iterator) {
		Collections.sort(iterator, new APLevelComparator());
		for(Workpackage actualWp : iterator) {
			DefaultMutableTreeNode nextNode = new DefaultMutableTreeNode(actualWp);
			List<Workpackage> nextIterator = new ArrayList<Workpackage>(actualWp.getFollowers());
			nextNode = createTree(nextNode, nextIterator);
			actualNode.add(nextNode);
		}

		return actualNode;
	}

	public void reload() {
		init();
	}
	/**
	 * Traversiert den Tree
	 */
	public void expandAll(JTree tree, boolean expand) {
	    TreeNode root = (TreeNode)tree.getModel().getRoot();

	    // Traverse tree from root
	    expandAll(tree, new TreePath(root), expand);
	}
	/**
	 * Traversiert den Tree
	 */
	private void expandAll(JTree tree, TreePath parent, boolean expand) {
	    TreeNode node = (TreeNode)parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) {
	        for (Enumeration<?> e=node.children(); e.hasMoreElements(); ) {
	            TreeNode n = (TreeNode)e.nextElement();
	            TreePath path = parent.pathByAddingChild(n);
	            expandAll(tree, path, expand);
	        }
	    }

	    // Expansion or collapse must be done bottom-up
	    if (expand) {
	        tree.expandPath(parent);
	    } else {
	        tree.collapsePath(parent);
	    }
	}

}
