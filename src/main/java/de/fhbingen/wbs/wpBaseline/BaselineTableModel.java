package de.fhbingen.wbs.wpBaseline;

import de.fhbingen.wbs.translation.LocalizedStrings;
import javax.swing.table.DefaultTableModel;

/**
* Created by Maxi on 18.02.14.
*/
class BaselineTableModel extends DefaultTableModel {

    private static final long serialVersionUID = -7758000405864191565L;
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

    @SuppressWarnings("rawtypes")
    // auto generated code from Eclipse
    private Class[] columnTypes;

    @SuppressWarnings({"unchecked", "rawtypes" })
    // auto generated code from Eclipse
    public Class getColumnClass(final int columnIndex) {
        return columnTypes[columnIndex];
    }

    private boolean[] columnEditables;

    public boolean isCellEditable(final int row, final int column) {
        return columnEditables[column];
    }
}
