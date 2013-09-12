package wpOverview;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * 
 * Diese Klasse stellt die Funktionalität zum Erstellen der verschiendenen Bäume zur Verfügung
 * 
 * @author Andre Paffenholz
 * @version 0.1 - 21.01.2011
 */


import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;


public class TreeBuilder {

	//Datenelement mit allen relaventern (!=0) Nummern der AP ID
	private static int[] wpID;
	
	
	
	
    /**
     * createTreeAktiv erzeugt die den JTree für die inaktiven APs
     * Diese Methode wird durch createTrees() aufgerufen und bekommt die Arbeitspakete übergeben
     * @param wpList alle Arbeitspakete des aktuellen Projekts als ArrayList
     */
	public static void createTreeAktiv(DefaultMutableTreeNode rootAktiv, ArrayList<Workpackage> wpList){

		for(int i=0;i<wpList.size();i++){
			Workpackage wp = wpList.get(i);
			//Workpackage nextwp = wpList.get(i+1);
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(wp);
						
			//Das Projekt-Arbeitspaket soll ignoriert werden, sowie die Inaktiven und die fertigen APs - siehe createTree Funktionen drunter
			if(wp.getLvl1ID()>0 && !wp.isIstInaktiv() && !((wp.getStatus()>=100 && !wp.isIstOAP()) || (wp.isIstOAP() && wp.getAc() > 0 && wp.getStatus() >= 100))){
					
				String wpTmpID = wp.toString().split(" - ")[0]; // Nur AP Nummer ohne Namen
                String searchID;
                String tmpArr[];

                int index0 = wpTmpID.indexOf("0");
                if(index0 < 0) {
                    searchID = wpTmpID.substring(0,wpTmpID.length());
                } else {
                    searchID = wpTmpID.substring(0, index0);
                }

                tmpArr = searchID.split("\\.");
				wpID = new int[tmpArr.length];
				for(int j=0;j<tmpArr.length;j++)
					wpID[j] = Integer.parseInt(tmpArr[j].trim());
				
				
				DefaultMutableTreeNode parent = null;
				
				if(rootAktiv.getChildCount() > 0)
					parent = findParent(rootAktiv, wpID);
				
				if(parent != null)
					parent.add(n);
				else
					rootAktiv.add(n);
				
			}
		}	

	}
	
	
	
	
	
	/**
	 * createTreeInaktiv erzeugt den JTree für die inaktiven APs
	 * Diese Methode wird durch createTrees() aufgerufen und bekommt die Arbeitspakete übergeben
	 * @param wpList alle Arbeitspakete des aktuellen Projekts als ArrayList
	 */
		public static void createTreeInaktiv(DefaultMutableTreeNode rootInaktiv, ArrayList<Workpackage> wpList){
	
			for(int i=0;i<wpList.size();i++){
				Workpackage wp = wpList.get(i);
				//Workpackage nextwp = wpList.get(i+1);
				DefaultMutableTreeNode n = new DefaultMutableTreeNode(wp);
							
				//Das Projekt-Arbeitspaket soll ignoriert werden, sowie die Inaktiven und die fertigen APs - siehe createTree Funktionen drunter
				if(wp.getLvl1ID()>0 && wp.isIstInaktiv()){ //&& !wp.isIstInaktiv() && !((wp.getStatus()>=100 && !wp.isIstOAP()) || (wp.isIstOAP() && wp.getAc() > 0 && wp.getStatus() >= 100))){
						
					String wpTmpID = wp.toString().split(" - ")[0]; // Nur AP Nummer ohne Namen
                    String searchID;
                    String tmpArr[];

                    int index0 = wpTmpID.indexOf("0");
                    if(index0 < 0) {
                        searchID = wpTmpID.substring(0,wpTmpID.length());
                    } else {
                        searchID = wpTmpID.substring(0, index0);
                    }

                    tmpArr = searchID.split("\\.");
					wpID = new int[tmpArr.length];
					for(int j=0;j<tmpArr.length;j++)
						wpID[j] = Integer.parseInt(tmpArr[j].trim());
					
					
					DefaultMutableTreeNode parent = null;
					
					if(rootInaktiv.getChildCount() > 0)
						parent = findParent(rootInaktiv, wpID);
					
					if(parent != null)
						parent.add(n);
					else
						rootInaktiv.add(n);
					
				}
			}	
	
		}

		
		
		
		
		
		/**
		 * createTreeFertig erzeugt den JTree für die fertigen APs
		 * Diese Methode wird durch createTrees() aufgerufen und bekommt die Arbeitspakete übergeben
		 * @param wpList alle Arbeitspakete des aktuellen Projekts als ArrayList
		 */
		public static void createTreeFertig(DefaultMutableTreeNode rootFertig, ArrayList<Workpackage> wpList){
		
				for(int i=0;i<wpList.size();i++){
					Workpackage wp = wpList.get(i);
					//Workpackage nextwp = wpList.get(i+1);
					DefaultMutableTreeNode n = new DefaultMutableTreeNode(wp);
								
					//Das Projekt-Arbeitspaket soll ignoriert werden, sowie die Inaktiven und die fertigen APs - siehe createTree Funktionen drunter
					if(wp.getLvl1ID()>0 && ((wp.getStatus()>=100 && !wp.isIstOAP()) || (wp.isIstOAP() && wp.getAc() > 0 && wp.getStatus() >= 100))){
							
						String wpTmpID = wp.toString().split(" - ")[0]; // Nur AP Nummer ohne Namen
                        String searchID;
                        String tmpArr[];

                        int index0 = wpTmpID.indexOf("0");
                        if(index0 < 0) {
                            searchID = wpTmpID.substring(0,wpTmpID.length());
                        } else {
                            searchID = wpTmpID.substring(0, index0);
                        }

                        tmpArr = searchID.split("\\.");
						wpID = new int[tmpArr.length];
						for(int j=0;j<tmpArr.length;j++)
							wpID[j] = Integer.parseInt(tmpArr[j].trim());
						
						
						DefaultMutableTreeNode parent = null;
						
						if(rootFertig.getChildCount() > 0)
							parent = findParent(rootFertig, wpID);
						
						if(parent != null)
							parent.add(n);
						else
							rootFertig.add(n);
						
					}
				}	
		
			}

		
		
		

		/**
		 * createTreeAlle erzeugt den JTree mit allen Arbeitspaketen
		 * Diese Methode wird durch createTrees() aufgerufen und bekommt die Arbeitspakete übergeben
		 * @param wpList alle Arbeitspakete des aktuellen Projekts als ArrayList
		 */
			public static void createTreeAlle(DefaultMutableTreeNode rootAlle, ArrayList<Workpackage> wpList){
				
				for(int i=0;i<wpList.size();i++){
					Workpackage wp = wpList.get(i);
					//Workpackage nextwp = wpList.get(i+1);
					DefaultMutableTreeNode n = new DefaultMutableTreeNode(wp);
								
					//Das Projekt-Arbeitspaket soll ignoriert werden, sowie die Inaktiven und die fertigen APs - siehe createTree Funktionen drunter
					if(wp.getLvl1ID()>0){
							
						String wpTmpID = wp.toString().split(" - ")[0]; // Nur AP Nummer ohne Namen
                        String searchID;
                        String tmpArr[];

                        int index0 = wpTmpID.indexOf("0");
                        if(index0 < 0) {
                            searchID = wpTmpID.substring(0,wpTmpID.length());
                        } else {
                            searchID = wpTmpID.substring(0, index0);
                        }

                        tmpArr = searchID.split("\\.");
						wpID = new int[tmpArr.length];
						for(int j=0;j<tmpArr.length;j++)
							wpID[j] = Integer.parseInt(tmpArr[j].trim());
						
						
						DefaultMutableTreeNode parent = null;
						
						if(rootAlle.getChildCount() > 0)
							parent = findParent(rootAlle, wpID);
						
						if(parent != null)
							parent.add(n);
						else
							rootAlle.add(n);
						
					}
				}	
		
			}

	


	/**
	 * Liefert den passenden Vaterknoten zu einer übergebnenen AP id
	 * Funktionsweise: Es wird direkt auf die richtigen Kinderknoten im Baum zugegriffen
	 * 		Beispiel: 1.2.3.0 -> int[] = {1,2,3}
	 * 		root -> Kindknoten (1-1) -> Kindknoten (2-1) -> Kindknoten(3-1)		
	 * 
	 * @param node root Knoten im Baum
	 * @param wp Array mit int Zahlen des Arbeitspaketes (ohne 0)
	 * @return Vaterknoten im Baum oder NULL
	 */
    private static DefaultMutableTreeNode findParent(DefaultMutableTreeNode node, int[] wp){
        try {
            for(int i=0;i<wp.length-1;i++)
                node = (DefaultMutableTreeNode) node.getChildAt(wp[i]-1);
        }
        catch(Exception ex){
            //Wird bei den root-Nodes ausgegeben, da keine Vaterknoten vorhanden sind.
            System.out.println("Error in Node: " + node.toString() + " no Parent Node found.");
            //ex.printStackTrace();
        }
        return node;
    }

}
