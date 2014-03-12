package de.fhbingen.wbs.globals;

import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


/**
 * Studienprojekt:	PSYS WBS 2.0<br/>
 *
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 *			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang<br/>
 *
 * Diese Klasse dient zum Anzeigen des Tooltips in der Baumansicht der Arbeitspakete.<br/>
 *
 * @author Michael Anstatt
 * @version 2.0 - 2012-08-21
 */
public class ToolTipTree extends JTree {
	private static final long serialVersionUID = -2190230060274475752L;

	public ToolTipTree(DefaultMutableTreeNode root) {
		super(root);
		this.setToolTipText("");
	}

	public String getToolTipText(MouseEvent evt) {
		if (getRowForLocation(evt.getX(), evt.getY()) == -1) {
			return null;
		}
		TreePath curPath = getPathForLocation(evt.getX(), evt.getY());
		return ((Workpackage) ((DefaultMutableTreeNode) curPath.getLastPathComponent()).getUserObject()).getToolTipString();
	}
}
