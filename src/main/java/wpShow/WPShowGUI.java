package wpShow;

import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import functions.WpManager;
import globals.Controller;
import globals.FilterJTextField;
import globals.Workpackage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

import dbServices.WorkerService;
import dbServices.ValuesService;
import wpOverview.WPOverview;
import wpWorker.Worker;

import java.awt.GridLayout;
import java.awt.Font;

/**
 * Studienprojekt: WBS<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Andre Paffenholz, <br/>
 * Peter Lange, <br/>
 * Daniel Metzler,<br/>
 * Samson von Graevenitz<br/>
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: <br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Eingabemaske fuer Arbeitspakete <br/>
 * 
 * @author Samson von Graevenitz, Peter Lange, Michael Anstatt, Marc-Eric
 *         Baumgärtner
 * @version 2.0 - 2012-0824
 */
public class WPShowGUI extends JFrame {

    private static final long serialVersionUID = -5497472870591199416L;
    private JTable tblAufwand;
    private JTextField txfNr;
    private JTextField txfName;
    private JTextField txfSV;
    private JTextField txfCPI;
    private JTextField txfACCost;
    private JTextField txfBACCost;
    private JTextField txfEV;
    private JTextField txfEAC;
    private JTextField txfETCCost;
    private JTextField txfStartCalc;
    private JTextField txfStartHope;
    private JTextField txfEndCalc;
    private JTextField txfEndHope;
    private JTextField txfWorker;
    private JTextField txfPV;
    private JTextField txfSPI;
    private JButton btnAddAncestor;
    private JButton btnEditAncestor;
    private JButton btnAddFollower;
    private JButton btnEditFollower;
    private JButton btnAddWorker;
    private JButton btnRemoveWorker;
    private JButton btnSave;
    private JButton btnCancel;
    private JComboBox<Worker> cobAddWorker;
    private JComboBox<Worker> cobRemoveWorker;
    private JTextArea txfDesc;
    private JCheckBox chbOAP;
    private JCheckBox chbInaktiv;
    private JButton btnAddAufwand;
    private JComboBox<Worker> cobLeiter;
    private JTextField txfTagessatz;
    private JProgressBar barComplete;
    private JScrollPane scrollTable;
    private JLabel lblTagessatz;
    private JButton btnOk;
    private WPShow function;
    private JLabel lblPvSpi;
    private JLabel lblVorgnger;
    private JLabel lblNachfolger;
    private JPanel panel_9;
    private JButton btnCPI;
    private JButton btnSPI;
    private JPanel panel_10;
    private JTextField txfBAC;
    private JTextField txfETC;
    private JTextField txfAC;
    private JPanel panel_11;
    private JTextField txfDate;
    private JButton btnAllPV;

    private final General generalStrings;
    private final Wbs wbsStrings;
    private final Button buttonStrings;

    public WPShowGUI(String title, WPShow function, JFrame parent) {
        super(title);
        this.function = function;

        generalStrings = LocalizedStrings.getGeneralStrings();
        wbsStrings = LocalizedStrings.getWbs();
        buttonStrings = LocalizedStrings.getButton();

        JSplitPane splitPane = new JSplitPane();
        splitPane.setEnabled(false);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new EmptyBorder(5, 5, 5, 3));
        splitPane.setLeftComponent(leftPanel);
        GridBagLayout gbl_leftPanel = new GridBagLayout();
        gbl_leftPanel.columnWidths = new int[] { 0, 0, 0 };
        gbl_leftPanel.rowHeights =
                new int[] { 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_leftPanel.columnWeights =
                new double[] { 1.0, 1.0, Double.MIN_VALUE };
        gbl_leftPanel.rowWeights =
                new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        leftPanel.setLayout(gbl_leftPanel);

        JLabel lblNr = new JLabel(wbsStrings.workPackageId());
        GridBagConstraints gbc_lblNr = new GridBagConstraints();
        gbc_lblNr.anchor = GridBagConstraints.WEST;
        gbc_lblNr.insets = new Insets(0, 0, 5, 5);
        gbc_lblNr.gridx = 0;
        gbc_lblNr.gridy = 0;
        leftPanel.add(lblNr, gbc_lblNr);

        txfNr = new FilterJTextField();
        GridBagConstraints gbc_txfNr = new GridBagConstraints();
        gbc_txfNr.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfNr.insets = new Insets(0, 0, 5, 0);
        gbc_txfNr.gridx = 1;
        gbc_txfNr.gridy = 0;
        leftPanel.add(txfNr, gbc_txfNr);
        txfNr.setColumns(10);

        JLabel lblName = new JLabel(wbsStrings.workPackage()); // TODO OK? war:
                                                               // name
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.anchor = GridBagConstraints.WEST;
        gbc_lblName.insets = new Insets(0, 0, 5, 5);
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 1;
        leftPanel.add(lblName, gbc_lblName);

        txfName = new FilterJTextField();
        GridBagConstraints gbc_txfName = new GridBagConstraints();
        gbc_txfName.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfName.insets = new Insets(0, 0, 5, 0);
        gbc_txfName.gridx = 1;
        gbc_txfName.gridy = 1;
        leftPanel.add(txfName, gbc_txfName);
        txfName.setColumns(10);

        JPanel panel_5 = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel_5.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        GridBagConstraints gbc_panel_5 = new GridBagConstraints();
        gbc_panel_5.insets = new Insets(0, 0, 5, 0);
        gbc_panel_5.fill = GridBagConstraints.BOTH;
        gbc_panel_5.gridx = 1;
        gbc_panel_5.gridy = 2;
        leftPanel.add(panel_5, gbc_panel_5);

        chbOAP = new JCheckBox("OAP");
        panel_5.add(chbOAP);

        chbInaktiv = new JCheckBox(generalStrings.inactive());
        panel_5.add(chbInaktiv);

        JLabel lblBeschreibung = new JLabel(generalStrings.description());
        GridBagConstraints gbc_lblBeschreibung = new GridBagConstraints();
        gbc_lblBeschreibung.anchor = GridBagConstraints.WEST;
        gbc_lblBeschreibung.insets = new Insets(0, 0, 5, 5);
        gbc_lblBeschreibung.gridx = 0;
        gbc_lblBeschreibung.gridy = 3;
        leftPanel.add(lblBeschreibung, gbc_lblBeschreibung);

        txfDesc = new JTextArea() {
            private static final long serialVersionUID = -3874181090738553731L;

            public String getText() {
                return FilterJTextField.filterText(super.getText());
            }

        };
        txfDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txfDesc.setLineWrap(true);
        txfDesc.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        GridBagConstraints gbc_txfDesc = new GridBagConstraints();
        gbc_txfDesc.insets = new Insets(0, 0, 5, 0);
        gbc_txfDesc.fill = GridBagConstraints.BOTH;
        gbc_txfDesc.gridx = 1;
        gbc_txfDesc.gridy = 3;
        leftPanel.add(txfDesc, gbc_txfDesc);

        lblVorgnger = new JLabel(generalStrings.predecessor());
        GridBagConstraints gbc_lblVorgnger = new GridBagConstraints();
        gbc_lblVorgnger.anchor = GridBagConstraints.WEST;
        gbc_lblVorgnger.insets = new Insets(0, 0, 5, 5);
        gbc_lblVorgnger.gridx = 0;
        gbc_lblVorgnger.gridy = 4;
        leftPanel.add(lblVorgnger, gbc_lblVorgnger);

        JPanel panel_2 = new JPanel();
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.insets = new Insets(0, 0, 5, 0);
        gbc_panel_2.fill = GridBagConstraints.BOTH;
        gbc_panel_2.gridx = 1;
        gbc_panel_2.gridy = 4;
        leftPanel.add(panel_2, gbc_panel_2);

        btnAddAncestor = new JButton(buttonStrings.add());
        btnAddAncestor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        panel_2.setLayout(new GridLayout(0, 2, 5, 0));
        panel_2.add(btnAddAncestor);

        btnEditAncestor = new JButton(buttonStrings.manage());
        panel_2.add(btnEditAncestor);

        lblNachfolger = new JLabel(generalStrings.successor());
        GridBagConstraints gbc_lblNachfolger = new GridBagConstraints();
        gbc_lblNachfolger.anchor = GridBagConstraints.WEST;
        gbc_lblNachfolger.insets = new Insets(0, 0, 5, 5);
        gbc_lblNachfolger.gridx = 0;
        gbc_lblNachfolger.gridy = 5;
        leftPanel.add(lblNachfolger, gbc_lblNachfolger);

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(null);
        GridBagConstraints gbc_panel_3 = new GridBagConstraints();
        gbc_panel_3.insets = new Insets(0, 0, 5, 0);
        gbc_panel_3.fill = GridBagConstraints.BOTH;
        gbc_panel_3.gridx = 1;
        gbc_panel_3.gridy = 5;
        leftPanel.add(panel_3, gbc_panel_3);
        panel_3.setLayout(new GridLayout(0, 2, 5, 0));

        btnAddFollower = new JButton(buttonStrings.add());
        panel_3.add(btnAddFollower);

        btnEditFollower = new JButton(buttonStrings.manage());
        panel_3.add(btnEditFollower);

        JLabel lblVerantwortlicher =
                new JLabel(generalStrings.responsiblePerson());
        GridBagConstraints gbc_lblVerantwortlicher = new GridBagConstraints();
        gbc_lblVerantwortlicher.anchor = GridBagConstraints.WEST;
        gbc_lblVerantwortlicher.insets = new Insets(0, 0, 5, 5);
        gbc_lblVerantwortlicher.gridx = 0;
        gbc_lblVerantwortlicher.gridy = 6;
        leftPanel.add(lblVerantwortlicher, gbc_lblVerantwortlicher);

        cobLeiter = new JComboBox<Worker>();
        GridBagConstraints gbc_cobLeiter = new GridBagConstraints();
        gbc_cobLeiter.fill = GridBagConstraints.HORIZONTAL;
        gbc_cobLeiter.insets = new Insets(0, 0, 5, 0);
        gbc_cobLeiter.gridx = 1;
        gbc_cobLeiter.gridy = 6;
        leftPanel.add(cobLeiter, gbc_cobLeiter);

        JLabel lblMitarbeiter = new JLabel(LocalizedStrings.getLogin().user()); // TODO
                                                                                // ok?
                                                                                // war
                                                                                // Mitarbeiter
        GridBagConstraints gbc_lblMitarbeiter = new GridBagConstraints();
        gbc_lblMitarbeiter.anchor = GridBagConstraints.WEST;
        gbc_lblMitarbeiter.insets = new Insets(0, 0, 5, 5);
        gbc_lblMitarbeiter.gridx = 0;
        gbc_lblMitarbeiter.gridy = 7;
        leftPanel.add(lblMitarbeiter, gbc_lblMitarbeiter);

        txfWorker = new FilterJTextField();
        txfWorker.setEditable(false);
        txfWorker.setEditable(false);
        GridBagConstraints gbc_txfWorker = new GridBagConstraints();
        gbc_txfWorker.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfWorker.insets = new Insets(0, 0, 5, 0);
        gbc_txfWorker.gridx = 1;
        gbc_txfWorker.gridy = 7;
        leftPanel.add(txfWorker, gbc_txfWorker);
        txfWorker.setColumns(10);

        JPanel panel_4 = new JPanel();
        GridBagConstraints gbc_panel_4 = new GridBagConstraints();
        gbc_panel_4.insets = new Insets(0, 0, 5, 0);
        gbc_panel_4.fill = GridBagConstraints.BOTH;
        gbc_panel_4.gridx = 1;
        gbc_panel_4.gridy = 8;
        leftPanel.add(panel_4, gbc_panel_4);
        GridBagLayout gbl_panel_4 = new GridBagLayout();
        gbl_panel_4.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel_4.rowHeights = new int[] { 23, 23, 0 };
        gbl_panel_4.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        gbl_panel_4.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        panel_4.setLayout(gbl_panel_4);

        btnAddWorker = new JButton("+");
        cobAddWorker = new JComboBox<Worker>();
        GridBagConstraints gbc_cobAddWorker = new GridBagConstraints();
        gbc_cobAddWorker.fill = GridBagConstraints.BOTH;
        gbc_cobAddWorker.insets = new Insets(0, 0, 5, 5);
        gbc_cobAddWorker.gridx = 0;
        gbc_cobAddWorker.gridy = 0;
        panel_4.add(cobAddWorker, gbc_cobAddWorker);
        GridBagConstraints gbc_btnAddWorker = new GridBagConstraints();
        gbc_btnAddWorker.fill = GridBagConstraints.BOTH;
        gbc_btnAddWorker.insets = new Insets(0, 0, 5, 0);
        gbc_btnAddWorker.gridx = 1;
        gbc_btnAddWorker.gridy = 0;
        panel_4.add(btnAddWorker, gbc_btnAddWorker);

        cobRemoveWorker = new JComboBox<Worker>();
        GridBagConstraints gbc_cobRemoveWorker = new GridBagConstraints();
        gbc_cobRemoveWorker.fill = GridBagConstraints.BOTH;
        gbc_cobRemoveWorker.insets = new Insets(0, 0, 0, 5);
        gbc_cobRemoveWorker.gridx = 0;
        gbc_cobRemoveWorker.gridy = 1;
        panel_4.add(cobRemoveWorker, gbc_cobRemoveWorker);

        btnRemoveWorker = new JButton("-");
        GridBagConstraints gbc_btnRemoveWorker = new GridBagConstraints();
        gbc_btnRemoveWorker.fill = GridBagConstraints.BOTH;
        gbc_btnRemoveWorker.gridx = 1;
        gbc_btnRemoveWorker.gridy = 1;
        panel_4.add(btnRemoveWorker, gbc_btnRemoveWorker);

        JLabel lblStart = new JLabel(wbsStrings.calculatedStart());
        GridBagConstraints gbc_lblStart = new GridBagConstraints();
        gbc_lblStart.anchor = GridBagConstraints.WEST;
        gbc_lblStart.insets = new Insets(0, 0, 5, 5);
        gbc_lblStart.gridx = 0;
        gbc_lblStart.gridy = 9;
        leftPanel.add(lblStart, gbc_lblStart);

        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 9;
        leftPanel.add(panel, gbc_panel);
        panel.setLayout(new GridLayout(0, 2, 5, 0));

        txfStartHope = new FilterJTextField();
        panel.add(txfStartHope);
        txfStartHope.setColumns(10);

        txfStartCalc = new FilterJTextField();
        txfStartCalc.setEditable(false);
        txfStartCalc.setEditable(false);
        panel.add(txfStartCalc);
        txfStartCalc.setColumns(10);

        JLabel lblEnd = new JLabel(wbsStrings.calculatedRelease());
        GridBagConstraints gbc_lblEnd = new GridBagConstraints();
        gbc_lblEnd.anchor = GridBagConstraints.WEST;
        gbc_lblEnd.insets = new Insets(0, 0, 5, 5);
        gbc_lblEnd.gridx = 0;
        gbc_lblEnd.gridy = 10;
        leftPanel.add(lblEnd, gbc_lblEnd);

        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 10;
        leftPanel.add(panel_1, gbc_panel_1);
        panel_1.setLayout(new GridLayout(0, 2, 5, 0));

        txfEndHope = new FilterJTextField();
        panel_1.add(txfEndHope);
        txfEndHope.setColumns(10);

        txfEndCalc = new FilterJTextField();
        txfEndCalc.setEditable(false);
        txfEndCalc.setEditable(false);
        panel_1.add(txfEndCalc);
        txfEndCalc.setColumns(10);

        JLabel lblStatus = new JLabel(generalStrings.status());
        GridBagConstraints gbc_lblStatus = new GridBagConstraints();
        gbc_lblStatus.anchor = GridBagConstraints.WEST;
        gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
        gbc_lblStatus.gridx = 0;
        gbc_lblStatus.gridy = 11;
        leftPanel.add(lblStatus, gbc_lblStatus);

        barComplete = new JProgressBar();
        GridBagConstraints gbc_barComplete = new GridBagConstraints();
        gbc_barComplete.fill = GridBagConstraints.HORIZONTAL;
        gbc_barComplete.insets = new Insets(0, 0, 5, 0);
        gbc_barComplete.gridx = 1;
        gbc_barComplete.gridy = 11;
        leftPanel.add(barComplete, gbc_barComplete);

        JLabel lblCpi = new JLabel(wbsStrings.cpiAndSpi());
        GridBagConstraints gbc_lblCpi = new GridBagConstraints();
        gbc_lblCpi.anchor = GridBagConstraints.WEST;
        gbc_lblCpi.insets = new Insets(0, 0, 5, 5);
        gbc_lblCpi.gridx = 0;
        gbc_lblCpi.gridy = 12;
        leftPanel.add(lblCpi, gbc_lblCpi);

        JPanel panel_7 = new JPanel();
        GridBagConstraints gbc_panel_7 = new GridBagConstraints();
        gbc_panel_7.insets = new Insets(0, 0, 5, 0);
        gbc_panel_7.fill = GridBagConstraints.BOTH;
        gbc_panel_7.gridx = 1;
        gbc_panel_7.gridy = 12;
        leftPanel.add(panel_7, gbc_panel_7);
        panel_7.setLayout(new GridLayout(1, 2, 5, 0));

        txfCPI = new FilterJTextField();
        txfCPI.setEnabled(false);
        panel_7.add(txfCPI);
        txfCPI.setEditable(false);
        txfCPI.setEditable(false);
        txfCPI.setColumns(10);

        txfSPI = new FilterJTextField();
        txfSPI.setEnabled(false);
        txfSPI.setEditable(false);
        txfSPI.setEditable(false);
        txfSPI.setColumns(10);
        panel_7.add(txfSPI);

        panel_9 = new JPanel();
        GridBagConstraints gbc_panel_9 = new GridBagConstraints();
        gbc_panel_9.insets = new Insets(0, 0, 5, 0);
        gbc_panel_9.fill = GridBagConstraints.BOTH;
        gbc_panel_9.gridx = 1;
        gbc_panel_9.gridy = 13;
        leftPanel.add(panel_9, gbc_panel_9);
        panel_9.setLayout(new GridLayout(1, 2, 5, 0));

        btnCPI = new JButton(wbsStrings.graph(wbsStrings.cpi()));
        panel_9.add(btnCPI);

        btnSPI = new JButton(wbsStrings.graph(wbsStrings.spi()));
        panel_9.add(btnSPI);

        JLabel lblBac =
                new JLabel(wbsStrings.bac() + " / " + wbsStrings.etc() + " / "
                        + wbsStrings.ac());
        lblBac.setToolTipText(wbsStrings.inDays());
        GridBagConstraints gbc_lblBac = new GridBagConstraints();
        gbc_lblBac.anchor = GridBagConstraints.WEST;
        gbc_lblBac.insets = new Insets(0, 0, 5, 5);
        gbc_lblBac.gridx = 0;
        gbc_lblBac.gridy = 14;
        leftPanel.add(lblBac, gbc_lblBac);

        panel_10 = new JPanel();
        GridBagConstraints gbc_panel_10 = new GridBagConstraints();
        gbc_panel_10.insets = new Insets(0, 0, 5, 0);
        gbc_panel_10.fill = GridBagConstraints.BOTH;
        gbc_panel_10.gridx = 1;
        gbc_panel_10.gridy = 14;
        leftPanel.add(panel_10, gbc_panel_10);
        panel_10.setLayout(new GridLayout(1, 0, 5, 0));

        txfBAC = new FilterJTextField();
        txfBAC.setToolTipText(wbsStrings.inDays(wbsStrings.bac()));
        txfBAC.setColumns(5);
        panel_10.add(txfBAC);

        txfETC = new FilterJTextField();
        txfETC.setToolTipText(wbsStrings.inDays(wbsStrings.etc()));
        txfETC.setColumns(5);
        panel_10.add(txfETC);

        txfAC = new FilterJTextField();
        txfAC.setEditable(false);
        txfAC.setColumns(5);
        panel_10.add(txfAC);

        JLabel lblPv = new JLabel(wbsStrings.sv() + " / " + wbsStrings.pv());
        GridBagConstraints gbc_lblPv = new GridBagConstraints();
        gbc_lblPv.anchor = GridBagConstraints.WEST;
        gbc_lblPv.insets = new Insets(0, 0, 5, 5);
        gbc_lblPv.gridx = 0;
        gbc_lblPv.gridy = 15;
        leftPanel.add(lblPv, gbc_lblPv);

        JPanel panel_6 = new JPanel();
        GridBagConstraints gbc_panel_6 = new GridBagConstraints();
        gbc_panel_6.insets = new Insets(0, 0, 5, 0);
        gbc_panel_6.fill = GridBagConstraints.BOTH;
        gbc_panel_6.gridx = 1;
        gbc_panel_6.gridy = 15;
        leftPanel.add(panel_6, gbc_panel_6);
        panel_6.setLayout(new GridLayout(0, 2, 5, 0));

        txfSV = new FilterJTextField();
        panel_6.add(txfSV);
        txfSV.setEditable(false);
        txfSV.setEditable(false);
        txfSV.setColumns(10);

        txfPV = new FilterJTextField();
        txfPV.setEditable(false);
        txfPV.setEditable(false);
        panel_6.add(txfPV);
        txfPV.setColumns(10);

        JLabel lblBacKosten =
                new JLabel(generalStrings.costs(wbsStrings.bac()));
        GridBagConstraints gbc_lblBacKosten = new GridBagConstraints();
        gbc_lblBacKosten.anchor = GridBagConstraints.WEST;
        gbc_lblBacKosten.insets = new Insets(0, 0, 5, 5);
        gbc_lblBacKosten.gridx = 0;
        gbc_lblBacKosten.gridy = 16;
        leftPanel.add(lblBacKosten, gbc_lblBacKosten);

        txfBACCost = new FilterJTextField();
        txfBACCost.setEditable(false);
        txfBACCost.setEditable(false);
        GridBagConstraints gbc_txfBACCost = new GridBagConstraints();
        gbc_txfBACCost.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfBACCost.insets = new Insets(0, 0, 5, 0);
        gbc_txfBACCost.gridx = 1;
        gbc_txfBACCost.gridy = 16;
        leftPanel.add(txfBACCost, gbc_txfBACCost);
        txfBACCost.setColumns(10);

        JLabel lblAcKosten = new JLabel(generalStrings.costs(wbsStrings.ac()));
        GridBagConstraints gbc_lblAcKosten = new GridBagConstraints();
        gbc_lblAcKosten.anchor = GridBagConstraints.WEST;
        gbc_lblAcKosten.insets = new Insets(0, 0, 5, 5);
        gbc_lblAcKosten.gridx = 0;
        gbc_lblAcKosten.gridy = 17;
        leftPanel.add(lblAcKosten, gbc_lblAcKosten);

        txfACCost = new FilterJTextField();
        txfACCost.setEditable(false);
        txfACCost.setEditable(false);
        GridBagConstraints gbc_txfACCost = new GridBagConstraints();
        gbc_txfACCost.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfACCost.insets = new Insets(0, 0, 5, 0);
        gbc_txfACCost.gridx = 1;
        gbc_txfACCost.gridy = 17;
        leftPanel.add(txfACCost, gbc_txfACCost);
        txfACCost.setColumns(10);

        JLabel lblEv = new JLabel(wbsStrings.ev());
        GridBagConstraints gbc_lblEv = new GridBagConstraints();
        gbc_lblEv.anchor = GridBagConstraints.WEST;
        gbc_lblEv.insets = new Insets(0, 0, 5, 5);
        gbc_lblEv.gridx = 0;
        gbc_lblEv.gridy = 18;
        leftPanel.add(lblEv, gbc_lblEv);

        txfEV = new FilterJTextField();
        txfEV.setEditable(false);
        txfEV.setEditable(false);
        GridBagConstraints gbc_txfEV = new GridBagConstraints();
        gbc_txfEV.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfEV.insets = new Insets(0, 0, 5, 0);
        gbc_txfEV.gridx = 1;
        gbc_txfEV.gridy = 18;
        leftPanel.add(txfEV, gbc_txfEV);
        txfEV.setColumns(10);

        JLabel lblEac = new JLabel(wbsStrings.eac());
        GridBagConstraints gbc_lblEac = new GridBagConstraints();
        gbc_lblEac.anchor = GridBagConstraints.WEST;
        gbc_lblEac.insets = new Insets(0, 0, 5, 5);
        gbc_lblEac.gridx = 0;
        gbc_lblEac.gridy = 19;
        leftPanel.add(lblEac, gbc_lblEac);

        txfEAC = new FilterJTextField();
        txfEAC.setEditable(false);
        txfEAC.setEditable(false);
        GridBagConstraints gbc_txfEAC = new GridBagConstraints();
        gbc_txfEAC.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfEAC.insets = new Insets(0, 0, 5, 0);
        gbc_txfEAC.gridx = 1;
        gbc_txfEAC.gridy = 19;
        leftPanel.add(txfEAC, gbc_txfEAC);
        txfEAC.setColumns(10);

        JLabel lblEtcKosten =
                new JLabel(generalStrings.costs(wbsStrings.etc()));
        GridBagConstraints gbc_lblEtcKosten = new GridBagConstraints();
        gbc_lblEtcKosten.anchor = GridBagConstraints.WEST;
        gbc_lblEtcKosten.insets = new Insets(0, 0, 5, 5);
        gbc_lblEtcKosten.gridx = 0;
        gbc_lblEtcKosten.gridy = 20;
        leftPanel.add(lblEtcKosten, gbc_lblEtcKosten);

        txfETCCost = new FilterJTextField();
        txfETCCost.setEditable(false);
        txfETCCost.setEditable(false);
        GridBagConstraints gbc_txfETCCost = new GridBagConstraints();
        gbc_txfETCCost.insets = new Insets(0, 0, 5, 0);
        gbc_txfETCCost.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfETCCost.gridx = 1;
        gbc_txfETCCost.gridy = 20;
        leftPanel.add(txfETCCost, gbc_txfETCCost);
        txfETCCost.setColumns(10);

        lblTagessatz = new JLabel(wbsStrings.dailyRate());
        GridBagConstraints gbc_lblTagessatz = new GridBagConstraints();
        gbc_lblTagessatz.insets = new Insets(0, 0, 5, 5);
        gbc_lblTagessatz.anchor = GridBagConstraints.WEST;
        gbc_lblTagessatz.gridx = 0;
        gbc_lblTagessatz.gridy = 21;
        leftPanel.add(lblTagessatz, gbc_lblTagessatz);

        txfTagessatz = new FilterJTextField();
        txfTagessatz.setEditable(false);
        txfTagessatz.setEditable(false);
        GridBagConstraints gbc_txfTagessatz = new GridBagConstraints();
        gbc_txfTagessatz.insets = new Insets(0, 0, 5, 0);
        gbc_txfTagessatz.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfTagessatz.gridx = 1;
        gbc_txfTagessatz.gridy = 21;
        leftPanel.add(txfTagessatz, gbc_txfTagessatz);
        txfTagessatz.setColumns(10);

        lblPvSpi =
                new JLabel(wbsStrings.pv() + " / " + wbsStrings.spi()
                        + generalStrings.date());
        GridBagConstraints gbc_lblPvSpi = new GridBagConstraints();
        gbc_lblPvSpi.insets = new Insets(0, 0, 5, 5);
        gbc_lblPvSpi.anchor = GridBagConstraints.WEST;
        gbc_lblPvSpi.gridx = 0;
        gbc_lblPvSpi.gridy = 22;
        leftPanel.add(lblPvSpi, gbc_lblPvSpi);

        panel_11 = new JPanel();
        GridBagConstraints gbc_panel_11 = new GridBagConstraints();
        gbc_panel_11.insets = new Insets(0, 0, 5, 0);
        gbc_panel_11.fill = GridBagConstraints.BOTH;
        gbc_panel_11.gridx = 1;
        gbc_panel_11.gridy = 22;
        leftPanel.add(panel_11, gbc_panel_11);
        panel_11.setLayout(new GridLayout(1, 0, 5, 0));

        txfDate = new FilterJTextField();
        txfDate.setEditable(false);
        txfDate.setEnabled(false);
        panel_11.add(txfDate);
        txfDate.setText(Controller.DATE_DAY.format(ValuesService.getNextFriday(
                new Date(System.currentTimeMillis())).getTime()));

        btnAllPV = new JButton(generalStrings.history());
        btnAllPV.addActionListener(function.getBtnAllPVListener());
        panel_11.add(btnAllPV);

        btnAddAufwand = new JButton(wbsStrings.enter(wbsStrings.workEffort()));
        GridBagConstraints gbc_btnAddAufwand = new GridBagConstraints();
        gbc_btnAddAufwand.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnAddAufwand.insets = new Insets(0, 0, 5, 0);
        gbc_btnAddAufwand.gridx = 1;
        gbc_btnAddAufwand.gridy = 23;
        leftPanel.add(btnAddAufwand, gbc_btnAddAufwand);

        JPanel panel_8 = new JPanel();
        GridBagConstraints gbc_panel_8 = new GridBagConstraints();
        gbc_panel_8.anchor = GridBagConstraints.EAST;
        gbc_panel_8.gridwidth = 2;
        gbc_panel_8.fill = GridBagConstraints.VERTICAL;
        gbc_panel_8.gridx = 0;
        gbc_panel_8.gridy = 24;
        leftPanel.add(panel_8, gbc_panel_8);
        GridBagLayout gbl_panel_8 = new GridBagLayout();
        gbl_panel_8.columnWidths = new int[] { 0, 0, 0, 0 };
        gbl_panel_8.rowHeights = new int[] { 0, 0 };
        gbl_panel_8.columnWeights =
                new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
        gbl_panel_8.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        panel_8.setLayout(gbl_panel_8);

        btnOk = new JButton(buttonStrings.ok());
        GridBagConstraints gbc_btnOk = new GridBagConstraints();
        gbc_btnOk.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnOk.insets = new Insets(0, 0, 0, 5);
        gbc_btnOk.gridx = 0;
        gbc_btnOk.gridy = 0;
        panel_8.add(btnOk, gbc_btnOk);

        btnSave = new JButton(buttonStrings.apply());
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        GridBagConstraints gbc_btnSave = new GridBagConstraints();
        gbc_btnSave.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSave.insets = new Insets(0, 0, 0, 5);
        gbc_btnSave.gridx = 1;
        gbc_btnSave.gridy = 0;
        panel_8.add(btnSave, gbc_btnSave);

        btnCancel = new JButton(buttonStrings.cancel());
        GridBagConstraints gbc_btnCancel = new GridBagConstraints();
        gbc_btnCancel.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnCancel.gridx = 2;
        gbc_btnCancel.gridy = 0;
        panel_8.add(btnCancel, gbc_btnCancel);

        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(null);
        splitPane.setRightComponent(rightPanel);
        rightPanel.setLayout(new BorderLayout(0, 0));

        tblAufwand = new JTable();
        tblAufwand.setRowSelectionAllowed(false);
        tblAufwand.setModel(new DefaultTableModel(new Object[][] { { "", "",
                "", null }, }, new String[] {
                LocalizedStrings.getLogin().user(), // TODO ok? war: Name
                wbsStrings.workEffort(), generalStrings.date(),
                generalStrings.description() }) {

            private static final long serialVersionUID = 5903248957368034227L;
            @SuppressWarnings("rawtypes")
            // autogenerierter Code von Eclipse
            Class[] columnTypes = new Class[] { String.class, String.class,
                    String.class, String.class };

            @SuppressWarnings({ "unchecked", "rawtypes" })
            // autogenerierter Code von Eclipse
                    public
                    Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        tblAufwand.getColumnModel().getColumn(0).setMaxWidth(150);
        tblAufwand.getColumnModel().getColumn(1).setMaxWidth(100);
        tblAufwand.getColumnModel().getColumn(2).setMaxWidth(150);

        scrollTable = new JScrollPane(tblAufwand);
        rightPanel.add(scrollTable, BorderLayout.CENTER);

        this.pack();

        Controller.centerComponent(parent, this);

        this.setVisible(true);

        addButtonAction();
    }

    protected String getNr() {
        return txfNr.getText();
    }

    protected String getWpName() {
        return txfName.getText();
    }

    protected boolean getIsOAP() {
        return chbOAP.isSelected();
    }

    protected boolean getIsInaktiv() {
        return chbInaktiv.isSelected();
    }

    protected Double getBAC() throws ParseException {
        if (txfBAC.getText().equals("")) {
            return null;
        }
        return Double.parseDouble(txfBAC.getText());
    }

    protected double getETC() throws ParseException {
        return Double.parseDouble(txfETC.getText());
    }

    protected Worker getLeiter() {
        return (Worker) cobLeiter.getSelectedItem();
    }

    protected void setWorkers(List<String> workers) {
        String responsableWorkers = "";
        for (String actualWorker : workers) {
            responsableWorkers += actualWorker + ", ";
        }
        if (responsableWorkers.length() != 0) {
            responsableWorkers =
                    responsableWorkers.substring(0,
                            responsableWorkers.length() - 2);
        }
        this.txfWorker.setText(responsableWorkers);
    }

    protected String[] getWorkers() {
        return this.txfWorker.getText().split(", ");
    }

    protected Worker getAddWorker() {
        return (Worker) cobAddWorker.getSelectedItem();
    }

    protected Worker getRemoveWorker() {
        return (Worker) cobRemoveWorker.getSelectedItem();
    }

    protected Date getStartHope() throws ParseException {
        if (txfStartHope.getText().equals("")) {
            return null;
        }
        return Controller.DATE_DAY.parse(txfStartHope.getText());
    }

    protected Date getEndHope() throws ParseException {
        if (txfEndHope.getText().equals("")) {
            return null;
        }
        return Controller.DATE_DAY.parse(txfEndHope.getText());
    }

    protected void setValues(Workpackage wp, boolean leiter,
            String[][] aufwaende, ArrayList<Worker> allWorkers,
            Set<String> actualWPWorkers) {

        this.txfNr.setText(wp.getStringID());
        this.txfName.setText(wp.getName());
        this.chbOAP.setSelected(wp.isIstOAP());
        this.chbInaktiv.setSelected(wp.isIstInaktiv());
        this.txfDesc.setText(wp.getBeschreibung());

        this.cobLeiter.removeAllItems();
        Worker maLeiter = null;
        for (Worker actualWorker : allWorkers) {
            this.cobLeiter.addItem(actualWorker);
            if (actualWorker.getId() == wp.getFid_Leiter()) {
                maLeiter = actualWorker;
            }
        }
        if (maLeiter != null) {
            this.cobLeiter.setSelectedItem(maLeiter);
        }

        if (wp.getStartDateHope() != null) {
            this.txfStartHope.setText(Controller.DATE_DAY.format(wp
                    .getStartDateHope()));
        } else {
            this.txfStartHope.setText("");
        }

        if (wp.getEndDateHope() != null) {
            this.txfEndHope.setText(Controller.DATE_DAY.format(wp
                    .getEndDateHope()));
        } else {
            this.txfEndHope.setText("");
        }

        if (wp.getStartDateCalc() != null) {
            this.txfStartCalc.setText(Controller.DATE_DAY.format(wp
                    .getStartDateCalc()));
        } else {
            this.txfStartCalc.setText("");
        }

        if (wp.getEndDateCalc() != null) {
            this.txfEndCalc.setText(Controller.DATE_DAY.format(wp
                    .getEndDateCalc()));
        } else {
            this.txfEndCalc.setText("");
        }

        DecimalFormatSymbols ds = new DecimalFormatSymbols();
        ds.setDecimalSeparator('.');
        DecimalFormat workDaysFormater = new DecimalFormat("#0.000", ds);

        this.barComplete.setValue(WpManager.calcPercentComplete(wp.getBac(),
                wp.getEtc(), wp.getAc()));
        this.txfCPI.setText("" + Controller.DECFORM_VALUES.format(wp.getCpi()));
        this.txfSPI.setText("" + Controller.DECFORM_VALUES.format(wp.getSpi()));
        this.txfBAC.setText("" + workDaysFormater.format(wp.getBac()));
        this.txfETC.setText("" + workDaysFormater.format(wp.getEtc()));
        this.txfAC.setText("" + workDaysFormater.format(wp.getAc()));
        this.txfEV.setText("" + Controller.DECFORM_VALUES.format(wp.getEv()));
        this.txfSV.setText(""
                + Controller.DECFORM_VALUES.format(wp.getSv())
                        .replace(",", ".") + generalStrings.currencySymbol());
        this.txfPV.setText(""
                + Controller.DECFORM_VALUES.format(wp.getPv())
                        .replace(",", ".") + generalStrings.currencySymbol());
        this.txfBACCost.setText(""
                + Controller.DECFORM_VALUES.format(wp.getBac_kosten()).replace(
                        ",", ".") + generalStrings.currencySymbol());
        this.txfACCost.setText(""
                + Controller.DECFORM_VALUES.format(wp.getAc_kosten()).replace(
                        ",", ".") + generalStrings.currencySymbol());
        this.txfEV.setText(""
                + Controller.DECFORM_VALUES.format(wp.getEv())
                        .replace(",", ".") + generalStrings.currencySymbol());
        this.txfEAC.setText(""
                + Controller.DECFORM_VALUES.format(wp.getEac()).replace(",",
                        ".") + generalStrings.currencySymbol());
        this.txfETCCost.setText(""
                + Controller.DECFORM_VALUES.format(wp.getEtc_kosten()).replace(
                        ",", ".") + generalStrings.currencySymbol());
        this.txfTagessatz.setText(""
                + Controller.DECFORM_VALUES.format(wp.getWptagessatz())
                        .replace(",", ".") + generalStrings.currencySymbol());

        txfCPI.setDisabledTextColor(WPOverview.getCPIColor(wp.getCpi(),
                wp.getAc())[0]);
        txfCPI.setBackground(WPOverview.getCPIColor(wp.getCpi(), wp.getAc())[1]);

        txfSPI.setDisabledTextColor(WPOverview.getSPIColor(wp.getSpi(),
                wp.getAc())[0]);
        txfSPI.setBackground(WPOverview.getSPIColor(wp.getSpi(), wp.getAc())[1]);

        updateDependencyCount(wp);

        fillWorkerCombos(actualWPWorkers);
        fillWorkedTable(aufwaende, leiter);

    }

    public void setOAPView(boolean leiter) {
        setUAPViews(false, leiter);
        setOAPViews(true, leiter);
    }

    public void setUAPView(boolean leiter) {
        setOAPViews(false, leiter);
        setUAPViews(true, leiter);
    }

    private void setOAPViews(boolean isOAP, boolean leiter) {
        boolean notIfOAP = !isOAP;
        this.cobAddWorker.setVisible(notIfOAP);
        this.cobRemoveWorker.setVisible(notIfOAP);
        this.btnAddWorker.setVisible(notIfOAP);
        this.btnRemoveWorker.setVisible(notIfOAP);
        this.txfBAC.setEditable(notIfOAP);
        this.txfETC.setEditable(notIfOAP);
        this.txfTagessatz.setVisible(notIfOAP);
        this.lblTagessatz.setVisible(notIfOAP);
        this.btnAddAufwand.setVisible(notIfOAP);
        this.scrollTable.setVisible(notIfOAP);
        this.txfBAC.setEditable(notIfOAP);

        this.txfName.setEditable(leiter);
        this.chbOAP.setEnabled(leiter);
        this.chbInaktiv.setEnabled(leiter);
        this.lblVorgnger.setVisible(leiter);
        this.lblNachfolger.setVisible(leiter);
        this.txfStartHope.setEditable(leiter);
        this.txfEndHope.setEditable(leiter);

        this.cobLeiter.setEnabled(leiter);
        this.btnAddWorker.setVisible(leiter);
        this.btnRemoveWorker.setVisible(leiter);
        this.cobAddWorker.setVisible(leiter);
        this.cobRemoveWorker.setVisible(leiter);
        this.btnAddAncestor.setVisible(leiter);
        this.btnAddFollower.setVisible(leiter);
        this.btnEditAncestor.setVisible(leiter);
        this.btnEditFollower.setVisible(leiter);

        this.pack();

    }

    private void setUAPViews(boolean isUAP, boolean leiter) {
        if (!leiter) {
            this.tblAufwand.removeColumn(this.tblAufwand.getColumn("Name"));
            this.txfBAC.setEnabled(false);
        } else {

        }
    }

    protected String getDescription() {
        return this.txfDesc.getText();
    }

    public void fillWorkerCombos(Set<String> actualWPWorkers) {
        cobAddWorker.removeAllItems();
        cobRemoveWorker.removeAllItems();
        List<Worker> dummies = new ArrayList<Worker>();
        for (String actualID : actualWPWorkers) {
            dummies.add(new Worker(actualID));
        }
        cobAddWorker.addItem(new Worker(""));
        cobRemoveWorker.addItem(new Worker(""));
        for (Worker actualWorker : WorkerService.getRealWorkers()) {
            if (!dummies.contains(actualWorker)) {
                cobAddWorker.addItem(actualWorker);
            } else {
                cobRemoveWorker.addItem(actualWorker);
            }

        }
        if (this.getLeiter() != null) {
            cobRemoveWorker.removeItem(this.getLeiter()); // Leiter soll nicht
                                                          // zum loeschen zur
                                                          // Verfuegung stehen
        }

        String responsableWorkers = "";
        for (String actualWorker : actualWPWorkers) {
            responsableWorkers += actualWorker + ", ";
        }
        if (responsableWorkers.length() != 0) {
            responsableWorkers =
                    responsableWorkers.substring(0,
                            responsableWorkers.length() - 2);
        }
        this.txfWorker.setText(responsableWorkers);
    }

    private void fillWorkedTable(String[][] values, boolean leiter) {
        DefaultTableModel model = (DefaultTableModel) tblAufwand.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        if (values[0] != null) {
            for (String[] actualValue : values) {
                if (actualValue[0] != null) {
                    Vector<String> row = new Vector<String>();
                    if (leiter) {
                        row.addElement(actualValue[0]);
                    } else {
                        row.addElement("");
                    }
                    row.addElement(actualValue[1]);
                    row.addElement(actualValue[2]);
                    row.addElement(actualValue[3]);
                    model.addRow(row);
                }

            }
        }

    }

    public void addButtonAction() {
        for (Component actualComponent : getComponents()) {
            if (actualComponent instanceof JTextField) {
                actualComponent.addKeyListener(function
                        .getChangeListenerTextfield());
            } else if (actualComponent instanceof JCheckBox) {
                ((JCheckBox) actualComponent).addItemListener(function
                        .getChangeItemListener());
            }
        }

        btnAddAncestor.addActionListener(function.getBtnAddAncestorListener());
        btnEditAncestor
                .addActionListener(function.getBtnEditAncestorListener());
        btnAddFollower.addActionListener(function.getBtnAddFollowerListener());
        btnEditFollower
                .addActionListener(function.getBtnEditFollowerListener());
        cobLeiter.addItemListener(function.getChangeItemListener());
        btnAddWorker.addActionListener(function
                .getBtnAddWorkerListener(cobAddWorker));
        btnRemoveWorker.addActionListener(function
                .getBtnRemoveWorkerListener(cobRemoveWorker));
        btnAddAufwand.addActionListener(function.getBtnAddAufwandListener());
        btnOk.addActionListener(function.getBtnOKListener());
        btnSave.addActionListener(function.getBtnSaveListener());
        btnCancel.addActionListener(function.getBtnCancelListener());
        txfBAC.addFocusListener(function.getBACETCListener());
        txfETC.addFocusListener(function.getBACETCListener());

        btnSPI.addActionListener(function.getBtnSPIListener());
        btnCPI.addActionListener(function.getBtnCPIListener());

    }

    protected void setETC(double etc) {
        txfETC.setText("" + etc);
    }

    public void setNewView(boolean newWp) {
        txfNr.setEditable(newWp);
    }

    public void updateDependencyCount(Workpackage wp) {
        int ancestors = 0;
        int followers = 0;
        if (WpManager.getAncestors(wp) != null) {
            ancestors = WpManager.getAncestors(wp).size();
        }
        if (WpManager.getFollowers(wp) != null) {
            followers = WpManager.getFollowers(wp).size();
        }
        btnEditAncestor.setText(generalStrings.toManage() + " (" + ancestors
                + ")");
        btnEditFollower.setText(generalStrings.toManage() + " (" + followers
                + ")");
    }
}
