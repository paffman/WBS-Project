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

package de.fhbingen.wbs.wpOverview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Workpackage;

/**
 * /** TreeRenderer which creates the single entries in the tree. The used
 * icons are from "http://sublink.ca/icons/sweetieplus/" and are under the
 * Creative Commons License: This license allows you to use the icons in
 * any client work, or commercial products such as WordPress themes or
 * applications.
 */
public class TreeCellRenderer extends DefaultTreeCellRenderer {

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = 5446711829460583776L;

    /**
     * Maximum percentage.
     */
    private static final int MAX_PERCENTAGE = 100;

    /**
     * Font size.
     */
    private static final int FONT_SIZE = 12;

    /** The single work package icons. */
    private ImageIcon std, std_oap, fertig_oap, fertig_uap;

    /** The standard no dependency icon. */
    private ImageIcon std_no_dep = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            this.getClass().getResource("/_icons/std.png"))); // NON-NLS

    /** The no dependency icon for upper work packages. */
    private ImageIcon std_oap_no_dep = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            this.getClass().getResource("/_icons/std_oap.png"))); // NON-NLS

    /**
     * The no dependency icon for upper work packages which are already
     * done.
     */
    private ImageIcon fertig_oap_no_dep = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            this.getClass().getResource("/_icons/fertige_oap.png"))); // NON-NLS

    /**
     * The no dependency icon for under work packages which are already
     * done.
     */
    private ImageIcon fertig_uap_no_dep = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            this.getClass().getResource("/_icons/fertige_uap.png"))); // NON-NLS

    /** The standard dependency icon. */
    private ImageIcon std_dep = new ImageIcon(Toolkit.getDefaultToolkit()
        .getImage(this.getClass().getResource(
                "/_icons/std_dep.png"))); // NON-NLS

    /** The dependency icon for upper work packages. */
    private ImageIcon std_oap_dep = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            this.getClass().getResource("/_icons/std_oap_dep.png"))); // NON-NLS

    /** The dependency icon for upper work packages which are already done. */
    private ImageIcon fertig_oap_dep = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            this.getClass().getResource(
                    "/_icons/fertige_oap_dep.png"))); // NON-NLS

    /** The dependency icon for under work packages which are already done. */
    private ImageIcon fertig_uap_dep = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            this.getClass().getResource(
                    "/_icons/fertige_uap_dep.png"))); // NON-NLS

    /** The inactive icon. */
    private ImageIcon inaktiv = new ImageIcon(Toolkit.getDefaultToolkit()
        .getImage(this.getClass().getResource(
                "/_icons/inaktiv.png"))); // NON-NLS

    @Override
    public final Component getTreeCellRendererComponent(final JTree tree,
        final Object value, final boolean selected, final boolean expanded,
        final boolean leaf, final int row, final boolean hasFocus) {

        // Checks if the value is an instance of a TreeNode
        if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            Object userObject = ((DefaultMutableTreeNode) value)
                .getUserObject();

            // Checks if the userObject is a work package or a simple
            // number in the tree
            if (userObject instanceof Workpackage) {
                Workpackage wp = (Workpackage) userObject;

                Set<Workpackage> followers = WpManager.getFollowers(wp);
                Set<Workpackage> ancestors = WpManager.getAncestors(wp);

                if ((followers != null && !followers.isEmpty())
                    || (ancestors != null && !ancestors.isEmpty())) {
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

                // Here the font and the background will be colored.

                // Font is shown italic if the package is inactive
                if (wp.getLvl1ID() > 0 && wp.isIstInaktiv()) {
                    Font aktFont = getFont();
                    aktFont = aktFont.deriveFont(Font.ITALIC);
                    setFont(aktFont);
                    // ImageIcon

                    if (!expanded) {
                        super.setClosedIcon(inaktiv);
                    } else {
                        super.setOpenIcon(inaktiv);
                    }
                    if (leaf) {
                        super.setLeafIcon(inaktiv);
                    } else {
                        super.setIcon(inaktiv);
                    }
                } else {
                    // if the package is done, the font is shown bold.
                    if (wp.getLvl1ID() > 0
                        && WpManager.calcPercentComplete(wp.getBac(),
                            wp.getEtc(), wp.getAc()) == MAX_PERCENTAGE
                        && wp.getAc() > 0) {
                        Font aktFont = getFont();
                        if (aktFont == null) {
                            setFont(new Font(Font.SANS_SERIF, Font.PLAIN,
                                    FONT_SIZE));
                            aktFont = getFont();
                        }
                        aktFont = aktFont.deriveFont(Font.BOLD);
                        setFont(aktFont);

                        if (!expanded) {
                            super.setClosedIcon(fertig_oap);
                        } else {
                            super.setOpenIcon(fertig_oap);
                        }

                        if (leaf && wp.isIstOAP()) {
                            super.setLeafIcon(fertig_oap);
                        } else if (leaf) {
                            super.setLeafIcon(fertig_uap);
                        } else {
                            super.setIcon(fertig_uap);
                        }

                    } else {
                        Font aktFont = getFont();
                        if (aktFont != null) {
                            aktFont = aktFont.deriveFont(Font.PLAIN);
                            setFont(aktFont);
                        }

                        if (!expanded) {
                            super.setClosedIcon(std_oap);
                        } else {
                            super.setOpenIcon(std_oap);
                        }

                        if (leaf && wp.isIstOAP()) {
                            super.setLeafIcon(std_oap);
                        } else if (leaf) {
                            super.setLeafIcon(std);
                        } else {
                            super.setIcon(std_oap);
                        }
                    }
                }

                // Here the tree is colored
                if (wp.getAc() > 0) {
                    if (cpi <= 0.97) { //TODO Intervals
                        this.setBackgroundNonSelectionColor(
                                Legende.LOW_CPI_COLOR);
                        this.setBackgroundSelectionColor(Legende.LOW_CPI_COLOR);
                        this.setTextSelectionColor(Color.black);
                        this.setTextNonSelectionColor(Color.black);

                        if (cpi < 0.94) {
                            this.setBackgroundNonSelectionColor(
                                    Legende.VERY_LOW_CPI_COLOR);
                            this.setBackgroundSelectionColor(
                                    Legende.VERY_LOW_CPI_COLOR);
                            this.setTextSelectionColor(Color.black);
                            this.setTextNonSelectionColor(Color.black);
                        }

                        if (cpi < 0.6) {
                            this.setBackgroundNonSelectionColor(
                                    Legende.ULTRA_LOW_CPI_COLOR);
                            this.setBackgroundSelectionColor(
                                    Legende.ULTRA_LOW_CPI_COLOR);
                            this.setTextSelectionColor(Color.white);
                            this.setTextNonSelectionColor(Color.white);
                        }

                    } else {
                        if (cpi > 1.03) {
                            this.setBackgroundNonSelectionColor(
                                    Legende.HIGH_CPI_COLOR);
                            this.setBackgroundSelectionColor(
                                    Legende.HIGH_CPI_COLOR);
                            this.setTextSelectionColor(Color.white);
                            this.setForeground(Color.white);
                            this.setTextNonSelectionColor(Color.white);
                        } else {
                            this.setTextSelectionColor(Color.black);
                            this.setTextNonSelectionColor(Color.black);
                            this.setBackgroundNonSelectionColor(
                                    Legende.EVEN_CPI_COLOR);
                            this.setBackgroundSelectionColor(
                                    Legende.EVEN_CPI_COLOR);
                        }
                    }
                } else {
                    this.setBackgroundNonSelectionColor(Legende.NO_CPI_COLOR);
                    this.setBackgroundSelectionColor(Legende.NO_CPI_COLOR);
                    this.setTextSelectionColor(Color.black);
                    this.setTextNonSelectionColor(Color.black);
                }

            }
        }

        // returns the call and the return from the method from the super
        // class
        return super.getTreeCellRendererComponent(tree, value, selected,
            expanded, leaf, row, hasFocus);
    }
}
