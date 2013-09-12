/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * für das Anlegen eines ArbeitspaketObjektes
 * 
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 */

package baseline;

public class Arbeitspaket {

	private int id1;
	private int id2;
	private int id3;
	private String idx;
	private String name;
	private double bac;
	private double ac;
	private double ev;
	private double etc;
	private double eac;
	private double bac_kosten;
	private double ac_kosten;
	private double etc_kosten;
	private boolean istOAP;
	private boolean inaktiv;
	
	/**
	 * 
	 * @param id1 des Arbeitspaketes
	 * @param id2 des Arbeitspaketes
	 * @param id3 des Arbeitspaketes
	 * @param idx des Arbeitspaketes
	 * @param name des Arbeitspaketes
	 * @param bac des Arbeitspaketes
	 * @param ac des Arbeitspaketes
	 * @param ev des Arbeitspaketes
	 * @param etc des Arbeitspaketes
	 * @param eac des Arbeitspaketes
	 * @param bac_kosten des Arbeitspaketes
	 * @param ac_kosten des Arbeitspaketes
	 * @param etc_kosten des Arbeitspaketes
	 * @param istOAP ob Arbeitspaket ein OAP ist
	 * @param inaktiv ob Arbeitspaket inaktiv ist
	 */
	public Arbeitspaket(int id1, int id2, int id3, String idx, String name, 
			double bac, double ac, double ev, double etc, double eac,  
			double bac_kosten,  double ac_kosten,  double etc_kosten, boolean istOAP, boolean inaktiv){
		this.id1 = id1;
		this.id2 = id2;
		this.id3 = id3;
		this.idx = idx;
		this.name = name;
		this.bac = bac;
		this.ac = ac;
		this.ev = ev;
		this.etc = etc;
		this.eac = eac;
		this.bac_kosten = bac_kosten;
		this.ac_kosten = ac_kosten;
		this.etc_kosten = etc_kosten;
		this.istOAP = istOAP;
		this.inaktiv = inaktiv;
	}

	/**
	 * Getter für die name
	 * @return name des Arbeitspaketes
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter für boolean inaktiv
	 * @return boolean ob Arbeitspaket inaktiv ist
	 */
	public boolean getInaktiv() {
		return inaktiv;
	}

	/**
	 * Getter für boolean istOAP
	 * @return boolean ob Arbeitspaket in OAP ist
	 */
	public boolean getistOAP() {
		return istOAP;
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
	 * Getter für den BAC
	 * @return BAC des Arbeitspaketes
	 */
	public double getBac() {
		return bac;
	}

	/**
	 * Getter für den AC
	 * @return AC des Arbeitspaketes
	 */
	public double getAc() {
		return ac;
	}

	/**
	 * Getter für den EV
	 * @return EV des Arbeitspaketes
	 */
	public double getEv() {
		return ev;
	}

	/**
	 * Getter für den ETC
	 * @return ETC des Arbeitspaketes
	 */
	public double getEtc() {
		return etc;
	}

	/**
	 * Getter für den EAC
	 * @return EAC des Arbeitspaketes
	 */
	public double getEac() {
		return eac;
	}

	/**
	 * Getter für die BAC_Kosten
	 * @return BAC_Kosten des Arbeitspaketes
	 */
	public double getBac_kosten() {
		return bac_kosten;
	}


	/**
	 * Getter für die AC_Kosten
	 * @return AC_Kosten des Arbeitspaketes
	 */
	public double getAc_kosten() {
		return ac_kosten;
	}

	/**
	 * Getter für die ETC_Kosten
	 * @return ETC_Kosten des Arbeitspaketes
	 */
	public double getEtc_kosten() {
		return etc_kosten;
	}

	
}
