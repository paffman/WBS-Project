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
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.ToolTipTree;
import de.fhbingen.wbs.globals.Workpackage;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.fhbingen.wbs.wpOverview.TreeCellRenderer;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpOverview.WPOverviewGUI;

/**
 * Panel for the tree.
 */
public class TreePanel extends JPanel {

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = -7713839856873070570L;

    /** Context menu entry to add a work effort. */
    JMenuItem miContAufw;

    /** Context menu entry to add a new work package. */
    JMenuItem miContAPadd;

    /** Context menu entry to delete a work package. */
    JMenuItem miContAPdel;

    // JMenuItem miContReassign;

    /** Context menu entry to open a node. */
    JMenuItem miChildrenOut;

    /** Colors the single entries of the tree and renders it. */
    private TreeCellRenderer renderer = new TreeCellRenderer();

    /** The tree with all nodes. */
    private JTree treeAll;

    /** The functionality of the WPOverview. */
    private WPOverview over;

    /** The context menu for the tree. */
    JPopupMenu treeContextMenu;

    /** A list with the work packages. */
    private ArrayList<Workpackage> nodes;

    /** The parent frame. */
    private JFrame parent;

    /** Context menu entry to close a node. */
    public JMenuItem miChildrenIn;

    /** List with only selected work packages. */
    private ArrayList<Workpackage> onlyThese;

    /**
     * Constructor: Creates a tree with all nodes.
     * @param nodes
     *            List with nodes.
     * @param over
     *            Functionality of the WPOverview.
     * @param parent
     *            The parent frame.
     */
    public TreePanel(final ArrayList<Workpackage> nodes,
        final WPOverview over, final JFrame parent) {
        this(nodes, nodes, over, parent);
    }

    /**
     * Constructor: Creates a tree with only chosen nodes.
     * @param nodes
     *            List with all nodes.
     * @param onlyThese
     *            List with the chosen nodes.
     * @param over
     *            Functionality of the WPOverview.
     * @param parent
     *            The parent frame.
     */
    public TreePanel(final ArrayList<Workpackage> nodes,
        final ArrayList<Workpackage> onlyThese, final WPOverview over,
        final JFrame parent) {
        this.over = over;
        this.parent = parent;
        this.nodes = nodes;
        this.onlyThese = onlyThese;
        init();
    }

    /**
     * Create the context menu of the tree.
     */
    private void init() {
        Button buttonStrings = LocalizedStrings.getButton();
        Wbs wbsStrings = LocalizedStrings.getWbs();

        treeContextMenu = new JPopupMenu();
        miContAufw = new JMenuItem(buttonStrings.enter(wbsStrings
            .workEffort()));
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

        DefaultMutableTreeNode root;

        root = WPOverview.createTree(nodes, onlyThese);

        treeAll = new ToolTipTree(root);

        // set draggable if user is pm
        if (this.over.getUser().getProjLeiter()) {
            treeAll.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            treeAll.setDragEnabled(true);
            treeAll.setDropMode(DropMode.USE_SELECTION);
            treeAll.setDropTarget(new DropTarget(treeAll, new DropTargetAdapter() {
                @Override
                public void drop(DropTargetDropEvent dtde) {
                    Point droppedLocation = dtde.getLocation();
                    TreePath droppedPath = treeAll.getPathForLocation(droppedLocation.x, droppedLocation.y);

                    Workpackage sourceWorkpackage =
                            (Workpackage) ((DefaultMutableTreeNode)
                                    treeAll.getSelectionPath().getLastPathComponent()).getUserObject();
                    Workpackage targetWorkpackage = null;

                    ArrayList<Workpackage> targetParentWorkpackages = new ArrayList<>();

                    if (droppedPath != null) {
                        for (Object path : droppedPath.getPath()) {
                            targetParentWorkpackages.add((Workpackage) ((DefaultMutableTreeNode) path).getUserObject());
                        }
                    }

                    if (droppedPath != null &&
                        !(targetWorkpackage = (Workpackage)
                            ((DefaultMutableTreeNode) droppedPath.getLastPathComponent())
                                .getUserObject())
                            .equals(sourceWorkpackage) &&
                        !targetParentWorkpackages.contains(sourceWorkpackage) &&
                        !((DefaultMutableTreeNode) treeAll.getSelectionPath().getParentPath().getLastPathComponent()).getUserObject().equals(targetWorkpackage)
                    ) {
                        sourceWorkpackage.changeParent(targetWorkpackage);

                        dtde.acceptDrop(dtde.getDropAction());
                        dtde.dropComplete(true);
                    } else {
                        dtde.rejectDrop();
                        dtde.dropComplete(false);
                    }
                }
            }));
        }

        treeAll.setToolTipText("");
        treeAll.setCellRenderer(renderer);
        treeAll.setModel(new DefaultTreeModel(root));

        // JPanel panel = new JPanel();
        this.setLayout(new BorderLayout());
        this.add(treeAll, BorderLayout.CENTER);
        // this.add(panel);
        new TreePanelAction(this, over, parent);

        TreePath path = find((DefaultMutableTreeNode) treeAll.getModel()
            .getRoot(), over.getSelectedWorkpackage());

        if (path != null) {
            treeAll.expandPath(path);
            treeAll.setSelectionPath(path);
        } else {
            if (over.getSelectedWorkpackage() != null) {
                Workpackage parentWP = WpManager.getWorkpackage(over
                    .getSelectedWorkpackage().getOAPID());
                // work package doesn't exist. Search for upper work
                // package.
                if (parent != null) {
                    path = find((DefaultMutableTreeNode) treeAll.getModel()
                        .getRoot(), parentWP);
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
     * Returns a path to the searched node.
     * @param root
     *            The root node.
     * @param wp
     *            The searched work package.
     * @return A TreePath to the searched node.
     */
    private TreePath find(final DefaultMutableTreeNode root,
        final Workpackage wp) {
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> e = root
            .depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = e.nextElement();
            if (node.getUserObject() != null) {
                if (((Workpackage) node.getUserObject()).equals(wp)) {
                    return new TreePath(node.getPath());
                }
            }

        }
        return null;
    }

    /**
     * Returns the hole tree.
     * @return A JTree which represents the hole tree.
     */
    public final JTree getTree() {
        return treeAll;
    }

    /**
     * Reload the tree.
     */
    public final void reload() {
        init();
    }

    /**
     * Change the hole tree.
     * @param nodes
     *            All nodes of the tree.
     * @param onlyThese
     *            The shown nodes of the tree.
     */
    public final void setNodes(final ArrayList<Workpackage> nodes,
        final ArrayList<Workpackage> onlyThese) {
        this.nodes = nodes;
        this.onlyThese = onlyThese;
    }

}
