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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Workpackage;

import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpShow.WPShow;

/**
 * Functionality of the TreePanel.
 */
public class TreePanelAction {

    /** The GUI of the tree panel. */
    private TreePanel gui;

    /**
     * Constructor.
     * @param gui
     *            The GUI of the tree panel.
     * @param over
     *            The functionality of the WPOverview.
     * @param parent
     *            The parent frame.
     */
    public TreePanelAction(final TreePanel gui, final WPOverview over,
        final JFrame parent) {
        this.gui = gui;

        gui.getTree().addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) gui
                    .getTree().getLastSelectedPathComponent();
                Workpackage selected = null;
                if (node != null) {
                    selected = (Workpackage) node.getUserObject();
                    if (e.getClickCount() == 2
                        && ((Workpackage) node.getUserObject()) != null) {
                        new WPShow(over, selected, false, parent);
                    }
                }

                over.setSelectedWorkpackage(selected);
            }

            public void mouseReleased(final MouseEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) gui
                    .getTree().getLastSelectedPathComponent();
                Workpackage selected = null;
                if (node != null) {
                    selected = (Workpackage) node.getUserObject();
                    if (e.isPopupTrigger()) {
                        gui.getTree().setSelectionPath(
                            gui.getTree().getPathForLocation(e.getX(),
                                e.getY()));

                        // Show context menu
                        gui.treeContextMenu.show(e.getComponent(),
                            e.getX(), e.getY());
                    }
                }
                over.setSelectedWorkpackage(selected);
            }
        });

        if (WPOverview.getUser().getProjLeiter()) {

            // Insert work package
            gui.miContAPadd.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    over.readAP(true);
                }
            });

            // Delete work package
            gui.miContAPdel.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    WpManager.removeAP(over.getSelectedWorkpackage());
                    over.reload();
                }
            });

            // Add effort
            gui.miContAufw.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    over.readAP(false);
                }
            });

            // Insert structure
            // WBS 2.0 out commented: no function
            // gui.miContReassign.addActionListener(new ActionListener() {
            // public void actionPerformed(ActionEvent e) {
            // new WPReassign(over.tp, WPOverview.getUser(), over);
            // }
            // });

            gui.miChildrenOut.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    DefaultMutableTreeNode selected =
                            (DefaultMutableTreeNode) gui.getTree()
                                    .getLastSelectedPathComponent();
                    expand(selected);
                }

            });

            gui.miChildrenIn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    DefaultMutableTreeNode selected =
                            (DefaultMutableTreeNode) gui.getTree()
                                    .getLastSelectedPathComponent();
                    collapse(selected);
                }

            });
        }
    }

    /**
     * Opens the selected node.
     * @param actualNode
     *            The node which is open.
     */
    private void expand(final DefaultMutableTreeNode actualNode) {
        gui.getTree().expandPath(
            new TreePath(((DefaultTreeModel) gui.getTree().getModel())
                .getPathToRoot(actualNode)));
        for (int i = 0; i < actualNode.getChildCount(); i++) {
            expand((DefaultMutableTreeNode) actualNode.getChildAt(i));
        }
    }

    /**
     * Closes the selected node.
     * @param actualNode
     *            The node which is close.
     */
    private void collapse(final DefaultMutableTreeNode actualNode) {
        for (int i = 0; i < actualNode.getChildCount(); i++) {
            collapse((DefaultMutableTreeNode) actualNode.getChildAt(i));
        }
        gui.getTree().collapsePath(
            new TreePath(((DefaultTreeModel) gui.getTree().getModel())
                .getPathToRoot(actualNode)));
    }
}
