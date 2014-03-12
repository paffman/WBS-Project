package de.fhbingen.wbs.gui.wpBaseline;

import de.fhbingen.wbs.translation.LocalizedStrings;
import javax.swing.table.DefaultTableModel;

/**
* Created by Maxi on 18.02.14.
*/
class BaselineTableModel extends DefaultTableModel {
    /**
     * UID for serializability.
     */
    private static final long serialVersionUID = -7758000405864191565L;
    /**
     * Is editable values for each column.
     */
    private boolean[] columnEditables;

    /** auto generated code from Eclipse. */
    @SuppressWarnings("rawtypes")
    private Class[] columnTypes;

    /**
     * Default constructor.
     */
    public BaselineTableModel() {
        super(new Object[][]{{null, null, null, null, null, null, "",
                null, "", null, null, null, null, null, null}, },
                new String[]{LocalizedStrings.getWbs().workPackage(),
                        LocalizedStrings.getWbs().bac(),
                        LocalizedStrings.getWbs().ac(),
                        LocalizedStrings.getWbs().etc(),
                        LocalizedStrings.getWbs().cpi(),
                        LocalizedStrings.getWbs().bac() + " "
                                + LocalizedStrings.getGeneralStrings().costs(),
                        LocalizedStrings.getWbs().ac() + " "
                                + LocalizedStrings.getGeneralStrings().costs(),
                        LocalizedStrings.getWbs().etc() + " "
                                + LocalizedStrings.getGeneralStrings().costs(),
                        LocalizedStrings.getWbs().eac(),
                        LocalizedStrings.getWbs().ev(),
                        LocalizedStrings.getGeneralStrings().trend(),
                        LocalizedStrings.getWbs().pv(),
                        LocalizedStrings.getWbs().sv(),
                        LocalizedStrings.getWbs().spi(),
                        LocalizedStrings.getGeneralStrings().status()});

        columnTypes = new Class[]{String.class, String.class,
                String.class, String.class, String.class, String.class,
                String.class, String.class, String.class, String.class,
                String.class, String.class, String.class, String.class,
                Object.class};
        columnEditables = new boolean[]{false, false, false, false,
                false, false, false, false, false, false, false, false,
                false, false, false};
    }
    /** auto generated code from Eclipse */
    @SuppressWarnings({"unchecked", "rawtypes" })
    public Class getColumnClass(final int columnIndex) {
        return columnTypes[columnIndex];
    }

    /**
     * Checks if cell is editable.
     * @param row row to check.
     * @param column column to check.
     * @return true if cell is editable.
     */
    public boolean isCellEditable(final int row, final int column) {
        return columnEditables[column];
    }
}
