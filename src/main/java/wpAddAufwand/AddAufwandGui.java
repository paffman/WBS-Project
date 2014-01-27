package wpAddAufwand;

import globals.FilterJTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
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
import wpWorker.Worker;

/**
 * GUI zum Hinzufügen eines Aufwandes zum Arbeitspaket.
 */
public class AddAufwandGui extends JFrame {
    /**
     * Text field with work package id.
     */
    private JTextField txfNr;
    /**
     * Text field with work package name.
     */
    private JTextField txfName;
    /**
     * Text field with work effort description.
     */
    private JTextField txfBeschreibung;
    /**
     * Text field with work effort value.
     */
    private JTextField txfAufwand;
    /**
     * ComboBox for selecting the user that did the work effort.
     */
    private JComboBox<Worker> cobUser;
    /**
     * Text field for date entry.
     */
    private JFormattedTextField txfDatum;
    /**
     * OK button.
     */
    private JButton btnEdit;
    /**
     * Close button.
     */
    private JButton btnSchliessen;
    /**
     * Default width of dialog.
     */
    private static final int DEFAULT_WIDTH = 300;
    /**
     * Default height of dialog.
     */
    private static final int DEFAULT_HEIGHT = 200;


    /**
     * Main-Frame bekommt den Namen "WPAufwand" zugewiesen es wird das Windows
     * Look and Feel verwendet die verschiedenen Menüs, Buttons etc. werden
     * initialisiert und zu dem GridBagLayout hinzugefügt und angeordnet mittels
     * createGbc
     */
    public AddAufwandGui() {
        super("WPAufwand");
        initialize();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try { //TODO nicht schon woanders?
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows"
                    + ".WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Could not load LookAndFeel");
        }

        JLabel lblID = new JLabel("ArbeitspaketID");
        txfNr = new FilterJTextField();
        getTxfNr().setEnabled(false);

        JLabel lblName = new JLabel("Arbeitspaket Name");
        txfName = new FilterJTextField();
        getTxfName().setEnabled(false);

        JLabel lblUser = new JLabel("Benutzer");
        cobUser = new JComboBox<Worker>();

        JLabel lblBeschreibung = new JLabel("Beschreibung");
        txfBeschreibung = new FilterJTextField();

        JLabel lblAufwand = new JLabel("Aufwand");
        txfAufwand = new FilterJTextField();

        JLabel lblDatum = new JLabel("Datum");
        //Textfeld Datum bekommt das Format dd.mm.yyyy und hat das aktuelle
        // Datum drinnen stehen
        try {
            txfDatum = new JFormattedTextField(new MaskFormatter("##.##.####"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String str = dateFormat.format(new Date());
            getTxfDatum().setText(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnEdit = new JButton("Editieren");
        btnSchliessen = new JButton("Schließen");

        createGBC(lblID, 0, 0, 1, 1);
        createGBC(getTxfNr(), 1, 0, 1, 1);
        createGBC(lblName, 0, 1, 1, 1);
        createGBC(getTxfName(), 1, 1, 1, 1);
        createGBC(lblUser, 0, 2, 1, 1);
        createGBC(getCobUser(), 1, 2, 1, 1);
        createGBC(lblBeschreibung, 0, 3, 1, 1);
        createGBC(getTxfBeschreibung(), 1, 3, 1, 1);
        createGBC(lblAufwand, 0, 4, 1, 1);
        createGBC(getTxfAufwand(), 1, 4, 1, 1);
        createGBC(lblDatum, 0, 5, 1, 1);
        createGBC(getTxfDatum(), 1, 5, 1, 1);
        createGBC(getBtnEdit(), 0, 6, 1, 1);
        createGBC(getBtnSchliessen(), 1, 6, 1, 1);

        setVisible(true);

    }

    /**
     * Methode initialize zum erstellen des Layouts Gui ist 300 Pixel breit und
     * 220 Pixel lang Es wird ein GridbagLayout verwendet Gui wird mittig
     * platziert.
     */
    private void initialize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2) - DEFAULT_WIDTH / 2,
                (int) (screenSize.getHeight() / 2) - DEFAULT_HEIGHT / 2);

        this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        GridBagLayout gbl = new GridBagLayout();
        getContentPane().setLayout(gbl);
    }

    /**
     * void createGBC(args) Methode createGBC zum Hinzufügen der einzelnen
     * Komponenten zum GridBagLayout.
     *
     * @param c      Komponente die zum Layout hinzugefügt wird
     * @param x      Anordnung auf der X-Achse (Breite)
     * @param y      Anordnung auf der Y-Achse (Länge)
     * @param width  Breite des Elementes
     * @param height Höhe des Elementes
     */
    //TODO extract to utility class.
    private void createGBC(final Component c, final int x, final int y,
                           final int width, final int height) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.weightx = width;
        gbc.weighty = height;
        gbc.gridheight = height;
        gbc.insets = new Insets(1, 1, 1, 1);
        add(c, gbc);

    }

    /**
     * Gets text field for work package id.
     * @return text field for work package id.
     */
    public final JTextField getTxfNr() {
        return txfNr;
    }

    /**
     * Gets text field for work package name.
     * @return text field for work package name.
     */
    public final JTextField getTxfName() {
        return txfName;
    }

    /**
     * Gets text field for work effort description.
     * @return text field for work effort description.
     */
    public final JTextField getTxfBeschreibung() {
        return txfBeschreibung;
    }

    /**
     * Gets text field for work effort.
     * @return work effort text field.
     */
    public final JTextField getTxfAufwand() {
        return txfAufwand;
    }

    /**
     * Gets combo box for user selection.
     * @return combo box for user selection.
     */
    public final JComboBox<Worker> getCobUser() {
        return cobUser;
    }

    /**
     * Gets text field for date.
     * @return text field for date.
     */
    public final JFormattedTextField getTxfDatum() {
        return txfDatum;
    }

    /**
     * Gets ok button.
     * @return ok button.
     */
    public final JButton getBtnEdit() {
        return btnEdit;
    }

    /**
     * Gets close button.
     * @return close button.
     */
    public final JButton getBtnSchliessen() {
        return btnSchliessen;
    }
}
