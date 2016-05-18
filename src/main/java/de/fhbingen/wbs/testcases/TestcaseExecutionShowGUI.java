package de.fhbingen.wbs.testcases;


import de.fhbingen.wbs.globals.Controller;
import de.fhbingen.wbs.globals.FilterJTextField;
import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * The GUI for the testcase execution
 */
public class TestcaseExecutionShowGUI extends JFrame{

    /**
     * The icon for an sucessful testcase execution.
     */
    private static final ImageIcon sucessfulIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage
            (TestcaseExecutionShowGUI.class.getResource("/_icons/sucessful.png")));

    /**
     * The icon for an unsucessful testcase execution.
     */
    private static final ImageIcon unsucessfulIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage
            (TestcaseExecutionShowGUI.class.getResource("/_icons/unsucessful.png")));

    /**
     * The label to describe the workpackage ID.
     */
    private JLabel lblWPId;

    /**
     * The label to describe the testname.
     */
    private JLabel lblTestname;

    /**
     * The label to describe the precondition.
     */
    private JLabel lblPrecondition;

    /**
     * The label to describe the description.
     */
    private JLabel lblDescription;

    /**
     * The label to describe the expected result.
     */
    private JLabel lblExpectedResult;

    /**
     * The label to describe the remark.
     */
    private JLabel lblRemark;

    /**
     * The textfield for the workpackage ID.
     */
    private FilterJTextField txfWPId;

    /**
     * The textfield for the testname.
     */
    private FilterJTextField txfTestname;

    /**
     * The textarea for the precondition.
     */
    private JTextArea txaPrecondition;

    /**
     * The textarea for the description.
     */
    private JTextArea txaDescription;

    /**
     * The textarea for the expected result.
     */
    private JTextArea txaExpectedResult;

    /**
     * The textarea for the remark.
     */
    private JTextArea txaRemark;

    /**
     * The button for an sucessful testcase execution.
     */
    private JButton btnSucessful;

    /**
     * The button for an unsucessful testcase execution.
     */
    private JButton btnUnsucessful;

    /**
     * The panel for the content
     */
    private JPanel pnlContent;

    /**
     * The panel for the top content with tescase informations.
     */
    private JPanel pnlTop;

    /**
     * The panel for bottom content with the sucessful and unsucessful buttons.
     */
    private JPanel pnlBottom;

    /**
     * The parent frame.
     */
    private JFrame parent;

    /**
     * General strings.
     */
    private General generalStrings;

    /**
     * Wbs strings.
     */
    private Wbs wbsStrings;

    /**
     * Button strings.
     */
    private Button buttonStrings;

    /**
     * TescaseExecution for the functionality.
     */
    private TestcaseExecutionShow testcaseExecution;

    /**
     * Value for the two different options to execute an testcase or testcases.
     * True -> guide the user throw the testcases
     * False -> user only execute one testcase
     */
    private boolean allTestcases;



    /**
     *
     * @param title
     * @param testcaseExecution
     * @param parent
     * @param allTestcases
     */
    public TestcaseExecutionShowGUI(final String title, final TestcaseExecutionShow testcaseExecution, final JFrame parent, boolean allTestcases){
        super(title);

        this.testcaseExecution = testcaseExecution;
        this.parent = parent;
        this.allTestcases = allTestcases;

        generalStrings = LocalizedStrings.getGeneralStrings();
        wbsStrings = LocalizedStrings.getWbs();
        buttonStrings = LocalizedStrings.getButton();

        //Content panel
        pnlContent = new JPanel();
        pnlContent.setLayout(new BorderLayout());
        getContentPane().add(pnlContent, BorderLayout.CENTER);

        //top panel
        pnlTop = new JPanel();
        pnlTop.setBorder(new EmptyBorder(5, 5, 0, 3));
        GridBagLayout gbl_pnlTop = new GridBagLayout();
        gbl_pnlTop.columnWidths = new int[]{100, 100, 100};
        gbl_pnlTop.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        pnlTop.setLayout(gbl_pnlTop);
        pnlContent.add(pnlTop, BorderLayout.CENTER);

        lblWPId = new JLabel(wbsStrings.workPackageId());
        GridBagConstraints gbc_lblWPId = new GridBagConstraints();
        gbc_lblWPId.anchor = GridBagConstraints.WEST;
        gbc_lblWPId.insets = new Insets(0, 0, 5, 5);
        gbc_lblWPId.gridx = 0;
        gbc_lblWPId.gridy = 0;
        pnlTop.add(lblWPId, gbc_lblWPId);

        txfWPId = new FilterJTextField();
        txfWPId.setEditable(false);
        GridBagConstraints gbc_txfWPId = new GridBagConstraints();
        gbc_txfWPId.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfWPId.insets = new Insets(0, 0, 5, 0);
        gbc_txfWPId.gridx = 1;
        gbc_txfWPId.gridy = 0;
        gbc_txfWPId.gridwidth = 2;
        pnlTop.add(txfWPId, gbc_txfWPId);

        lblTestname = new JLabel(generalStrings.testcase());
        GridBagConstraints gbc_lblTestname = new GridBagConstraints();
        gbc_lblTestname.anchor = GridBagConstraints.WEST;
        gbc_lblTestname.insets = new Insets(0, 0, 5, 5);
        gbc_lblTestname.gridx = 0;
        gbc_lblTestname.gridy = 1;
        pnlTop.add(lblTestname, gbc_lblTestname);

        txfTestname = new FilterJTextField();
        txfTestname.setEditable(false);
        GridBagConstraints gbc_txfTestname = new GridBagConstraints();
        gbc_txfTestname.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfTestname.insets = new Insets(0, 0 , 5, 0);
        gbc_txfTestname.gridx = 1;
        gbc_txfTestname.gridy = 1;
        gbc_txfTestname.gridwidth = 2;
        pnlTop.add(txfTestname, gbc_txfTestname);

        lblPrecondition = new JLabel(generalStrings.precondition());
        GridBagConstraints gbc_lblPrecondition = new GridBagConstraints();
        gbc_lblPrecondition.anchor = GridBagConstraints.WEST;
        gbc_lblPrecondition.insets = new Insets(0, 0, 5, 5);
        gbc_lblPrecondition.gridx = 0;
        gbc_lblPrecondition.gridy = 2;
        pnlTop.add(lblPrecondition, gbc_lblPrecondition);

        txaPrecondition = new JTextArea(5, 1);
        txaPrecondition.setEditable(false);
        txaPrecondition.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txaPrecondition.setLineWrap(true);
        JScrollPane scrollPrecondition = new JScrollPane(txaPrecondition);
        GridBagConstraints gbc_scrollPrecondition = new GridBagConstraints();
        gbc_scrollPrecondition.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollPrecondition.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPrecondition.gridx = 1;
        gbc_scrollPrecondition.gridy = 2;
        gbc_scrollPrecondition.gridwidth = 2;
        pnlTop.add(scrollPrecondition, gbc_scrollPrecondition);

        lblDescription = new JLabel(generalStrings.description());
        GridBagConstraints gbc_lblDescription = new GridBagConstraints();
        gbc_lblDescription.anchor = GridBagConstraints.WEST;
        gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
        gbc_lblDescription.gridx = 0;
        gbc_lblDescription.gridy = 3;
        pnlTop.add(lblDescription, gbc_lblDescription);

        txaDescription = new JTextArea(5,1);
        txaDescription.setEditable(false);
        txaDescription.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txaDescription.setLineWrap(true);
        JScrollPane scrollDescription = new JScrollPane(txaDescription);
        GridBagConstraints gbc_scrollDescription = new GridBagConstraints();
        gbc_scrollDescription.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollDescription.insets = new Insets(0, 0, 5, 0);
        gbc_scrollDescription.gridx = 1;
        gbc_scrollDescription.gridy = 3;
        gbc_scrollDescription.gridwidth = 2;
        pnlTop.add(scrollDescription, gbc_scrollDescription);

        lblExpectedResult = new JLabel(generalStrings.expectedResult());
        GridBagConstraints gbc_lblExpectedResult = new GridBagConstraints();
        gbc_lblExpectedResult.anchor = GridBagConstraints.WEST;
        gbc_lblExpectedResult.insets = new Insets(0, 0, 5, 5);
        gbc_lblExpectedResult.gridx = 0;
        gbc_lblExpectedResult.gridy = 4;
        pnlTop.add(lblExpectedResult, gbc_lblExpectedResult);

        txaExpectedResult = new JTextArea(5, 1);
        txaExpectedResult.setEditable(false);
        txaExpectedResult.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txaExpectedResult.setLineWrap(true);
        JScrollPane scrollExceptedResult = new JScrollPane(txaExpectedResult);
        GridBagConstraints gbc_scrollExceptedResult = new GridBagConstraints();
        gbc_scrollExceptedResult.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollExceptedResult.insets = new Insets(0, 0, 5, 0);
        gbc_scrollExceptedResult.gridx = 1;
        gbc_scrollExceptedResult.gridy = 4;
        gbc_scrollExceptedResult.gridwidth = 2;
        pnlTop.add(scrollExceptedResult, gbc_scrollExceptedResult);

        lblRemark = new JLabel(generalStrings.remark());
        GridBagConstraints gbc_lblRemark = new GridBagConstraints();
        gbc_lblRemark.anchor = GridBagConstraints.WEST;
        gbc_lblRemark.insets = new Insets(0, 0, 5, 5);
        gbc_lblRemark.gridx = 0;
        gbc_lblRemark.gridy = 5;
        pnlTop.add(lblRemark, gbc_lblRemark);

        txaRemark = new JTextArea(5, 1);
        txaRemark.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txaRemark.setLineWrap(true);
        JScrollPane scrollRemark = new JScrollPane(txaRemark);
        GridBagConstraints gbc_scrollRemark = new GridBagConstraints();
        gbc_scrollRemark.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollRemark.insets = new Insets(0, 0, 5, 0);
        gbc_scrollRemark.gridx = 1;
        gbc_scrollRemark.gridy = 5;
        gbc_scrollRemark.gridwidth = 2;
        pnlTop.add(scrollRemark, gbc_scrollRemark);

        //bottom panel with buttons
        pnlBottom = new JPanel();
        pnlBottom.setLayout(new GridLayout(1, 2));
        pnlBottom.setBorder(new LineBorder(Color.gray));
        pnlContent.add(pnlBottom, BorderLayout.SOUTH);

        btnSucessful = new JButton(wbsStrings.sucessful(), sucessfulIcon);
        pnlBottom.add(btnSucessful);

        btnUnsucessful = new JButton(wbsStrings.unsucessful(), unsucessfulIcon);
        pnlBottom.add(btnUnsucessful);


        this.pack();
        this.setResizable(false);
        Controller.centerComponent(parent, this);
        this.setVisible(true);
    }





}
