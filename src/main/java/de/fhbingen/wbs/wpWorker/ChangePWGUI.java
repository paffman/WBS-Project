/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis Copyright (C) 2013 FH-Bingen This
 * program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY;; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.wpWorker;

import de.fhbingen.wbs.globals.FilterJTextField;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Login;
import de.fhbingen.wbs.translation.Messages;

/** GUI to change the password. */
public class ChangePWGUI extends JFrame { // TODO extend JDialog

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = 1L;

    /**
     * Default frame width.
     */
    private static final int DEFAULT_WIDTH = 300;

    /**
     * Default frame height.
     */
    private static final int DEFAULT_HEIGHT = 150;

    /** The used layout for the GUI: GridBagLayout. */
    private GridBagLayout gbl;

    /** The text field to insert the user. */
    public JTextField txfUser;

    /**
     * The password fields for the old password, the new password and to
     * confirm the new password.
     */
    public JPasswordField txfOldPW, txfNewPW, txfNewPWConfirm;

    /** The buttons "OK" and "Cancel". */
    public JButton btnOk, btnCancel;

    /**
     * The main frame gets the name "Change Password" and uses the
     * "Windows Look and Feel". The single components from the GUI are
     * initialized and added to the GridBagLayout.
     */
    public ChangePWGUI() {
        super();

        Login login = LocalizedStrings.getLogin();
        Messages messages = LocalizedStrings.getMessages();
        Button buttons = LocalizedStrings.getButton();

        setTitle(login.changePassword());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try { // TODO wird das nicht schon woanders gesetzt?
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows."
                + "WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println(messages.couldNotLoadLookAndFeel());
        }
        initialize();

        JLabel lblUser = new JLabel(login.currentUser());
        txfUser = new FilterJTextField();
        txfUser.setEnabled(false);
        JLabel lblOldPW = new JLabel(login.oldPassword());
        txfOldPW = new JPasswordField();
        JLabel lblNewPW = new JLabel(login.newPassword());
        txfNewPW = new JPasswordField();
        JLabel lblNewPWConfirm = new JLabel(login.repeatPassword());
        txfNewPWConfirm = new JPasswordField();
        btnOk = new JButton(buttons.ok());
        btnCancel = new JButton(buttons.cancel());

        createGBC(lblUser, 0, 0, 1, 1);
        createGBC(txfUser, 1, 0, 1, 1);
        createGBC(lblOldPW, 0, 1, 1, 1);
        createGBC(txfOldPW, 1, 1, 1, 1);
        createGBC(lblNewPW, 0, 2, 1, 1);
        createGBC(txfNewPW, 1, 2, 1, 1);
        createGBC(lblNewPWConfirm, 0, 3, 1, 1);
        createGBC(txfNewPWConfirm, 1, 3, 1, 1);
        createGBC(btnOk, 0, 4, 1, 1);
        createGBC(btnCancel, 1, 4, 1, 1);
    }

    /**
     * Creates the layout. The used layout is a GridBagLayout. The GUI is
     * inserted in the center.
     */
    private void initialize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2) - DEFAULT_WIDTH / 2,
            (int) (screenSize.getHeight() / 2) - DEFAULT_HEIGHT / 2);

        this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        gbl = new GridBagLayout();
        getContentPane().setLayout(gbl);
        setVisible(true);
    }

    /**
     * Inserts the single components to the layout.
     * @param c
     *            The component which is insert.
     * @param x
     *            The location on the x-axis.
     * @param y
     *            The location on the y-axis.
     * @param width
     *            The width of the element.
     * @param height
     *            The height of the element.
     */
    private void createGBC(final Component c, final int x, final int y,
        final int width, final int height) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.weightx = width;
        gbc.weighty = height;
        gbc.gridheight = height;
        gbc.insets = new Insets(1, 1, 1, 1);
        gbl.setConstraints(c, gbc);
        add(c);
    }
}
