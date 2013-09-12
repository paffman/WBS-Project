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
 * Sortieren von AP nach dem Enddatum<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 23.08.2012
 */
public class APEndDateComparator implements Comparator<Workpackage>  {
	/**
	 * Vergleicht zwei Workpackpackages nach ihrem Enddatum
	 * @param ap1 Workpackage1 das Verglichen werden soll
	 * @param ap2 Workpackage2 das Verglichen werden soll
	 */
	@Override
	public int compare(Workpackage ap1, Workpackage ap2) {
		if(ap1.getEndDateHope() != null && ap2.getEndDateHope() != null) {
			return ap1.getEndDateHope().compareTo(ap2.getEndDateHope());
		}
		if(ap1.getEndDateHope() != null) {
			return -1;
		} 
		if(ap2.getEndDateHope() != null) {
			return 1;
		}
		return 0;
	}
	
}
