/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis Copyright (C) 2013 FH-Bingen This
 * program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY;; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.wpOverview.tabs;

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

import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.ToolTipTree;
import de.fhbingen.wbs.globals.Workpackage;

import de.fhbingen.wbs.wpComparators.APLevelComparator;
import de.fhbingen.wbs.wpOverview.TreeCellRenderer;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpOverview.WPOverviewGUI;

/**
 * The follower panel.
 */
public class FollowerPanel extends JPanel {

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = -6543342094372254375L;

    JMenuItem miContAufw;
    JMenuItem miContAPadd;
    JMenuItem miContAPdel;
    // JMenuItem miContReassign;

    // JTree CellRenderer - ist fÃ¼r die einzelnen EintrÃ¤ge im Baum
    // verantwortlich z.B. bei der fÃ¼rbung
    private TreeCellRenderer renderer = new TreeCellRenderer();
    private JTree treeAlle;
    JPopupMenu treeContextMenu;

    /**
     * Constructor. Call the method init().
     */
    public FollowerPanel() {
        init();
    }

    /**
     * Creates the context menu in the tree.
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
            miContAPadd = new JMenuItem(
                buttonStrings.addNewNeutral(wbsStrings.workPackage()));
            miContAPadd.setIcon(WPOverviewGUI.newAp);
            treeContextMenu.add(miContAPadd);
            treeContextMenu.addSeparator();
            miContAPdel = new JMenuItem(buttonStrings.delete(wbsStrings
                .workPackage()));
            miContAPdel.setIcon(WPOverviewGUI.delAP);
            treeContextMenu.add(miContAPdel);
            treeContextMenu.addSeparator();
            // WBS 2.0 auskommentiert keine Funktion
            // miContReassign = new
            // JMenuItem("Arbeitspaket rekursiv einfÃ¼gen");
            // miContReassign.setIcon(WPOverviewGUI.reass);
            // treeContextMenu.add(miContReassign);
        }

        this.removeAll();

        List<Workpackage> iterator = new ArrayList<Workpackage>(
            WpManager.getNoAncestorWps());
        DefaultMutableTreeNode root = createTree(
            new DefaultMutableTreeNode(""), new ArrayList<Workpackage>(
                iterator));

        treeAlle = new ToolTipTree(root);
        treeAlle.setCellRenderer(renderer);
        treeAlle.setModel(new DefaultTreeModel(root));

        expandAll(treeAlle, true);

        // JPanel panel = new JPanel();
        this.setLayout(new BorderLayout());
        this.add(treeAlle, BorderLayout.CENTER);
    }

    /**
     * Creates the tree.
     * @param actualNode
     *            The node where the work packages are inserted.
     * @param iterator
     *            A list with work packages.
     * @return The actual node.
     */
    private DefaultMutableTreeNode createTree(
        final DefaultMutableTreeNode actualNode,
        final List<Workpackage> iterator) {
        Collections.sort(iterator, new APLevelComparator());
        for (Workpackage actualWp : iterator) {
            DefaultMutableTreeNode nextNode = new DefaultMutableTreeNode(
                actualWp);
            List<Workpackage> nextIterator = new ArrayList<Workpackage>(
                actualWp.getFollowers());
            nextNode = createTree(nextNode, nextIterator);
            actualNode.add(nextNode);
        }

        return actualNode;
    }

    /** Reloads the tree. */
    public final void reload() {
        init();
    }

    /**
     * Traverse the tree.
     * @param tree
     *            The tree which has to traverse.
     * @param expand
     *            True: If the tree nodes should be expanded. False: Else.
     */
    public final void expandAll(final JTree tree, final boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    /**
     * Traverse the tree.
     * @param tree
     *            The tree which has to traverse.
     * @param parent
     *            The path of the tree.
     * @param expand
     *            True: If the tree nodes should be expanded. False: Else.
     */
    private void expandAll(final JTree tree, final TreePath parent,
        final boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
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
