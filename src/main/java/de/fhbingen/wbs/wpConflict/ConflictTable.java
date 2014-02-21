package de.fhbingen.wbs.wpConflict;

import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Workpackage;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpOverview.WPOverviewGUI;
import de.fhbingen.wbs.wpShow.WPShow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * UI class for displaying conflicts.
 * Currently also controller for conflicts.
 */
public class ConflictTable extends JTable {
    /**
     * Serialization UID.
     */
    private static final long serialVersionUID = -6055310228750206338L;

    /**
     * Default table model.
     */
    private DefaultTableModel model;

    /**
     * All conflicts.
     */
    private ArrayList<ConflictCompat> conflicts;

    /**
     * The context menu.
     */
    private JPopupMenu contextMenu;

    /**
     * Number of the last clicked row.
     */
    private int lastClickedRow;

    /**
     * WPOverview instance.
     */
    private WPOverview over;


    /**
     * Translation interface.
     */
    private final General generalStrings;

    /**
     * Constructor.
     *
     * @param wpOverview
     *            WPOverview GUI.
     * @param parent
     *            ParentFrame.
     */
    public ConflictTable(final WPOverview wpOverview, final JFrame parent) {
        generalStrings = LocalizedStrings.getGeneralStrings();
        reload();
        this.over = wpOverview;
        contextMenu = new JPopupMenu();
        JMenuItem miRemove = new JMenuItem(LocalizedStrings.getButton()
                .delete(LocalizedStrings.getGeneralStrings().conflict()));
        miRemove.setIcon(WPOverviewGUI.delAP);
        miRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) { //TODO MVC that.
                removeConflict(lastClickedRow);
            }

        });
        contextMenu.add(miRemove);

        final JTable thisTable = this;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                lastClickedRow = thisTable.rowAtPoint(e.getPoint());
                if (e.getButton() == MouseEvent.BUTTON3) {
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());

                    thisTable.setEditingRow(lastClickedRow);
                } else if (e.getClickCount() == 2) {
                    if (conflicts.get(thisTable.rowAtPoint(e.getPoint()))
                            .getTriggerApStringId() != null) {
                        Workpackage selected =
                                WpManager.getWorkpackage(conflicts.get(
                                        thisTable.rowAtPoint(e.getPoint()))
                                        .getTriggerApStringId());
                        new WPShow(wpOverview, selected, false, parent);
                    }

                }
            }
        });
    }

    /**
     * Deleted conflict from UI and database.
     * TODO fix conflict deletion.
     * @param row
     *          Number of row of the conflict.
     */
    private void removeConflict(final int row) {
            conflicts.remove(row).deleteConflictFromDatabase();
            model.removeRow(row);
            over.reload();
    }

    /**
     * Adds a conflict to the UI and the database.
     *
     * @param conflict
     *            Conflict to be added.
     */
    public final void addConflict(final ConflictCompat conflict) {
        Set<ConflictCompat> conflictHashSet = new HashSet<>();
        conflictHashSet.add(conflict);
        addConflicts(conflictHashSet);
    }

    /**
     * Adds conflicts of a given set to the UI and database.
     *
     * @param newConflicts
     *            Conflicts to be added
     */
    public final void addConflicts(final Set<ConflictCompat> newConflicts) {
        for (int i = 0; i < this.getRowCount(); i++) {
            model.removeRow(i);
        }
        HashSet<ConflictCompat> singleConflicts = new HashSet<>(conflicts);
        for (ConflictCompat actualConflict : newConflicts) {
            if (!singleConflicts.contains(actualConflict)) {
                conflicts.add(actualConflict);
                model.addRow(actualConflict.createStringArray());
                actualConflict.saveConflictToDatabase();
            }
        }
        this.repaint();
    }

    /**
     * Reloads the conflict table.
     */
    public final void reload() {
        model = new DefaultTableModel();

        model.addColumn(generalStrings.pointOfTime());
        model.addColumn(generalStrings.reason());
        model.addColumn(generalStrings.causer());
        model.addColumn(LocalizedStrings.getWbs().affectedWorkpackages());

        conflicts = new ArrayList<>(ConflictCompat.getAllConflictsFromDatabase());

        for (ConflictCompat actualConflict : conflicts) {
            model.addRow(actualConflict.createStringArray());
        }

        this.setModel(model);
    }

    @Override
    public final boolean isCellEditable(final int x, final int y) {
        return false; //Table should not be editable
    }
}
