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
 * Sortieren von AP nach Ihrem Startdatum<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 23.08.2012
 */
public class APDateComparator implements Comparator<Workpackage> {
	/**
	 * Vergleicht zwei Workpackages nach ihrem Startdatum
	 * @param ap1 Workpackage1 das Verglichen werden soll
	 * @param ap2 Workpackage2 das Verglichen werden soll
	 */
	@Override
	public int compare(Workpackage ap1, Workpackage ap2) {

		Integer[] ap1IDs = ap1.getLvlIDs();
		Integer[] ap2IDs = ap2.getLvlIDs();

		int position = 0;
		int levels = ap1IDs.length;

		while (ap1IDs[position].equals(ap2IDs[position])) {
			position++;
		}

		if (position + 1 < levels && ap1IDs[position + 1] == 0 && ap2IDs[position + 1] == 0) {
			return startDateCompare(ap1, ap2);
		} else {
			return 0;
		}

	}
	/**
	 * Vergleicht zwei Workpackages nach ihrem Startdatum
	 * @param ap1 Workpackage1 das Verglichen werden soll
	 * @param ap2 Workpackage2 das Verglichen werden soll
	 * @return
	 */
	private int startDateCompare(Workpackage ap1, Workpackage ap2) {
		if (ap1.getStartDateCalc() == null) {
			return 0;
		} else if (ap2.getStartDateCalc() == null) {
			return 0;
		} else {
			if (ap1.getStartDateCalc().before(ap2.getStartDateCalc())) {
				return -1;
			} else if (ap1.getStartDateCalc().after(ap2.getStartDateCalc())) {
				return 1;
			} else {
				return 0;
			}
		}

	}

}
