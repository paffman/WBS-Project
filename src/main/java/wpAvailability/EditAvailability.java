package wpAvailability;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import calendar.Availability;
import calendar.Day;
import dbServices.CalendarService;
import dbServices.WorkerService;

import wpConflict.Conflict;
import wpOverview.WPOverview;
import wpOverview.tabs.AvailabilityGraph;
import wpWorker.Worker;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Diese Klasse wird zum editieren der Verfuegbarkeiten benutzt.<br/>
 * Sie bietet die Funktionalitaet fuer die Klasse EditAvailabilityGUI.<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 2012-08-21
 */
public class EditAvailability {
    private EditAvailabilityGUI gui;
    private Availability availability = null;

    /**
     * Vorhandene Availability bearbeiten
     * 
     * @param avGraph
     *            Verfuegbarkeiten Graph
     * @param availability
     *            Verfuegbarkeiten
     * @param parent
     *            ParentFrame
     */
    public EditAvailability(final AvailabilityGraph avGraph,
            Availability availability, JFrame parent) {
        this.availability = availability;
        gui =
                new EditAvailabilityGUI(this, "Verfügbarkeit bearbeiten",
                        parent);
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
            public void windowClosing(WindowEvent e) {
                avGraph.reloadMain();

            }
        });

    }

    /**
     * Legt neue Availability an
     * 
     * @param avGraph
     *            Verfuegbarkeits Graph
     * @param worker
     *            Arbeiter
     * @param day
     *            Day Objekt mit Arbeitszeiten
     * @param parent
     *            ParentFrame
     */
    public EditAvailability(final AvailabilityGraph avGraph, Worker worker,
            Day day, JFrame parent) {
        gui = new EditAvailabilityGUI(this, "Neue Verfügbarkeit", parent);
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
            public void windowClosing(WindowEvent e) {
                avGraph.remake();
            }
        });
    }

    /**
     * Speichert die Verfuegbarkeit in der DB
     */
    public void save() {
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
                                .getAllDay(), gui.getAvailable(), start, end,
                                gui.getDescription(), availability.getId()));
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
                                .getAllDay(), gui.getAvailable(), start, end,
                                gui.getDescription()));
            }

        }

        gui.dispose();
        // Schliessen simulieren, notwendig fuer WindowListener
        WindowEvent closingEvent =
                new WindowEvent(gui, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(closingEvent);

    }

    /**
     * Loescht Verfuegbarkeit aus der DB
     */
    public void delete() {
        if (availability != null) {
            if (gui.getWorker().equals(AvailabilityGraph.PROJECT_WORKER)) {
                CalendarService.deleteProjectAvailability(availability);
            } else {
                CalendarService.deleteWorkerAvailability(availability);
            }
        }

        gui.dispose();
        // Schliessen simulieren, notwendig fuer WindowListener
        WindowEvent closingEvent =
                new WindowEvent(gui, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue()
                .postEvent(closingEvent);
    }

    protected ActionListener getSaveListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WPOverview.throwConflict(new Conflict(new Date(System
                        .currentTimeMillis()), Conflict.CHANGED_RESOURCES,
                        WPOverview.getUser().getId()));
                save();
            }
        };
    }

    protected ActionListener getDeleteListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WPOverview.throwConflict(new Conflict(new Date(System
                        .currentTimeMillis()), Conflict.CHANGED_RESOURCES,
                        WPOverview.getUser().getId()));
                delete();
            }
        };
    }

    protected ActionListener getCancelListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
            }
        };
    }

    protected ItemListener getCbAvailableListener() {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                gui.setAvailableView(e.getStateChange() == ItemEvent.SELECTED);
            }
        };
    }
}
