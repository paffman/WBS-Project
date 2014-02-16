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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Login;

/**
 * GUI to add the work effort to a work package.
 */
public class WBSUserGUI extends JFrame {

    /** The used layout: GridBagLayout. */
    protected GridBagLayout gbl;

    /** The single labels to describe elements on the GUI. */
    protected JLabel lblLogin, lblVorname, lblName, lblBerechtigung,
        lblTagessatz;

    /**
     * The single text fields to insert the login, first name, name, and
     * the daily rate.
     */
    protected JTextField txfLogin, txfVorname, txfName, txfTagessatz;

    /** The single buttons to edit, add, close, or reset a password. */
    protected JButton btnedit, btnhinzufuegen, btnschliessen, btnPwReset;

    /** Check box to set the permissions of the user. */
    protected JCheckBox cbBerechtigung;

    /**
     * Default frame width.
     */
    private static final int DEFAULT_WIDTH = 300;

    /**
     * Default frame height.
     */
    private static final int DEFAULT_HEIGHT = 200;

    /**
     * Constructor. The GUI components are initialized and added to the
     * layout.
     */
    public WBSUserGUI() {
        super();
        initialize();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows."
                + "WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println(LocalizedStrings.getErrorMessages()
                .couldNotLoadLookAndFeel());
        }
        Login loginStrings = LocalizedStrings.getLogin();
        Button button = LocalizedStrings.getButton();

        lblLogin = new JLabel(loginStrings.loginLong() + ":");
        txfLogin = new FilterJTextField();

        lblVorname = new JLabel(loginStrings.firstName() + ":");
        txfVorname = new FilterJTextField();

        lblName = new JLabel(loginStrings.surname() + ":");
        txfName = new FilterJTextField();

        lblBerechtigung = new JLabel(
            loginStrings.projectManagerPermission() + ":");
        cbBerechtigung = new JCheckBox();

        lblTagessatz = new JLabel(LocalizedStrings.getWbs().dailyRate());
        txfTagessatz = new FilterJTextField();

        btnedit = new JButton(button.ok());
        btnhinzufuegen = new JButton(button.ok());
        btnschliessen = new JButton(button.close());
        btnPwReset = new JButton(button.passwordReset());

        createGBC(lblLogin, 0, 0, 1, 1);
        createGBC(txfLogin, 1, 0, 1, 1);
        createGBC(lblVorname, 0, 1, 1, 1);
        createGBC(txfVorname, 1, 1, 1, 1);
        createGBC(lblName, 0, 2, 1, 1);
        createGBC(txfName, 1, 2, 1, 1);
        createGBC(lblBerechtigung, 0, 3, 1, 1);
        createGBC(cbBerechtigung, 1, 3, 1, 1);
        createGBC(lblTagessatz, 0, 4, 1, 1);
        createGBC(txfTagessatz, 1, 4, 1, 1);
        createGBC(btnedit, 0, 5, 1, 1);
        createGBC(btnhinzufuegen, 0, 5, 1, 1);
        createGBC(btnschliessen, 1, 5, 1, 1);
        createGBC(btnPwReset, 0, 6, 1, 1);

        setVisible(true);
    }

    /**
     * Creates the layout. Width: 300 pixel. Height: 200 pixel. The GUI is
     * inserted in the center of the layout.
     */
    private void initialize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2) - DEFAULT_WIDTH / 2,
            (int) (screenSize.getHeight() / 2) - DEFAULT_HEIGHT / 2);
        this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        gbl = new GridBagLayout();
        getContentPane().setLayout(gbl);
    }

    /**
     * Adds single components to the layout.
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
