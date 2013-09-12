/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Beinhaltet Darstellung und Funktion des CPI Diagrammes
 * 
 * 
 * @author Peter Lange
 * @version 1.0 - 15.02.2011
 */

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
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

public class ChartFunctionCPI{
	
	//Variable um die Zeichnung von Werte im Diagramm zu verhindern (falls zu wenig Daten vorhanden sind)
	boolean noLines=false;
	ChartFrame frame;
	
	/**
	 * Konstruktor fuer CPI Diagramm
	 * 
	 * wird aufgerufen von:
	 * - ToolBar
	 * - WPOverviewButtonAction
	 */
	public ChartFunctionCPI(){
		
		TimeSeriesCollection dataset=getDBData();
		JFreeChart chart=createChart(dataset);
		
		if(!noLines){	
			
			frame = new ChartFrame("CPI",chart);
			int width = 1024;
			int height = 700;
			Toolkit tk=Toolkit.getDefaultToolkit();
			Dimension screenSize=tk.getScreenSize();
			frame.setLocation((int)(screenSize.getWidth()/2)-width/2,(int)(screenSize.getHeight()/2)-height/2);
			frame.setResizable(true);
			frame.setVisible(true);
			frame.setSize(new Dimension(width, height));
			//zoom deaktivieren
			frame.getChartPanel().setDomainZoomable(false);
			frame.getChartPanel().setRangeZoomable(false);
			//deaktivieren des erweiteren Menues 
			frame.getChartPanel().setPopupMenu(null);
			//Listener um den Zoom des Diagrammes einzugrenzen
		}
	}
	
	
	/**
	 * 
	 * @param dataset - Set mit den darzustellenden Daten
	 * @return - JFreeChart - das darzustellende Diagramm
	 *
	 *	 */
	
	private JFreeChart createChart(XYDataset dataset) {
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
			"CPI-Vergleich", // title
			"Datum", // x-axis label
			"CPI", // y-axis label
			dataset, // data
			true, // create legend?
			true, // generate tooltips?
			false // generate URLs?
		);
	

		chart.setBackgroundPaint(Color.white);
		XYPlot plot=chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		
				
		//Liniendicke für Projekt-CPI erhoehen
		plot.getRenderer().setSeriesStroke(0,new BasicStroke(5.0f));
		//Markierung bei einem CPI von 1.0
		plot.addRangeMarker(new ValueMarker(1.0,Color.BLACK,new BasicStroke(1.0f)));
		
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
	 * TimeSeriesCollection getDBData()
	 * Liest alle Werte aus der DB zur Anzeige(Datum, CPI für Gesamtprojekt und Oberpakete)
	 * 
	 * @return TimesSeriesCollection - Sammlung aller aus der DB ausgelesenen Werte
	 */
 	private TimeSeriesCollection getDBData(){
	
		
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
						//packetname wird zugeteilt
						ts[i]=new TimeSeries("CPI-"+name);
						gesamtID.removeFirst();
					}else{
						//packetId wird zugeteilt, falls kein Name vorhanden
						ts[i]=new TimeSeries("CPI-"+gesamtID.removeFirst());
					}
					
				}else{
					//else noetig falls es luecken zwischen den IDs der Oberpacketen gibt. Serien werden angelegt, aber da keine Werte
					//vorhanden sind, aber später nicht angezeigt
					ts[i]=new TimeSeries("CPI"+i);
				}
			}
			
			SQLExecuter sqlCPIs = new SQLExecuter();
			SQLExecuter sqlDate = new SQLExecuter();
			ResultSet rsCPI = sqlCPIs.executeQuery("SELECT LVL1ID,CPI,FID_Base FROM Analysedaten Where LVL2ID=0 AND LVL3ID=0");
			try {
					
				while(rsCPI.next()){
				
					ResultSet rsBase = sqlDate.executeQuery("SELECT Datum FROM Baseline Where ID="+rsCPI.getInt("FID_Base")+";");
					rsBase.next();
					Date ds=rsBase.getDate(1);
					Calendar c=Calendar.getInstance();
					c.setTime(ds);
					ts[rsCPI.getInt("LVL1ID")].addOrUpdate(new Day(c.get(Calendar.DATE),c.get(Calendar.MONTH)+1,c.get(Calendar.YEAR)),rsCPI.getDouble("CPI"));
					rsBase.close();
					sqlDate.closeConnection();
						
				}
				rsCPI.close();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				sqlCPIs.closeConnection();
			}
				
			TimeSeriesCollection dataset=new TimeSeriesCollection();
			for(TimeSeries tseries: ts){
				if(!tseries.equals(null))
					dataset.addSeries(tseries);
			}
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
 	public String getName(String id){
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


}
