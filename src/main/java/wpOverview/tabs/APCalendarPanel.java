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

import de.fhbingen.wbs.translation.LocalizedStrings;
import functions.WpManager;
import globals.Controller;
import globals.Workpackage;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import jdbcConnection.MDBConnect;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarPainter;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import wpComparators.APLevelComparator;
import wpOverview.WPOverview;

/**
 * The class for the work package calendar.
 */
public class APCalendarPanel extends JPanel {

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = -4157955530411333945L;

    /** Represents the count of levels. */
    private final static int showLevels = 6;

    /** A list with the colors. */
    private List<Integer> colorList;

    IntervalCategoryDataset dataset;

    /** The chart. */
    JFreeChart chart;

    /**
     * Default constructor: initialize the work package calendar inclusive
     * the listeners.
     */
    public APCalendarPanel() {
        init();

    }

    /**
     * Initialize the work package calendar panel inclusive the listeners.
     */
    private void init() {
        List<Workpackage> userWp = new ArrayList<Workpackage>(
            WpManager.getUserWp(WPOverview.getUser()));

        Collections.sort(userWp, new APLevelComparator());

        dataset = createDataset(userWp);
        chart = createChart(dataset);

        final ChartPanel chartPanel = new ChartPanel(chart);

        final JPopupMenu popup = new JPopupMenu();
        JMenuItem miSave = new JMenuItem(LocalizedStrings.getButton().save(
            LocalizedStrings.getWbs().timeLine()));
        miSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                String dbPath = MDBConnect.pathDB;// TODO warum mdb?
                String outfile = dbPath.subSequence(4,
                    dbPath.lastIndexOf("/") + 1)
                    + "chart-" + System.currentTimeMillis() + ".jpg";
                try {
                    ChartUtilities.saveChartAsJPEG(new File(outfile),
                        chart, chartPanel.getWidth(), chartPanel.getWidth());
                    Controller.showMessage(LocalizedStrings.getMessages()
                        .timeLineSaved(outfile));
                } catch (IOException e) {
                    Controller.showError(LocalizedStrings
                        .getErrorMessages().timeLineExportError());
                }
            }

        });
        popup.add(miSave);

        chartPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });
        chartPanel.setMinimumDrawHeight(50 + 15 * userWp.size());
        chartPanel.setMaximumDrawHeight(50 + 15 * userWp.size());
        chartPanel.setMaximumDrawWidth(9999);
        chartPanel.setPreferredSize(new Dimension((int) chartPanel
            .getPreferredSize().getWidth(), 50 + 15 * userWp.size()));

        chartPanel.setPopupMenu(null);

        this.setLayout(new BorderLayout());

        this.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        panel.add(chartPanel, constraints);

        panel.setBackground(Color.white);
        this.add(panel, BorderLayout.CENTER);

        GanttRenderer.setDefaultShadowsVisible(false);
        GanttRenderer.setDefaultBarPainter(new BarPainter() {

            @Override
            public void paintBar(final Graphics2D g,
                final BarRenderer arg1, final int row, final int col,
                final RectangularShape rect, final RectangleEdge arg5) {

                String wpName = (String) dataset.getColumnKey(col);
                int i = 0;
                int spaceCount = 0;
                while (wpName.charAt(i++) == ' ' && spaceCount < 17) {
                    spaceCount++;
                }

                g.setColor(new Color(spaceCount * 15, spaceCount * 15,
                    spaceCount * 15));
                g.fill(rect);
                g.setColor(Color.black);
                g.setStroke(new BasicStroke());
                g.draw(rect);
            }

            @Override
            public void paintBarShadow(final Graphics2D arg0,
                final BarRenderer arg1, final int arg2, final int arg3,
                final RectangularShape arg4, final RectangleEdge arg5,
                final boolean arg6) {

            }

        });

        ((CategoryPlot) chart.getPlot()).setRenderer(new GanttRenderer() {
            private static final long serialVersionUID = -6078915091070733812L;

            public void drawItem(final Graphics2D g2,
                final CategoryItemRendererState state,
                final Rectangle2D dataArea, final CategoryPlot plot,
                final CategoryAxis domainAxis, final ValueAxis rangeAxis,
                final CategoryDataset dataset, final int row,
                final int column, final int pass) {
                super.drawItem(g2, state, dataArea, plot, domainAxis,
                    rangeAxis, dataset, row, column, pass);
            }
        });

    }

    /**
     * Convert work packages to tasks.
     * @param userWp
     *            list with work packages.
     * @return IntervalCategoryDataset: tasks of the work packages.
     */
    public final IntervalCategoryDataset createDataset(
        final List<Workpackage> userWp) {

        final TaskSeries s1 = new TaskSeries(LocalizedStrings
            .getGeneralStrings().overview());
        colorList = new ArrayList<Integer>();
        for (Workpackage actualPackage : userWp) {
            if (actualPackage.getEndDateCalc() != null
                && actualPackage.getStartDateCalc() != null) {
                if (actualPackage.getlastRelevantIndex() <= showLevels) {

                    Date endDateCalc = null;
                    Date start = null;

                    endDateCalc = actualPackage.getEndDateCalc();
                    start = actualPackage.getStartDateCalc();

                    String indent = "";

                    for (int i = 0; i < actualPackage
                        .getlastRelevantIndex(); i++) {
                        indent += "   ";
                    }
                    if (!endDateCalc.before(start)) {
                        Task t = new Task(
                            indent + actualPackage.toString(),
                            new SimpleTimePeriod(start, endDateCalc));
                        t.setPercentComplete(0.01 * WpManager
                            .calcPercentComplete(actualPackage.getBac(),
                                actualPackage.getEtc(),
                                actualPackage.getAc()));
                        s1.add(t);
                        colorList.add(actualPackage.getlastRelevantIndex());
                    }

                }
            }
        }

        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);

        return collection;
    }

    /**
     * Create the JFreeChart.
     * @param dataset
     *            task list for the JFreeChart.
     * @return JFreeChart of tasks.
     */
    private JFreeChart createChart(final IntervalCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart("", "", "",
            dataset, true, false, false);
        chart.getCategoryPlot().getDomainAxis().setCategoryMargin(0.4);
        chart.getCategoryPlot().getDomainAxis().setLowerMargin(0);
        chart.getCategoryPlot().getDomainAxis().setUpperMargin(0);

        chart.getCategoryPlot().getDomainAxis()
            .setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        chart.getCategoryPlot().getDomainAxis()
            .setTickLabelInsets(new RectangleInsets(0, 0, 0, 0));

        chart
            .getCategoryPlot()
            .getDomainAxis()
            .setCategoryLabelPositions(
                new CategoryLabelPositions(new CategoryLabelPosition(
                    RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT,
                    CategoryLabelWidthType.RANGE, 1),
                    new CategoryLabelPosition(RectangleAnchor.LEFT,
                        TextBlockAnchor.CENTER_LEFT,
                        CategoryLabelWidthType.RANGE, 1),
                    new CategoryLabelPosition(RectangleAnchor.LEFT,
                        TextBlockAnchor.CENTER_LEFT,
                        CategoryLabelWidthType.RANGE, 1),
                    new CategoryLabelPosition(RectangleAnchor.LEFT,
                        TextBlockAnchor.CENTER_LEFT,
                        CategoryLabelWidthType.RANGE, 1)));
        return chart;
    }

    /**
     * Reload the panel.
     */
    public final void reload() {
        init();
    }
}
