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

package wpOverview;

import dbaccess.DBModelManager;
import dbaccess.data.Project;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Messages;
import functions.WpManager;
import globals.Controller;
import globals.Workpackage;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import wpAddAufwand.AddAufwand;
import wpComparators.APLevelComparator;
import wpConflict.Conflict;
import wpShow.WPShow;
import wpWorker.User;

/**
 * GUI to select and view the work packages from the users.
 */
public class WPOverview {

    /** A static variable for the count of the levels. */
    public static int levelCount = getEbenen();

    /** The work package overview GUI. */
    private static WPOverviewGUI gui;

    /** The user from which the work packages are shown. */
    private static User usr;

    /** Translation interface. */
    private final Messages messageStrings;

    /** The selected work package. */
    private Workpackage selectedWorkpackage;

    /**
     * Constructor: WPOverview() initialized the WPOverviewGUI and contains
     * the listener from the WPOverviewGUI in the method addButtonAction.
     * @param user
     *            The user from which the work package is shown.
     * @param parent
     *            The wished JFrame.
     */
    public WPOverview(final User user, final JFrame parent) {
        messageStrings = LocalizedStrings.getMessages();

        WPOverview.usr = user;

        gui = new WPOverviewGUI(this, parent);
        gui.setTitle(LocalizedStrings.getProject()
            .projectOverviewWindowTitle() + ": " + getProjektName());
        gui.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(final WindowEvent arg0) {
                Controller.leaveDB();
            }

            @Override
            public void windowClosing(final WindowEvent arg0) {
                Controller.leaveDB();
            }
        });

        new WPOverviewButtonAction(this, gui);

        reload();

    }

    /**
     * Returns the name of the project.
     * @return The projects name.
     */
    private String getProjektName() {
        List<Project> proj = DBModelManager.getProjectModel().getProject();
        String name = "";
        for (Project p : proj) {
            name = p.getName();
        }

        return name;
    }

    /**
     * Returns a String with the count of the fields for LVLxID, based on
     * the defined levels from the project. This method is called by the
     * method generateAnalyse() in the class Baseline.
     * @return String with LVLxID
     */
    public static String generateXEbene() {
        String nullen = "0";
        for (int i = 4; i < levelCount; i++) { //TODO is this still right?
            nullen += ".0";
        }
        return nullen;

    }

    /**
     * Return the selected work package.
     * @return The selected work package.
     */
    public final Workpackage getSelectedWorkpackage() {
        return this.selectedWorkpackage;
    }

    /**
     * Set the selected work package.
     * @param wp
     *            The work package which is selected.
     */
    public final void setSelectedWorkpackage(final Workpackage wp) {
        this.selectedWorkpackage = wp;
    }

    /**
     * Return a count of the defined levels from the project. This method
     * is called by the static field "ebenen" to instantiate it.
     * @return The count of the levels from the project.
     */
    public static int getEbenen() {
        List<Project> proj = DBModelManager.getProjectModel().getProject();

        for (Project p : proj) {
            levelCount = p.getLevels();
        }
        return levelCount;
    }

    /**
     * Returns the user.
     * @return The user from the selected work package.
     */
    public static User getUser() {
        if (usr != null) {
            return usr;
        } else {
            return new User("Leiter", true); // TODO was tut das?
        }

    }

    /**
     * This method helps to create a new work package. Also it captures the
     * efforts for existing work packages. This method is called from the
     * menu and from the context menu from a tree node.
     * @param isNewAp
     *            True: the work package is a new work package. False: the
     *            work package isn't new.
     */
    public final void readAP(final boolean isNewAp) {
        if (getSelectedWorkpackage() != null) {

            Workpackage selectedAP = getSelectedWorkpackage();

            boolean istOAP = selectedAP.isIstOAP();

            // Should a new work package be called?
            if (isNewAp) {
                if (istOAP) {
                    new WPShow(this, selectedAP, true, gui);

                } else {
                    JOptionPane.showMessageDialog(WPOverview.gui,
                        messageStrings.selectTopLevelWorkPackage());
                }
            } else { // or an existing?
                if (!istOAP) {
                    WPShow wpshow = new WPShow(this, selectedAP, false, gui);
                    new AddAufwand(wpshow, selectedAP);

                } else {
                    JOptionPane.showMessageDialog(WPOverview.gui,
                        messageStrings
                            .noWorkEffortsOnTopLevelWorkPackages());
                }
            }

        } else {
            JOptionPane.showMessageDialog(WPOverview.gui,
                messageStrings.selectWorkPackageToAddNew());
        }
    }

    /**
     * Update the main GUI.
     */
    public final void reload() {
        gui.reloadTrees();
        gui.reloadWorkers();
        gui.reloadTimeline();
        gui.reloadAvailabilities();
        gui.reloadConflicts();
        gui.reloadBaseline();
        gui.repaint();
        WPOverviewGUI.setStatusText(LocalizedStrings.getButton().refresh(
            LocalizedStrings.getGeneralStrings().view()));
    }

    /**
     * Creates the work package tree.
     * @return The root node from the tree.
     */
    public static DefaultMutableTreeNode createTree() {
        return createTree(new ArrayList<>(WpManager.getAllAp()));
    }

    /**
     * Creates the work package tree.
     * @param wpList
     *            A list with work packages which are inserted into the
     *            tree.
     * @return The root node from the tree.
     */
    public static DefaultMutableTreeNode createTree(
        final ArrayList<Workpackage> wpList) {
        return createTree(wpList, wpList);
    }

    /**
     * Creates the work package tree.
     * @param wpList
     *            A list with all work packages which are inserted into the
     *            tree.
     * @param onlyThese
     *              Render only these work packages.
     * @return The root node from the tree.
     */
    public static DefaultMutableTreeNode createTree(
        final ArrayList<Workpackage> wpList, final List<Workpackage>
            onlyThese) {
        final ArrayList<Workpackage> wpListCopy = new ArrayList<>(wpList);
        final ArrayList<Workpackage> onlyTheseCopy = new ArrayList<>(onlyThese);
        if (!wpListCopy.isEmpty()) {
            Collections.sort(wpListCopy, new APLevelComparator());

            int levels = WpManager.getRootAp().getLvlIDs().length;
            DefaultMutableTreeNode[] parents =
                    new DefaultMutableTreeNode[levels + 1];

            parents[0] = new DefaultMutableTreeNode(wpListCopy.remove(0));

            int lastRelevantIndex;
            for (Workpackage actualWP : wpListCopy) {
                lastRelevantIndex = actualWP.getlastRelevantIndex();
                if (onlyTheseCopy.contains(actualWP)) {
                    try {
                        parents[lastRelevantIndex] = new DefaultMutableTreeNode(
                            actualWP);
                        parents[lastRelevantIndex - 1].add(parents[actualWP
                            .getlastRelevantIndex()]);
                    } catch (ArrayIndexOutOfBoundsException
                        | NullPointerException e) {
                        System.err.println(LocalizedStrings.getMessages()
                            .treeStructureProblemAt(actualWP.toString()));
                    }
                }

            }
            return parents[0];
        }
        return new DefaultMutableTreeNode();
    }

    /**
     * Insert a conflict to the conflicts table and set the icon from the
     * conflict tab to "Warning".
     * @param conflict
     *            Conflict which should be thrown.
     */
    public static void throwConflict(final Conflict conflict) {
        if (gui != null) {
            gui.throwConflict(conflict);
        }

    }

    /**
     * Set the icon from the conflict tab to "No Warning".
     */
    public static void releaseAllConflicts() {
        if (gui != null) {
            gui.releaseConflicts();
        }
    }

    /**
     * Returns a Color array with the according color from the cpi/ac.
     * @param cpi
     *            Value of the cpi.
     * @param ac
     *            Value of the ac.
     * @return A color array with the according color values.
     */
    public static Color[] getCPIColor(final double cpi, final double ac) {
        Color[] colors = new Color[2];
        //TODO rewrite intervals
        if (ac > 0) {
            if (cpi < 0.97) {
                colors[0] = Color.black;
                colors[1] = Legende.LOW_CPI_COLOR;
                if (cpi < 0.94) {
                    colors[0] = Color.black;
                    colors[1] = Legende.VERY_LOW_CPI_COLOR;
                }
                if (cpi < 0.6) {
                    colors[0] = Color.white;
                    colors[1] = Legende.ULTRA_LOW_CPI_COLOR;
                }

            } else {
                if (cpi > 1.03) {
                    colors[0] = Color.white;
                    colors[1] = Legende.HIGH_CPI_COLOR;
                } else {
                    colors[0] = Color.black;
                    colors[1] = Legende.EVEN_CPI_COLOR;
                }
            }
        } else {
            colors[0] = Color.black;
            colors[1] = Legende.NO_CPI_COLOR;
        }

        return colors;

    }

    /**
     * Returns a Color array with the according color from the spi/ac.
     * @param spi
     *            Value of the spi.
     * @param ac
     *            Value of the ac.
     * @return A color array with the according color values.
     */
    public static Color[] getSPIColor(final double spi, final double ac) {
        return getCPIColor(spi, ac);
    }
}
