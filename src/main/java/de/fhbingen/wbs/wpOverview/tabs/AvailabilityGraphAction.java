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

package de.fhbingen.wbs.wpOverview.tabs;

import de.fhbingen.wbs.calendar.Availability;
import de.fhbingen.wbs.calendar.Day;
import de.fhbingen.wbs.dbServices.CalendarService;
import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.PlotEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import de.fhbingen.wbs.wpAvailability.EditAvailability;
import de.fhbingen.wbs.wpWorker.Worker;

/**
 * Action listener of the class AvailabilityGraphGUI.
 */
public class AvailabilityGraphAction {

    /** The GUI of this class. */
    private AvailabilityGraphGUI gui;

    /** The parent frame. */
    private JFrame parent;

    /**
     * Constructor.
     * @param gui
     *            The GUI of the AvailabilityGraph.
     * @param parent
     *            The parent frame.
     */
    public AvailabilityGraphAction(final AvailabilityGraphGUI gui,
        final JFrame parent) {
        this.gui = gui;
        this.parent = parent;
        setAction();

    }

    /**
     * Add the action listener to the graph.
     */
    private void setAction() {
        gui.pnlGraph.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseClicked(final ChartMouseEvent e) {
                if (e.getEntity() instanceof PlotEntity) {
                    Point2D p = gui.pnlGraph.translateScreenToJava2D(e
                        .getTrigger().getPoint());
                    CategoryPlot plot = (CategoryPlot) gui.pnlGraph
                        .getChart().getPlot();
                    Rectangle2D plotArea = gui.pnlGraph.getScreenDataArea();

                    DateAxis rangeAxis = (DateAxis) plot.getRangeAxis();
                    RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();

                    CategoryAxis catAxis = plot.getDomainAxis();
                    RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();

                    double chartY = rangeAxis.java2DToValue(p.getX(),
                        plotArea, rangeAxisEdge);

                    CategoryDataset categories = (CategoryDataset) plot
                        .getDataset(0);

                    int categoryCount = categories.getColumnCount();

                    for (int i = 0; i < categoryCount; i++) {
                        double catStart = catAxis.getCategoryStart(i,
                            categoryCount, plotArea, domainAxisEdge);
                        double catEnd = catAxis.getCategoryEnd(i,
                            categoryCount, plotArea, domainAxisEdge);

                        if (e.getTrigger().getY() >= catStart
                            && e.getTrigger().getY() < catEnd) {
                            new EditAvailability(gui.function, gui.function
                                .getWorkers().get(i), new Day(new Date(
                                (long) chartY)), parent);
                        }

                    }
                } else {

                    CategoryItemEntity item = (CategoryItemEntity) e
                        .getEntity();
                    CategoryPlot plot = (CategoryPlot) gui.pnlGraph
                        .getChart().getPlot();
                    Rectangle2D plotArea = gui.pnlGraph.getScreenDataArea();
                    RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();

                    DateAxis dateAxis = (DateAxis) plot.getRangeAxis();

                    double d = dateAxis.java2DToValue(e.getEntity()
                        .getArea().getBounds2D().getX(), plotArea,
                        rangeAxisEdge);
                    double d2 = dateAxis.java2DToValue(e.getEntity()
                        .getArea().getBounds2D().getX()
                        + e.getEntity().getArea().getBounds2D().getWidth(),
                        plotArea, rangeAxisEdge);

                    Date startDate = new Date((long) d);
                    Date endDate = new Date((long) d2);

                    CategoryDataset categories = (CategoryDataset) plot
                        .getDataset(0);
                    int workerIndex = categories.getColumnIndex(item
                        .getColumnKey());
                    Worker worker = gui.function.getWorkers().get(
                        workerIndex);

                    Set<Availability> found = CalendarService
                        .getAllWorkerAvailability(worker.getId(),
                            startDate, endDate);
                    Availability foundAv = found
                        .toArray(new Availability[1])[0];

                    if (foundAv != null) {
                        new EditAvailability(gui.function, foundAv, parent);
                    } else {
                        found = CalendarService.getProjectAvailability(
                            startDate, endDate);
                        foundAv = found.toArray(new Availability[1])[0];
                        if (foundAv != null) {
                            new EditAvailability(gui.function, foundAv,
                                parent);
                        } else {
                            JOptionPane.showMessageDialog(new JFrame(
                                LocalizedStrings.getGeneralStrings()
                                    .warning()), LocalizedStrings
                                .getErrorMessages()
                                .availabilityCanNotBeChanged());
                        }
                    }
                }

            }

            @Override
            public void chartMouseMoved(final ChartMouseEvent arg0) {

            }

        });
        /**
         * ActionListener
         */
        gui.btnNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                gui.function.increment();
            }

        });
        /**
         * ActionListener
         */
        gui.btnPrev.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                gui.function.decrement();
            }

        });

        for (int i = 0; i < gui.buttons.length; i++) {
            addButtonListener(i);
        }
        /**
         * ActionListener
         */
        gui.btnManualAv.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    gui.function.setManualAv(true);
                } else {
                    gui.function.setManualAv(false);
                }
            }

        });

    }

    /**
     * ActionListener.
     * @param buttonId The id of the button.
     */
    private void addButtonListener(final int buttonId) {
        gui.buttons[buttonId].addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    deselectOthers(buttonId);
                    gui.function.changeView(buttonId);
                }
            }

        });
    }

    /**
     * Deselects all buttons but not the assigned one.
     * @param buttonId
     *            The button which should not be deselected.
     */
    private void deselectOthers(final int buttonId) {
        for (int i = 0; i < gui.buttons.length; i++) {
            if (i != buttonId) {
                gui.buttons[i].setSelected(false);
            }
        }
    }

}
