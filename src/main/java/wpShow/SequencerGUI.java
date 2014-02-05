package wpShow;

import de.fhbingen.wbs.translation.LocalizedStrings;
import functions.WpManager;
import globals.Controller;
import globals.ToolTipTree;
import globals.Workpackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import wpOverview.TreeCellRenderer;
import wpOverview.WPOverview;
import wpOverview.WPOverviewGUI;

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
 * Auswahlmoeglichkeit fuer Vorgaenger/Nachfolger-Arbeitspakete mit Baumansicht
 *
 * WindowBuilder autogeneriert<br/>
 *
 * @author WindowBuilder(Michael Anstatt)
 * @version 2.0 - 18.08.2012
 */

public class SequencerGUI extends JFrame {

	public static final int MODE_ADD_ANCHESTOR = 0;
	public static final int MODE_ADD_FOLLOWER = 1;
	public static final int MODE_SHOW_ANCHESTOR = 2;
	public static final int MODE_SHOW_FOLLOWER = 3;
	public static final int MODE_DELETE_ANCHESTOR = 4;
	public static final int MODE_DELETE_FOLLOWER = 5;

	private static final long serialVersionUID = 3378227061357215775L;
	private JTree tree;
	private Workpackage wp;
	private int mode;
	private WPShowGUI parent;
	private Workpackage selected;
	/**
	 * Konstruktor
	 * @param wp Ausgewaehltes AP
	 * @param mode
	 * @param parent Workpackage GUI
	 */
	public SequencerGUI(final Workpackage wp, final int mode, WPShowGUI parent) {
		super(LocalizedStrings.getWbs().addDependencyWindowTitle());
		this.wp = wp;
		this.mode = mode;
		this.parent = parent;
		if (mode == MODE_SHOW_ANCHESTOR || mode == MODE_DELETE_ANCHESTOR) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
			for (Workpackage actualWp : wp.getAncestors()) {
				root.add(new DefaultMutableTreeNode(actualWp));
			}
			tree = new ToolTipTree(root);
		} else if (mode == MODE_SHOW_FOLLOWER || mode == MODE_DELETE_FOLLOWER) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
			for (Workpackage actualWp : wp.getFollowers()) {
				root.add(new DefaultMutableTreeNode(actualWp));
			}
			tree = new ToolTipTree(root);
		} else {
			tree = new ToolTipTree(WPOverview.createTree());
		}

		final JPopupMenu popUp = new JPopupMenu();

		final JMenuItem delete = new JMenuItem(LocalizedStrings.getButton()
                .delete(LocalizedStrings.getGeneralStrings().dependency()));
		delete.setIcon(WPOverviewGUI.delAP);
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteAction();
			}
		});

		final JMenuItem add = new JMenuItem(LocalizedStrings.getButton()
                .add(LocalizedStrings.getGeneralStrings().dependency()));
		add.setIcon(WPOverviewGUI.newAp);
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAction();
			}
		});

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selected = (Workpackage) ((DefaultMutableTreeNode) tree.getClosestPathForLocation(e.getX(), e.getY()).getLastPathComponent()).getUserObject();
				if (SwingUtilities.isRightMouseButton(e)) {
					switch (mode) {
					case MODE_ADD_ANCHESTOR:
						popUp.add(add);
						popUp.show(e.getComponent(), e.getX(), e.getY());
						break;
					case MODE_ADD_FOLLOWER:
						popUp.add(add);
						popUp.show(e.getComponent(), e.getX(), e.getY());
						break;
					case MODE_DELETE_ANCHESTOR:
						popUp.add(delete);
						popUp.show(e.getComponent(), e.getX(), e.getY());
						break;
					case MODE_DELETE_FOLLOWER:
						popUp.add(delete);
						popUp.show(e.getComponent(), e.getX(), e.getY());
						break;
					}

				}
			}

		});

		tree.setCellRenderer(new TreeCellRenderer());

		this.add(new JScrollPane(tree));

		this.setSize(400, 800);

		Controller.centerComponent(parent, this);
		this.setVisible(true);
	}
	/**
	 * Fuegt je nach Auswahl einen neuen Vorgaenger oder Nachfolger hinzu
	 */
	private void addAction() {
		switch (mode) {
		case MODE_ADD_ANCHESTOR:
			if (WpManager.insertAncestor(selected, wp)) {
				JOptionPane.showMessageDialog(null, LocalizedStrings
                        .getMessages().workPackageXwasCreatedAsY(selected
                                .toString(),
                                LocalizedStrings.getGeneralStrings()
                                        .predecessor()));
				parent.updateDependencyCount(wp);
				dispose();
			}
			break;
		case MODE_ADD_FOLLOWER:
			if (WpManager.insertFollower(selected, wp)) {
				JOptionPane.showMessageDialog(null, LocalizedStrings
                        .getMessages().workPackageXwasCreatedAsY(selected
                                .toString(),
                                LocalizedStrings.getGeneralStrings()
                                        .successor()));
				parent.updateDependencyCount(wp);
				dispose();
			}
			break;
		}
	}
	/**
	 * Loescht je nach Auswahl einen Vorgaenger oder Nachfolger
	 */
	private void deleteAction() {
		switch (mode) {
		case MODE_DELETE_ANCHESTOR:
			WpManager.removeAncestor(selected, wp);
            JOptionPane.showMessageDialog(null, LocalizedStrings
                    .getMessages().workpackageXwasDeletedAsY(selected
                            .toString(),
                            LocalizedStrings.getGeneralStrings().predecessor
                                    ()));
            parent.updateDependencyCount(wp);
			dispose();
			break;
		case MODE_DELETE_FOLLOWER:
			WpManager.removeFollower(selected, wp);
            JOptionPane.showMessageDialog(null, LocalizedStrings
                    .getMessages().workpackageXwasDeletedAsY(selected
                            .toString(), LocalizedStrings.getGeneralStrings()
                            .successor()));
            parent.updateDependencyCount(wp);
			dispose();
			break;

		}
	}

}
