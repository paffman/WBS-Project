package wpComparators;

import globals.Workpackage;

import java.util.Comparator;

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
 * Sortieren von AP nach der Hoehe des BAC<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 23.08.2012
 */
public class APBacComparator implements Comparator<Workpackage> {
	/**
	 * Vergleicht zwei Workpackages nach ihrem BAC
	 * @param ap1 Workpackage1 das Verglichen werden soll
	 * @param ap2 Workpackage2 das Verglichen werden soll
	 */
	@Override
	public int compare(Workpackage wp1, Workpackage wp2) {
		return wp1.getBac().compareTo(wp2.getBac());
	}

}
