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

import dbServices.ConflictService;
import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import functions.WpManager;
import globals.Controller;
import globals.Workpackage;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import wpConflict.Conflict;
import wpConflict.ConflictTable;
import wpOverview.tabs.APCalendarPanel;
import wpOverview.tabs.AvailabilityGraphGUI;
import wpOverview.tabs.BaselinePanel;
import wpOverview.tabs.FollowerPanel;
import wpOverview.tabs.TreePanel;
import wpOverview.tabs.WorkerPanel;

/**
 * GUI to select the work packages from the user. The used icons are from:
 * http://sublink.ca/icons/sweetieplus/ as well as:
 * http://p.yusukekamiyamane.com/ Diagona Icons Copyright (C) 2007 Yusuke
 * Kamiyamane. All rights reserved. The icons are licensed under a Creative
 * Commons Attribution 3.0 license.
 * <http://creativecommons.org/licenses/by/3.0/> Sweetieplus: It's under
 * the Creative Commons Licence: This licence allows you to use the icons
 * in any client work, or commercial products such as WordPress themes or
 * applications.
 */
public class WPOverviewGUI extends JFrame {

    /** The icon for a new work package. */
    public static ImageIcon newAp = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/newAP.png")));

    /** The icon for delete a work package. */
    public static ImageIcon delAP = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/delAP.png")));

    /** The icon to update the work packages. */
    public static ImageIcon akt = new ImageIcon(Toolkit.getDefaultToolkit()
        .getImage(
            WPOverviewGUI.class.getResource("/_icons/aktualisieren.png")));

    /** The icon to update the upper work package. */
    public static ImageIcon aktoap = new ImageIcon(
        Toolkit.getDefaultToolkit()
            .getImage(
                WPOverviewGUI.class
                    .getResource("/_icons/OAPaktualisieren.png")));

    /** The icon for the data base calculation. */
    public static ImageIcon dbcalc = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/DBberechnen.png")));

    /** The icon for closing the tool. */
    public static ImageIcon schlies = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/schliessen.png")));

    /** The icon to change the password. */
    public static ImageIcon pw = new ImageIcon(Toolkit.getDefaultToolkit()
        .getImage(WPOverviewGUI.class.getResource("/_icons/pw.png")));

    /** The icon to sign out. */
    public static ImageIcon abmeld = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/abmeld.png")));

    /** The icon to show the help of the tool. */
    public static ImageIcon hilfe = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/hilfe.png")));

    /** The icon to show the info dialog. */
    public static ImageIcon info = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/info.png")));

    /** The icon to show the work effort. */
    public static ImageIcon aufw = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/aufwand.png")));

    /** The icon to re-assign a work package. */
    public static ImageIcon reass = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/reassign.png")));

    public static ImageIcon childrenOut = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/down.png")));

    public static ImageIcon childrenIn = new ImageIcon(Toolkit
        .getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/up.png")));

    private final Wbs wbsStrings;
    private final General generalStrings;
    private final Button buttonStrings;

    /** The icon for the conflict tab. */
    private ImageIcon conflictIcon;

    /** Contains the single tabs. */
    protected JTabbedPane tabs;

    /** The button to close the window. */
    protected JButton btnSchliessen;

    /** The items which are in the menu bar. */
    protected JMenuItem miAktualisieren, miAbmelden, miBeenden, miHilfe,
        miInfo, miAP, miChangePW, miDelAp, miImportInitial, miCalcDuration;

    /** The tool bar. */
    protected ToolBar toolbar;

    /** The caption of the cpi values. */
    private Legende legende;

    /** The panel which contains all work packages. */
    private TreePanel treeAlle;

    /** The panel which contains all employees. */
    private WorkerPanel workers;

    /** The panel which contains the baselines. */
    private BaselinePanel baseline;

    /** The GUI to show the availability from the single employees. */
    private AvailabilityGraphGUI availability;

    /** The table which shows the conflicts. */
    private ConflictTable conflicts;

    /** The panel which contains the time line. */
    private APCalendarPanel timeline;

    /** The panel which contains the followers. */
    private FollowerPanel follower;

    /** The panel which contains all work packages which are done. */
    private TreePanel treeFertig;

    /** The panel which contains all open work packages. */
    private TreePanel treeOffen;

    /** The label which shows the status bar. */
    private static JLabel lblStatusbar;

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = -374634221909256041L;

    /**
     * Constructor: The DBOverviewGUI() main frame get the name
     * "WP-Overview". The windows look and feel is used. The different
     * menus, buttons etc. are initialized. A TabbedPane is used to show
     * the active/inactive work packages in the different tabs. Every tab
     * uses the GridBagLayout.
     * @param over
     *            The functionality of the WPOverview.
     * @param parent
     *            The wished JFrame.
     */
    public WPOverviewGUI(final WPOverview over, final JFrame parent) {
        super();

        wbsStrings = LocalizedStrings.getWbs();
        generalStrings = LocalizedStrings.getGeneralStrings();
        buttonStrings = LocalizedStrings.getButton();

        initialize();

        setJMenuBar(createMenu());

        toolbar = new ToolBar(over, this);
        this.add(toolbar, BorderLayout.NORTH);

        ArrayList<Workpackage> allUserWp = new ArrayList<Workpackage>(
            WpManager.getUserWp(WPOverview.getUser()));
        treeAlle = new TreePanel(new ArrayList<Workpackage>(), over, parent);
        treeOffen = new TreePanel(allUserWp, new ArrayList<Workpackage>(
            WpManager.getUserWpOpen(WPOverview.getUser())), over, parent);
        treeFertig = new TreePanel(allUserWp, new ArrayList<Workpackage>(
            WpManager.getUserWpFinished(WPOverview.getUser())), over,
            parent);

        workers = new WorkerPanel(over);
        baseline = new BaselinePanel(over, parent);
        availability = new AvailabilityGraphGUI(over, parent);
        conflicts = new ConflictTable(over, parent);
        timeline = new APCalendarPanel();
        follower = new FollowerPanel();

        tabs = createTabs(treeAlle, treeOffen, treeFertig, workers,
            baseline, availability, conflicts, timeline);
        this.add(tabs, BorderLayout.CENTER);

        legende = new Legende();
        lblStatusbar = new JLabel(" ");
        this.add(createFooter(legende, lblStatusbar), BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Creates the layout from the GUI. The width is 610 pixel and the
     * height is 500 pixel.
     */
    private void initialize() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // setResizable(false);
        int width = 1024;
        int height = 700;
        // set the minimal window size.
        setSize(1024, 700);
        // set the window central.
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2) - width / 2,
            (int) (screenSize.getHeight() / 2) - height / 2);
        // Attitude by maximize the window.
        // setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows."
                + "WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println(LocalizedStrings.getErrorMessages()
                .couldNotLoadLookAndFeel());
        }
    }

    /**
     * Creates the menu bar. (File, Help)
     * @return The menu bar.
     */
    private JMenuBar createMenu() {
        JMenu mnDatei, mnHilfe;
        JMenuBar mnbMenu;
        mnbMenu = new JMenuBar();
        mnDatei = new JMenu(buttonStrings.file());
        mnHilfe = new JMenu(buttonStrings.help());
        miAktualisieren = new JMenuItem(buttonStrings.refresh());
        miAktualisieren.setIcon(akt);
        miCalcDuration = new JMenuItem(
            buttonStrings.calculate(generalStrings.duration()));
        miCalcDuration.setIcon(aktoap);
        miChangePW = new JMenuItem(buttonStrings.change(LocalizedStrings
            .getLogin().password()));
        miChangePW.setIcon(pw);
        miAbmelden = new JMenuItem(buttonStrings.logout());
        miAbmelden.setIcon(abmeld);
        miBeenden = new JMenuItem(buttonStrings.close());
        miBeenden.setIcon(schlies);
        miHilfe = new JMenuItem(buttonStrings.help());
        miHilfe.setIcon(hilfe);
        miInfo = new JMenuItem(buttonStrings.info());
        miInfo.setIcon(info);

        // menu entries for the project leader.
        if (WPOverview.getUser().getProjLeiter()) {
            miAP = new JMenuItem(buttonStrings.enter(wbsStrings
                .workPackage()));
            miAP.setIcon(newAp);
            miDelAp = new JMenuItem(buttonStrings.delete(wbsStrings
                .workPackage()));
            miDelAp.setIcon(newAp);
            miImportInitial = new JMenuItem(
                buttonStrings.calculate(LocalizedStrings.getDatabase()
                    .importedDatabase()));
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
     * Creates the TabbedPane with the single tabs.
     * @param treeAlle
     *            Tree with all work packages.
     * @param treeOffen
     *            Tree with opened work packages.
     * @param treeFertig
     *            Tree with closed work packages.
     * @param workers
     *            The employee panel.
     * @param baseline
     *            BaselinePanel
     * @param availability
     *            AvailabilityGraphGUI
     * @param conflicts
     *            ConflictTable
     * @param timeline
     *            APCalendarPanel
     * @return The TabbedPane with all tabs.
     */
    private JTabbedPane createTabs(final TreePanel treeAlle,
        final TreePanel treeOffen, final TreePanel treeFertig,
        final WorkerPanel workers, final BaselinePanel baseline,
        final AvailabilityGraphGUI availability,
        final ConflictTable conflicts, final APCalendarPanel timeline) {

        final JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(new EmptyBorder(3, 3, 3, 3));
        tabs.addTab(wbsStrings.workPackages(generalStrings.all()),
            createScrollPane(treeAlle));
        tabs.addTab(wbsStrings.workPackages(generalStrings.open()),
            createScrollPane(treeOffen));
        tabs.addTab(wbsStrings.workPackages(generalStrings.finished()),
            createScrollPane(treeFertig));
        if (WPOverview.getUser().getProjLeiter()) {
            tabs.addTab(LocalizedStrings.getLogin().user(),
                createScrollPane(workers));
            tabs.addTab(wbsStrings.baseline(), baseline);
        }
        tabs.addTab(generalStrings.availabilities(),
            createScrollPane(availability));
        tabs.addTab(wbsStrings.timeLine(), createScrollPane(timeline));
        if (WPOverview.getUser().getProjLeiter()) {
            tabs.addTab(generalStrings.dependencies(),
                createScrollPane(follower));
        }
        if (ConflictService.getAllConflicts().size() == 0) {
            conflictIcon = new ImageIcon(
                (Toolkit.getDefaultToolkit().getImage(WPOverviewGUI.class
                    .getResource("/_icons/noWarning.png"))));
        } else {
            conflictIcon = new ImageIcon(
                (Toolkit.getDefaultToolkit().getImage(WPOverviewGUI.class
                    .getResource("/_icons/warning.png"))));
        }
        if (WPOverview.getUser().getProjLeiter()) {

            final JPanel spConflicts = createScrollPane(conflicts);
            final String conflictText = generalStrings.conflicts();
            final WPOverviewGUI wpOverviewGUI = this;
            tabs.addTab(conflictText, conflictIcon, spConflicts);
            tabs.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        if (tabs.getTitleAt(tabs.getSelectedIndex())
                            .equals(conflictText)) {
                            JFrame conflictFrame = new JFrame();
                            conflictFrame.add(spConflicts);
                            conflictFrame.setSize(800, 600);
                            Controller.centerComponent(wpOverviewGUI,
                                conflictFrame);
                            conflictFrame.setVisible(true);
                            conflictFrame
                                .addWindowListener(new WindowAdapter() {
                                    @Override
                                    public void windowClosing(
                                        final WindowEvent e) {
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
     * Creates the footer of the window.
     * @param legende
     *            The caption which explains the cpi values.
     * @param lblStatusbar
     *            The label of the status bar.
     * @return The footer.
     */
    private JComponent createFooter(final Legende legende,
        final JLabel lblStatusbar) {
        JPanel south = new JPanel();
        btnSchliessen = new JButton(buttonStrings.close());
        south.setLayout(new GridLayout(3, 1));
        south.add(legende);
        south.add(btnSchliessen);
        lblStatusbar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        south.add(lblStatusbar);
        return south;
    }

    /**
     * Creates a panel with a scroll pane.
     * @param component
     *            The wished component.
     * @return A panel with a scroll pane inside.
     */
    private JPanel createScrollPane(final Component component) {
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
     * Deactivate/activate the caption.
     * @param show
     *            True: activate the caption. False: deactivate the caption
     */
    public final void showLegende(final boolean show) {
        legende.setVisible(show);
    }

    /**
     * Change the text in the status bar.
     * @param str
     *            The String which should be shown in the status bar.
     */
    public static void setStatusText(final String str) {
        lblStatusbar.setText(str);
    }

    /**
     * Reload the tree.
     */
    protected final void reloadTrees() {
        ArrayList<Workpackage> allUserWp = new ArrayList<Workpackage>(
            WpManager.getUserWp(WPOverview.getUser()));
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
     * Reload the employees.
     */
    protected final void reloadWorkers() {
        workers.reload();
    }

    /**
     * Reload the time line.
     */
    public final void reloadTimeline() {
        timeline.reload();
    }

    /**
     * Insert a conflict to the conflicts table and set the icon of the
     * conflict tab to "Warning".
     * @param conflict
     *            Conflict which should be thrown.
     */
    protected final void throwConflict(final Conflict conflict) {
        conflictIcon.setImage(Toolkit.getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/warning.png")));
        conflicts.addConflict(conflict);
    }

    /**
     * Set the icon of the conflict tab to "No Warning".
     */
    protected final void releaseConflicts() {
        conflictIcon.setImage(Toolkit.getDefaultToolkit().getImage(
            WPOverviewGUI.class.getResource("/_icons/noWarning.png")));
    }

    /**
     * Reload the availabilities.
     */
    public final void reloadAvailabilities() {
        availability.reload();
    }

    /**
     * Reload the conflicts.
     */
    public final void reloadConflicts() {
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
     * Reload the baseline.
     */
    public final void reloadBaseline() {
        baseline.reload();
    }
}
