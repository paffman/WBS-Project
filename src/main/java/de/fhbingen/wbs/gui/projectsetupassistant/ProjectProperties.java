package de.fhbingen.wbs.gui.projectsetupassistant;

import c10n.C10N;
import de.fhbingen.wbs.gui.StaticUtilityMethods;
import de.fhbingen.wbs.translation.ProjectSetup;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Dialog asking for the project properties.
 * This includes project specific settings as well as an user account for
 * the project manager.
 */
public class ProjectProperties extends JDialog {
    /**
     * Default preferred size.
     */
    private static final Dimension PREFERRED_SIZE = new Dimension(640, 280);
    /**
     * Translation message object.
     */
    private final ProjectSetup msg;
    /**
     * Panel in the South of the border layout on the content pane.
     */
    private final JPanel southPanel;
    /**
     * Next button.
     */
    private final JButton buttonNext;
    /**
     * Cancel button.
     */
    private final JButton buttonCancel;
    /**
     * Back button.
     */
    private final JButton buttonBack;
    /**
     * Interface to the controller.
     */
    private final Actions actionHandler;
    /**
     * Text field for the project name.
     */
    private final JTextField textFieldProjectName;
    /**
     * Text field for level depth of the project.
     */
    private final JTextField textFieldProjectLevels;
    /**
     * Text field for start date of the project.
     */
    private final JTextField textFieldStartDate;
    /**
     * Text field for first name of project manager.
     */
    private final JTextField textFieldFirstName;
    /**
     * Text field for surname of project manager.
     */
    private final JTextField textFieldSurname;
    /**
     * Text field for user name of the project manager account.
     */
    private final JTextField textFieldUserName;
    /**
     * Password field for the new password of the project manager account.
     */
    private final JPasswordField textFieldPassword;
    /**
     * Password field for the repeated password of the project manager
     * account.
     */
    private final JPasswordField textFieldPassword2;
    /**
     * Label of the text field {@link de.fhbingen.wbs.gui
     * .projectsetupassistant.ProjectProperties#textFieldProjectName}.
     */
    private final JLabel labelTextFieldProjectName;
    /**
     * Label of the text field {@link de.fhbingen.wbs.gui
     * .projectsetupassistant.ProjectProperties#textFieldProjectLevels}.
     */
    private final JLabel labelTextFieldProjectLevels;
    /**
     * Label of the text field {@link de.fhbingen.wbs.gui
     * .projectsetupassistant.ProjectProperties#textFieldStartDate}.
     */
    private final JLabel labelTextFieldStartDate;
    /**
     * Label of the text field {@link de.fhbingen.wbs.gui
     * .projectsetupassistant.ProjectProperties#textFieldFirstName}.
     */
    private final JLabel labelTextFieldFirstName;
    /**
     * Label of the text field {@link de.fhbingen.wbs.gui
     * .projectsetupassistant.ProjectProperties#textFieldSurname}.
     */
    private final JLabel labelTextFieldSurname;
    /**
     * Label of the text field {@link de.fhbingen.wbs.gui
     * .projectsetupassistant.ProjectProperties#textFieldUserName}.
     */
    private final JLabel labelTextFieldUserName;
    /**
     * Label of the text field {@link de.fhbingen.wbs.gui
     * .projectsetupassistant.ProjectProperties#textFieldPassword}.
     */
    private final JLabel labelTextFieldPassword;
    /**
     * Label of the text field {@link de.fhbingen.wbs.gui
     * .projectsetupassistant.ProjectProperties#textFieldPassword2}.
     */
    private final JLabel labelTextFieldPassword2;
    /**
     * Center panel that holds all labels and text fields that relate to
     * project properties.
     */
    private final JPanel centerPanelProjectProperties;
    /**
     * Center panel that holds all labels and text fields that relate to the
     * project manager account.
     */
    private final JPanel centerPanelProjectManagerAccount;

    /**
     * Interface for GUI actions.
     */
    public interface Actions {
        /**
         * Gets called by the next button.
         */
        void nextButtonPressedProjectProperties();

        /**
         * Gets called by the cancel button.
         */
        void cancelButtonPressed();
    }

    /**
     * Default constructor.
     * @param parent parent frame.
     * @param handler responsible for handling all user actions.
     */
    public ProjectProperties(final JFrame parent, final Actions handler) {
        super(parent);
        msg = C10N.get(ProjectSetup.class);

        //set action handler
        actionHandler = handler;

        //Layout Elements
        final BorderLayout layoutBorderContentPane = new BorderLayout();
        Container contentPane = new JPanel(layoutBorderContentPane);
        setContentPane(contentPane);

        southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        contentPane.add(southPanel, BorderLayout.SOUTH);

        //center layout
        final GridLayout layoutCenterGrid = new GridLayout(1, 2);
        final GridBagLayout layoutCenterGridBagLeft = new GridBagLayout();
        final GridBagLayout layoutCenterGridBagRight = new GridBagLayout();
        JPanel centerPanel = new JPanel(layoutCenterGrid);
        centerPanelProjectProperties = new JPanel(layoutCenterGridBagLeft);
        centerPanelProjectManagerAccount = new JPanel(layoutCenterGridBagRight);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        centerPanelProjectProperties.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), msg.projectProperties(),
                TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));

        centerPanelProjectManagerAccount.setBorder(BorderFactory.
                createTitledBorder(BorderFactory.createEtchedBorder(),
                                   msg.projectManagerAccount(),
                                   TitledBorder.RIGHT,
                                   TitledBorder.DEFAULT_POSITION));
        centerPanel.add(centerPanelProjectProperties);
        centerPanel.add(centerPanelProjectManagerAccount);

        //Buttons
        buttonNext = new JButton(msg.next());
        buttonCancel = new JButton(msg.cancel());
        buttonBack = new JButton(msg.back());
        buttonBack.setEnabled(false);

        //Text Fields Properties - left panel
        textFieldProjectName = new JTextField();
        textFieldProjectLevels = new JTextField();
        textFieldStartDate = new JTextField();


        //TextFields Admin Account- right panel
        textFieldFirstName = new JTextField();
        textFieldSurname = new JTextField();
        textFieldUserName = new JTextField();
        textFieldPassword = new JPasswordField();
        textFieldPassword2 = new JPasswordField();

        //Labels for TextFields
        labelTextFieldProjectName = new JLabel(msg.projectName() + ":",
                JLabel.RIGHT);
        labelTextFieldProjectLevels = new JLabel(msg.projectTiers() + ":",
                JLabel.RIGHT);
        labelTextFieldStartDate = new JLabel(msg.startDate() + ":",
                JLabel.RIGHT);

        labelTextFieldFirstName = new JLabel(msg.firstName() + ":",
                JLabel.RIGHT);
        labelTextFieldSurname = new JLabel(msg.surname() + ":",
                JLabel.RIGHT);
        labelTextFieldUserName = new JLabel(msg.loginLong() + ":",
                JLabel.RIGHT);
        labelTextFieldPassword = new JLabel(msg.password() + ":",
                JLabel.RIGHT);
        labelTextFieldPassword2 = new JLabel(msg.repeatPassword() + ":",
                JLabel.RIGHT);


        //set labelFor() for screen reader compatibility
        labelTextFieldProjectName.setLabelFor(labelTextFieldProjectName);
        labelTextFieldProjectLevels.setLabelFor(textFieldProjectLevels);
        labelTextFieldStartDate.setLabelFor(textFieldStartDate);
        labelTextFieldFirstName.setLabelFor(textFieldFirstName);
        labelTextFieldSurname.setLabelFor(textFieldSurname);
        labelTextFieldUserName.setLabelFor(textFieldUserName);
        labelTextFieldPassword.setLabelFor(textFieldPassword);
        labelTextFieldPassword2.setLabelFor(textFieldPassword2);

        addUiElements();
        addActionListeners();

        setupDialogProperties();
        pack();
    }

    /**
     * Gets text of project name text field.
     * @return  value of project name field.
     */
    public final String getProjectName() {
        return textFieldProjectName.getText();
    }

    /**
     * Gets text of project level text field.
     * @return value of project level field.
     */
    public final String getProjectLevels() {
        return textFieldProjectLevels.getText();
    }

    /**
     * Gets text of project start date text field.
     * @return value of start date text field as string.
     */
    public final String getStartDate() {
        return textFieldStartDate.getText();
    }

    /**
     * Gets text of project manager first name text field.
     * @return value name of first name text field.
     */
    public final String getFirstName() {
        return textFieldFirstName.getText();
    }

    /**
     * Gets text of project manager surname text field.
     * @return value of surname text field.
     */
    public final String getSurname() {
        return textFieldSurname.getText();
    }

    /**
     * Gets text of project manager user name text field.
     * @return value of user name text field.
     */
    public final String getUserName() {
        return textFieldUserName.getText();
    }

    /**
     * Gets text of project manager password field.
     * Be sure to Arrays.fill(0, returned); to decrease risk of password theft.
     * @return value of password field.
     */
    public final char[] getPassword() {
        return textFieldPassword.getPassword();
    }

    /**
     * Gets text of project manager repeat password field.
     * Be sure to Arrays.fill(0, returned); to decrease risk of password theft.
     * @return value of repeat password field.
     */
    public final char[] getPassword2() {
        return textFieldPassword2.getPassword();
    }

    /**
     * Sets the text of password fields to "" to increase security.
     */
    public final void clearPasswordFields() {
        textFieldPassword.setText("");
        textFieldPassword2.setText("");
    }
    /**
     * Adds the Ui elements to the panel using layout managers.
     */
    private void addUiElements() {
        //south panel
        southPanel.add(buttonBack);
        southPanel.add(buttonNext);
        southPanel.add(buttonCancel);
        //gridbag properties
        final Insets insetsLabelLeft = new Insets(10, 40, 5, 5);
        final Insets insetsTextFieldLeft = new Insets(10, 5, 5, 20);
        final Insets insetsLabelRight = new Insets(10, 20, 5, 5);
        final Insets insetsTextFieldRight = new Insets(10, 5, 5, 40);
        final int textFieldWeightx = 1;
        //left center panel
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectProperties,
                labelTextFieldProjectName, 0, 0, 0, 0,
                GridBagConstraints.BOTH, insetsLabelLeft);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectProperties,
                labelTextFieldProjectLevels, 0, 1, 0, 0,
                        GridBagConstraints.BOTH, insetsLabelLeft);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectProperties,
                labelTextFieldStartDate, 0, 2, 0, 0, GridBagConstraints.BOTH,
                insetsLabelLeft);

        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectProperties,
                textFieldProjectName, 1, 0, textFieldWeightx,
                0, GridBagConstraints.HORIZONTAL,
                insetsTextFieldLeft);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectProperties,
                textFieldProjectLevels, 1, 1, textFieldWeightx,
                0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldLeft);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectProperties,
                textFieldStartDate, 1, 2, textFieldWeightx, 0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldLeft);

        //Spacer
        StaticUtilityMethods.
                addWithAllGridBagConstraints(centerPanelProjectProperties,
                new JPanel(), 0, 3, 2, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);

        //right center panel
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                labelTextFieldFirstName, 0, 0, 0, 0,
                GridBagConstraints.BOTH, insetsLabelRight);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                labelTextFieldSurname, 0, 1, 0, 0, GridBagConstraints.BOTH,
                insetsLabelRight);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                labelTextFieldUserName, 0, 2, 0, 0, GridBagConstraints.BOTH,
                insetsLabelRight);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                labelTextFieldPassword, 0, 3, 0, 0, GridBagConstraints.BOTH,
                insetsLabelRight);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                labelTextFieldPassword2, 0, 4, 0, 0, GridBagConstraints.BOTH,
                insetsLabelRight);

        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                textFieldFirstName, 1, 0, textFieldWeightx,
                0, GridBagConstraints.HORIZONTAL,
                insetsTextFieldRight);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                textFieldSurname, 1, 1, textFieldWeightx,
                0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldRight);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                textFieldUserName, 1, 2, textFieldWeightx, 0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldRight);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                textFieldPassword, 1, 3, textFieldWeightx, 0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldRight);
        StaticUtilityMethods.
                addWithGridBagConstraints(centerPanelProjectManagerAccount,
                textFieldPassword2, 1, 4, textFieldWeightx, 0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldRight);
        //Spacer
        StaticUtilityMethods.
                addWithAllGridBagConstraints(centerPanelProjectManagerAccount,
                new JPanel(), 0, 5, 2, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    }

    /**
     * Sets up some dialog properties.
     */
    private void setupDialogProperties() {
        setTitle(msg.projectSetup() + ": " + msg.projectProperties());
        setModal(true);
        getRootPane().setDefaultButton(buttonNext);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        //positioning and size
        setLocationRelativeTo(getParent());
        setPreferredSize(PREFERRED_SIZE);
        setMinimumSize(PREFERRED_SIZE);
    }

    /**
     * Setup actionListeners to call to the interface.
     */
    private void addActionListeners() {
        buttonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                actionHandler.nextButtonPressedProjectProperties();
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
}
