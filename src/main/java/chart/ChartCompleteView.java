package chart;

import functions.WpManager;
import globals.Workpackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import jdbcConnection.SQLExecuter;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.Range;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import dbServices.ValuesService;
import dbaccess.DBModelManager;
import dbaccess.data.AnalyseData;
import dbaccess.data.Baseline;
import wpComparators.APLevelComparator;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Diese Klassse zeigt einen JFrame mit Diagramm fuer die prozentuale
 * Fertigstellung eines Arbeitspakets<br/>
 * 
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 2012-08-20
 */
public class ChartCompleteView extends WBSChart {

    private Workpackage wp;

    /**
     * Konstruktor
     * 
     * @param parent
     *            fuer mittige Positionierung relativ zu diesem JFrame
     */
    public ChartCompleteView(JFrame parent) {
        super(getDBData(), parent);
        createSpecials();
    }

    /**
     * Kontruktor
     * 
     * @param wp
     *            Arbeitspaket zu dem der Chart angezeigt werden soll
     * @param parent
     *            fuer mittige Positionierung relativ zu diesem JFrame
     */
    public ChartCompleteView(Workpackage wp, JFrame parent) {
        super(getDBData(wp), parent);
        this.wp = wp;
        createSpecials();
    }

    /**
     * Liest Daten aus der DB
     * 
     * @return zum Anzeigen vorbereitete Daten
     */
    private static TimeSeriesCollection getDBData() {
        return getDBData(WpManager.getRootAp());
    }

    /**
     * Liest Daten aus der DB
     * 
     * @param wp
     *            Arbeitspaket, wenn Root werden alle Daten gelesen
     * @return zum Anzeigen vorbereitete Daten
     */
    protected static TimeSeriesCollection getDBData(Workpackage wp) {

        TimeSeriesCollection dataset = new TimeSeriesCollection();

        boolean dataFound = false;
        TimeSeries diagonal = new TimeSeries("Soll");
        Calendar minCal = new GregorianCalendar();
        minCal.setTime(new calendar.Day(ValuesService.getPreviousFriday(
                wp.getStartDateCalc()).getTime()));

        Calendar maxCal = new GregorianCalendar();
        maxCal.setTime(new calendar.Day(ValuesService.getNextFriday(
                wp.getEndDateCalc()).getTime(), true));
        Map<Date, Double> pvs =
                ValuesService.getPVs(minCal.getTime(), maxCal.getTime());

        List<Date> orderedDates = new ArrayList<Date>(pvs.keySet());
        Collections.sort(orderedDates);

        for (Date actualDate : orderedDates) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(actualDate);
            diagonal.add(
                    new Day(cal.get(Calendar.DATE),
                            cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)),
                    (ValuesService.getApPv(wp.getStringID(), cal) / ValuesService
                            .getApPv(wp.getStringID(), maxCal)) * 100);
        }

        if (!diagonal.isEmpty()) {
            dataFound = true;
        }
        dataset.addSeries(diagonal);

        if (wp.equals(WpManager.getRootAp())) {
            List<Workpackage> allWp =
                    new ArrayList<Workpackage>(WpManager.getAllAp());
            Collections.sort(allWp, new APLevelComparator());

            for (Workpackage actualWp : allWp) {
                if (actualWp.equals(WpManager.getRootAp())) {
                    dataset.addSeries(getTimeSeries(actualWp));
                } else if (actualWp.getlastRelevantIndex() <= levels) {
                    dataFound = true;
                    dataset.addSeries(getTimeSeries(actualWp));
                }
            }
        } else {
            TimeSeries ts = getTimeSeries(wp);
            if (!ts.isEmpty()) {
                dataFound = true;
            }
            dataset.addSeries(ts);
        }
        if (dataFound) {
            return dataset;
        } else {
            return dataset;
        }

    }

    /**
     * Generiert fuer einzelne Arbeitspakete ihren Anzeige-Graph
     * 
     * @param wp
     * @return Anzeigbarer Graph
     */
    protected static TimeSeries getTimeSeries(final Workpackage wp) {
        AnalyseData rslt =
                DBModelManager.getAnalyseDataModel().getAnalyseData(
                        wp.getWpId());

        TimeSeries actualTs = new TimeSeries(wp.toString());
        Baseline bl = null;
        GregorianCalendar cal = new GregorianCalendar();

        if (rslt != null) {
            bl =
                    DBModelManager.getBaselineModel().getBaseline(
                            rslt.getFid_baseline());
            if (bl != null) {
                cal.setTime(bl.getBl_date());
                actualTs.addOrUpdate(
                        new Day(cal.get(Calendar.DATE),
                                cal.get(Calendar.MONTH) + 1, cal
                                        .get(Calendar.YEAR)), WpManager
                                .calcPercentComplete(rslt.getBac(),
                                        rslt.getEtc(), rslt.getAc()));
            }

        }

        return actualTs;
    }

    @Override
    protected void createSpecials() {

        // Festlegen der Y- Achse auf werte zwischen 0 und 101%
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setRange(new Range(-1, 101));

        // SOLL - Kurve
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(5.0f));
        plot.getRenderer().setSeriesPaint(0, Color.BLACK);

        // Projekt Kurve
        plot.getRenderer().setSeriesStroke(1, new BasicStroke(5.0f));
        plot.getRenderer().setSeriesPaint(1, Color.RED);

        plot.addDomainMarker(new ValueMarker(System.currentTimeMillis(),
                Color.RED, new BasicStroke(1.0f)));

        if (this.wp != null) {
            Marker actualPercent =
                    new ValueMarker(WpManager.calcPercentComplete(wp.getBac(),
                            wp.getEtc(), wp.getAc()), Color.GREEN,
                            new BasicStroke(1.5f));
            // actualPercent.setLabel("aktueller Fortschritt");
            plot.addRangeMarker(actualPercent);
        }

        frame.getChartPanel().getChart().setTitle("Fortschritt (PV)");
    }
}
