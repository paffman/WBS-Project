/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * für das Anlegen eines AufwandObjektes
 * 
 * 
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 */

package baseline;

public class Aufwand {
	private int id1;
	private int id2;
	private int id3;
	private String idx;
	private String name;
	
	/**
	 * 
	 * @param id1 des Arbeitspaketes
	 * @param id2 des Arbeitspaketes
	 * @param id3 des Arbeitspaketes
	 * @param idx Sting id des Arbeitspaketes
	 * @param name des Arbeitspaketes
	 */
	public Aufwand(int id1, int id2, int id3, String idx, String name){						
		this.id1=id1;
		this.id2=id2;
		this.id3=id3;
		this.idx=idx;
		this.name=name;
	}

	/**
	 * Getter für die id1
	 * @return id1 des Arbeitspaketes
	 */
	public int getId1() {
		return id1;
	}
	
	/**
	 * Getter für die id2
	 * @return id2 des Arbeitspaketes
	 */
	public int getId2() {
		return id2;
	}

	
	/**
	 * Getter für die id3
	 * @return id3 des Arbeitspaketes
	 */
	public int getId3() {
		return id3;
	}

	/**
	 * Getter für die idx
	 * @return idx des Arbeitspaketes
	 */
	public String getIdx() {
		return idx;
	}

	/**
	 * Getter für den Namen
	 * @return Name des Arbeitspaketes
	 */
	public String getName() {
		return name;
	}

	
}
