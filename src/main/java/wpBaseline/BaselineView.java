package wpBaseline;

import de.fhbingen.wbs.translation.LocalizedStrings;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import dbaccess.DBModelManager;
import dbaccess.data.AnalyseData;
import functions.WpManager;
import globals.Controller;
import globals.Workpackage;
import jdbcConnection.SQLExecuter;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Funktionalitaet der BaselineViewGUI<br/>
 *
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 20.08.2012
 */
public class BaselineView {
    /**
     * Konstruktor
     *
     * @param baselineID
     *            ID der gewuenschten Baseline
     * @param parent
     *            ParentFrame
     */
    public BaselineView(int baselineID, JFrame parent) {
        try {
            String[][] data = getData(baselineID).toArray(new String[1][1]);
            BaselineViewGUI gui = new BaselineViewGUI(parent);
            for (String[] actualRow : data) {
                gui.addRow(actualRow);
            }
            ;

        } catch (SQLException e) {
            Controller.showError(LocalizedStrings.getErrorMessages()
                    .baselineLoadingError());
            e.printStackTrace();
        }
    }

    /**
     * Fuellt die BaselineViewGUI mit Daten aus der DB
     *
     * @param baselineID
     *            ID der gewuenschten Baseline
     * @return Liste mit StringArrays der Daten
     * @throws SQLException
     */
    private List<String[]> getData(int baselineID) throws SQLException {
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
                        Controller.DECFORM.format(ad.getBac_costs()) + " EUR";
                actualData[i++] =
                        Controller.DECFORM.format(ad.getAc_costs()) + " EUR";
                actualData[i++] =
                        Controller.DECFORM.format(ad.getEtc_costs()) + " EUR";
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
        return allData;
    }
}
