package de.fhbingen.wbs.gui.projectsetupassistant;

import c10n.C10N;
import de.fhbingen.wbs.gui.SwingUtilityMethods;
import de.fhbingen.wbs.translation.ProjectSetup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Dialog asking for the database admin login.
 */
public class DatabaseAdminLogin extends JDialog {

    /**
     * Default preferred size.
     */
    private static final Dimension PREFERRED_SIZE = new Dimension(450, 200);

    /**
     * UID for serialization.
     */
    private static final long serialVersionUID = 2300059179886700170L;

    /**
     * Panel for southern buttons (ok, cancel, back).
     */
    private final JPanel southPanel;

    /**
     * Panel for center form fields.
     */
    private final JPanel centerPanel;

    /**
     * OK button.
     */
    private final JButton buttonOk;

    /**
     * Cancel button.
     */
    private final JButton buttonCancel;

    /**
     * Back button.
     */
    private final JButton buttonBack;

    /**
     * Text field for server address.
     */
    private final JTextField textFieldServerAddress;
    /**
     * Text field for root user on the server.
     */
    private final JTextField textFieldUserName;
    /**
     * Password field for root password on the server.
     */
    private final JPasswordField textFieldPassword;
    /**
     * Label for server address text field.
     */
    private final JLabel labelTextFieldServerAddress;
    /**
     * Label for user name text field.
     */
    private final JLabel labelTextFieldUserName;
    /**
     * Label for password field.
     */
    private final JLabel labelTextFieldPassword;

    /**
     * Responsible for handling all user actions.
     */
    private final Actions actionHandler;

    /**
     * Get cosmopolitan translations.
     */
    private final ProjectSetup msg;

    /**
     * Interface do define possible user actions to be handled by the
     * controller.
     */
    public interface Actions {
        /**
         * Gets called by the ok button.
         */
        void nextButtonPressedDatabaseAdminLogin();

        /**
         * Gets called by the cancel button.
         */
        void cancelButtonPressed();

        /**
         * Gets called by the back button.
         */
        void backButtonPressedDatabaseAdminLogin();
    }

    /**
     * Default constructor.
     * @param parent parent frame.
     * @param handler responsible for handling all user actions.
     */
    public DatabaseAdminLogin(final JFrame parent, final Actions handler) {
        super(parent);
        msg = C10N.get(ProjectSetup.class);
        //set action handler
        actionHandler = handler;

        //Layout Elements
        final BorderLayout layoutBorderContentPane = new BorderLayout();

        JPanel contentPane = new JPanel(layoutBorderContentPane);
        setContentPane(contentPane);

        southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        contentPane.add(southPanel, BorderLayout.SOUTH);

        final GridBagLayout layoutCenterGridBag = new GridBagLayout();
        centerPanel = new JPanel(layoutCenterGridBag);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        //Buttons
        buttonOk = new JButton(msg.ok());
        buttonCancel = new JButton(msg.cancel());
        buttonBack = new JButton(msg.back());

        //TextFields
        textFieldServerAddress = new JTextField();
        textFieldUserName = new JTextField();
        textFieldPassword = new JPasswordField();
        //Labels for TextFields
        labelTextFieldServerAddress = new JLabel(msg.serverAddress() + ":",
                JLabel.RIGHT);
        labelTextFieldUserName = new JLabel(msg.rootLoginName() + ":",
                JLabel.RIGHT);
        labelTextFieldPassword = new JLabel(msg.rootPassword() + ":",
                JLabel.RIGHT);
        //set labelFor() for screen reader compatibility
        labelTextFieldServerAddress.setLabelFor(textFieldServerAddress);
        labelTextFieldUserName.setLabelFor(textFieldUserName);
        labelTextFieldPassword.setLabelFor(textFieldPassword);

        addUiElements();
        addActionListeners();

        setupDialogProperties();
    }

    /**
     * Gets current text from server address field.
     * @return current server address text.
     */
    public final String getServerAddress() {
        return textFieldServerAddress.getText();
    }

    /**
     * Gets current text from user name field.
     * @return current user name text.
     */
    public final String getUserName() {
        return textFieldUserName.getText();
    }

    /**
     * Gets current text from password field.
     * @return current password text.
     */
    public final char[] getPassword() {
        return textFieldPassword.getPassword();
    }

    /**
     * Adds the Ui elements to the panel using layout managers.
     */
    private void addUiElements() {
        southPanel.add(buttonBack);
        southPanel.add(buttonOk);
        southPanel.add(buttonCancel);

        final Insets insetsLabel = new Insets(10, 40, 5,
                5);
        final Insets insetsTextField = new Insets(10, 5, 5,
                40);
        final int textFieldWeightx = 1;
        final int textFieldWeighty = 0;
        SwingUtilityMethods.addWithGridBagConstraints(centerPanel,
                labelTextFieldServerAddress, 0, 0, 0, 0,
                GridBagConstraints.BOTH, insetsLabel);
        SwingUtilityMethods.addWithGridBagConstraints(centerPanel,
                labelTextFieldUserName, 0, 1, 0, 0, GridBagConstraints.BOTH,
                insetsLabel);
        SwingUtilityMethods.addWithGridBagConstraints(centerPanel,
                labelTextFieldPassword, 0, 2, 0, 0, GridBagConstraints.BOTH,
                insetsLabel);
        SwingUtilityMethods.addWithGridBagConstraints(centerPanel,
                textFieldServerAddress, 1, 0, textFieldWeightx,
                textFieldWeighty, GridBagConstraints.HORIZONTAL,
                insetsTextField);
        SwingUtilityMethods.addWithGridBagConstraints(centerPanel,
                textFieldUserName, 1, 1, textFieldWeightx, textFieldWeighty,
                GridBagConstraints.HORIZONTAL, insetsTextField);
        SwingUtilityMethods.addWithGridBagConstraints(centerPanel,
                textFieldPassword, 1, 2, textFieldWeightx, textFieldWeighty,
                GridBagConstraints.HORIZONTAL, insetsTextField);

    }

    /**
     * Sets up some dialog properties.
     */

    private void setupDialogProperties() {
        setModal(true);
        getRootPane().setDefaultButton(buttonOk);
        setTitle(msg.projectSetup() + ": " + msg.databaseAdminLogin());
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        //positioning and size
        setPreferredSize(PREFERRED_SIZE);
        setMinimumSize(PREFERRED_SIZE);
        setMaximumSize(PREFERRED_SIZE);
        pack();
        setLocationRelativeTo(getParent());
    }

    /**
     * Setup actionListeners to call to the interface.
     */
    private void addActionListeners() {
        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                actionHandler.nextButtonPressedDatabaseAdminLogin();
            }
        });

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                actionHandler.backButtonPressedDatabaseAdminLogin();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

                actionHandler.cancelButtonPressed();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                actionHandler.cancelButtonPressed();
            }
        });
    }

    /**
     * Sets the text of password field to "" to increase security.
     */
    public final void clearPasswordField() {
        textFieldPassword.setText("");
    }

}
