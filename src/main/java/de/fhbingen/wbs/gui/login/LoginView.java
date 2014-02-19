/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-Bingen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.gui.login;

import de.fhbingen.wbs.gui.delegates.SimpleDialogDelegate;
import de.fhbingen.wbs.translation.LocalizedStrings;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * The GUI for the Login Screen.
 *
 * http://sublink.ca/icons/sweetieplus/ sowie:
 * http://http://p.yusukekamiyamane.com/ Diagona Icons Copyright (C)
 * 2007 Yusuke Kamiyamane. All rights reserved. The icons are licensed
 * under a Creative Commons Attribution 3.0 license.
 * <http://creativecommons.org/licenses/by/3.0/> Sweetieplus: Sie
 * unterliegen der Creative Commons Licence: This licence allows you to
 * use the icons in any client work, or commercial products such as
 * WordPress themes or applications.
 */
public class LoginView extends JFrame {

    /**
     * generated serialVersionUID.
     */
    protected static final long serialVersionUID = 1L;
    /**
     * Height of the window.
     */
    private static final int WINDOW_HEIGHT = 280;
    /**
     * Width of the window.
     */
    private static final int WINDOW_WIDTH = 400;
    /**
     * Button to confirm db connection entries and start the wbs-tool.
     */
    private JButton okButton;
    /**
     * Button to close the window.
     */
    private JButton closeButton;
    /**
     * The GridBagLayout for the window.
     */
    private GridBagLayout gbl;
    /**
     * The MenuBar for the window.
     */
    private JMenuBar mainMenuBar;
    /**
     * File-Menu of the MenuBar.
     */
    private JMenu fileMenu;
    /**
     * Help-Menu of the MenuBar.
     */
    private JMenu helpMenu;
    /**
     * Items of the Menues.
     */
    private JMenuItem okMenuItem, openMenuItem, closeMenuItem, helpMenuItem,
            infoMenuItem, newDbMenuItem;
    /**
     * Checkbox for project leader login.
     */
    private JCheckBox plCheckBox;
    /**
     * Fields for the database connection.
     */
    private JTextField hostField, dbNameField, userField;
    /**
     * PasswordFields for the passwords needed for a database connection.
     */
    private JPasswordField dbPwPasswordField, pwPasswordField;

    /**
     * Icon for help menu.
     */
    private ImageIcon helpIcon = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(this.getClass().getResource("/_icons/hilfe.png")));
    /**
     * Icon for info menu.
     */
    private ImageIcon infoIcon = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(this.getClass().getResource("/_icons/info.png")));
    /**
     * Icon for ok menu.
     */
    private ImageIcon okIcon = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(this.getClass().getResource("/_icons/weiter.png")));
    /**
     * Icon for close menu.
     */
    private ImageIcon closeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(this.getClass().getResource("/_icons/schliessen.png")));

    /**
     * Responsible for handling all user actions.
     */
    private final ActionsDelegate actionsDelegate;

    /**
     * Interface do define possible user actions to be handled by the
     * controller.
     */
    public interface ActionsDelegate extends SimpleDialogDelegate {
        /**
         * The help action is performed.
         */
        void helpPerformed();

        /**
         * The info action is performed.
         */
        void infoPerformed();

        /**
         * The new db action is performed. This will happen when the user tris
         * to open the project setup assistant.
         */
        void newDbPerformed();
    }

    /**
     * Defines all methods which need to be implemented by the data source.
     */
    public interface DataSource {

        /**
         * Returns the last database host the user entered in the login screen.
         * @return The last db host.
         */
        String getLastDbHost();

        /**
         * Returns the last database name the user entered in the login screen.
         * @return The last db name.
         */
        String getLastDbName();

        /**
         * Returns the last index password the user entered in the login
         * screen.
         * @return The last index password.
         */
        String getLastDbIndexPw();

        /**
         * Returns the last username the user entered in the login screen.
         * @return The last username.
         */
        String getLastDbUser();
    }

    /**
     * Konstuktor LoginView() Main-Frame bekommt den Namen "WBS-File"
     * zugewiesen es wird das Windows Look and Feel verwendet die verschiedenen
     * Menüs, Buttons etc. werden initialisiert und zu dem GridBagLayout
     * hinzugefügt und angeordnet mittels createGbc
     *
     * @param delegate Object handling the user actions.
     * @param dataSource Object providing the data to the GUI.
     */
    public LoginView(final ActionsDelegate delegate, final DataSource dataSource) {
        super("Login");

        this.actionsDelegate = delegate;

        initialize();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // menus
        mainMenuBar = new JMenuBar();
        fileMenu = new JMenu(LocalizedStrings.getDbChooser().file());
        helpMenu = new JMenu(LocalizedStrings.getDbChooser().help());
        okMenuItem = new JMenuItem(LocalizedStrings.getDbChooser().next());
        okMenuItem.setIcon(okIcon);
        closeMenuItem = new JMenuItem(LocalizedStrings.getDbChooser().cancel());
        closeMenuItem.setIcon(closeIcon);
        helpMenuItem = new JMenuItem(LocalizedStrings.getDbChooser().help());
        helpMenuItem.setIcon(helpIcon);
        infoMenuItem = new JMenuItem(LocalizedStrings.getDbChooser().info());
        infoMenuItem.setIcon(infoIcon);
        newDbMenuItem =
                new JMenuItem(LocalizedStrings.getDbChooser()
                        .projectSetupAssistant());

        mainMenuBar.add(fileMenu);
        fileMenu.add(newDbMenuItem);
        fileMenu.add(okMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(closeMenuItem);
        mainMenuBar.add(helpMenu);
        helpMenu.add(helpMenuItem);
        helpMenu.addSeparator();
        helpMenu.add(infoMenuItem);

        // LocalizedStrings.getDbChooser() and input elements
        JLabel titleLabel =
                new JLabel(LocalizedStrings.getDbChooser().database() + ":");

        Font oldFont = titleLabel.getFont();
        Font boldFont = new Font(oldFont.getFontName(), Font.BOLD,
                oldFont.getSize());

        titleLabel.setFont(boldFont);

        JLabel hostLabel =
                new JLabel(LocalizedStrings.getDbChooser().serverAddress()
                        + ":");
        JLabel dbNameLabel =
                new JLabel(LocalizedStrings.getDbChooser().databaseName() + ":");
        JLabel dbPwLabel =
                new JLabel(LocalizedStrings.getDbChooser().indexPassword()
                        + ":");
        JLabel titleUserLabel =
                new JLabel(LocalizedStrings.getDbChooser().user() + ":");
        titleUserLabel.setFont(boldFont);
        JLabel userLabel =
                new JLabel(LocalizedStrings.getDbChooser().loginLong() + ":");
        JLabel pwLabel =
                new JLabel(LocalizedStrings.getDbChooser().password() + ":");
        plCheckBox =
                new JCheckBox(LocalizedStrings.getDbChooser().projectManager());
        hostField = new JTextField();
        dbNameField = new JTextField();
        userField = new JTextField();
        dbPwPasswordField = new JPasswordField();
        pwPasswordField = new JPasswordField();
        closeButton = new JButton(LocalizedStrings.getDbChooser().cancel());
        okButton = new JButton(LocalizedStrings.getDbChooser().ok());

        // load saved database into fields
        if (dataSource.getLastDbHost() != null) {
            hostField.setText(dataSource.getLastDbHost());
        }
        if (dataSource.getLastDbName() != null) {
            dbNameField.setText(dataSource.getLastDbName());
        }
        if (dataSource.getLastDbIndexPw() != null) {
            dbPwPasswordField.setText(dataSource.getLastDbIndexPw());
        }
        if (dataSource.getLastDbUser() != null) {
            userField.setText(dataSource.getLastDbUser());
        }

        // place all elements in the window.
        this.setJMenuBar(mainMenuBar);
        createGBC(titleLabel, 0, 0, 4, 1);
        createGBC(hostLabel, 0, 1, 1, 1);
        createGBC(hostField, 1, 1, 3, 1);
        createGBC(dbNameLabel, 0, 2, 1, 1);
        createGBC(dbNameField, 1, 2, 3, 1);
        createGBC(dbPwLabel, 0, 3, 1, 1);
        createGBC(dbPwPasswordField, 1, 3, 3, 1);
        createGBC(titleUserLabel, 0, 4, 4, 1);
        createGBC(userLabel, 0, 5, 1, 1);
        createGBC(userField, 1, 5, 3, 1);
        createGBC(pwLabel, 0, 6, 1, 1);
        createGBC(pwPasswordField, 1, 6, 3, 1);
        createGBC(plCheckBox, 0, 7, 1, 1);
        createGBC(okButton, 2, 8, 1, 1);
        createGBC(closeButton, 3, 8, 1, 1);

        // show gui
        setVisible(true);

        // set Focus to password field if the host is already set and therefore
        // a saved database has been loaded.
        if (!hostField.getText().equals("")) {
            pwPasswordField.requestFocus();
        }

        addActionListeners();

        this.pack();
    }

    /**
     * Method to create the gui. A GridBagLayout is used for the positioning of
     * the elements.
     */
    private void initialize() {
        int width = WINDOW_WIDTH;
        int height = WINDOW_HEIGHT;
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2) - width / 2,
                (int) (screenSize.getHeight() / 2) - height / 2);

        setSize(new Dimension(width, height));
        gbl = new GridBagLayout();
        getContentPane().setLayout(gbl);
    }


    /**
     * Setup actionListeners to call to the delegate interface.
     */
    private void addActionListeners() {
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                actionsDelegate.cancelPerformed();
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                actionsDelegate.confirmPerformed();
            }
        });

        okMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                actionsDelegate.confirmPerformed();
            }
        });

        closeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                actionsDelegate.cancelPerformed();
            }
        });

        helpMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                actionsDelegate.helpPerformed();
            }
        });

        infoMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                actionsDelegate.infoPerformed();
            }
        });

        newDbMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                actionsDelegate.newDbPerformed();
            }
        });
    }

    /**
     * void createGBC(args) wird am Anordnen der Komponenten auf dem JFrame
     * aufgerufen Methode createGBC zum Hinzufügen der einzelnen Komponenten
     * zum GridBagLayout.
     *
     * @param c
     *            Komponente die zum Layout hinzugefügt wird
     * @param x
     *            Anordnung auf der X-Achse (Breite)
     * @param y
     *            Anordnung auf der Y-Achse (Länge)
     * @param width
     *            Breite des Elementes
     * @param height
     *            Höhe des Elementes
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
        gbc.insets = new Insets(5, 5, 5, 5);
        gbl.setConstraints(c, gbc);
        add(c);
    }

    /**
     * @return the okButton
     */
    protected final JButton getOkButton() {
        return okButton;
    }

    /**
     * @return the closeButton
     */
    protected final JButton getCloseButton() {
        return closeButton;
    }

    /**
     * @return the okMenuItem
     */
    protected final JMenuItem getOkMenuItem() {
        return okMenuItem;
    }

    /**
     * @return the openMenuItem
     */
    protected final JMenuItem getOpenMenuItem() {
        return openMenuItem;
    }

    /**
     * @return the closeMenuItem
     */
    protected final JMenuItem getCloseMenuItem() {
        return closeMenuItem;
    }

    /**
     * @return the helpMenuItem
     */
    protected final JMenuItem getHelpMenuItem() {
        return helpMenuItem;
    }

    /**
     * @return the infoMenuItem
     */
    protected final JMenuItem getInfoMenuItem() {
        return infoMenuItem;
    }

    /**
     * @return the newDbMenuItem
     */
    protected final JMenuItem getNewDbMenuItem() {
        return newDbMenuItem;
    }

    /**
     * Returns the value of the host text field.
     *
     * @return The host name.
     */
    public final String getHost() {
        return hostField.getText();
    }

    /**
     * Returns the value of the database name text field.
     *
     * @return The db name.
     */
    public final String getDbName() {
        return dbNameField.getText();
    }

    /**
     * Returns the value of the user text field.
     *
     * @return The username.
     */
    public final String getUsername() {
        return userField.getText();
    }

    /**
     * Returns the entered index password.
     *
     * @return The index password.
     */
    public final char[] getIndexPassword() {
        return dbPwPasswordField.getPassword();
    }

    /**
     * Returns the entered user password.
     *
     * @return The user password.
     */
    public final char[] getUserPassword() {
        return pwPasswordField.getPassword();
    }

    /**
     * Returns weather or not the project leader checkbox is checked.
     *
     * @return <code>true</code> PL checkbox is checked,
     *         <code>false</code> otherwise.
     */
    public final boolean isProjectLeader() {
        return plCheckBox.isSelected();
    }

    /**
     * @return the plCheckBox
     */
    protected final JCheckBox getPlCheckBox() {
        return plCheckBox;
    }

    /**
     * @return the hostField
     */
    protected final JTextField getHostField() {
        return hostField;
    }

    /**
     * @return the dbNameField
     */
    protected final JTextField getDbNameField() {
        return dbNameField;
    }

    /**
     * @return the userField
     */
    protected final JTextField getUserField() {
        return userField;
    }

    /**
     * @return the dbPwPasswordField
     */
    protected final JPasswordField getDbPwPasswordField() {
        return dbPwPasswordField;
    }

    /**
     * @return the pwPasswordField
     */
    protected final JPasswordField getPwPasswordField() {
        return pwPasswordField;
    }
}
