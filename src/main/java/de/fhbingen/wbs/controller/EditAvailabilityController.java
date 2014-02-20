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

package de.fhbingen.wbs.controller;

import de.fhbingen.wbs.gui.EditAvailabilityView;
import de.fhbingen.wbs.wpConflict.Conflict;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JFrame;

import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpOverview.tabs.AvailabilityGraph;
import de.fhbingen.wbs.wpWorker.Worker;
import de.fhbingen.wbs.calendar.Availability;
import de.fhbingen.wbs.calendar.Day;
import de.fhbingen.wbs.dbServices.CalendarService;
import de.fhbingen.wbs.dbServices.WorkerService;
import de.fhbingen.wbs.translation.LocalizedStrings;

/**
 * Is used to edit the availabilities. Also this class is the functionality
 * for the class EditAvailabilityView.
 */
public class EditAvailabilityController implements EditAvailabilityView.Actions {
    /** The GUI of this class. */
    private EditAvailabilityView gui;
    /** Represents the availability. */
    private Availability availability = null;

    /**
     * Edit the selected availability.
     * @param avGraph
     *            The graph of availability.
     * @param availability
     *            The availability.
     * @param parent
     *            The parent frame.
     */
    public EditAvailabilityController(final AvailabilityGraph avGraph,
                                      final Availability availability,
                                      final JFrame parent) {
        this.availability = availability;
        gui = new EditAvailabilityView(this, LocalizedStrings.getWbs()
            .editAvailability(), parent);
        gui.setNewView(false);

        List<Worker> workers = WorkerService.getRealWorkers();
        workers.add(AvailabilityGraph.PROJECT_WORKER);
        gui.setWorkers(workers.toArray(new Worker[1]));

        if (availability != null) {
            if (WPOverview.getUser().getProjLeiter()) {
                gui.setWorker(new Worker(availability.getUserID()));
            } else {
                gui.setWorker(WPOverview.getUser());
                gui.disableWorkerSelection();
            }

            gui.setAllDay(availability.isAllDay());
            gui.setAvailable(availability.isAvailabe());

            gui.setStart(availability.getStartTime());
            gui.setEnd(availability.getEndTime());

            gui.setDescription(availability.getDescription());
        }

        gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                avGraph.reloadMain();

            }
        });

    }

    /**
     * Creates a new availability.
     * @param avGraph
     *            The graph of availability.
     * @param worker
     *            The worker from which the availability is edit.
     * @param day
     *            A Day object with the work times.
     * @param parent
     *            The parent frame
     */
    public EditAvailabilityController(final AvailabilityGraph avGraph, final Worker worker, final Day day, final JFrame parent) {
        gui = new EditAvailabilityView(this, LocalizedStrings.getWbs()
            .newAvailability(), parent);
        gui.setNewView(true);

        List<Worker> workers = WorkerService.getRealWorkers();
        workers.add(AvailabilityGraph.PROJECT_WORKER);
        gui.setWorkers(workers.toArray(new Worker[1]));

        if (WPOverview.getUser().getProjLeiter()) {
            if (worker != null) {
                gui.setWorker(worker);
            }
        } else {
            gui.setWorker(WPOverview.getUser());
            gui.disableWorkerSelection();
        }

        Calendar cal = new GregorianCalendar();
        cal.setTime(day);

        cal.set(Calendar.HOUR_OF_DAY, 8);

        gui.setStart(cal.getTime());
        cal.add(Calendar.HOUR, 8);
        gui.setEnd(cal.getTime());

        gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                avGraph.remake();
            }
        });
    }

    /**
     * Saves the availability in the data base.
     */
    public final void save() {
        Date start = gui.getStart();
        Date end = gui.getEnd();

        if (availability != null) {
            if (gui.getWorker().equals(AvailabilityGraph.PROJECT_WORKER)) {
                CalendarService.setProjectAvailability(new Availability(gui
                    .getWorker().getId(), gui.getAllDay(), gui
                    .getAvailable(), start, end, gui.getDescription(),
                    availability.getId()));
            } else {
                CalendarService.setWorkerAvailability(
                    gui.getWorker().getLogin(),
                    new Availability(gui.getWorker().getId(), gui
                        .getAllDay(), gui.getAvailable(), start, end, gui
                        .getDescription(), availability.getId()));
            }
        } else {
            if (gui.getWorker().equals(AvailabilityGraph.PROJECT_WORKER)) {
                CalendarService.setProjectAvailability(new Availability(gui
                    .getWorker().getId(), gui.getAllDay(), gui
                    .getAvailable(), start, end, gui.getDescription()));
            } else {
                CalendarService.setWorkerAvailability(
                    gui.getWorker().getLogin(),
                    new Availability(gui.getWorker().getId(), gui
                        .getAllDay(), gui.getAvailable(), start, end, gui
                        .getDescription()));
            }

        }

        gui.dispose();
     // Simulates the closing, is used for the WindowListener
        WindowEvent closingEvent = new WindowEvent(gui,
            WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue()
            .postEvent(closingEvent);

    }

    /**
     * Deletes the availability from the data base.
     */
    public final void delete() {
        if (availability != null) {
            if (gui.getWorker().equals(AvailabilityGraph.PROJECT_WORKER)) {
                CalendarService.deleteProjectAvailability(availability);
            } else {
                CalendarService.deleteWorkerAvailability(availability);
            }
        }

        gui.dispose();
     // Simulates the closing, is used for the WindowListener
        WindowEvent closingEvent = new WindowEvent(gui,
            WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue()
            .postEvent(closingEvent);
    }

    /**
     * Returns the ActionListener from the save button.
     * @return The ActionListener.
     */
    protected final ActionListener getSaveListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

            }
        };
    }

    @Override
    public final void buttonCancel() {
        gui.dispose();
    }

    @Override
    public final void buttonDelete() {
        WPOverview.throwConflict(new Conflict(new Date(System
                .currentTimeMillis()), Conflict.CHANGED_RESOURCES,
                WPOverview.getUser().getId()));
        delete();
    }

    @Override
    public final void buttonSave() {
        WPOverview.throwConflict(new Conflict(new Date(System
                .currentTimeMillis()), Conflict.CHANGED_RESOURCES,
                WPOverview.getUser().getId()));
        save();
    }

    @Override
    public final void checkboxChanged(final ItemEvent e) {
        gui.setAvailableView(e.getStateChange() == ItemEvent.SELECTED);
    }
}
