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
 * Sortieren von AP nach ihrer ID<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 23.08.2012
 */
public class APLevelComparator implements Comparator<Workpackage> {
	/**
	 * Vergleicht zwei Workpackages nach ihren IDs
	 * @param ap1 Workpackage1 das Verglichen werden soll
	 * @param ap2 Workpackage2 das Verglichen werden soll
	 */
	@Override
	public int compare(Workpackage ap1, Workpackage ap2) {
		
		Integer[] ap1IDs = ap1.getLvlIDs();
		Integer[] ap2IDs = ap2.getLvlIDs();
		
		int position = 0;
		int levels = ap1IDs.length;

		while(ap1IDs[position].equals(ap2IDs[position])) {
			position++;
		}
		
		if(position+1 < levels && ap1IDs[position+1]==0 && ap2IDs[position+1]==0) {
			return ap1IDs[position].compareTo(ap2IDs[position]);
		} else {
			return ap1IDs[position].compareTo(ap2IDs[position]);
		}
	
	}

}
