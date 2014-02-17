package de.fhbingen.wbs.gui.workeffort;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.MaskFormatter;

import de.fhbingen.wbs.globals.FilterJTextField;
import de.fhbingen.wbs.gui.delegates.SimpleDialogDelegate;
import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import de.fhbingen.wbs.wpWorker.Worker;

/**
 * Studienprojekt: WBS<br/> Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: Andre Paffenholz, <br/> Peter Lange, <br/> Daniel
 * Metzler,<br/> Samson von Graevenitz<br/> Studienprojekt: PSYS WBS
 * 2.0<br/> Kunde: Pentasys AG, Jens von Gersdorff<br/> Projektmitglieder:
 * <br/> Michael Anstatt,<br/> Marc-Eric Baumgärtner,<br/> Jens
 * Eckes,<br/> Sven Seckler,<br/> Lin Yang<br/> GUI zum Hinzufügen eines
 * Aufwandes zum Arbeitspaket<br/>
 * @author Daniel Metzler, Michael Anstatt
 * @version 2.0 2012-0821
 */
public class AddWorkEffortView extends JFrame {

    private static final long serialVersionUID = 1L;
    protected GridBagLayout gbl;

    private JLabel lblID, lblName, lblUser, lblBeschreibung, lblAufwand,
        lblDatum;
    protected JTextField txfNr, txfName, txfBeschreibung, txfAufwand;
    protected JComboBox<Worker> cobUser;
    protected JFormattedTextField txfDatum;
    protected JButton btnOk, btnCancel;

    /**
     * Interface do define possible user actions to be handled by the
     * controller.
     */
    public interface ActionsDelegate extends SimpleDialogDelegate {
        /**
         * The user performed the work effort press action. This happens when
         * insert data into the work effort text field.
         */
        void workEffortPressPerformed(KeyEvent e);
        
        /**
         * The user performed the work effort release action. This happens when
         * insert data into the work effort text field.
         */
        void workEffortReleasePerformed(KeyEvent e);
    }

    /**
     * Responsible for handling all user actions.
     */
    private final ActionsDelegate actionsDelegate;

    /**
     * Main-Frame bekommt den Namen "WPAufwand" zugewiesen es wird das
     * Windows Look and Feel verwendet die verschiedenen Menüs, Buttons
     * etc. werden initialisiert und zu dem GridBagLayout hinzugefügt und
     * angeordnet mittels createGbc
     */
    public AddWorkEffortView(final ActionsDelegate delegate) {
        super();

        this.actionsDelegate = delegate;

        initialize();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try {
            UIManager
                .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println(LocalizedStrings.getErrorMessages()
                .couldNotLoadLookAndFeel());
        }
        Wbs wbsStrings = LocalizedStrings.getWbs();
        Button buttonStrings = LocalizedStrings.getButton();

        lblID = new JLabel(wbsStrings.workPackageId());
        txfNr = new FilterJTextField();
        txfNr.setEnabled(false);

        lblName = new JLabel(wbsStrings.workPackageName());
        txfName = new FilterJTextField();
        txfName.setEnabled(false);

        lblUser = new JLabel(LocalizedStrings.getLogin().user());
        cobUser = new JComboBox<Worker>();

        lblBeschreibung = new JLabel(LocalizedStrings.getGeneralStrings()
            .description());
        txfBeschreibung = new FilterJTextField();

        lblAufwand = new JLabel(wbsStrings.workEffort());
        txfAufwand = new FilterJTextField();

        lblDatum = new JLabel(LocalizedStrings.getGeneralStrings().date());
        // Textfeld Datum bekommt das Format dd.mm.yyyy und hat das
        // aktuelle Datum drinnen stehen
        try {
            txfDatum = new JFormattedTextField(new MaskFormatter(
                "##.##.####"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String str = dateFormat.format(new Date());
            txfDatum.setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnOk = new JButton(buttonStrings.ok());
        btnCancel = new JButton(buttonStrings.cancel());

        createGBC(lblID, 0, 0, 1, 1);
        createGBC(txfNr, 1, 0, 1, 1);
        createGBC(lblName, 0, 1, 1, 1);
        createGBC(txfName, 1, 1, 1, 1);
        createGBC(lblUser, 0, 2, 1, 1);
        createGBC(cobUser, 1, 2, 1, 1);
        createGBC(lblBeschreibung, 0, 3, 1, 1);
        createGBC(txfBeschreibung, 1, 3, 1, 1);
        createGBC(lblAufwand, 0, 4, 1, 1);
        createGBC(txfAufwand, 1, 4, 1, 1);
        createGBC(lblDatum, 0, 5, 1, 1);
        createGBC(txfDatum, 1, 5, 1, 1);
        createGBC(btnOk, 0, 6, 1, 1);
        createGBC(btnCancel, 1, 6, 1, 1);

        addActionListeners();

        setVisible(true);
    }

    /**
     * Setup actionListeners to call to the delegate interface.
     */
    private void addActionListeners() {
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                actionsDelegate.cancelPerformed();
            }
        });

        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                actionsDelegate.confirmPerformed();
            }
        });
        
        txfAufwand.addKeyListener(new KeyAdapter() {
            public void  keyPressed(final KeyEvent e) {
                actionsDelegate.workEffortPressPerformed(e);
            }
            public void keyReleased(final KeyEvent e) {
                actionsDelegate.workEffortReleasePerformed(e);
            }

        });
    }

    /**
     * Methode initialize zum erstellen des Layouts Gui ist 300 Pixel breit
     * und 220 Pixel lang Es wird ein GridbagLayout verwendet Gui wird
     * mittig plaziert
     */
    private void initialize() {
        int width = 300;
        int height = 200;
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2) - width / 2,
            (int) (screenSize.getHeight() / 2) - height / 2);

        this.setSize(new Dimension(width, height));
        gbl = new GridBagLayout();
        getContentPane().setLayout(gbl);
    }

    /**
     * void createGBC(args) Methode createGBC zum Hinzufügen der einzelnen
     * Komponenten zum GridBagLayout
     * @param c
     *            Komponente die zum Layout hinzugefügt wird
     * @param x
     *            Anordnung auf der X-Achse (Breite)
     * @param y
     *            Anordnung auf der Y-Achse (Länge)
     * @param width
     *            Breite des Elementes
     * @param height
     *            Höhe des Elementes
     */
    private void createGBC(Component c, int x, int y, int width, int height) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.weightx = width;
        gbc.weighty = height;
        gbc.gridheight = height;
        gbc.insets = new Insets(1, 1, 1, 1);
        gbl.setConstraints(c, gbc);
        add(c);
    }
    
    /** Sets the id of the work package.
     * @param id The id of the work package.*/
    public final void setId(String id){
        txfNr.setText(id);
    }
    
    /** Sets the name of the work package.
     * @param name The name of the work package.*/
    public final void setName(String name){
        txfName.setText(name);
    }

    /** Returns a combo box with all workers of this work package.
     * @return A combo box with all workers. */
    public final JComboBox<Worker> getWorker(){
        return cobUser;
    }
    
    /** Returns the description of the work effort.
     * @return The description of the work effort as String. */
    public final String getDescription(){
        return txfBeschreibung.getText();
    }
    
    /** Sets the work effort.
     * @param id The work effort of the work package.*/
    public final void setWorkEffort(String effort){
        txfAufwand.setText(effort);
    }
    
    /** Returns the work effort.
     * @return The work effort as String. */
    public final String getWorkEffort(){
        return txfAufwand.getText();
    }
    
    /** Returns the date of the work effort.
     * @return The date of the work effort as String. */
    public final String getDate(){
        return txfDatum.getText();
    }
}
