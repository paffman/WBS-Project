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

package wpOverview.tabs;

import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.jfree.chart.ChartPanel;

import wpOverview.WPOverview;

/**
 * GUI of the availability graphs.
 */
public class AvailabilityGraphGUI extends javax.swing.JPanel {

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = 602668733483664542L;

    /** The functionality for the graph. */
    public AvailabilityGraph function;

    /** The panel for the GUI. */
    protected ChartPanel pnlGraph;

    protected JToggleButton[] buttons;
    protected JButton btnNext;
    protected JButton btnPrev;
    protected JToggleButton btnManualAv;

    /**
     * Constructor.
     * @param over
     *            The functionality of the graph.
     * @param parent
     *            The parent frame.
     */
    public AvailabilityGraphGUI(final WPOverview over, final JFrame parent) {
        super();
        initGUI();
        new AvailabilityGraphAction(this, parent);
        function = new AvailabilityGraph(this, over);
        buttons[2].doClick();
    }

    /**
     * Load the GUI.
     */
    protected final void initGUI() {
        this.setLayout(new BorderLayout());
        this.add(createOptionPanel(), BorderLayout.NORTH);

        pnlGraph = new ChartPanel(null);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        panel.add(pnlGraph, constraints);
        panel.setBackground(Color.white);
        this.add(panel, BorderLayout.CENTER);
    }

    /**
     * Creates the panel for the GUI.
     * @return The created panel.
     */
    protected final JPanel createOptionPanel() {
        General generalStrings = LocalizedStrings.getGeneralStrings();

        JPanel optionPanel = new JPanel();

        buttons = new JToggleButton[4];
        buttons[AvailabilityGraph.DAY] = new JToggleButton(
            generalStrings.day());
        buttons[AvailabilityGraph.WEEK] = new JToggleButton(
            generalStrings.week());
        buttons[AvailabilityGraph.MONTH] = new JToggleButton(
            generalStrings.month());
        buttons[AvailabilityGraph.YEAR] = new JToggleButton(
            generalStrings.year());
        for (int i = 0; i < buttons.length; i++) {
            optionPanel.add(buttons[i]);
        }
        optionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        btnPrev = new JButton("<");
        optionPanel.add(btnPrev);

        btnNext = new JButton(">");
        optionPanel.add(btnNext);

        btnManualAv = new JToggleButton(LocalizedStrings.getButton().show(
            LocalizedStrings.getWbs().manualAvailabilities()));
        optionPanel.add(btnManualAv);

        return optionPanel;
    }

    /**
     * Recalculate the graph.
     */
    public final void reload() {
        function.remake();
    }

}
