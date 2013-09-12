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
 * Listener, der den Start des Drag&Drop Vorgangs markiert
 * 
 * @author Andre Paffenholz
 * @version 0.1 - 21.01.2011
 */

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class TreeDragSource implements DragSourceListener, DragGestureListener {

	  DragSource source;

	  DragGestureRecognizer recognizer;

	  //TransferableTreeNode transferable;
	  Transferable transferable;

	  DefaultMutableTreeNode oldNode;

	  JTree sourceTree;

	  /**
	   * Default-Konstruktor
	   * @param tree aktueller JTRee
	   * @param actions erlaubte Aktionen
	   */
	  public TreeDragSource(JTree tree, int actions) {
	    sourceTree = tree;
	    source = new DragSource();
	    recognizer = source.createDefaultDragGestureRecognizer(sourceTree,
	        actions, this);
	  }

	  /*
	   * Drag Gesture Handler
	   */
	  public void dragGestureRecognized(DragGestureEvent dge) {
	    TreePath path = sourceTree.getSelectionPath();
	    if ((path == null) || (path.getPathCount() <= 1)) {
	      // We can't move the root node or an empty selection
	      return;
	    }
	    oldNode = (DefaultMutableTreeNode) path.getLastPathComponent();
	    transferable = new TransferableTreeNode(path);
	    //erlaubt das Ablegen überall im Tree
	    
	    if(oldNode != null) {
	    	//alte ID raussuchen und an das DropTarget übergeben, um die SQL Anweisung korrekt ausführen zu können
	    	String[] tmpData = oldNode.toString().split("-");
		    TreeDropTarget.oldWpId =  tmpData[0].trim();
			source.startDrag(dge, DragSource.DefaultMoveDrop, transferable, this);
	    }

	  }

	  /*
	   * Drag Event Handlers
	   */
	  public void dragEnter(DragSourceDragEvent dsde) {
	  }

	  public void dragExit(DragSourceEvent dse) {
	  }

	  public void dragOver(DragSourceDragEvent dsde) {
	  }

	  public void dropActionChanged(DragSourceDragEvent dsde) {

	  }

	  public void dragDropEnd(DragSourceDropEvent dsde) {
	    if (dsde.getDropSuccess()
	        && (dsde.getDropAction() == DnDConstants.ACTION_MOVE)) {
	      ((DefaultTreeModel) sourceTree.getModel())
	          .removeNodeFromParent(oldNode);
	    }
	  }
	}