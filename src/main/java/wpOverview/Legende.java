package wpOverview;

import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Studienprojekt:	WBS
 *
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz,
 * 						Peter Lange,
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 *
 * Legende in der Packetuebersicht um die Einteilung der farblichen Aufteilung des CPI zu erklaeren
 *
 * @author Samson von Graevenitz
 * @version 0.3- .12.2010
 */

public class Legende extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel text;
	private Canvas color;

	/**
	 * Default-Konstructor:
	 * Erzeugt ein JPanel mit den verschiedenen CPI-Farben und deren Zahlenwerte
	 */
	public Legende(){
		super();

		setLayout(new FlowLayout());

		add(new JLabel(LocalizedStrings.getWbs().cpiColors() + ":"));
		text = new JLabel();
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setOpaque(true);

		add(new JLabel());

		color = new Canvas();
		color.setBackground(new Color(00, 80, 00));
		color.setSize(20, 20);
		add(color);
		text.setText("1.03 >");
		add(text);

		color = new Canvas();
		color.setBackground(Color.GREEN);
		color.setSize(20, 20);
		add(color);
		text = new JLabel("1.03 - 0.97");
		add(text);

		color = new Canvas();
		color.setBackground(Color.YELLOW);
		color.setSize(20, 20);
		add(color);
		text = new JLabel("0.97 - 0.94");
		add(text);

		color = new Canvas();
		color.setBackground(Color.RED);
		color.setSize(20, 20);
		add(color);
		text = new JLabel("0.94 - 0.6");
		add(text);

		color = new Canvas();
		color.setBackground(new Color(80, 00, 00));
		color.setSize(20, 20);
		add(color);
		text = new JLabel("< 0.6");
		add(text);
	}
	/**
	 * Umsetzen der Sichtbarkeit der Legende (Legende erscheint in den Paketuebersichten, aber nicht in Mitarbeiterauflistung und Baselineübersicht)
	 * @param state - true:sichtbar, false:nicht sichtbar
	 */
	public void setVisible(boolean state){
		super.setVisible(state);
	}

}
