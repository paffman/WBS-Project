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

import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import globals.FilterJTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dbaccess.DBModelManager;
import dbaccess.data.Baseline;
import wpOverview.WPOverview;

/**
 * The panel of the baseline window.
 */
public class BaselinePanel extends JPanel {

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = 7264492119053048051L;

    /** The button to add the baseline. */
    JButton btnAddBaseline;

    /** The button to show the baseline. */
    JButton btnShowBaseline;

    /** The button to create a chart. */
    JButton btnChart;

    JButton btnComp;

    /** The combo box to choose a baseline. */
    JComboBox<String> cobChooseBaseline;

    /** The label of the baseline. */
    private JLabel lblBaseline;

    /** The text field for the description. */
    JTextField txfBeschreibung;

    /** The description of the label. */
    private JLabel lblBeschreibung;

    /**
     * Constructor.
     * @param over
     *            The functionality of the WPOverview.
     * @param parent
     *            The parent frame.
     */
    public BaselinePanel(final WPOverview over, final JFrame parent) {
        Button buttonStrings = LocalizedStrings.getButton();
        Wbs wbsStrings = LocalizedStrings.getWbs();

        lblBaseline = new JLabel(LocalizedStrings.getGeneralStrings()
            .choose(wbsStrings.baseline()));

        btnAddBaseline = new JButton(buttonStrings.addNewFemale(wbsStrings
            .baseline()));
        btnShowBaseline = new JButton(buttonStrings.show(wbsStrings
            .baseline()));
        btnChart = new JButton(buttonStrings.show(wbsStrings.cpiGraph()));
        btnComp = new JButton(buttonStrings.show(wbsStrings.completion()));

        cobChooseBaseline = new JComboBox<String>();

        txfBeschreibung = new FilterJTextField(70);
        txfBeschreibung.setMinimumSize(new Dimension(250, 18));
        lblBeschreibung = new JLabel(wbsStrings.baseline() + " "
            + LocalizedStrings.getGeneralStrings().description());

        JPanel panel = new JPanel(false);
        panel.setSize(new Dimension(400, 200));
        GridBagLayout gbl = new GridBagLayout();
        panel.setLayout(gbl);

        addComponent(panel, gbl, lblBaseline, 0, 0, 1, 1);
        addComponent(panel, gbl, cobChooseBaseline, 1, 0, 1, 1);
        addComponent(panel, gbl, btnShowBaseline, 2, 0, 1, 1);
        addComponent(panel, gbl, lblBeschreibung, 0, 1, 1, 1);
        addComponent(panel, gbl, txfBeschreibung, 1, 1, 1, 1);
        addComponent(panel, gbl, btnAddBaseline, 2, 1, 1, 1);
        addComponent(panel, gbl, btnChart, 1, 2, 1, 1);
        addComponent(panel, gbl, btnComp, 1, 3, 1, 1);

        this.setLayout(new BorderLayout(10, 10));
        this.add(BorderLayout.BEFORE_FIRST_LINE, panel);

        new BaselinePanelAction(this, over, parent);

        getBaselines(WPOverview.getUser().getProjLeiter());
    }

    /**
     * Load the baselines in the combo box.
     * @param leiter
     *            True: user is a leader. False: user isn't a leader.
     */
    protected final void getBaselines(final boolean leiter) {
        if (leiter) {
            cobChooseBaseline.removeAllItems();
            List<Baseline> baselines = DBModelManager.getBaselineModel()
                .getBaseline();
            for (Baseline b : baselines) {
                String Beschreibung = b.getDescription();
                Date dte = b.getBl_date();
                int number = b.getId();
                cobChooseBaseline.addItem(number + " | " + dte + " | "
                    + Beschreibung);
            }
            cobChooseBaseline.setSelectedIndex(cobChooseBaseline
                .getItemCount() - 1);
            cobChooseBaseline
                .setMinimumSize(new Dimension((int) cobChooseBaseline
                    .getPreferredSize().getHeight(), 400));
            if (cobChooseBaseline.getItemCount() == 0) {
                btnShowBaseline.setEnabled(false);
                btnChart.setEnabled(false);
                btnComp.setEnabled(false);
            } else {
                btnShowBaseline.setEnabled(true);
                btnChart.setEnabled(true);
                btnComp.setEnabled(true);
            }
        }
    }

    /**
     * This method is called by ordering the components on the panel. It
     * inserts the single components to the GridBagLayout.
     * @param cont
     *            The container on that the GridBagLayout is insert.
     * @param gbl
     *            Actual GridBagLayout.
     * @param c
     *            The component which is insert into the container.
     * @param x
     *            The value of the x-axis.
     * @param y
     *            The value of the y-axis.
     * @param width
     *            The width of the element.
     * @param height
     *            The height of the element.
     */
    private void addComponent(final Container cont,
        final GridBagLayout gbl, final Component c, final int x,
        final int y, final int width, final int height) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbl.setConstraints(c, gbc);
        cont.add(c);
    }

    /** Reload the baselines in the combo box. */
    public final void reload() {
        getBaselines(WPOverview.getUser().getProjLeiter());
    }
}
