package chart;

import functions.WpManager;
import globals.Workpackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import jdbcConnection.SQLExecuter;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.Range;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import dbServices.ValuesService;

import wpComparators.APLevelComparator;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * 
 * Diese Klassse zeigt einen JFrame mit Diagramm fuer die prozentuale Fertigstellung eines Arbeitspakets<br/>
 * 
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 2012-08-20
 */
public class ChartCompleteView extends WBSChart {

	private Workpackage wp;

	/**
	 * Konstruktor
	 * 
	 * @param parent fuer mittige Positionierung relativ zu diesem JFrame
	 */
	public ChartCompleteView(JFrame parent) {
		super(getDBData(), parent);
		createSpecials();
	}

	/**
	 * Kontruktor
	 * 
	 * @param wp Arbeitspaket zu dem der Chart angezeigt werden soll
	 * @param parent fuer mittige Positionierung relativ zu diesem JFrame
	 */
	public ChartCompleteView(Workpackage wp, JFrame parent) {
		super(getDBData(wp), parent);
		this.wp = wp;
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
	 * @param wp Arbeitspaket, wenn Root werden alle Daten gelesen
	 * 
	 * @return zum Anzeigen vorbereitete Daten
	 */
	protected static TimeSeriesCollection getDBData(Workpackage wp) {

		TimeSeriesCollection dataset = new TimeSeriesCollection();

		boolean dataFound = false;
		TimeSeries diagonal = new TimeSeries("Soll");
		Calendar minCal = new GregorianCalendar();
		minCal.setTime(new calendar.Day(ValuesService.getPreviousFriday(wp.getStartDateCalc()).getTime()));

		Calendar maxCal = new GregorianCalendar();
		maxCal.setTime(new calendar.Day(ValuesService.getNextFriday(wp.getEndDateCalc()).getTime(), true));
		Map<Date, Double> pvs = ValuesService.getPVs(minCal.getTime(), maxCal.getTime());

		List<Date> orderedDates = new ArrayList<Date>(pvs.keySet());
		Collections.sort(orderedDates);

		for (Date actualDate : orderedDates) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(actualDate);
			diagonal.add(new Day(cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)),
					(ValuesService.getApPv(wp.getStringID(), cal) / ValuesService.getApPv(wp.getStringID(), maxCal)) * 100);
		}

		if (!diagonal.isEmpty()) {
			dataFound = true;
		}
		dataset.addSeries(diagonal);

		if (wp.equals(WpManager.getRootAp())) {
			List<Workpackage> allWp = new ArrayList<Workpackage>(WpManager.getAllAp());
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
	protected static TimeSeries getTimeSeries(Workpackage wp) {
		ResultSet rs = SQLExecuter.executeQuery("SELECT * FROM Analysedaten WHERE LVL1ID = " + wp.getLvlID(1) + " AND LVL2ID = " + wp.getLvlID(2)
				+ " AND LVL3ID = " + wp.getLvlID(3) + " AND LVLxID ='" + wp.getLvlxID() + "'");

		TimeSeries actualTs = new TimeSeries(wp.toString());
		ResultSet rs2 = null;
		GregorianCalendar cal = new GregorianCalendar();

		try {
			while (rs.next()) {
				rs2 = SQLExecuter.executeQuery("SELECT * FROM Baseline WHERE ID = " + rs.getInt("FID_Base") + "");
				if (rs2.next()) {
					cal.setTime(rs2.getDate("Datum"));
					actualTs.addOrUpdate(new Day(cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)),
							WpManager.calcPercentComplete(rs.getDouble("BAC"), rs.getDouble("ETC"), rs.getDouble("AC")));
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				rs.getStatement().close();
				if (rs2 != null) {
					rs2.getStatement().close();
				}
			} catch (SQLException e) {

			}
		}

		return actualTs;
	}

	@Override
	protected void createSpecials() {

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

		plot.addDomainMarker(new ValueMarker(System.currentTimeMillis(), Color.RED, new BasicStroke(1.0f)));

		if (this.wp != null) {
			Marker actualPercent = new ValueMarker(WpManager.calcPercentComplete(wp.getBac(), wp.getEtc(), wp.getAc()), Color.GREEN, new BasicStroke(1.5f));
			// actualPercent.setLabel("aktueller Fortschritt");
			plot.addRangeMarker(actualPercent);
		}

		frame.getChartPanel().getChart().setTitle("Fortschritt (PV)");
	}
}
