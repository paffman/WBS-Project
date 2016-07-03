package de.fhbingen.wbs.chart;

import de.fhbingen.wbs.dbServices.ValuesService;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.AnalyseData;
import de.fhbingen.wbs.dbaccess.data.Baseline;
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Controller;
import de.fhbingen.wbs.globals.Workpackage;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.util.ExtensionAndFolderFilter;
import de.fhbingen.wbs.wpComparators.APLevelComparator;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.Range;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

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
    private JFrame parent;
    private WBSChart chart;

    /**
     * Konstruktor
     *
     * @param parent
     *            fuer mittige Positionierung relativ zu diesem JFrame
     */
    public ChartCompleteView(JFrame parent) {
        super(getDBData(), parent);
        this.parent = parent;
        chart = this;
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
        this.parent = parent;
        chart = this;
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
        TimeSeries diagonal =
                new TimeSeries(LocalizedStrings.getChart().target());
        Calendar minCal = new GregorianCalendar();
        minCal.setTime(new de.fhbingen.wbs.calendar.Day(ValuesService.getPreviousFriday(
                wp.getStartDateCalc()).getTime()));

        Calendar maxCal = new GregorianCalendar();
        maxCal.setTime(new de.fhbingen.wbs.calendar.Day(ValuesService.getNextFriday(
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
                    (ValuesService.getApPv(wp.getWpId(), cal) / ValuesService
                            .getApPv(wp.getWpId(), maxCal)) * 100);
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
        List<AnalyseData> rslt =
                DBModelManager.getAnalyseDataModel().getAnalyseData(
                        wp.getWpId());

        TimeSeries actualTs = new TimeSeries(wp.toString());
        Baseline bl = null;
        GregorianCalendar cal = new GregorianCalendar();

        for ( AnalyseData aData : rslt ){
            bl =
                    DBModelManager.getBaselineModel().getBaseline(
                            aData.getFid_baseline());
            if (bl != null) {
                cal.setTime(bl.getBl_date());
                actualTs.addOrUpdate(
                        new Day(cal.get(Calendar.DATE),
                                cal.get(Calendar.MONTH) + 1, cal
                                        .get(Calendar.YEAR)), WpManager
                                .calcPercentComplete(aData.getBac(),
                                        aData.getEtc(), aData.getAc()));
            }

        }

        return actualTs;
    }

    @Override
    protected void createSpecials() {


        final JPopupMenu popup = new JPopupMenu();

        String wpName;
        if(wp != null) {
            wpName = wp.getName();

        }
        else{
            wpName = "gesamt";
        }
        JMenuItem miSave = new JMenuItem(LocalizedStrings.getButton().save(
                LocalizedStrings.getWbs().wpChart() + " " + wpName));
        miSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {


                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new ExtensionAndFolderFilter(
                        "jpg", "jpeg")); //NON-NLS
                chooser.setSelectedFile(new File("chart-" //NON-NLS
                        + System.currentTimeMillis()
                        + ".jpg"));
                int returnVal = chooser.showSaveDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File outfile =
                                chooser.getSelectedFile();
                        ChartUtilities.saveChartAsJPEG(outfile,
                                charty,
                                frame.getWidth(),
                                frame.getHeight());
                        Controller.showMessage(LocalizedStrings.getMessages()
                                .wpChartSaved(outfile.getCanonicalPath()));
                    } catch (IOException e) {
                        Controller.showError(LocalizedStrings
                                .getErrorMessages().timeLineExportError());
                    }
                }
            }

        });
        popup.add(miSave);



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
            plot.addRangeMarker(actualPercent);
        }

        frame.getChartPanel().getChart()
                .setTitle(LocalizedStrings.getChart().progressPv());


        frame.getChartPanel().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3){
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
    }
}
