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
 * Repräsentiert eine TreeNode die per Drag&Drop verschoben wird
 * 
 * @author Andre Paffenholz
 * @version 0.1 - 21.01.2011
 */


import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.tree.TreePath;


class TransferableTreeNode implements Transferable {
	
	//Ein Datenelement dass für die Zwischenablage zuständig ist
	public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class,
			"Tree Path");

	private DataFlavor flavors[] = { TREE_PATH_FLAVOR };
	private TreePath path;

	/**
	 * Default-Konstruktor
	 * @param tp TreePath
	 */
	public TransferableTreeNode(TreePath tp) {
		path = tp;
	}

	
	/**
	 * Gibt die Metainformationen aus der Zwischenablage zurück
	 * @return Metainformationen der Zwischenablage
	 */
	public synchronized DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	
	/**
	 * Gibt an, ob Metainformationen der Klassen übereinstimmen
	 * @return true = gleiche Informationen, false = andere Informationen
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (flavor.getRepresentationClass() == TreePath.class);
	}

	
	/**
	 * Gibt einen Transferable Treepath zurück
	 * @return Transferable TreePath
	 */
	public synchronized Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (isDataFlavorSupported(flavor)) {
			return (Object) path;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}
}