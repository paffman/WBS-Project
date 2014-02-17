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

package de.fhbingen.wbs.gui.wpworker;

import de.fhbingen.wbs.globals.FilterJTextField;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * GUI to add a new employee or change an existing one.
 */
public class WBSUserView extends JFrame {

    /** Mode for editing an existing user. */
    public static final int EDIT_USER = 0;

    /** Mode for creating a new. */
    public static final int NEW_USER = 1;

    /** The current display mode. */
    private int mode;

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
     * Defines possible user actions to be handled by the controller.
     */
    public interface Delegate {

        /**
         * Information of a current employee should be updated.
         */
        void changeEmployeePerformed();

        /**
         * A new employee should be added.
         */
        void addEmployeePerformed();

        /**
         * The user performed the cancel action.
         */
        void cancelPerformed();

        /**
         * The user wants to reset the password.
         */
        void resetPasswordPerformed();
    }

    /** The delegate object handling user events. */
    private final Delegate delegate;

    /**
     * Constructor. The GUI components are initialized and added to the
     * layout.
     * @param aDelegate Object handling the user events.
     */
    public WBSUserView(final Delegate aDelegate) {
        super();
        this.delegate = aDelegate;
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

        addActionListeners();

        setVisible(true);
    }

    /**
     * Returns the current display mode.
     *
     * @return The current mode.
     */
    public final int getDisplayMode() {
        return mode;
    }

    /**
     * Sets the display mode. Possible values are <code>WBSUserView
     * .EDIT_USER</code> (default) or <code>WBSUserView.NEW_USER</code>.
     * @param aMode The display mode.
     */
    public final void setDisplayMode(final int aMode) {
        if (aMode == NEW_USER) {
            this.mode = NEW_USER;
            btnhinzufuegen.setVisible(true);
            txfLogin.setEnabled(true);
            btnedit.setVisible(false);
            btnPwReset.setVisible(false);
        } else {
            this.mode = EDIT_USER;
            btnhinzufuegen.setVisible(false);
            txfLogin.setEnabled(false);
            btnedit.setVisible(true);
            btnPwReset.setVisible(true);
        }
    }

    /**
     * Returns the login.
     *
     * @return The login.
     */
    public final String getLogin() {
        return txfLogin.getText();
    }

    /**
     * Sets the login.
     *
     * @param aLogin The new login.
     */
    public final void setLogin(final String aLogin) {
        txfLogin.setText(aLogin);
    }

    /**
     * Returns the name.
     *
     * @return The name.
     */
    public final String getName() {
        return txfName.getText();
    }

    /**
     * Sets the name.
     *
     * @param aName The new name.
     */
    public final void setName(final String aName) {
        txfName.setText(aName);
    }

    /**
     * Returns the first name.
     *
     * @return The first name.
     */
    public final String getFirstName() {
        return txfVorname.getText();
    }

    /**
     * Sets the first name.
     *
     * @param aFirstName The new first name.
     */
    public final void setFirstName(final String aFirstName) {
        txfVorname.setText(aFirstName);
    }

    /**
     * Returns the permission.
     *
     * @return <code>true</code> if the user should have project leader
     * permission; <code>false</code> otherwise.
     */
    public final boolean getPermission() {
        return cbBerechtigung.isSelected();
    }

    /**
     * Sets the permission.
     *
     * @param aPermission The new permission.
     */
    public final void setPermission(final boolean aPermission) {
        cbBerechtigung.setSelected(aPermission);
    }

    /**
     * Returns the daily rate.
     *
     * @return The daily rate.
     */
    public final double getDailyRate() {
        if (txfTagessatz.getText().equals("")) {
            return 0.0;
        }
        return Double.parseDouble(txfTagessatz.getText());
    }

    /**
     * Sets the daily rate.
     *
     * @param aRate The new daily rate.
     */
    public final void setDailyRate(final double aRate) {
        txfTagessatz.setText(Double.toString(aRate));
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

    /**
     * Setup actionListeners to call to the delegate interface.
     */
    private void addActionListeners() {
        btnedit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                delegate.changeEmployeePerformed();
            }
        });

        btnhinzufuegen.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                delegate.addEmployeePerformed();
            }
        });

        btnschliessen.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                delegate.cancelPerformed();
            }
        });

        btnPwReset.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                delegate.resetPasswordPerformed();
            }
        });
    }
}
