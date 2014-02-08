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

package wpBaseline;

import functions.WpManager;
import globals.Controller;
import globals.Workpackage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import dbaccess.DBModelManager;
import dbaccess.data.AnalyseData;
import de.fhbingen.wbs.translation.LocalizedStrings;

/**
 * Functionality of the BaselineViewGUI.
 */
public class BaselineView {
    /**
     * Constructor.
     * @param baselineID
     *            id from the wished baseline
     * @param parent
     *            ParentFrame
     */
    public BaselineView(final int baselineID, final JFrame parent) {
        try {
            String[][] data = getData(baselineID).toArray(new String[1][1]);
            BaselineViewGUI gui = new BaselineViewGUI(parent);
            for (String[] actualRow : data) {
                gui.addRow(actualRow);
            }

        } catch (SQLException e) {
            Controller.showError(LocalizedStrings.getErrorMessages()
                .baselineLoadingError());
            e.printStackTrace();
        }
    }

    /**
     * Insert the data from the database into the BaselineViewGUI.
     * @param baselineID
     *            id from the wished baseline
     * @return A list with the data in form of a String-array
     * @throws SQLException
     *             Throws an SQLException
     */
    private List<String[]> getData(final int baselineID)
        throws SQLException {
        List<AnalyseData> data = DBModelManager.getAnalyseDataModel()
            .getAnalyseDataForBaseline(baselineID);
        List<String[]> allData = new ArrayList<String[]>();
        for (AnalyseData ad : data) {

            Workpackage actualWp = WpManager.getWorkpackage(ad.getFid_wp());
            if (actualWp.getlastRelevantIndex() <= 3) {
                String[] actualData = new String[15];
                int i = 0;
                String spacer = "";
                for (int j = 0; j < actualWp.getlastRelevantIndex(); j++) {
                    spacer += " ";
                }
                actualData[i++] = spacer + actualWp.toString();
                actualData[i++] = Controller.DECFORM.format(ad.getBac());
                actualData[i++] = Controller.DECFORM.format(ad.getAc());
                actualData[i++] = Controller.DECFORM.format(ad.getEtc());
                actualData[i++] = Controller.DECFORM.format(ad.getCpi());
                actualData[i++] = Controller.DECFORM.format(ad
                    .getBac_costs()) + " EUR";
                actualData[i++] = Controller.DECFORM.format(ad
                    .getAc_costs()) + " EUR";
                actualData[i++] = Controller.DECFORM.format(ad
                    .getEtc_costs()) + " EUR";
                actualData[i++] = Controller.DECFORM.format(ad.getEac())
                    + " EUR";
                actualData[i++] = Controller.DECFORM.format(ad.getEv())
                    + " EUR";
                actualData[i++] = Controller.DECFORM.format(WpManager
                    .calcTrend(ad.getEv(), ad.getAc_costs()));
                actualData[i++] = Controller.DECFORM.format(ad.getPv())
                    + " EUR";
                actualData[i++] = Controller.DECFORM.format(ad.getSv())
                    + " EUR";
                actualData[i++] = Controller.DECFORM.format(ad.getSpi());
                actualData[i++] = ""
                    + WpManager.calcPercentComplete(ad.getBac(),
                        ad.getEtc(), ad.getAc());
                allData.add(actualData);
            }
        }
        return allData;
    }
}
