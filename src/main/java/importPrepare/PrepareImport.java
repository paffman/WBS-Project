package importPrepare;

import java.sql.ResultSet;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Berechnen der Daten von externen Projekten, die nicht direkt in der WBS Anwendung erzeugt worden sind
 * und als MDB mit dem Programm geladen worden sind.
 * 
 * @author Andre Paffenholz
 * @version 0.3 - .12.2010
 */
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sun.util.LocaleServiceProviderPool.LocalizedObjectGetter;
import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import dbaccess.data.WorkEffort;
import dbaccess.data.Workpackage;
import dbaccess.data.WorkpackageAllocation;
import de.fhbingen.wbs.translation.LocalizedStrings;
import functions.CalcOAPBaseline;
import functions.WpManager;
import wpOverview.WPOverview;

public class PrepareImport {

    /**
     * User Konstruktor
     * 
     * @param wpo
     *            - WPOverview: WPOverview Objekt, ueber das PrepareImport
     *            erzeugt worden ist
     */

    public PrepareImport(WPOverview wpo) {
        prepareAPs();
        new CalcOAPBaseline(LocalizedStrings.getGeneralStrings().initImportDb(), wpo);
    }

    /**
     * void prepareAPs() liest alle normalen Arbeitspakete ein und prüft die
     * Werte, falls noetig werden Aenderungen vorgenommen - wird vom Konstruktor
     * aufgerufen
     */

    public void prepareAPs() {
        try {
            List<Workpackage> wps =
                    DBModelManager.getWorkpackageModel().getWorkpackage(true);
            for (Workpackage wp : wps) {
                String stingId = wp.getStringID();

                double dailyRate = getTagessatz(wp.getId());
                double bac = wp.getBac();
                double bacCost = bac * dailyRate;
                double ac = getAufwand(wp.getId());
                double acCost = getACKosten(wp.getId());
                double etc = wp.getEtc();

                if (etc < 0) {
                    etc = 0.;
                }

                double percentComplete =
                        WpManager.calcPercentComplete(bac, etc, ac);
                double etcCost = (etc * dailyRate);
                double eac;
                if (bacCost > 0) {
                    eac = acCost + etcCost;
                } else {
                    eac = 0.0;
                }

                wp.setAc(ac);

                Double cpi;
                if (acCost + etcCost == 0.) {
                    if (bacCost == 0.) {
                        cpi = 1.0;
                    } else {
                        cpi = 10.0;
                    }
                } else {
                    cpi = bacCost / (acCost + etcCost);
                }

                wp.setCpi(cpi);
                wp.setAcCosts(acCost);
                wp.setEtcCosts(etcCost);
                wp.setEv(bac * percentComplete / 100 * dailyRate);
                wp.setBacCosts(bacCost);
                wp.setDailyRate(dailyRate);
                wp.setEac(eac);
                DBModelManager.getWorkpackageModel().updateWorkpackage(wp);
                WpManager.updateAP(WpManager.getWorkpackage(stingId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Liefert den Aufwand fuer ein durch die Parameter bestimmtes Paket zurueck
     * 
     * @param wp
     *            Id of a workpackage.
     * @return der Aufwand als Double
     */

    public Double getAufwand(int wp) {
        return DBModelManager.getWorkEffortModel().getWorkEffortSum(wp);
    }

    /**
     * Liefert die ACKosten fuer ein durch die Parameter bestimmtes Paket
     * zurueck
     * 
     * @param lvl1ID
     *            - Level1 ID als int
     * @param lvl2ID
     *            - Level2 ID als int
     * @param lvl3ID
     *            - Level3 ID als int
     * @param lvlxID
     *            - Levelx ID als String
     * @return ACKosten als Double
     * @throws SQLException
     */

    public double getACKosten(int wp) throws SQLException {
        double acCosts = 0.;
        List<WorkEffort> efforts =
                DBModelManager.getWorkEffortModel().getWorkEffort(wp);
        Employee employee;
        for (WorkEffort effort : efforts) {
            employee =
                    DBModelManager.getEmployeesModel().getEmployee(
                            effort.getFid_emp());
            if (employee != null) {
                acCosts += effort.getEffort() * employee.getDaily_rate();
            }
        }
        return acCosts;
    }

    /**
     * Berechnet den Mittelwert der Tagessätze der Mitarbeiter, die einem AP
     * zugewiesen sind Wird aufgerufen von: - WPShow.addButtonAction() - von
     * beiden Konstruktoren der WPShow
     * 
     * @return der gemittelte Tagessatz als Double
     */
    public double getTagessatz(int wp) {

        Double wptagessatz = 0.0;
        List<Integer> workers = new ArrayList<Integer>();

        List<WorkpackageAllocation> wpAlloc = DBModelManager.getWorkpackageAllocationModel().getWorkpackageAllocation(wp);
        for ( WorkpackageAllocation wpa : wpAlloc ){
            workers.add(wpa.getFid_emp());
        }

        for (int j = 0; j < workers.size(); j++) {
            int id = workers.get(j);
            Employee emp =
                    DBModelManager.getEmployeesModel().getEmployee(id);
            if (emp != null) {
                wptagessatz += emp.getDaily_rate();
            }
        }
        wptagessatz /= workers.size();
        if (Double.isNaN(wptagessatz)) {
            wptagessatz = 0.0;
        }

        return wptagessatz;
    }
}
