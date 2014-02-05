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
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Diese Klasse dient zur Bearbeitung und Anzeigen von Verfuegbarkeiten.<br/>
 * Sie greift auf die Funktionalitaet der Klasse EditAvailability zu.<br/>
 *
 * @author Michael Anstatt
 * @version 2.0 - 18.07.2012
 */
public class EditAvailabilityGUI extends JFrame {

    private static final long serialVersionUID = 457946443136567295L;
    private JTextField tfName;
    private JCheckBox cbAllDay;
    private JComboBox<Worker> cmbWorker;
    private JCheckBox cbAvailable;
    private JSpinner spnStart;
    private JSpinner spnEnd;
    private JPanel panel_1;
    private JButton btnSave;
    private JButton btnDelete;
    private JButton btnAbbrechen;

    /**
     * Initialisiert GUI, wird von allen public-Konstruktoren aufgerufen
     *
     * @param headline
     *            Ueberschrift des Fensters
     * @param parent
     */
    EditAvailabilityGUI(EditAvailability function, String headline,
            JFrame parent) {
        super(headline);
        initGUI();
        btnSave.addActionListener(function.getSaveListener());
        btnDelete.addActionListener(function.getDeleteListener());
        cbAvailable.addItemListener(function.getCbAvailableListener());
        btnAbbrechen.addActionListener(function.getCancelListener());
        Controller.centerComponent(parent, this);
    }

    /**
     * GUI initialisieren
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
     * Elemente auf Panel anordnen
     *
     * @return fertiges {@link JPanel}
     */
    private JPanel createPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        layout.columnWidths = new int[] { 150, 200 };
        layout.columnWeights = new double[] { 0.1, 1.0 };
        layout.rowWeights =
                new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 1.0 };
        layout.rowHeights = new int[] { 20, 7, 7, 7, 7, 7, 7, 7, 7 };
        panel.setLayout(layout);

        int padding = 2;
        General generalStrings = LocalizedStrings.getGeneralStrings();
        Button buttonStrings = LocalizedStrings.getButton();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        {
            JLabel lblName = new JLabel();
            panel.add(lblName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 5, 5), 0, 0));
            lblName.setText(generalStrings.description() + "(" +
                    generalStrings.optional() + ")");
        }
        {
            JLabel lblStart = new JLabel();
            panel.add(lblStart, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 5, 5), 0, 0));
            lblStart.setText(generalStrings.start());
        }
        {
            JLabel lblEnd = new JLabel();
            panel.add(lblEnd, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 5, 5), 0, 0));
            lblEnd.setText(generalStrings.end());
        }
        {
            tfName = new FilterJTextField();
            panel.add(tfName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(2, 2, 5, 2), 0, 0));
        }
        {
            cbAllDay = new JCheckBox();
            cbAllDay.setSelected(true);
            cbAllDay.setEnabled(false);
            panel.add(cbAllDay, new GridBagConstraints(1, padding, 1, 1, 0.0,
                    0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 5, 2), 0, 0));
            cbAllDay.setText(generalStrings.fullTime());
        }
        {
            cbAvailable = new JCheckBox();
            panel.add(cbAvailable, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 5, 2), 0, 0));
            cbAvailable.setText(generalStrings.available());
        }
        {
            spnStart = new JSpinner(new SpinnerDateModel());
            spnStart.setEditor(new JSpinner.DateEditor(spnStart, ("dd.MM.yyyy")));
            panel.add(spnStart, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 5, 2), 0, 0));
        }
        {
            spnEnd = new JSpinner(new SpinnerDateModel());
            spnEnd.setEditor(new JSpinner.DateEditor(spnEnd, ("dd.MM.yyyy")));
            panel.add(spnEnd, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 5, 2), 0, 0));
            JLabel lblWorker = new JLabel(LocalizedStrings.getLogin().user());
            //was: "Betrifft"
            cmbWorker = new JComboBox<Worker>();
            panel.add(lblWorker, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 5, 5), 0, 0));
            panel.add(cmbWorker, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 5, 2), 0, 0));
        }
        {
            panel_1 = new JPanel();
            GridBagConstraints gbc_panel_1 = new GridBagConstraints();
            gbc_panel_1.weighty = 1.0;
            gbc_panel_1.gridwidth = 2;
            gbc_panel_1.fill = GridBagConstraints.BOTH;
            gbc_panel_1.gridx = 0;
            gbc_panel_1.gridy = 8;
            panel.add(panel_1, gbc_panel_1);
            GridBagLayout gbl_panel_1 = new GridBagLayout();
            gbl_panel_1.columnWidths = new int[] { 79, 0, 0, 0 };
            gbl_panel_1.rowHeights = new int[] { 23, 0 };
            gbl_panel_1.columnWeights =
                    new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
            gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
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
        }
        return panel;
    }

    protected String getDescription() {
        return tfName.getText();
    }

    protected void setDescription(String desc) {
        tfName.setText(desc);
    }

    /**
     * Liefert das ausgewaehlte Startdatum
     *
     * @return Date Objekt des gewaehlten Startdatums
     */
    protected Date getStart() {
        return ((SpinnerDateModel) spnStart.getModel()).getDate();
    }

    /**
     * Liefert das ausgewaehlte Enddatums
     *
     * @return Date Objekt des gewaehlten Enddatums
     */
    protected Date getEnd() {
        return ((SpinnerDateModel) spnEnd.getModel()).getDate();
    }

    /**
     * Liefert den ausgewaehlten Arbeiter
     *
     * @return Worker Objekt des gewaehlten Arbeiters
     */
    protected Worker getWorker() {
        return (Worker) cmbWorker.getSelectedItem();
    }

    /**
     * Boolean ob Ganztaegig
     *
     * @return boolean ob Gaenztaegig
     */
    protected boolean getAllDay() {
        return cbAllDay.isSelected();
    }

    /**
     * Verfuegbarkeit ausgewaehlt?
     *
     * @return Boolean ob Verfuegbar ausgewaehlt
     */
    protected boolean getAvailable() {
        return cbAvailable.isSelected();
    }

    /**
     * Setzt das Auswahlfeld fuer das Startdatum auf ein bestimmtes Datum
     *
     * @param start
     *            Vorgewaehltes Startdatum
     */
    protected void setStart(Date start) {
        spnStart.getModel().setValue(start);
    }

    /**
     * Setzt das Auswahlfeld fuer das Enddatum auf ein bestimmtes Datum
     *
     * @param end
     *            Vorgewaehltes Enddatum
     */
    protected void setEnd(Date end) {
        spnEnd.getModel().setValue(end);
    }

    /**
     * Setzt das Auswahlfeld fuer den Mitarbeiter auf einen bestimmten
     * Mitarbeiter
     *
     * @param worker
     *            Vorgewaehlter Mitarbeiter
     */
    protected void setWorker(Worker worker) {
        cmbWorker.setSelectedItem(worker);
    }

    /**
     * Setzt das Auswahlfeld fuer Mitarbeiter auf mehrere Mitarbeiter
     *
     * @param workerArray
     *            Vorgewaehlte Mitarbeiter
     */
    protected void setWorkers(Worker[] workerArray) {
        ComboBoxModel<Worker> cmbWorkerModel =
                new DefaultComboBoxModel<Worker>(workerArray);
        cmbWorker.setModel(cmbWorkerModel);
    }

    /**
     * Setzt die Checkbox Ganztaegig
     *
     * @param allDay
     *            Checked oder Unchecked
     */
    protected void setAllDay(boolean allDay) {
        cbAllDay.setSelected(allDay);
    }

    /**
     * Setzt die Checkbox Verfuegbar
     *
     * @param allDay
     *            Checked oder Unchecked
     */
    protected void setAvailable(boolean available) {
        cbAvailable.setSelected(available);
    }

    /**
     * Setzt das Dateformat der Verfuegbarkeit
     *
     * @param available
     *            true mit stunden, minuten false ohne
     */
    protected void setAvailableView(boolean available) {
        cbAllDay.setSelected(!available);
        if (available) {
            spnStart.setEditor(new JSpinner.DateEditor(spnStart,
                    ("dd.MM.yyyy HH:mm")));
            spnEnd.setEditor(new JSpinner.DateEditor(spnEnd,
                    ("dd.MM.yyyy HH:mm")));
        } else {
            spnStart.setEditor(new JSpinner.DateEditor(spnStart, ("dd.MM.yyyy")));
            spnEnd.setEditor(new JSpinner.DateEditor(spnEnd, ("dd.MM.yyyy")));
        }
    }

    /**
     * Macht den Delete Button sichtbar/nicht sichtbar
     *
     * @param newView
     *            ob nicht sichtbar
     */
    protected void setNewView(boolean newView) {
        btnDelete.setVisible(!newView);
    }

    /**
     * Deaktiviert das Auswahlfeld fuer Arbeiter
     */
    public void disableWorkerSelection() {
        this.cmbWorker.setEnabled(false);
    }
}
