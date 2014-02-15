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
 * Shows the availabilities of the employees.
 */
public class AvailabilityGraph {

    /** The GUI of the availability window. */
    private AvailabilityGraphGUI gui;

    /** Represent a day. */
    public static final int DAY = 0;

    /** Represent a week. */
    public static final int WEEK = 1;

    /** Represent a month. */
    public static final int MONTH = 2;

    /** Represent a year. */
    public static final int YEAR = 3;

    /** The worker of the project. */
    public static final Worker PROJECT_WORKER = new Worker(LocalizedStrings
        .getGeneralStrings().projectAvailability());

    /** The actual day. */
    private GregorianCalendar actualDay;

    /** The start date. */
    private GregorianCalendar actualStart;

    /** The end date. */
    private GregorianCalendar actualEnd;

    /** The actual view */
    private int actualView;

    /** Manual Availabilities flag. */
    private boolean showManualAv;

    /** A list with all workers. */
    private List<Worker> workers;

    /** The functionality of the work package overview. */
    private WPOverview over;

    /**
     * Constructor.
     * @param gui
     *            GUI of the AvailabilityGraph
     * @param over
     *            The functionality of the GUI.
     */
    public AvailabilityGraph(final AvailabilityGraphGUI gui,
        final WPOverview over) {
        this.over = over;
        this.gui = gui;
        actualDay = new GregorianCalendar();
        actualDay.setTimeInMillis(System.currentTimeMillis());

        actualStart = new GregorianCalendar();
        actualEnd = new GregorianCalendar();

    }

    /**
     * Increase the actual chosen view. (DAY / MONTH / WEEK/ YEAR)
     */
    protected final void increment() {
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
        default:
            break;
        }
        changeView(actualView);
    }

    /**
     * Decrease the actual chosen view. (DAY / MONTH / WEEK/ YEAR)
     */
    protected final void decrement() {
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
        default:
            break;
        }
        changeView(actualView);
    }

    /**
     * Change the view to DAY, WEEK, MONTH or YEAR.
     * @param newView
     *            DAY / MONTH / WEEK oder YEAR
     */
    protected final void changeView(final int newView) {
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
        default:
            break;
        }
    }

    /**
     * Set the view to DAY.
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
     * Set the view to WEEK.
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
            .calendarWeekAbbreviation()
            + " "
            + new SimpleDateFormat("ww yyyy").format(helper.getTime()));
    }

    /**
     * Set the view to MONTH.
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

        makeChart(new SimpleDateFormat("MMMM yyyy")
            .format(helper.getTime()));
    }

    /**
     * Set the view to YEAR.
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
     * Creates a chart of the actual view with a title.
     * @param title
     *            The title of the chart.
     */
    protected final void makeChart(final String title) {
        DateTickUnit tick = null;
        switch (actualView) {
        case DAY:
            tick = new DateTickUnit(DateTickUnitType.HOUR, 1,
                new SimpleDateFormat("HH"));
            break;
        case WEEK:
            tick = new DateTickUnit(DateTickUnitType.DAY, 1,
                new SimpleDateFormat("E, dd.MM."));
            break;
        case MONTH:
            tick = new DateTickUnit(DateTickUnitType.DAY, 1,
                new SimpleDateFormat("d."));
            break;
        case YEAR:
            tick = new DateTickUnit(DateTickUnitType.MONTH, 1,
                new SimpleDateFormat("M"));
            break;
        default:
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
     * Creates a task for the project availability.
     * @param set
     *            Set of the project availability
     * @return The created task.
     */
    private Task createProjectTask(final Set<Availability> set) {
        if (set.size() > 0) {
            final Task mainTask = new Task(
                AvailabilityGraph.PROJECT_WORKER.getLogin(),
                actualStart.getTime(), actualEnd.getTime());
            for (Availability projectAvailability : set) {
                mainTask.addSubtask(new Task(
                    AvailabilityGraph.PROJECT_WORKER.getLogin(),
                    projectAvailability.getStartTime(), projectAvailability
                        .getEndTime()));
            }
            return mainTask;
        } else {
            return new Task(AvailabilityGraph.PROJECT_WORKER.getLogin(),
                new Date(0), new Date(0));
        }

    }

    /**
     * Creates a task for the employees.
     * @param worker
     *            The specific employee.
     * @param workerAvailabilaties
     *            Set of the employee availabilities.
     * @return The created task.
     */
    private Task createWorkerTask(final Worker worker,
        final Set<Availability> workerAvailabilaties) {
        if (workerAvailabilaties.size() > 0) {
            final Task mainTask = new Task(worker.getVorname() + " "
                + worker.getName(), actualStart.getTime(),
                actualEnd.getTime());
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

    /**
     * Creates a data set.
     * @return The created data set
     */
    public final IntervalCategoryDataset createDataset() {

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

        TreeSet<Availability> projectAvailability = CalendarService
            .getRealProjectAvailability(actualStart.getTime(),
                actualEnd.getTime());

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

        workers.add(0, AvailabilityGraph.PROJECT_WORKER);

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(stdTasks);

        if (showManualAv) {
            collection.add(manualTasks);
            collection.add(notTasks);
        }
        return collection;
    }

    /**
     * Set the manual av.
     * @param b
     *            True: set the manual av to true. Else: set the manual av
     *            to false.
     */
    public final void setManualAv(final boolean b) {
        showManualAv = b;
        remake();
    }

    /**
     * Returns a list with all employees.
     * @return A list with the employees.
     */
    public final List<Worker> getWorkers() {
        return workers;

    }

    /** Remakes the chart. */
    public final void remake() {
        makeChart(gui.pnlGraph.getChart().getTitle().getText());
    }

    /** Reloads the main. */
    public final void reloadMain() {
        over.reload();
    }

}
