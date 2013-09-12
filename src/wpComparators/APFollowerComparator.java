package wpComparators;

import java.util.Comparator;

import functions.WpManager;
import globals.Workpackage;

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
 * Sortieren von AP nach Anzahl ihrer Nachfolger<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 23.08.2012
 */
public class APFollowerComparator implements Comparator<Workpackage> {
	/**
	 * Vergleicht zwei Workpackages nach ihrer Anzahl an Nachfolgern
	 * @param ap1 Workpackage1 das Verglichen werden soll
	 * @param ap2 Workpackage2 das Verglichen werden soll
	 */
	@Override
	public int compare(Workpackage ap1, Workpackage ap2) {
		if (WpManager.getFollowers(ap1) != null && WpManager.getFollowers(ap2) != null) {
			return WpManager.getFollowers(ap1).size() - WpManager.getFollowers(ap2).size();
		} else if (WpManager.getFollowers(ap1) == null && WpManager.getFollowers(ap2) == null) {
			return 0;
		} else if (WpManager.getFollowers(ap1) == null) {
			return 1;
		} else {
			return -1;
		}

	}

}
