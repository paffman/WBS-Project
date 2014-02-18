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

package de.fhbingen.wbs.wpBaseline;

import de.fhbingen.wbs.translation.LocalizedStrings;

import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Controller;
import de.fhbingen.wbs.globals.Workpackage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.AnalyseData;

/**
 * Functionality of the BaselineView.
 */
public class BaselineViewController {
    /**
     * Constructor.
     *
     * @param baselineID
     *            id from the wished baseline
     * @param parent
     *            ParentFrame
     */
    public BaselineViewController(final int baselineID, final JFrame parent) {
        try {
            String[][] data = getData(baselineID).toArray(new String[1][1]);
            BaselineView gui = new BaselineView(parent);
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
     * Insert the data from the database into the BaselineView.
     *
     * @param baselineID
     *            id from the wished baseline
     * @return A list with the data in form of a String-array
     * @throws SQLException
     *             Throws an SQLException
     */
    private List<String[]> getData(final int baselineID) throws SQLException {
        List<AnalyseData> data =
                DBModelManager.getAnalyseDataModel().getAnalyseDataForBaseline(
                        baselineID);
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
                actualData[i++] =
                        Controller.DECFORM.format(ad.getBac_costs())
                                + " EUR";
                actualData[i++] =
                        Controller.DECFORM.format(ad.getAc_costs())
                                + " EUR";
                actualData[i++] =
                        Controller.DECFORM.format(ad.getEtc_costs())
                                + " EUR";
                actualData[i++] =
                        Controller.DECFORM.format(ad.getEac()) + " EUR";
                actualData[i++] =
                        Controller.DECFORM.format(ad.getEv()) + " EUR";
                actualData[i++] =
                        Controller.DECFORM.format(WpManager.calcTrend(
                                ad.getEv(), ad.getAc_costs()));
                actualData[i++] =
                        Controller.DECFORM.format(ad.getPv()) + " EUR";
                actualData[i++] =
                        Controller.DECFORM.format(ad.getSv()) + " EUR";
                actualData[i++] = Controller.DECFORM.format(ad.getSpi());
                actualData[i++] =
                        ""
                                + WpManager.calcPercentComplete(ad.getBac(),
                                        ad.getEtc(), ad.getAc());
                allData.add(actualData);
            }
        }
        return sortDataByStringId(allData);
    }

    /**
     * Sorts the given list by the StringID.
     * @param unsortedData an List to sort by the StringId.
     * @return the given list, but sorted.
     */
    private List<String[]>
            sortDataByStringId(final List<String[]> unsortedData) {

        Collections.sort(unsortedData, new Comparator<String[]>() {

            @Override
            public int compare(final String[] arg0, final String[] arg1) {
                String id0 = arg0[0].substring(0, arg0[0].indexOf("-") - 1);
                String id1 = arg1[0].substring(0, arg1[0].indexOf("-") - 1);
                String[] idStrings0 = id0.split("\\.");
                String[] idStrings1 = id1.split("\\.");

                int idInt0, idInt1;
                for (int i = 0; i < idStrings0.length; i++) {
                    idInt0 = Integer.parseInt(idStrings0[i].trim());
                    idInt1 = Integer.parseInt(idStrings1[i].trim());
                    if (idInt0 < idInt1) {
                        return -1;
                    } else if (idInt0 > idInt1) {
                        return 1;
                    }
                }
                return 0;
            }
        });
        return unsortedData;
    }
}
