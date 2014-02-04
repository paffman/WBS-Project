package chart;

import globals.Controller;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import de.fhbingen.wbs.translation.LocalizedStrings;

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
 * Diese Klasse wird als Superklasse zum Anzeigen der Graphen benutzt.<br/>
 * 
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 2012-08-21
 */
public abstract class WBSChart {

	public static int levels = 1;

	protected ChartFrame frame;
	protected XYPlot plot;

	/**
	 * Konstruktor 
	 * 
	 * 
	 * @param dataSet vorbereitete Daten
	 * @param parent zur mittigen Positinierung auf dem Bildschirm
	 */
	public WBSChart(TimeSeriesCollection dataSet, JFrame parent) {
		if (dataSet == null || dataSet.getSeries().isEmpty()) {
			Controller.showMessage("Es sind zuwenig Daten vorhanden oder die Laufzeit des AP ist nicht lang genug");

		} else {
			JFreeChart chart = createChart(dataSet);
			frame = new ChartFrame(LocalizedStrings.getChart().diagramView(), chart);
			frame.setSize(new Dimension(1024, 700));
			frame.setResizable(true);
			frame.getChartPanel().setPopupMenu(null);
			frame.getChartPanel().setRangeZoomable(false);
			Controller.centerComponent(parent, frame);
			frame.setVisible(true);
		}

	}

	/**
	 * Erzeugt aus den Daten den Chart (also mit Farben, Linienstaerke etc)
	 * @param dataset
	 * @return
	 */
	protected JFreeChart createChart(TimeSeriesCollection dataset) {
		JFreeChart chart = ChartFactory.createTimeSeriesChart("", "Datum", "", dataset, true, true, false);

		chart.setBackgroundPaint(Color.white);
		plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		chart.setBackgroundPaint(Color.white);

		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
		}

		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(Controller.DATE_DAY);

		return chart;
	}

	/**
	 * Wird von Unterklassen ueberschrieben um spezifische Anzeigefunktionen zu ermoeglichen
	 */
	protected abstract void createSpecials();
}
