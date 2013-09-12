package globals;

import javax.swing.JTextField;
import javax.swing.text.Document;
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
 * Filtert Eingaben des Benutzers, dass keine Hochkommas und Anfuehrungszeichen in die DB gelangen koennen
 * 
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 2012-08-20
 */
public class FilterJTextField extends JTextField {
	
	private static final long serialVersionUID = -9182103248287537816L;
	
	public FilterJTextField() {
		super();
	}

	public FilterJTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
	}

	public FilterJTextField(int columns) {
		super(columns);
	}

	public FilterJTextField(String text, int columns) {
		super(text, columns);
	}

	public FilterJTextField(String text) {
		super(text);
	}

	public String getText() {
		return filterText(super.getText());
	}
	
	public static String filterText(String text) {
		String returnText = text.replace("'", "");
		returnText = returnText.replace("\"", "");
		return returnText;
	}

	
}
