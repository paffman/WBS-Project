package wpOverview;

import functions.WpManager;
import globals.Controller;
import globals.Workpackage;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import dbServices.ConflictService;

import wpConflict.Conflict;
import wpConflict.ConflictTable;
import wpOverview.tabs.APCalendarPanel;
import wpOverview.tabs.AvailabilityGraphGUI;
import wpOverview.tabs.BaselinePanel;
import wpOverview.tabs.FollowerPanel;
import wpOverview.tabs.TreePanel;
import wpOverview.tabs.WorkerPanel;

/**
 * Studienprojekt: WBS Kunde: Pentasys AG, Jens von Gersdorff Projektmitglieder:
 * Andre Paffenholz, Peter Lange, Daniel Metzler, Samson von Graevenitz
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * 
 * @author Samson von Graevenitz, Daniel Metzler, Andre Paffenholz, Michael
 *         Anstatt, Marc-Eric Baumgärtner
 * @version 2.0 - <aktuelles Datum> GUI zum auswählen der Arbeitspakete des
 *          User <br />
 *          Die verwendeten Icons stammen von:
 *          http://sublink.ca/icons/sweetieplus/ sowie:
 *          http://p.yusukekamiyamane.com/ Diagona Icons Copyright (C) 2007
 *          Yusuke Kamiyamane. All rights reserved. The icons are licensed under
 *          a Creative Commons Attribution 3.0 license.
 *          <http://creativecommons.org/licenses/by/3.0/> Sweetieplus: Sie
 *          unterliegen der Creative Commons Licence: This licence allows you to
 *          use the icons in any client work, or commercial products such as
 *          WordPress themes or applications.
 */
public class WPOverviewGUI extends JFrame {

    public static ImageIcon newAp = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(WPOverviewGUI.class.getResource("/_icons/newAP.png")));
    public static ImageIcon delAP = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(WPOverviewGUI.class.getResource("/_icons/delAP.png")));
    public static ImageIcon akt = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(
                    WPOverviewGUI.class
                            .getResource("/_icons/aktualisieren.png")));
    public static ImageIcon aktoap = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(
                    WPOverviewGUI.class
                            .getResource("/_icons/OAPaktualisieren.png")));
    public static ImageIcon dbcalc =
            new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                    WPOverviewGUI.class.getResource("/_icons/DBberechnen.png")));
    public static ImageIcon schlies = new ImageIcon(
            Toolkit.getDefaultToolkit().getImage(
                    WPOverviewGUI.class.getResource("/_icons/schliessen.png")));
    public static ImageIcon pw = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(WPOverviewGUI.class.getResource("/_icons/pw.png")));
    public static ImageIcon abmeld = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(WPOverviewGUI.class.getResource("/_icons/abmeld.png")));
    public static ImageIcon hilfe = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(WPOverviewGUI.class.getResource("/_icons/hilfe.png")));
    public static ImageIcon info = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(WPOverviewGUI.class.getResource("/_icons/info.png")));
    public static ImageIcon aufw = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(WPOverviewGUI.class.getResource("/_icons/aufwand.png")));
    public static ImageIcon reass = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(WPOverviewGUI.class.getResource("/_icons/reassign.png")));
    public static ImageIcon childrenOut = new ImageIcon(Toolkit
            .getDefaultToolkit().getImage(
                    WPOverviewGUI.class.getResource("/_icons/down.png")));
    public static ImageIcon childrenIn = new ImageIcon(Toolkit
            .getDefaultToolkit().getImage(
                    WPOverviewGUI.class.getResource("/_icons/up.png")));
    private ImageIcon conflictIcon;

    protected JTabbedPane tabs;
    protected JButton btnSchliessen;
    protected JMenuItem miAktualisieren, miAbmelden, miBeenden, miHilfe,
            miInfo, miAP, miChangePW, miDelAp, miImportInitial, miCalcDuration;
    protected ToolBar toolbar;

    private Legende legende;
    private TreePanel treeAlle;
    private WorkerPanel workers;
    private BaselinePanel baseline;
    private AvailabilityGraphGUI availability;
    private ConflictTable conflicts;
    private APCalendarPanel timeline;
    private FollowerPanel follower;
    private TreePanel treeFertig;
    private TreePanel treeOffen;

    private static JLabel lblStatusbar;

    private static final long serialVersionUID = -374634221909256041L;

    /**
     * Konstuktor DBOverviewGUI() Main-Frame bekommt den Namen "WP-Overview"
     * zugewiesen es wird das Windows Look and Feel verwendet die verschiedenen
     * Menüs, Buttons etc. werden initialisiert es wird ein TabbedPane
     * verwendet um die aktiven und inaktiven Arbeitspaket in verschieden Tabs
     * anzuzeigen und jedes zu TabbedPane wird das GridBagLayout hinzugefügt
     * 
     * @param over
     */
    public WPOverviewGUI(WPOverview over, JFrame parent) {
        super("WP-Overview");

        initialize();

        setJMenuBar(createMenu());

        toolbar = new ToolBar(over, this);
        this.add(toolbar, BorderLayout.NORTH);

        ArrayList<Workpackage> allUserWp =
                new ArrayList<Workpackage>(WpManager.getUserWp(WPOverview
                        .getUser()));
        treeAlle = new TreePanel(new ArrayList<Workpackage>(), over, parent);
        treeOffen =
                new TreePanel(allUserWp, new ArrayList<Workpackage>(
                        WpManager.getUserWpOpen(WPOverview.getUser())), over,
                        parent);
        treeFertig =
                new TreePanel(allUserWp, new ArrayList<Workpackage>(
                        WpManager.getUserWpFinished(WPOverview.getUser())),
                        over, parent);

        workers = new WorkerPanel(over);
        baseline = new BaselinePanel(over, parent);
        availability = new AvailabilityGraphGUI(over, parent);
        conflicts = new ConflictTable(over, parent);
        timeline = new APCalendarPanel();
        follower = new FollowerPanel();

        tabs =
                createTabs(treeAlle, treeOffen, treeFertig, workers, baseline,
                        availability, conflicts, timeline);
        this.add(tabs, BorderLayout.CENTER);

        legende = new Legende();
        lblStatusbar = new JLabel(" ");
        this.add(createFooter(legende, lblStatusbar), BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * void initialize() Methode initialize zum erstellen des Layouts Gui ist
     * 610 Pixel breit und 500 Pixel lang
     */
    private void initialize() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // setResizable(false);
        int width = 1024;
        int height = 700;
        // minimale Fenstergröße eintragen
        setSize(1024, 700);
        // Fenster mittig platzieren
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2) - width / 2,
                (int) (screenSize.getHeight() / 2) - height / 2);
        // Verhalten beim Maximieren festlegen
        // setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            UIManager
                    .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Could not load LookAndFeel");
        }
    }

    /**
     * Erstellt die MenuBar (Datei, Hilfe)
     * 
     * @return Menubar
     */
    private JMenuBar createMenu() {
        JMenu mnDatei, mnHilfe;
        JMenuBar mnbMenu;
        mnbMenu = new JMenuBar();
        mnDatei = new JMenu("Datei");
        mnHilfe = new JMenu("Hilfe");
        miAktualisieren = new JMenuItem("Aktualisieren");
        miAktualisieren.setIcon(akt);
        miCalcDuration = new JMenuItem("Dauer berechnen");
        miCalcDuration.setIcon(aktoap);
        miChangePW = new JMenuItem("Passwort ändern");
        miChangePW.setIcon(pw);
        miAbmelden = new JMenuItem("Abmelden");
        miAbmelden.setIcon(abmeld);
        miBeenden = new JMenuItem("Schließen");
        miBeenden.setIcon(schlies);
        miHilfe = new JMenuItem("Hilfe");
        miHilfe.setIcon(hilfe);
        miInfo = new JMenuItem("Info");
        miInfo.setIcon(info);

        // Menüeinträge für Projektleiter
        if (WPOverview.getUser().getProjLeiter()) {
            miAP = new JMenuItem("neues Arbeitspaket");
            miAP.setIcon(newAp);
            miDelAp = new JMenuItem("Arbeitspaket löschen");
            miDelAp.setIcon(newAp);
            miImportInitial = new JMenuItem("Importierte DB berechnen");
            miImportInitial.setIcon(dbcalc);
        }

        mnbMenu.add(mnDatei);
        mnbMenu.add(mnHilfe);

        if (WPOverview.getUser().getProjLeiter()) {
            mnDatei.add(miImportInitial);
        }

        // mnDatei.add(miAktualisieren);
        mnDatei.add(miCalcDuration);
        mnDatei.add(miChangePW);
        mnDatei.add(miAbmelden);
        mnDatei.addSeparator();
        mnDatei.add(miBeenden);
        mnHilfe.add(miHilfe);
        mnHilfe.addSeparator();
        mnHilfe.add(miInfo);
        return mnbMenu;
    }

    /**
     * Erstellt das Menu mit den Tabs und liefert es zurueck
     * 
     * @param treeAlle
     *            Tree mit allen APs
     * @param treeOffen
     *            Tree mit offenen APs
     * @param treeFertig
     *            Tree mit fertigen APs
     * @param workers
     *            Workerpanel
     * @param baseline
     *            BaselinePanel
     * @param availability
     *            AvailabilityGraphGUI
     * @param conflicts
     *            ConflictTable
     * @param timeline
     *            APCalendarPanel
     * @return JTabbedPane Komplettes TabMenu
     */
    private JTabbedPane createTabs(TreePanel treeAlle, TreePanel treeOffen,
            TreePanel treeFertig, WorkerPanel workers, BaselinePanel baseline,
            AvailabilityGraphGUI availability, final ConflictTable conflicts,
            APCalendarPanel timeline) {

        final JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(new EmptyBorder(3, 3, 3, 3));
        tabs.addTab("alle Arbeitspakete", createScrollPane(treeAlle));
        tabs.addTab("offene Arbeitspakete", createScrollPane(treeOffen));
        tabs.addTab("fertige Arbeitspakete", createScrollPane(treeFertig));
        if (WPOverview.getUser().getProjLeiter()) {
            tabs.addTab("Mitarbeiter", createScrollPane(workers));
            tabs.addTab("Baseline", baseline);
        }
        tabs.addTab("Verfügbarkeiten", createScrollPane(availability));
        tabs.addTab("Timeline", createScrollPane(timeline));
        if (WPOverview.getUser().getProjLeiter()) {
            tabs.addTab("Abhängigkeiten", createScrollPane(follower));
        }
        if (ConflictService.getAllConflicts().size() == 0) {
            conflictIcon =
                    new ImageIcon(
                            (Toolkit.getDefaultToolkit().getImage(WPOverviewGUI.class
                                    .getResource("/_icons/noWarning.png"))));
        } else {
            conflictIcon =
                    new ImageIcon(
                            (Toolkit.getDefaultToolkit().getImage(WPOverviewGUI.class
                                    .getResource("/_icons/warning.png"))));
        }
        if (WPOverview.getUser().getProjLeiter()) {

            final JPanel spConflicts = createScrollPane(conflicts);
            final String conflictText = "Konflikte";
            final WPOverviewGUI wpOverviewGUI = this;
            tabs.addTab(conflictText, conflictIcon, spConflicts);
            tabs.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        if (tabs.getTitleAt(tabs.getSelectedIndex()).equals(
                                conflictText)) {
                            JFrame conflictFrame = new JFrame();
                            conflictFrame.add(spConflicts);
                            conflictFrame.setSize(800, 600);
                            Controller.centerComponent(wpOverviewGUI,
                                    conflictFrame);
                            conflictFrame.setVisible(true);
                            conflictFrame
                                    .addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void
                                                windowClosing(WindowEvent e) {
                                            tabs.addTab(conflictText,
                                                    conflictIcon, spConflicts);
                                        }
                                    });
                        }
                    }
                }
            });
        }
        return tabs;
    }

    /**
     * Erzeugt die Fußzeile des Fensters
     * 
     * @param legende
     *            Legende die die Farben erklaert
     * @param lblStatusbar
     *            JLabel
     * @return
     */
    private JComponent createFooter(Legende legende, JLabel lblStatusbar) {
        JPanel south = new JPanel();
        btnSchliessen = new JButton("Schließen");
        south.setLayout(new GridLayout(3, 1));
        south.add(legende);
        south.add(btnSchliessen);
        lblStatusbar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        south.add(lblStatusbar);
        return south;
    }

    /**
     * Erzeugt ein Panel mit Scrollbalken
     * 
     * @param component
     *            Componente die scrollbar sein soll
     * @return Liefert ein Panel mit einen Scrollbalken zurueck
     */
    private JPanel createScrollPane(Component component) {
        // JPanel panel = new JPanel();
        //
        // GridBagLayout layout = new GridBagLayout();
        // GridBagConstraints constraints = new GridBagConstraints();
        // constraints.gridx = 0;
        // constraints.gridy = 0;
        // constraints.weightx = 1;
        // constraints.anchor = GridBagConstraints.PAGE_START;
        // constraints.fill = GridBagConstraints.BOTH;
        // panel.setLayout(layout);
        // panel.add(component, constraints);
        //
        // return new JScrollPane(panel);

        JPanel panel = new JPanel(false);
        panel.setLayout(new BorderLayout(2, 2));
        panel.add(new JScrollPane(component), BorderLayout.CENTER);
        // panel.add(btnAddMitarbeiter,BorderLayout.SOUTH);
        return panel;

    }

    /**
     * Deaktiviert/Aktiviert die Legende
     * 
     * @param show
     *            Boolean ob an oder abgeschaltet werden soll
     */
    public void showLegende(boolean show) {
        legende.setVisible(show);
    }

    /**
     * ändert den Text in der Statusbar
     * 
     * @param str
     *            String zur Anzeige in der Statusbar
     */
    public static void setStatusText(String str) {
        lblStatusbar.setText(str);
    }

    /**
     * Laedt den Baum neu
     */
    protected void reloadTrees() {
        System.out.println(WPOverview.getUser().getLogin()); // %%
        System.out.println(WpManager.getUserWp(WPOverview.getUser())); // %%
        ArrayList<Workpackage> allUserWp =
                new ArrayList<Workpackage>(WpManager.getUserWp(WPOverview
                        .getUser()));
        treeAlle.setNodes(allUserWp, allUserWp);
        treeAlle.reload();
        treeOffen.setNodes(
                allUserWp,
                new ArrayList<Workpackage>(WpManager.getUserWpOpen(WPOverview
                        .getUser())));
        treeOffen.reload();
        treeFertig.setNodes(
                allUserWp,
                new ArrayList<Workpackage>(WpManager
                        .getUserWpFinished(WPOverview.getUser())));
        treeFertig.reload();
        follower.reload();

    }

    /**
     * Laedt die Arbeiter neu
     */
    protected void reloadWorkers() {
        workers.reload();
    }

    /**
     * Laedt die Timeline neu
     */
    public void reloadTimeline() {
        timeline.reload();
    }

    /**
     * Fuegt einen Conflict zur Conflicttable und setzt das Icon des
     * Konflikt-Tabs auf Warning
     * 
     * @param conflict
     *            Konflikt der geworfen werden soll
     */
    protected void throwConflict(Conflict conflict) {
        conflictIcon.setImage(Toolkit.getDefaultToolkit().getImage(
                WPOverviewGUI.class.getResource("/_icons/warning.png")));
        conflicts.addConflict(conflict);
    }

    /**
     * Setzt das Icon des Konflikt-Tabs auf noWarning
     */
    protected void releaseConflicts() {
        conflictIcon.setImage(Toolkit.getDefaultToolkit().getImage(
                WPOverviewGUI.class.getResource("/_icons/noWarning.png")));
    }

    /**
     * Laedt die Verfuegbarkeiten neu
     */
    public void reloadAvailabilities() {
        availability.reload();
    }

    /**
     * Laedt die Konflikte neu
     */
    public void reloadConflicts() {
        if (ConflictService.getAllConflicts().size() == 0) {
            conflictIcon.setImage(Toolkit.getDefaultToolkit().getImage(
                    WPOverviewGUI.class.getResource("/_icons/noWarning.png")));
        } else {
            conflictIcon.setImage((Toolkit.getDefaultToolkit()
                    .getImage(WPOverviewGUI.class
                            .getResource("/_icons/warning.png"))));
        }
        conflicts.reload();
    }

    /**
     * Laedt die Baseline neu
     */
    public void reloadBaseline() {
        baseline.reload();
    }
}
