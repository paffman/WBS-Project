package wpBaseline;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import wpOverview.WPOverview;
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
 * Legt die Attribute der Value Zellen fest<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 20.08.2012
 */
public class ValueCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	private boolean right;
	private boolean colored;

	public ValueCellRenderer(boolean right, boolean colored) {
		this.right = right;
		this.colored = colored;
	}

	public Component getTableCellRendererComponent(JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
		Component renderer = super.getTableCellRendererComponent(aTable, value, aIsSelected, aHasFocus, aRow, aColumn);

		if (renderer instanceof JLabel) {
			JLabel l = (JLabel) renderer;
			if (right) {
				l.setHorizontalAlignment(JLabel.RIGHT);
			} else {
				l.setHorizontalAlignment(JLabel.LEFT);
			}

		}

		if (!colored) {
			if (aTable.getValueAt(aRow, 0).toString().substring(2, 3).equals(" ")) {
				renderer.setBackground(Color.white);
				renderer.setForeground(Color.black);
			} else if (aTable.getValueAt(aRow, 0).toString().substring(1, 2).equals(" ")) {
				renderer.setBackground(new Color(230, 230, 230));
				renderer.setForeground(Color.black);
			} else if (aTable.getValueAt(aRow, 0).toString().substring(0, 1).equals(" ")) {
				renderer.setBackground(Color.LIGHT_GRAY);
				renderer.setForeground(Color.black);
			} else {
				renderer.setBackground(Color.DARK_GRAY);
				renderer.setForeground(Color.white);
			}

			if (aIsSelected) {
				renderer.setBackground(Color.black);
				renderer.setForeground(Color.white);
			}

		} else {
			double doubleValue = Double.parseDouble(value.toString().replace(",", "."));
			renderer.setForeground(WPOverview.getSPIColor(doubleValue, 1)[0]);
			renderer.setBackground(WPOverview.getSPIColor(doubleValue, 1)[1]);
			if (renderer instanceof JLabel) {
				JLabel l = (JLabel) renderer;
				l.setHorizontalAlignment(JLabel.RIGHT);
			}
		}

		return this;
	}
}