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
 * GUI class for a project assistant.
 * A project assistant is used to create new projects and create necessary
 * database structures.
 * Created by Maxi on 02.01.14.
 */
public class ProjectProperties extends JDialog {
    private final ProjectSetup msg;
    private final JPanel centerPanel;
    private final JPanel southPanel;
    private final JButton buttonNext;
    private final JButton buttonCancel;
    private final JButton buttonBack;

    /**
     * Interface to the controller.
     */
    private final Actions actionHandler;

    /**
     * The content pane of this JFrame.
     * {@link javax.swing.JFrame#getContentPane()}
     */
    private final Container contentPane;

    /**
     * Default preferred size.
     */
    private static final Dimension PREFERRED_SIZE = new Dimension(560, 260);
    private final JTextField textFieldProjectName;
    private final JTextField textFieldProjectTiers;
    private final JTextField textFieldStartDate;
    private final JTextField textFieldFirstName;
    private final JTextField textFieldSurname;
    private final JTextField textFieldUserName;
    private final JPasswordField textFieldPassword;
    private final JLabel labelTextFieldProjectName;
    private final JLabel labelTextFieldProjectTiers;
    private final JLabel labelTextFieldStartDate;
    private final JLabel labelTextFieldFirstName;
    private final JLabel labelTextFieldSurname;
    private final JLabel labelTextFieldUserName;
    private final JLabel labelTextFieldPassword;
    private final JPanel centerPanelProjectProperties;
    private final JPanel centerPanelAdminAccount;
    private final JPasswordField textFieldPassword2;
    private final JLabel labelTextFieldPassword2;

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

        /**
         * Gets called by the back button.
         */
        void backButtonPressedProjectProperties();
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
        contentPane = new JPanel(layoutBorderContentPane);
        setContentPane(contentPane);

        southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        contentPane.add(southPanel, BorderLayout.SOUTH);

        //center layout
        final GridLayout layoutCenterGrid = new GridLayout(1, 2);
        final GridBagLayout layoutCenterGridBagLeft = new GridBagLayout();
        final GridBagLayout layoutCenterGridBagRight = new GridBagLayout();
        centerPanel = new JPanel(layoutCenterGrid);
        centerPanelProjectProperties = new JPanel(layoutCenterGridBagLeft);
        centerPanelAdminAccount = new JPanel(layoutCenterGridBagRight);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        centerPanelProjectProperties.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), msg.projectProperties(),
                TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION));

        centerPanelAdminAccount.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), msg.databaseAdminLogin(),
                TitledBorder.RIGHT,TitledBorder.DEFAULT_POSITION));
        centerPanel.add(centerPanelProjectProperties);
        centerPanel.add(centerPanelAdminAccount);

        //Buttons
        buttonNext = new JButton(msg.next());
        buttonCancel = new JButton(msg.cancel());
        buttonBack = new JButton(msg.back());
        buttonBack.setEnabled(false);

        //Text Fields Properties - left panel
        textFieldProjectName = new JTextField();
        textFieldProjectTiers = new JTextField();
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
        labelTextFieldProjectTiers = new JLabel(msg.projectTiers() + ":",
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
        labelTextFieldProjectTiers.setLabelFor(textFieldProjectTiers);
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
     * Gets text of project name form field.
     */
    public final String getProjectName() {
        return textFieldProjectName.getText();
    }

    /**
     * Gets text of project name form field.
     */
    public final String getProjectTiers() {
        return textFieldProjectTiers.getText();
    }

    /**
     * Gets text of project name form field.
     */
    public final String getStartDate() {
        return textFieldStartDate.getText();
    }

    /**
     * Gets text of project name form field.
     */
    public final String getFirstName() {
        return textFieldFirstName.getText();
    }

    /**
     * Gets text of project name form field.
     */
    public final String getSurname() {
        return textFieldSurname.getText();
    }

    /**
     * Gets text of project name form field.
     */
    public final String getUserName() {
        return textFieldUserName.getText();
    }

    /**
     * Gets text of project name form field.
     */
    public final char[] getPassword() {
        return textFieldPassword.getPassword();
    }

    /**
     * Gets text of project name form field.
     */
    public final char[] getPassword2() {
        return textFieldPassword2.getPassword();
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
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelProjectProperties,
                labelTextFieldProjectName, 0, 0, 0, 0,
                GridBagConstraints.BOTH, insetsLabelLeft);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelProjectProperties,
                labelTextFieldProjectTiers, 0, 1, 0, 0, GridBagConstraints.BOTH,
                insetsLabelLeft);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelProjectProperties,
                labelTextFieldStartDate, 0, 2, 0, 0, GridBagConstraints.BOTH,
                insetsLabelLeft);

        StaticUtilityMethods.addWithGridBagConstraints(centerPanelProjectProperties,
                textFieldProjectName, 1, 0, textFieldWeightx,
                0, GridBagConstraints.HORIZONTAL,
                insetsTextFieldLeft);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelProjectProperties,
                textFieldProjectTiers, 1, 1, textFieldWeightx,
                0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldLeft);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelProjectProperties,
                textFieldStartDate, 1, 2, textFieldWeightx, 0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldLeft);

        //Spacer
        StaticUtilityMethods.addWithAllGridBagConstraints(centerPanelProjectProperties,
                new JPanel(), 0, 3, 2, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);

        //right center panel
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                labelTextFieldFirstName, 0, 0, 0, 0,
                GridBagConstraints.BOTH, insetsLabelRight);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                labelTextFieldSurname, 0, 1, 0, 0, GridBagConstraints.BOTH,
                insetsLabelRight);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                labelTextFieldUserName, 0, 2, 0, 0, GridBagConstraints.BOTH,
                insetsLabelRight);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                labelTextFieldPassword, 0, 3, 0, 0, GridBagConstraints.BOTH,
                insetsLabelRight);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                labelTextFieldPassword2, 0, 4, 0, 0, GridBagConstraints.BOTH,
                insetsLabelRight);

        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                textFieldFirstName, 1, 0, textFieldWeightx,
                0, GridBagConstraints.HORIZONTAL,
                insetsTextFieldRight);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                textFieldSurname, 1, 1, textFieldWeightx,
                0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldRight);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                textFieldUserName, 1, 2, textFieldWeightx, 0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldRight);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                textFieldPassword, 1, 3, textFieldWeightx, 0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldRight);
        StaticUtilityMethods.addWithGridBagConstraints(centerPanelAdminAccount,
                textFieldPassword2, 1, 4, textFieldWeightx, 0,
                GridBagConstraints.HORIZONTAL, insetsTextFieldRight);
        //Spacer
        StaticUtilityMethods.addWithAllGridBagConstraints(centerPanelAdminAccount,
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
