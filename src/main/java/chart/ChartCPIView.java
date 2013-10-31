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
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JFrame;

import jdbcConnection.SQLExecuter;

import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


import wpComparators.APLevelComparator;
import wpShow.WPShowGUI;

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
 * Diese Klassse zeigt einen JFrame mit Diagramm fuer den CPI-Verlauf eines bestimmten Arbeitspakets an<br/>
 * 
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 2012-08-20
 */
public class ChartCPIView extends WBSChart {

	private Workpackage wp;

	/**
	 * Konstruktor
	 * zeigt Projekt-CPIs an
	 * 
	 * @param parent nur zur mittigen Positionierung des JFrames
	 */
	public ChartCPIView(JFrame parent) {
		super(getDBData(), parent);
		createSpecials();
	}

	/**
	 * Konstruktor 
	 * zeigt CPI-Verlauf fuer ein bestimmtes Arbeitspaket an
	 * 
	 * @param wp Arbeitspaket
	 * @param parent nur zur mittigen Positionierung des JFrames
	 */
	public ChartCPIView(Workpackage wp, WPShowGUI parent) {
		super(getDBData(wp), parent);
		this.wp = wp;
		createSpecials();
	}

	/**
	 * Liest Daten fuer das Projekt aus, die zur Anzeige des CPI-Verlaufs benoetigt werden
	 * @return TimeSeriesCollection, anzeigefertig fuer JFreeChart
	 */
	protected static TimeSeriesCollection getDBData() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
	
		List<Workpackage> allWp = new ArrayList<Workpackage>(WpManager.getAllAp());
		Collections.sort(allWp, new APLevelComparator());
		
		boolean foundData = false;
		
		for (Workpackage actualWp : allWp) {
			if (actualWp.equals(WpManager.getRootAp())) {
				dataset.addSeries(getTimeSeries(actualWp));
			} else if (actualWp.getlastRelevantIndex() <= levels) {
				dataset.addSeries(getTimeSeries(actualWp));
				foundData = true;
			}
		}
		if(foundData) {
			return dataset;
		} else {
			return null;
		}
		
	}

	/**
	 * Liest Daten fuer ein Arbeitspaket aus, die zur Anzeige des CPI-Verlaufs benoetigt werden
	 * 
	 * @param wp Arbeitspaket
	 * @return TimeSeriesCollection, anzeigefertig fuer JFreeChart
	 */
	private static TimeSeriesCollection getDBData(Workpackage wp) {
		if(wp.equals(WpManager.getRootAp())) {
			return getDBData();
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		TimeSeries ts = getTimeSeries(wp);
		dataset.addSeries(ts);
		if(ts.isEmpty()) {
			return null;
		} else {
			return dataset;
		}
	}

	/**
	 * Liest die durch getDBData() vorbereiteten Arbeitspaket-Daten aus der Datenbank
	 * @param wp aktuell auszulesendes Arbeitspaket
	 * @return TimeSeries bereits um von getDBData() in eine Collection gemergt werden
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
					actualTs.addOrUpdate(new Day(cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)), rs.getDouble("CPI"));
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

		if(plot != null) {
			plot.addRangeMarker(new ValueMarker(1.0, Color.BLACK, new BasicStroke(3.0f)));
			frame.getChartPanel().getChart().setTitle("CPI");
			
			if(this.wp!=null) {
				Marker actualCPI = new ValueMarker(WpManager.calcCPI(wp.getAc_kosten(), wp.getEtc_kosten(), wp.getBac_kosten()), Color.GREEN, new BasicStroke(1.5f));
				plot.addRangeMarker(actualCPI);
				
			}
		}
		
	}
}
