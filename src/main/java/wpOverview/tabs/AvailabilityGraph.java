package wpOverview.tabs;

import calendar.Availability;
import dbServices.CalendarService;
import dbServices.WorkerService;
import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import wpOverview.WPOverview;
import wpWorker.Worker;

/**
 * Studienprojekt:	PSYS WBS 2.0<br/>
 *
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 *			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang<br/>
 *
 * WindowBuilder autogeneriert<br/>
 *
 * @author WBS1.0 Team
 * @version 2.0
 */
/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Zeigt die Verfuegbarkeiten fuer Mitarbeiter an<br/>
 *
 * @author Michael Anstatt
 * @version 2.0 - 2012-08-21
 */
public class AvailabilityGraph {

    private AvailabilityGraphGUI gui;

    public static final int DAY = 0;
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;

    public static final Worker PROJECT_WORKER = new Worker(LocalizedStrings
            .getGeneralStrings().projectAvailability());

    private GregorianCalendar actualDay;
    private GregorianCalendar actualStart;
    private GregorianCalendar actualEnd;

    private int actualView;

    private boolean showManualAv;

    private List<Worker> workers;

    private WPOverview over;

    /**
     * Konstruktor
     *
     * @param gui
     *            GUI Klasse des AvailabilityGraph
     * @param over
     *            WPOverview Funktionalitaet
     */
    public AvailabilityGraph(AvailabilityGraphGUI gui, WPOverview over) {
        this.over = over;
        this.gui = gui;
        actualDay = new GregorianCalendar();
        actualDay.setTimeInMillis(System.currentTimeMillis());

        actualStart = new GregorianCalendar();
        actualEnd = new GregorianCalendar();

    }

    /**
     * Zaehlt, je nachdem welche Ansicht gewaehlt ist ( DAY / MONTH / WEEK /
     * YEAR) einen Tag, Monat, Woche oder Jahr in der Ansicht hoch
     */
    protected void increment() {
        switch (actualView) {
        case DAY:
            actualDay.add(Calendar.DAY_OF_YEAR, 1);
            break;
        case WEEK:
            actualDay.add(Calendar.WEEK_OF_YEAR, 1);
            break;
        case MONTH:
            actualDay.add(Calendar.MONTH, 1);
            break;
        case YEAR:
            actualDay.add(Calendar.YEAR, 1);
            break;
        }
        changeView(actualView);
    }

    /**
     * Zaehlt, je nachdem welche Ansicht gewaehlt ist ( DAY / MONTH / WEEK /
     * YEAR) einen Tag, Monat, Woche oder Jahr in der Ansicht runter
     */
    protected void decrement() {
        switch (actualView) {
        case DAY:
            actualDay.add(Calendar.DAY_OF_YEAR, -1);
            break;
        case WEEK:
            actualDay.add(Calendar.WEEK_OF_YEAR, -1);
            break;
        case MONTH:
            actualDay.add(Calendar.MONTH, -1);
            break;
        case YEAR:
            actualDay.add(Calendar.YEAR, -1);
            break;
        }
        changeView(actualView);
    }

    /**
     * Setzt die Ansicht auf Tag, Woche, Monat oder Jahr
     *
     * @param newView
     *            DAY / MONTH / WEEK oder YEAR
     */
    protected void changeView(int newView) {
        switch (newView) {
        case DAY:
            setDayView();
            break;
        case WEEK:
            setWeekView();
            break;
        case MONTH:
            setMonthView();
            break;
        case YEAR:
            setYearView();
            break;
        }
    }

    /**
     * setzt die Ansicht auf "Tag"
     */
    private void setDayView() {

        actualStart = (GregorianCalendar) actualDay.clone();
        actualStart.set(Calendar.HOUR_OF_DAY, 0);
        actualStart.set(Calendar.MINUTE, 0);
        actualStart.add(Calendar.HOUR, -1);

        actualEnd = (GregorianCalendar) actualDay.clone();
        actualEnd.set(Calendar.HOUR_OF_DAY, 23);
        actualEnd.set(Calendar.MINUTE, 59);
        actualEnd.add(Calendar.HOUR, 1);

        actualView = DAY;

        GregorianCalendar helper = (GregorianCalendar) actualEnd.clone();
        helper.add(Calendar.DATE, -1);

        makeChart(new SimpleDateFormat("EEEE, dd.MM.yyyy").format(helper
                .getTime()));
    }

    /**
     * setzt die Ansicht auf "Woche"
     */
    private void setWeekView() {

        actualStart = (GregorianCalendar) actualDay.clone();
        actualStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        actualStart.set(Calendar.HOUR_OF_DAY, 0);
        actualStart.set(Calendar.MINUTE, 0);

        actualEnd = (GregorianCalendar) actualDay.clone();
        actualEnd.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        actualEnd.set(Calendar.HOUR_OF_DAY, 23);
        actualEnd.set(Calendar.MINUTE, 59);

        actualStart.add(Calendar.HOUR_OF_DAY, -1);
        actualEnd.add(Calendar.HOUR_OF_DAY, 1);

        actualView = WEEK;

        GregorianCalendar helper = (GregorianCalendar) actualEnd.clone();
        helper.add(Calendar.DATE, -1);

        makeChart(LocalizedStrings.getGeneralStrings()
                .calendarWeekAbbreviation() +
                " " + new SimpleDateFormat("ww yyyy").format(helper.getTime()
        ));
    }

    /**
     * setzt die Ansicht auf "Monat"
     */
    private void setMonthView() {

        actualStart = (GregorianCalendar) actualDay.clone();
        actualStart.set(Calendar.DAY_OF_MONTH, 1);
        actualStart.set(Calendar.HOUR, 0);
        actualStart.set(Calendar.MINUTE, 0);

        actualEnd = (GregorianCalendar) actualDay.clone();
        actualEnd.set(Calendar.DAY_OF_MONTH,
                actualEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        actualEnd.set(Calendar.HOUR, 23);
        actualEnd.set(Calendar.MINUTE, 59);

        actualStart.add(Calendar.HOUR, -1);
        actualEnd.add(Calendar.HOUR, 1);

        actualView = MONTH;

        GregorianCalendar helper = (GregorianCalendar) actualEnd.clone();
        helper.add(Calendar.DATE, -1);

        makeChart(new SimpleDateFormat("MMMM yyyy").format(helper.getTime()));
    }

    /**
     * setzt die Ansicht auf "Jahr"
     */
    private void setYearView() {

        actualStart = (GregorianCalendar) actualDay.clone();
        actualStart.set(Calendar.DAY_OF_YEAR,
                actualDay.getActualMinimum(Calendar.DAY_OF_YEAR));
        actualStart.set(Calendar.HOUR, 0);
        actualStart.set(Calendar.MINUTE, 0);
        actualStart.add(Calendar.DAY_OF_YEAR, -1);

        actualEnd = (GregorianCalendar) actualDay.clone();
        actualEnd.set(Calendar.DAY_OF_YEAR,
                actualDay.getActualMaximum(Calendar.DAY_OF_YEAR));
        actualEnd.set(Calendar.HOUR, 23);
        actualEnd.set(Calendar.MINUTE, 59);

        actualView = YEAR;

        GregorianCalendar helper = (GregorianCalendar) actualEnd.clone();
        helper.add(Calendar.DATE, -1);

        makeChart(new SimpleDateFormat("yyyy").format(helper.getTime()));
    }

    /**
     * Erzeugt einen Chart in der ANsicht mit bestimmtem Titel
     *
     * @param title
     */
    protected void makeChart(String title) {
        DateTickUnit tick = null;
        switch (actualView) {
        case DAY:
            tick =
                    new DateTickUnit(DateTickUnitType.HOUR, 1,
                            new SimpleDateFormat("HH"));
            break;
        case WEEK:
            tick =
                    new DateTickUnit(DateTickUnitType.DAY, 1,
                            new SimpleDateFormat("E, dd.MM."));
            break;
        case MONTH:
            tick =
                    new DateTickUnit(DateTickUnitType.DAY, 1,
                            new SimpleDateFormat("d."));
            break;
        case YEAR:
            tick =
                    new DateTickUnit(DateTickUnitType.MONTH, 1,
                            new SimpleDateFormat("M"));
            break;
        }
        gui.pnlGraph.setChart(ChartFactory.createGanttChart(title, "", "",
                createDataset(), true, true, false));
        gui.pnlGraph.getChart().getTitle()
                .setHorizontalAlignment(HorizontalAlignment.LEFT);
        gui.pnlGraph.getChart().getTitle().setMargin(5, 10, 5, 5);
        gui.pnlGraph.getChart().getCategoryPlot().getDomainAxis()
                .setCategoryMargin(0.4);
        gui.pnlGraph.getChart().getCategoryPlot().getDomainAxis()
                .setLowerMargin(0);
        gui.pnlGraph.getChart().getCategoryPlot().getDomainAxis()
                .setUpperMargin(0);

        // chart.getCategoryPlot().getDomainAxis().getL

        gui.pnlGraph.getChart().getCategoryPlot().getDomainAxis()
                .setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));

        CategoryPlot plot = gui.pnlGraph.getChart().getCategoryPlot();
        DateAxis axis = (DateAxis) plot.getRangeAxis();
        axis.setMinimumDate(actualStart.getTime());
        axis.setMaximumDate(actualEnd.getTime());
        axis.setTickUnit(tick);

        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        renderer.setSeriesPaint(1, Color.green);
        renderer.setSeriesPaint(2, Color.red);
    }

    /**
     * Erzeugt den Balken fuer die Projektverfuegbarkeit
     *
     * @param set
     *            Set von Project-Availabilities
     * @return den Task, anzeigefertig fuer JFreeChart
     */
    private Task createProjectTask(Set<Availability> set) {
        if (set.size() > 0) {
            final Task mainTask =
                    new Task(AvailabilityGraph.PROJECT_WORKER.getLogin(),
                            actualStart.getTime(), actualEnd.getTime());
            for (Availability projectAvailability : set) {
                mainTask.addSubtask(new Task(AvailabilityGraph.PROJECT_WORKER
                        .getLogin(), projectAvailability.getStartTime(),
                        projectAvailability.getEndTime()));
            }
            return mainTask;
        } else {
            return new Task(AvailabilityGraph.PROJECT_WORKER.getLogin(),
                    new Date(0), new Date(0));
        }

    }

    private Task createWorkerTask(Worker worker,
            Set<Availability> workerAvailabilaties) {
        if (workerAvailabilaties.size() > 0) {
            final Task mainTask =
                    new Task(worker.getVorname() + " " + worker.getName(),
                            actualStart.getTime(), actualEnd.getTime());
            for (Availability workerAvailability : workerAvailabilaties) {
                mainTask.addSubtask(new Task(worker.getVorname() + " "
                        + worker.getName(), workerAvailability.getStartTime(),
                        workerAvailability.getEndTime()));
            }
            return mainTask;
        } else {
            return new Task(worker.getVorname() + " " + worker.getName(),
                    new Date(0), new Date(0));
        }

    }

    public IntervalCategoryDataset createDataset() {

        if (WPOverview.getUser().getProjLeiter()) {
            workers = WorkerService.getRealWorkers();
        } else {
            workers = new ArrayList<Worker>();
            Worker user = WPOverview.getUser();
            workers.add(new Worker(user.getLogin(), user.getVorname(), user
                    .getName(), 1, 100));
        }

        gui.pnlGraph.setMinimumDrawHeight(100 + 50 * workers.size() + 1);
        gui.pnlGraph.setMaximumDrawHeight(100 + 50 * workers.size() + 1);
        gui.pnlGraph.setPreferredSize(new Dimension((int) gui.pnlGraph
                .getPreferredSize().getWidth(), 100 + 50 * workers.size() + 1));

        final TaskSeries stdTasks = new TaskSeries(LocalizedStrings
                .getGeneralStrings().availability());
        final TaskSeries manualTasks = new TaskSeries(LocalizedStrings
                .getGeneralStrings().available());
        final TaskSeries notTasks = new TaskSeries(LocalizedStrings
                .getGeneralStrings().notAvailable());

        TreeSet<Availability> projectAvailability =
                CalendarService.getRealProjectAvailability(
                        actualStart.getTime(), actualEnd.getTime());

        stdTasks.add(createProjectTask(projectAvailability));

        if (showManualAv) {
            notTasks.add(createProjectTask(CalendarService
                    .getProjectAvailability(false)));
            manualTasks.add(createProjectTask(CalendarService
                    .getProjectAvailability(true)));
        }
        for (Worker worker : workers) {

            stdTasks.add(createWorkerTask(worker, CalendarService
                    .getRealWorkerAvailability(worker.getId(),
                            actualStart.getTime(), actualEnd.getTime())));
            if (showManualAv) {
                notTasks.add(createWorkerTask(worker, CalendarService
                        .getAllWorkerAvailability(worker.getId(), false)));
                manualTasks.add(createWorkerTask(worker, CalendarService
                        .getAllWorkerAvailability(worker.getId(), true)));
            }

        }

        workers.add(0, AvailabilityGraph.PROJECT_WORKER); // 0. Mitarbeiter ist
                                                          // Projektverfuegbarkeit

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(stdTasks);

        if (showManualAv) {
            collection.add(manualTasks);
            collection.add(notTasks);
        }
        return collection;
    }

    public void setManualAv(boolean b) {
        showManualAv = b;
        remake();
    }

    public List<Worker> getWorkers() {
        return workers;

    }

    public void remake() {
        makeChart(gui.pnlGraph.getChart().getTitle().getText());
    }

    public void reloadMain() {
        over.reload();
    }

}
