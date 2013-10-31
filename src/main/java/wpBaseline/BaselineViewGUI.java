package wpBaseline;

import globals.Controller;

import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
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
 * GUI mit Tabellenstruktur in der die Baselines angezeigt werden<br/>
 * 
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 20.08.2012
 */
public class BaselineViewGUI extends JFrame {
	private static final long serialVersionUID = -2739030780541939599L;
	private JTable table;
	/**
	 * Konstruktor
	 * @param parent ParentFrame
	 */
	public BaselineViewGUI(JFrame parent) {		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, "", null, "", null, null, null, null, null, null},
			},
			new String[] {
				"Arbeitspaket", "BAC", "AC", "ETC", "CPI", "BAC Kosten", "AC Kosten", "ETC Kosten", "EAC", "EV", "Trend", "PV", "SV", "SPI", "Status"
			}
		) {
			private static final long serialVersionUID = 5624811268311876732L;
			@SuppressWarnings("rawtypes") //Auto-Generierter Code von Eclipse
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Object.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" }) //Auto-Generierter Code von Eclipse
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(283);
		table.getColumnModel().getColumn(1).setPreferredWidth(45);
		table.getColumnModel().getColumn(2).setPreferredWidth(45);
		table.getColumnModel().getColumn(3).setPreferredWidth(45);
		table.getColumnModel().getColumn(4).setPreferredWidth(35);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(10).setPreferredWidth(35);
		table.getColumnModel().getColumn(11).setPreferredWidth(100);
		table.getColumnModel().getColumn(12).setPreferredWidth(100);
		
		table.getColumnModel().getColumn(0).setCellRenderer(new ValueCellRenderer(false, false));
		for(int i = 1; i<14; i++) {
			if(i == 4 || i == 13) {
				table.getColumnModel().getColumn(i).setCellRenderer(new ValueCellRenderer(true, true));
			} else {
				table.getColumnModel().getColumn(i).setCellRenderer(new ValueCellRenderer(true, false));
			}
		}
		table.getColumnModel().getColumn(14).setCellRenderer(new StatusCellRenderer()); //letze Spalte	
		

		getContentPane().add(table, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.removeRow(0);
		this.setSize(1100,750);
		this.setVisible(true);
		Controller.centerComponent(parent, this);
	}

	/**
	 * Fuegt eine Reihe in die Tabelle hinzu
	 * @param rowData Daten die in die Tabelle sollen
	 */
	protected void addRow(String[] rowData) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(rowData);
	}
}
