package wpOverview.tabs;

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

import calendar.Availability;
import calendar.Day;
import dbServices.CalendarService;

import wpAvailability.EditAvailability;
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
 * ActionListener der AvailabilityGraphGUI Klasse<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0
 */

public class AvailabilityGraphAction {

	private AvailabilityGraphGUI gui;
	private JFrame parent;
	/**
	 * 
	 * @param gui GUI Klasse des AvailabilityGraph
	 * @param parent ParentFrame
	 */
	public AvailabilityGraphAction(final AvailabilityGraphGUI gui, JFrame parent) {
		this.gui = gui;
		this.parent = parent;
		setAction();

	}
	/**
	 * Fuegt ActionListener zum Graph hinzu
	 */
	private void setAction() {
		gui.pnlGraph.addChartMouseListener(new ChartMouseListener() {

			@Override
			public void chartMouseClicked(ChartMouseEvent e) {
				if (e.getEntity() instanceof PlotEntity) {
					Point2D p = gui.pnlGraph.translateScreenToJava2D(e.getTrigger().getPoint());
					CategoryPlot plot = (CategoryPlot) gui.pnlGraph.getChart().getPlot();
					Rectangle2D plotArea = gui.pnlGraph.getScreenDataArea();

					DateAxis rangeAxis = (DateAxis) plot.getRangeAxis();
					RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();

					CategoryAxis catAxis = plot.getDomainAxis();
					RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();

					double chartY = rangeAxis.java2DToValue(p.getX(), plotArea, rangeAxisEdge);

					CategoryDataset categories = (CategoryDataset) plot.getDataset(0);

					int categoryCount = categories.getColumnCount();

					for (int i = 0; i < categoryCount; i++) {
						double catStart = catAxis.getCategoryStart(i, categoryCount, plotArea, domainAxisEdge);
						double catEnd = catAxis.getCategoryEnd(i, categoryCount, plotArea, domainAxisEdge);

						if (e.getTrigger().getY() >= catStart && e.getTrigger().getY() < catEnd) {
							new EditAvailability(gui.function, gui.function.getWorkers().get(i), new Day(new Date((long) chartY)), parent);
						}

					}
				} else {

					CategoryItemEntity item = (CategoryItemEntity) e.getEntity();
					CategoryPlot plot = (CategoryPlot) gui.pnlGraph.getChart().getPlot();
					Rectangle2D plotArea = gui.pnlGraph.getScreenDataArea();
					RectangleEdge rangeAxisEdge = plot.getRangeAxisEdge();

					DateAxis dateAxis = (DateAxis) plot.getRangeAxis();

					double d = dateAxis.java2DToValue(e.getEntity().getArea().getBounds2D().getX(), plotArea, rangeAxisEdge);
					double d2 = dateAxis.java2DToValue(e.getEntity().getArea().getBounds2D().getX() + e.getEntity().getArea().getBounds2D().getWidth(),
							plotArea, rangeAxisEdge);

					Date startDate = new Date((long) d);
					Date endDate = new Date((long) d2);

					CategoryDataset categories = (CategoryDataset) plot.getDataset(0);
					int workerIndex = categories.getColumnIndex(item.getColumnKey());
					Worker worker = gui.function.getWorkers().get(workerIndex);

					Set<Availability> found = CalendarService.getAllWorkerAvailability(worker.getLogin(), startDate, endDate);
					Availability foundAv = found.toArray(new Availability[1])[0];

					if (foundAv != null) {
						new EditAvailability(gui.function, foundAv, parent);
					} else {
						found = CalendarService.getProjectAvailability(startDate, endDate);
						foundAv = found.toArray(new Availability[1])[0];
						if (foundAv != null) {
							new EditAvailability(gui.function, foundAv, parent);
						} else {
							JOptionPane.showMessageDialog(new JFrame("Warnung"),
									"Diese Verfügbarkeit kann nicht bearbeitet werden, da automatisch gesetzt. Bitte verwenden Sie manuelle Verfügbarkeiten!");
						}
					}
				}

			}

			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
				
			}

		});
		/**
		 * ActionListener
		 */
		gui.btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gui.function.increment();
			}

		});
		/**
		 * ActionListener
		 */
		gui.btnPrev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					gui.function.setManualAv(true);
				} else {
					gui.function.setManualAv(false);
				}
			}

		});

	}
	/**
	 * ActionListener
	 */
	private void addButtonListener(final int buttonId) {
		gui.buttons[buttonId].addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					deselectOthers(buttonId);
					gui.function.changeView(buttonId);
				}
			}

		});
	}
	/**
	 * Unselectiert alle ausser der Uebergebenen
	 * @param buttonId welcher nicht unselectiert werden soll
	 */
	private void deselectOthers(int buttonId) {
		for (int i = 0; i < gui.buttons.length; i++) {
			if (i != buttonId) {
				gui.buttons[i].setSelected(false);
			}
		}
	}

}
