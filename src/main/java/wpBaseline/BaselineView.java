package wpBaseline;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;




import functions.WpManager;
import globals.Controller;
import globals.Workpackage;

import jdbcConnection.SQLExecuter;
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
 * Funktionalitaet der BaselineViewGUI<br/>
 * 
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 20.08.2012
 */
public class BaselineView {
	/**
	 * Konstruktor
	 * @param baselineID ID der gewuenschten Baseline
	 * @param parent ParentFrame
	 */
	public BaselineView(int baselineID, JFrame parent) {
		try {
			String[][] data = getData(baselineID).toArray(new String[1][1]);
			BaselineViewGUI gui = new BaselineViewGUI(parent);
			for(String[] actualRow : data) {
				gui.addRow(actualRow);
			};
			
			
		} catch (SQLException e) {
			Controller.showError("Problem beim lesen der Baseline");
			e.printStackTrace();
		}
	}
/**
 * Fuellt die BaselineViewGUI mit Daten aus der DB
 * @param baselineID ID der gewuenschten Baseline
 * @return Liste mit StringArrays der Daten
 * @throws SQLException
 */
	private List<String[]> getData(int baselineID) throws SQLException {
		ResultSet rs = SQLExecuter.executeQuery("SELECT * FROM Analysedaten WHERE FID_Base = " + baselineID + " ORDER BY LVL1ID ASC, LVL2ID ASC, LVL3ID ASC, LVLxID ASC;");
		List<String[]> allData = new ArrayList<String[]>();
		while (rs.next()) {
		
			Workpackage actualWp = WpManager.getWorkpackage(rs.getString("LVL1ID") + "." + rs.getString("LVL2ID") + "." + rs.getString("LVL3ID") + "."
					+ rs.getString("LVLxID"));
			if(actualWp.getlastRelevantIndex() <= 3) {
				String[] actualData = new String[15];
				int i = 0;
				String spacer = "";
				for(int j = 0; j<actualWp.getlastRelevantIndex(); j++) {
					spacer += " ";
				}
				actualData[i++] = spacer + actualWp.toString();
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("BAC"));
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("AC"));
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("ETC"));
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("CPI"));
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("BAC_Kosten"))+" EUR";
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("AC_Kosten"))+" EUR";
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("ETC_Kosten"))+" EUR";
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("EAC"))+" EUR";
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("EV"))+" EUR";
				actualData[i++] = Controller.DECFORM.format(WpManager.calcTrend(rs.getDouble("EV"), rs.getDouble("AC_Kosten")));
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("PV"))+" EUR";
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("SV"))+" EUR";
				actualData[i++] = Controller.DECFORM.format(rs.getDouble("SPI"));
				actualData[i++] = ""+WpManager.calcPercentComplete(rs.getDouble("BAC"), rs.getDouble("ETC"), rs.getDouble("AC"));
				allData.add(actualData);
			}
			
		}
		rs.getStatement().close();
		return allData;
	}
}
