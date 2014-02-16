package de.fhbingen.wbs.exception;

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
 * Dies Klasse dient als Exception, die von SemaphorService.java ausgeloest werden kann.<br/>
 * Dies geschieht wenn der Leiter Semaphor nicht belegt werden konnte oder es dabei Verbindungsprobleme gab.<br/>
 *
 * @author Jens Eckes
 * @version 2.0 - 2012-08-20
 */
public class SemaphorException extends Exception {

	private static final long serialVersionUID = -3369450901975408345L;

	public SemaphorException() {
		super();
	}

	public SemaphorException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SemaphorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SemaphorException(String arg0) {
		super(arg0);
	}

	public SemaphorException(Throwable arg0) {
		super(arg0);
	}

}
