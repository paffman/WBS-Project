
/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Beinhaltet Darstellung und Funktion des Comp(Fertigstellungs) Diagrammes
 * 
 * 
 * @author Peter Lange
 * @version 1.0 - 15.02.2011
**/
package chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import jdbcConnection.SQLExecuter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;


public class ChartFunctionComp {
	
	//Variable um die Zeichnung von Werte im Diagramm zu verhindern (falls zu wenig Daten vorhanden sind)
	boolean noLines=false;
	//Variable um die Zeichnung einer Diagonalen im Diagramm zu verhindern 
	boolean diagonale=true;
	ChartFrame frame;
	//Variable fuer die Anzahl der ErgebnisSerien (Linien) im Diagramm
	int series=0;

	

	/**
	 *  Konstruktor fuer Fertigstellungsdiagramm
	 *  
	 *  wird aufgerufen von:
	 * - WPOverviewButtonAction
	 */
	public ChartFunctionComp(){
		
		TimeSeriesCollection dataset=getDBData();
	
		if(!noLines){	
			JFreeChart chart=createChart(dataset);
			frame = new ChartFrame("Fertigstellung",chart);
			int width = 1024;
			int height = 700;
			Toolkit tk=Toolkit.getDefaultToolkit();
			Dimension screenSize=tk.getScreenSize();
			frame.setLocation((int)(screenSize.getWidth()/2)-width/2,(int)(screenSize.getHeight()/2)-height/2);
			frame.setResizable(true);
			frame.setVisible(true);
			frame.setSize(new Dimension(width, height));
			//deaktivieren des erweiteren Menues 
			frame.getChartPanel().setPopupMenu(null);
			//Zoom deaktivieren
			frame.getChartPanel().setDomainZoomable(false);
			frame.getChartPanel().setRangeZoomable(false);

		}
	}

	/**
	 * Erzeugt das Chart
	 * @param XYDataset - Set mit den darzustellenden Daten
	 * @return - JFreeChart - das darzustellenden Diagramm
	 */	
	private JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
			"Fortschritt", // title
			"Datum", // x-axis label
			"Prozent", // y-axis label
			dataset, // data
			true, // create legend?
			true, // generate tooltips?
			false // generate URLs?
		);
	

		chart.setBackgroundPaint(Color.white);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
		//entscheidung ob Diagonale gezeichnet wird
		if(!noLines&&diagonale){
		
			plot.getRenderer().setSeriesStroke(
					(series > 0) ? series-1 : 1, 
			    new BasicStroke(
			        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
			        1.0f, new float[] {6.0f, 6.0f}, 0.0f
			    ));
		}
		//Festlegen der Y- Achse auf werte zwischen 0 und 101%
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setRange(new Range(0, 101));
	
		TickUnits units = new TickUnits();
		units.add(new NumberTickUnit(10));
		plot.getRangeAxis().setStandardTickUnits(units);
			
		final DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
		dateAxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());
		dateAxis.setMaximumDate(getMaxDate());
		
		
		chart.setBackgroundPaint(Color.white);
				
		

				
		//Liniendicke für Projekt-Fortschritt erhöhen.
		plot.getRenderer().setSeriesStroke(0,new BasicStroke(5.0f));
		if(diagonale)		
			plot.getRenderer().setSeriesPaint(series-1,Color.BLACK);
			
		
		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
	
		}
		
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy"));
		return chart;
	}
	/**
	 * Liest alle Werte aus der DB zur Anzeige(Datum, CPI für Gesamtprojekt und Oberpakete)
	 * 
	 * @return TimesSeriesCollection - Sammlung aller aus der DB ausgelesenen Werte
	 */
 	private TimeSeriesCollection getDBData(){
 		Date maxDate=null;
 		Date minDate=null;
		
 		int anzLVL1EB=0;
 		SQLExecuter sqlIDs = new SQLExecuter();
				
		LinkedList<String> gesamtID=new LinkedList<String>();
		LinkedList<Integer> idLVL1=new LinkedList<Integer>();
		//ablaufen und speichern der LVL1ID und gesamtIDs der Pakete
		ResultSet rsgesamtID = sqlIDs.executeQuery("SELECT LVL1ID, LVL2ID, LVL3ID, LVLxID FROM Analysedaten Where LVL2ID=0 AND LVL3ID=0");
		try {
				while(rsgesamtID.next()){
					int num=rsgesamtID.getInt("LVL1ID");
					String na=String.valueOf(num+"."+rsgesamtID.getInt("LVL2ID")+"."+rsgesamtID.getInt("LVL3ID"))+"."+rsgesamtID.getString("LVLxID");
				
					if(!gesamtID.contains(na))
						gesamtID.add(na);
					if(!idLVL1.contains(num))
						idLVL1.add(num);				

			}
			rsgesamtID.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			sqlIDs.closeConnection();
		}
		
		//wenn Pakete vorhanden sind darstellen, ansonsten fehlermeldung
		if(idLVL1.size()>0){
			
			anzLVL1EB=1+idLVL1.get(idLVL1.size()-1);
			
			//Speicherarray für die einzelnen CPI Linien
			TimeSeries[] ts=new TimeSeries[anzLVL1EB];
			
			for(int i=0;i<anzLVL1EB;i++){
				if(idLVL1.contains(i)){
					String name=getName(gesamtID.getFirst());
					if(name!=null){
						//paketname wird zugeteilt
						ts[i]=new TimeSeries(name);
						gesamtID.removeFirst();
					}else{
						//paketId wird zugeteilt, falls kein Name vorhanden
						ts[i]=new TimeSeries(gesamtID.removeFirst());
					}
					
				}else{
					//else nötig falls es Lücken zwischen den IDs der Oberpaketen gibt. Serien werden angelegt, aber da keine Werte
					//vorhanden sind werden sie später nicht angezeigt
					//ts[i]=new TimeSeries(i);
                    ts[i]=new TimeSeries(Integer.toString(i));

				}
			}
					
			SQLExecuter sqlCPIs = new SQLExecuter();
			SQLExecuter sqlDate = new SQLExecuter();
			//auslesen des Datums
			ResultSet rsComp = sqlCPIs.executeQuery("SELECT LVL1ID,AC,ETC,BAC,FID_Base FROM Analysedaten Where LVL2ID=0 AND LVL3ID=0");
			try {
					
				while(rsComp.next()){
				
					ResultSet rsBase = sqlDate.executeQuery("SELECT Datum FROM Baseline Where ID="+rsComp.getInt("FID_Base")+";");
					rsBase.next();
					Date ds=rsBase.getDate(1);
					if(maxDate==null||ds.after(maxDate)){
						maxDate=ds;
					}
					if(minDate==null||ds.before(minDate)){
						minDate=ds;
					}
					Calendar c=Calendar.getInstance();
					c.setTime(ds);
					ts[rsComp.getInt("LVL1ID")].addOrUpdate(new Day(c.get(Calendar.DATE),c.get(Calendar.MONTH)+1,c.get(Calendar.YEAR)),
							berechneComp(rsComp.getDouble("BAC"),rsComp.getDouble("ETC"),rsComp.getDouble("AC")));
					rsBase.close();
					sqlDate.closeConnection();
						
				}
				rsComp.close();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				sqlCPIs.closeConnection();
			}
				
			TimeSeriesCollection dataset=new TimeSeriesCollection();
			TimeSeries diagonal=new TimeSeries("Soll");
			
	
			for(TimeSeries tseries: ts){
				if(!tseries.equals(null))
					dataset.addSeries(tseries);
			}
			
			if(!maxDate.equals(minDate)){
				Calendar c1=Calendar.getInstance();
				c1.setTime(getMaxDate());
				Calendar c2=Calendar.getInstance();
				c2.setTime(minDate);
				
				diagonal.add(new Day(c2.get(Calendar.DATE),c2.get(Calendar.MONTH)+1,c2.get(Calendar.YEAR)),0);
				diagonal.add(new Day(c1.get(Calendar.DATE),c1.get(Calendar.MONTH)+1,c1.get(Calendar.YEAR)),100);
				dataset.addSeries(diagonal);
				diagonale=true;
				
			}
			series=dataset.getSeries().size();
			return dataset;
		}else{
			JOptionPane.showMessageDialog(null,"Programm hat keine Baselinedaten. Bitte eine anlegen.","Keine Baseline",
                    JOptionPane.ERROR_MESSAGE);
			noLines=true;
			return null;
		}
		
	}
 	/**
 	 * Methode zur Rueckgabe einem Paketnamens, die zur eingegebenen ID passt
 	 * @param id - Arbeitspaket ID als String
 	 * @return String - Aktueller Name des Paketes
 	 */
 	private String getName(String id){
 		String[] tmpId = id.split("\\.");
		String lvlx = "";
			
		for(int i=3;i<tmpId.length;i++){
			if(lvlx.equals(""))
				lvlx = tmpId[i];
			else
				lvlx += "." + tmpId[i];
		}
			String name="";
		SQLExecuter sqlExecProjekt = new SQLExecuter();
		ResultSet rsName = sqlExecProjekt.executeQuery("SELECT Name FROM Analysedaten Where LVL1ID="+tmpId[0]+" AND LVL2ID="+tmpId[1]+" AND LVL3ID="+tmpId[2]+"AND LVLxID='"+lvlx+"';");
		try {
			while(rsName.next()){
				name=rsName.getString("Name");			
			}
			rsName.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			sqlExecProjekt.closeConnection();
		}
		if(name.equals(""))
			return null;
		return name;
 		
 	}
 	/**
 	 * Methode um herauszufinden was das maximale Datum im Projekt ist. Entweder wird das ReleaseDatum des 
 	 * letzten Pakets gewählt oder das Datum der letzten Base falls dieses Höher ist.
 	 * 
 	 * @return Date - Aktuelle höchstens Datum
 	 *  	 * 
 	 * wird aufgerufen in:
 	 * createChart(XYDataset)
 	 * getDBData()
 	 * 
 	 */ 	
 	private Date getMaxDate(){
 		Date max=null;
 		Date tempMax=null;
 		SQLExecuter sqlDateMax = new SQLExecuter();
		ResultSet rsDate = sqlDateMax.executeQuery("SELECT Max(Release) FROM Arbeitspaket");
		try{
				rsDate.next();
				max=rsDate.getDate(1);			
				rsDate.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			sqlDateMax.closeConnection();
		}
		
		SQLExecuter sqlDateBase = new SQLExecuter();
		rsDate = sqlDateBase.executeQuery("SELECT Max(Datum) FROM Baseline");
		try{
			
				rsDate.next();
				tempMax=rsDate.getDate(1);			
				rsDate.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			sqlDateBase.closeConnection();
		}
		//System.out.println(max+" "+tempMax);
		if(tempMax.after(max)){
			return tempMax;
		}
			
		return max;
 	}
 	
 	/**
 	 * Methode zum Bereichnen der Kompletierung(Fertigstellung) in %
 	 * @param BAC - Int BAC
 	 * @param ETC - Int ETC
 	 * @param AC - Int AC
 	 * @return int - Prozent Wert für die Fertigstellung eines Paketes
 	 */
 	private int berechneComp(double BAC, double ETC, double AC){
 		int stat;
 		if(BAC>0 && ETC==0)
			stat = 100;
		else{
			if(ETC>0 && AC>0){
				stat= (int) (AC*100 /(AC+ETC));
		    }
		    else{
		    	stat= 0;
		    } 
		}	
 		return stat;
 	}
 	
	

}
