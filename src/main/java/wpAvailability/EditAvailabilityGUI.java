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

package wpAvailability;

import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import globals.Controller;
import globals.FilterJTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import wpWorker.Worker;

/**
 * The GUI to edit the availabilities. The functionality is represent by
 * the class EditAvailability.
 */
public class EditAvailabilityGUI extends JFrame {

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = 457946443136567295L;

    /** To insert a reason/description why the worker is not available. */
    private JTextField tfName;

    /**
     * A check box which shows if the worker is not available for only a
     * few hours or the hole day.
     */
    private JCheckBox cbAllDay;

    /** A combo box to choose for which worker the availability is set. */
    private JComboBox<Worker> cmbWorker;

    /** A check box to change if the worker is available/ is not available. */
    private JCheckBox cbAvailable;

    /** A spinner to choose the start of the availability. */
    private JSpinner spnStart;

    /** A spinner to choose the end of the availability. */
    private JSpinner spnEnd;

    /** Contains the single GUI components. */
    private JPanel panel_1;

    /** The button to save the availability. */
    private JButton btnSave;

    /** The button to delete a availability. */
    private JButton btnDelete;

    /** The button to cancel the changes. */
    private JButton btnAbbrechen;

    /**
     * Constructor: Initialize the GUI.
     * @param function
     *            The functionality of this class.
     * @param headline
     *            The title of the window.
     * @param parent
     *            The parent frame.
     */
    EditAvailabilityGUI(final EditAvailability function,
        final String headline, final JFrame parent) {
        super(headline);
        initGUI();
        btnSave.addActionListener(function.getSaveListener());
        btnDelete.addActionListener(function.getDeleteListener());
        cbAvailable.addItemListener(function.getCbAvailableListener());
        btnAbbrechen.addActionListener(function.getCancelListener());
        Controller.centerComponent(parent, this);
    }

    /**
     * Initialize the GUI.
     */
    private void initGUI() {
        this.setLocationByPlatform(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(createPanel());
        this.pack();
        this.setVisible(true);
    }

    /**
     * Creates the panel which contains the GUI elements.
     * @return The created panel.
     */
    private JPanel createPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[] {150, 200 };
        layout.columnWeights = new double[] {0.1, 1.0 };
        layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
            0.1, 0.1, 1.0 };
        layout.rowHeights = new int[] {20, 7, 7, 7, 7, 7, 7, 7, 7 };
        panel.setLayout(layout);

        int padding = 2;
        General generalStrings = LocalizedStrings.getGeneralStrings();
        Button buttonStrings = LocalizedStrings.getButton();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblName = new JLabel();
        panel.add(lblName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
                2, 5, 5), 0, 0));
        lblName.setText(generalStrings.description() + "("
            + generalStrings.optional() + ")");

        JLabel lblStart = new JLabel();
        panel.add(lblStart, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
                2, 5, 5), 0, 0));
        lblStart.setText(generalStrings.start());

        JLabel lblEnd = new JLabel();
        panel.add(lblEnd, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
                2, 5, 5), 0, 0));
        lblEnd.setText(generalStrings.end());

        tfName = new FilterJTextField();
        panel.add(tfName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(2, 2, 5, 2), 0, 0));

        cbAllDay = new JCheckBox();
        cbAllDay.setSelected(true);
        cbAllDay.setEnabled(false);
        panel.add(cbAllDay, new GridBagConstraints(1, padding, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets(2, 2, 5, 2), 0, 0));
        cbAllDay.setText(generalStrings.fullTime());

        cbAvailable = new JCheckBox();
        panel.add(cbAvailable, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
                2, 5, 2), 0, 0));
        cbAvailable.setText(generalStrings.available());

        spnStart = new JSpinner(new SpinnerDateModel());
        spnStart
            .setEditor(new JSpinner.DateEditor(spnStart, ("dd.MM.yyyy")));
        panel.add(spnStart, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
                2, 5, 2), 0, 0));

        spnEnd = new JSpinner(new SpinnerDateModel());
        spnEnd.setEditor(new JSpinner.DateEditor(spnEnd, ("dd.MM.yyyy")));
        panel.add(spnEnd, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
                2, 5, 2), 0, 0));
        JLabel lblWorker = new JLabel(LocalizedStrings.getLogin().user());
        // was: "Betrifft"
        cmbWorker = new JComboBox<Worker>();
        panel.add(lblWorker, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
                2, 5, 5), 0, 0));
        panel.add(cmbWorker, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,
                2, 5, 2), 0, 0));

        panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.weighty = 1.0;
        gbc_panel_1.gridwidth = 2;
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 8;
        panel.add(panel_1, gbc_panel_1);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] {79, 0, 0, 0 };
        gbl_panel_1.rowHeights = new int[] {23, 0 };
        gbl_panel_1.columnWeights = new double[] {1.0, 1.0, 1.0,
            Double.MIN_VALUE };
        gbl_panel_1.rowWeights = new double[] {0.0, Double.MIN_VALUE };
        panel_1.setLayout(gbl_panel_1);

        btnSave = new JButton();
        btnSave.setText(buttonStrings.ok());
        GridBagConstraints gbc_btnSave = new GridBagConstraints();
        gbc_btnSave.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSave.anchor = GridBagConstraints.NORTH;
        gbc_btnSave.insets = new Insets(0, 0, 0, 5);
        gbc_btnSave.gridx = 0;
        gbc_btnSave.gridy = 0;
        panel_1.add(btnSave, gbc_btnSave);

        btnDelete = new JButton(buttonStrings.delete());
        GridBagConstraints gbc_btnDelete = new GridBagConstraints();
        gbc_btnDelete.insets = new Insets(0, 0, 0, 5);
        gbc_btnDelete.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnDelete.anchor = GridBagConstraints.NORTH;
        gbc_btnDelete.gridx = 1;
        gbc_btnDelete.gridy = 0;
        panel_1.add(btnDelete, gbc_btnDelete);

        btnAbbrechen = new JButton(buttonStrings.cancel());
        GridBagConstraints gbc_btnAbbrechen = new GridBagConstraints();
        gbc_btnAbbrechen.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnAbbrechen.gridx = 2;
        gbc_btnAbbrechen.gridy = 0;
        panel_1.add(btnAbbrechen, gbc_btnAbbrechen);

        return panel;
    }

    /**
     * Returns the description.
     * @return The description as String.
     */
    protected final String getDescription() {
        return tfName.getText();
    }

    /**
     * Sets the description.
     * @param desc
     *            The description as String.
     */
    protected final void setDescription(final String desc) {
        tfName.setText(desc);
    }

    /**
     * Returns the selected start date.
     * @return The selected date.
     */
    protected final Date getStart() {
        return ((SpinnerDateModel) spnStart.getModel()).getDate();
    }

    /**
     * Returns the selected end date.
     * @return The selected date.
     */
    protected final Date getEnd() {
        return ((SpinnerDateModel) spnEnd.getModel()).getDate();
    }

    /**
     * Returns the selected worker.
     * @return The selected worker.
     */
    protected final Worker getWorker() {
        return (Worker) cmbWorker.getSelectedItem();
    }

    /**
     * Boolean True: If the worker is available/not available the hole day.
     * False: Else.
     * @return boolean If the worker is available/not available the hole
     *         day or only a few hours.
     */
    protected final boolean getAllDay() {
        return cbAllDay.isSelected();
    }

    /**
     * Checks the availability of the selected worker.
     * @return True: If the selected worker is available. False: Else.
     */
    protected final boolean getAvailable() {
        return cbAvailable.isSelected();
    }

    /**
     * Sets the start date of the availability.
     * @param start
     *            The start date.
     */
    protected final void setStart(final Date start) {
        spnStart.getModel().setValue(start);
    }

    /**
     * Sets the end date of the availability.
     * @param end
     *            The end date.
     */
    protected final void setEnd(final Date end) {
        spnEnd.getModel().setValue(end);
    }

    /**
     * Sets for which worker the availability is set.
     * @param worker
     *            The worker for which the availability is set.
     */
    protected final void setWorker(final Worker worker) {
        cmbWorker.setSelectedItem(worker);
    }

    /**
     * Sets for which workers the availabilities are set.
     * @param workerArray
     *            An array with the workers for which the availability is
     *            set.
     */
    protected final void setWorkers(final Worker[] workerArray) {
        ComboBoxModel<Worker> cmbWorkerModel = new DefaultComboBoxModel<Worker>(
            workerArray);
        cmbWorker.setModel(cmbWorkerModel);
    }

    /**
     * Sets the availability of the worker to the hole day or only to a few
     * hours.
     * @param allDay
     *            True: If the availability of the worker is set to he hole
     *            day. False: Else.
     */
    protected final void setAllDay(final boolean allDay) {
        cbAllDay.setSelected(allDay);
    }

    /**
     * Sets if the worker is available or is not available.
     * @param available
     *            True: If the worker is available. False: If the worker is
     *            not available.
     */
    protected final void setAvailable(final boolean available) {
        cbAvailable.setSelected(available);
    }

    /**
     * Sets the date format of the availability.
     * @param available
     *            True: With hours and minutes. False: Without hours and
     *            minutes.
     */
    protected final void setAvailableView(final boolean available) {
        cbAllDay.setSelected(!available);
        if (available) {
            spnStart.setEditor(new JSpinner.DateEditor(spnStart,
                ("dd.MM.yyyy HH:mm")));
            spnEnd.setEditor(new JSpinner.DateEditor(spnEnd,
                ("dd.MM.yyyy HH:mm")));
        } else {
            spnStart.setEditor(new JSpinner.DateEditor(spnStart,
                ("dd.MM.yyyy")));
            spnEnd
                .setEditor(new JSpinner.DateEditor(spnEnd, ("dd.MM.yyyy")));
        }
    }

    /**
     * Sets the delete button to visible/not visible.
     * @param newView
     *            True: If the button should be visible. False: If the
     *            button shouldn't be visible.
     */
    protected final void setNewView(final boolean newView) {
        btnDelete.setVisible(!newView);
    }

    /**
     * Deactivates the field to select a worker.
     */
    public final void disableWorkerSelection() {
        this.cmbWorker.setEnabled(false);
    }
}
