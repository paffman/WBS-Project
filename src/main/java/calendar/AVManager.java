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

package calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dbServices.CalendarService;
import dbServices.WorkerService;

import wpOverview.tabs.AvailabilityGraph;
import wpWorker.Worker;

/**
 * Manages the availabilities to calculate them.
 */
public class AVManager {

    /**
     * Assignment: Day -> (Employee -> Availabilities).
     */
    private static Map<Day, Map<String, List<Availability>>> completeAvailabilities;

    /**
     * Assignment: Day -> (Employee -> Used hours).
     */
    private static Map<Day, Map<String, Integer>> consumedWork;

    /**
     * Assignment: Day -> (Employee -> Not used hours).
     */
    private static Map<Day, Map<String, Integer>> availableWork;

    /**
     * List with all employees.
     */
    private ArrayList<Worker> allProjectWorkers;

    /**
     * Contains all read days.
     */
    private Set<Day> readDays;

    /**
     * Constructor.
     */
    public AVManager() {
        this.allProjectWorkers = WorkerService.getRealWorkers();
        availableWork = new HashMap<Day, Map<String, Integer>>();
        consumedWork = new HashMap<Day, Map<String, Integer>>();
        completeAvailabilities = new HashMap<Day, Map<String, List<Availability>>>();
        readDays = new HashSet<Day>();
    }

    /**
     * Reads the next project working time.
     * @param oldDate
     *            Date, to which the next working time is searched.
     * @return The next working time.
     */
    public final Date getNextWorkDate(final Date oldDate) {
        if (oldDate != null) {
            List<Availability> dayProjAvs = new ArrayList<Availability>();
            Day actualDay = new Day(oldDate);
            Date returnDate = null;
            boolean readNext = false;
            while (returnDate == null) {
                while (readNext || dayProjAvs.isEmpty()) {
                    if (!readDays.contains(actualDay)) {
                        fillData(actualDay);
                    }
                    dayProjAvs = completeAvailabilities.get(actualDay).get(
                        AvailabilityGraph.PROJECT_WORKER.getLogin());
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(actualDay);
                    cal.add(Calendar.DATE, 1);
                    actualDay = new Day(cal.getTime());
                    readNext = false;
                }
                Collections.sort(dayProjAvs);
                for (Availability actualAv : dayProjAvs) {
                    if (actualAv.getStartTime().before(oldDate)
                        && actualAv.getEndTime().after(oldDate)) {
                        returnDate = oldDate; // oldDate is reside in an
                                              // availability.
                    } else if (actualAv.getStartTime().after(oldDate)
                        || actualAv.getStartTime().equals(oldDate)) {
                        returnDate = actualAv.getStartTime(); // The next
                                                              // availability
                                                              // is
                                                              // founded.
                    }
                    if (returnDate != null) {
                        return returnDate;
                    }
                }
                readNext = true;
            }
            return returnDate;
        } else {
            return null;
        }

    }

    /**
     * Reads the next working time for a specific day if on this day is
     * already worked for workedToday hours.
     * @param actualDay
     *            The date without the time.
     * @param workedToday
     *            The count of worked hours.
     * @return The next working time.
     */
    public final Date getNextWorkTime(Day actualDay, int workedToday) {
        List<Availability> dayProjAvs = new ArrayList<Availability>();
        Date returnDate = null;
        while (returnDate == null) {
            while (dayProjAvs.isEmpty()) {
                if (!readDays.contains(actualDay)) {
                    fillData(actualDay);
                }
                dayProjAvs = completeAvailabilities.get(actualDay).get(
                    AvailabilityGraph.PROJECT_WORKER.getLogin());
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(actualDay);
                cal.add(Calendar.DATE, 1);
                actualDay = new Day(cal.getTime());
            }
            for (Availability actualAv : dayProjAvs) {
                if (actualAv.getDuration() < workedToday) {
                    workedToday -= actualAv.getDuration();
                } else {
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(actualAv.getStartTime());
                    cal.add(Calendar.HOUR_OF_DAY, workedToday);
                    returnDate = cal.getTime();
                }
                if (returnDate != null) {
                    return returnDate;
                }
            }
        }

        return returnDate;
    }

    /**
     * Returns for all employees the count of hours, which the workers can
     * execute on this day (without already used hours).
     * @param actualDay
     *            The actual day.
     * @return A Map with assignment: EmployeeID -> all available hours on
     *         this day.
     */
    public final Map<String, Integer> getCompleteDayWorks(
        final Day actualDay) {

        if (!readDays.contains(actualDay)) {
            fillData(actualDay);
        }

        return availableWork.get(actualDay);
    }

    /**
     * Reads the availabilities from the employees from the data base.
     * @param actualDay
     *            The wished day.
     */
    private void fillData(final Day actualDay) {
        List<Availability> avs;
        Map<String, Integer> availableDayWorks = new HashMap<String, Integer>();
        Map<String, Integer> consumedDayWorks = new HashMap<String, Integer>();
        completeAvailabilities.put(actualDay,
            new HashMap<String, List<Availability>>());
        for (Worker actualWorker : allProjectWorkers) {
            avs = new ArrayList<Availability>();
            avs.addAll(CalendarService.getRealWorkerAvailability(
                actualWorker.getId(), actualDay, new Day(actualDay, true)));
            completeAvailabilities.get(actualDay).put(
                actualWorker.getLogin(), avs);
            availableDayWorks
                .put(
                    actualWorker.getLogin(),
                    getCompleteWorkerDayWork(actualWorker.getLogin(),
                        actualDay));
            consumedDayWorks.put(actualWorker.getLogin(), 0);
        }
        avs = new ArrayList<Availability>();
        avs.addAll(CalendarService.getRealProjectAvailability(actualDay,
            new Day(actualDay, true)));
        completeAvailabilities.get(actualDay).put(
            AvailabilityGraph.PROJECT_WORKER.getLogin(), avs);
        availableDayWorks.put(
            AvailabilityGraph.PROJECT_WORKER.getLogin(),
            getCompleteWorkerDayWork(
                AvailabilityGraph.PROJECT_WORKER.getLogin(), actualDay));
        consumedDayWorks
            .put(AvailabilityGraph.PROJECT_WORKER.getLogin(), 0);

        availableWork.put(actualDay, availableDayWorks);
        consumedWork.put(actualDay, consumedDayWorks);
        readDays.add(actualDay);
    }

    /**
     * Returns a map with not used hours for an specific day.
     * @param actualDay
     *            The wished day.
     * @param workers
     *            A list with employee which are assigned to a work
     *            package.
     * @return A Map with assignment: Employee -> available working time on
     *         this day.
     */
    public final Map<String, Integer> getRemainingDayWorks(
        final Day actualDay, final List<String> workers) {
        if (!readDays.contains(actualDay)) {
            fillData(actualDay);
        }
        Map<String, Integer> remainingDayWorks = new HashMap<String, Integer>();
        int remaining;
        for (String actualWorker : workers) {
            remaining = getCompleteDayWorks(actualDay).get(actualWorker)
                - getConsumedDayWork(actualDay).get(actualWorker);
            remainingDayWorks.put(actualWorker, remaining);
        }
        return remainingDayWorks;
    }

    /**
     * Returns the working time which is already not used on this day.
     * @param actualDay
     *            The wished day.
     * @param workers
     *            List with employees which are assigned to a work package.
     * @return Time in hours.
     */
    public final int getTogetherRemainingDayWork(final Day actualDay,
        final List<String> workers) {
        if (!readDays.contains(actualDay)) {
            fillData(actualDay);
        }
        int remaining = 0;
        for (String actualWorker : workers) {
            remaining += getCompleteDayWorks(actualDay).get(actualWorker)
                - getConsumedDayWork(actualDay).get(actualWorker);
        }
        return remaining;
    }

    /**
     * Returns the already used hours on this day from specific employees.
     * @param actualDay
     *            The wished day.
     * @return A Map with assignment: Employee -> already used working
     *         hours.
     */
    public final Map<String, Integer> getConsumedDayWork(final Day actualDay) {
        if (!readDays.contains(actualDay)) {
            fillData(actualDay);
        }
        return consumedWork.get(actualDay);
    }

    /**
     * Add a work effort to an employee.
     * @param workerID
     *            The employee.
     * @param actualDay
     *            The wished day.
     * @param hours
     *            The count of the worked hours.
     */
    public final void addWork(final String workerID, final Day actualDay,
        final int hours) {
        if (!readDays.contains(actualDay)) {
            fillData(actualDay);
        }
        Map<String, Integer> old = consumedWork.get(actualDay);
        old.put(workerID, old.get(workerID) + hours);
    }

    /**
     * Returns the possible working time of an specific employee.
     * @param actualWorkerID
     *            The specific employee.
     * @param actualDay
     *            The wished day.
     * @return The time in hours.
     */
    private int getCompleteWorkerDayWork(final String actualWorkerID,
        final Day actualDay) {
        int availableDayWork = 0;
        if (!completeAvailabilities.containsKey(actualDay)) {
            fillData(actualDay);
        }
        if (completeAvailabilities.get(actualDay).get(actualWorkerID) != null) {
            for (Availability actualAv : completeAvailabilities.get(
                actualDay).get(actualWorkerID)) {
                availableDayWork += actualAv.getDuration();
            }
        }
        return availableDayWork;
    }
}
