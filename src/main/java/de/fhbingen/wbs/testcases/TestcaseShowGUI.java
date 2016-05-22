package de.fhbingen.wbs.testcases;

import de.fhbingen.wbs.controller.TestCaseController;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.TestExecution;
import de.fhbingen.wbs.globals.Controller;
import de.fhbingen.wbs.globals.FilterJTextField;
import de.fhbingen.wbs.translation.*;
import de.fhbingen.wbs.translation.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.logging.Filter;

/**
 * The GUI to execute or modify Testcases.
 */
public class TestcaseShowGUI extends JFrame {

    /**
     * The icon for an sucessful testcase execution.
     */
    public static final ImageIcon sucessfulIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage
            (TestcaseShowGUI.class.getResource("/_icons/sucessful.png")));

    /**
     * The icon for an unsuccessful testcase execution.
     */
    public static final ImageIcon unsucessfulIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage
            (TestcaseShowGUI.class.getResource("/_icons/unsucessful.png")));

    /**
     * The icon for an not executed testcase.
     */
    public static final ImageIcon neutralIcon = new ImageIcon((Toolkit.getDefaultToolkit().getImage(
            TestcaseShowGUI.class.getResource("/_icons/neutral.png"))));


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
     * The button to save the changes.
     */
    private JButton btnSave;

    /**
     * The button to cancel the changes.
     */
    private JButton btnCancel;

    /**
     * The button to execute a testcase.
     */
    private JButton btnExecuteTestcase;

    /**
     * The table for the testcase executions.
     */
    private JTable tblTestExecutions;

    /**
     * The panel for the whole content.
     */
    private JPanel pnlContent;

    /**
     * The panel for the left side.
     */
    private JPanel pnlLeft;

    /**
     * The panel for the testcase information.
     */
    private JPanel pnlLeftScroll;

    /**
     * The scroll pane for the testcase informations.
     */
    private JScrollPane leftScrollPane;

    /**
     * The panel for the save and cancel button.
     */
    private JPanel pnlLeftBottom;

    /**
     * The panel for the right side with the testcase executions table and execution button.
     */
    private JPanel pnlRight;

    /**
     * The testcase for the functionality.
     */
    private TestcaseShow testCtrl;

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
     * TestCase
     */
    private TestCase testCase;

    /**
     * Controller for the testcases.
     */
    private TestCaseController testCaseController;


    /**
     *
     * @param title     Title from the frame.
     * @param testCtrl  Functionality from testcase.
     * @param parent    The parent frame.
     * @param testCase  TestCase
     */
    public TestcaseShowGUI(final String title, final TestcaseShow testCtrl, final JFrame parent, TestCase testCase, TestCaseController testCaseController){
        super(title);

        this.testCtrl = testCtrl;
        this.parent = parent;
        this.testCase = testCase;
        this.testCaseController = testCaseController;

        generalStrings = LocalizedStrings.getGeneralStrings();
        wbsStrings = LocalizedStrings.getWbs();
        buttonStrings = LocalizedStrings.getButton();


        //Content panel
        pnlContent = new JPanel();
        pnlContent.setLayout(new BorderLayout());
        getContentPane().add(pnlContent, BorderLayout.CENTER);

        //left panel
        pnlLeft = new JPanel();
        pnlLeft.setLayout(new BorderLayout());
        pnlContent.add(pnlLeft, BorderLayout.WEST);

        //left scroll panel with testcase information
        pnlLeftScroll = new JPanel();
        pnlLeftScroll.setBorder(new EmptyBorder(5,5,5,3));
        GridBagLayout gbl_pnlLeftScroll = new GridBagLayout();
        gbl_pnlLeftScroll.columnWidths = new int[]{100, 100, 100};
        gbl_pnlLeftScroll.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        pnlLeftScroll.setLayout(gbl_pnlLeftScroll);

        //left scroll pane with testcase information
        leftScrollPane = new JScrollPane(pnlLeftScroll, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pnlLeft.add(leftScrollPane, BorderLayout.CENTER);

        lblWPId = new JLabel(wbsStrings.workPackageId());
        GridBagConstraints gbc_lblWPId = new GridBagConstraints();
        gbc_lblWPId.anchor = GridBagConstraints.WEST;
        gbc_lblWPId.insets = new Insets(0, 0, 5, 5);
        gbc_lblWPId.gridx = 0;
        gbc_lblWPId.gridy = 0;
        pnlLeftScroll.add(lblWPId, gbc_lblWPId);

        txfWPId = new FilterJTextField();
        txfWPId.setEditable(false);
        GridBagConstraints gbc_txfWPId = new GridBagConstraints();
        gbc_txfWPId.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfWPId.insets = new Insets(0, 0, 5, 0);
        gbc_txfWPId.gridx = 1;
        gbc_txfWPId.gridy = 0;
        gbc_txfWPId.gridwidth = 2;
        pnlLeftScroll.add(txfWPId, gbc_txfWPId);

        lblTestname = new JLabel(generalStrings.testcase());
        GridBagConstraints gbc_lblTestname = new GridBagConstraints();
        gbc_lblTestname.anchor = GridBagConstraints.WEST;
        gbc_lblTestname.insets = new Insets(0, 0, 5, 5);
        gbc_lblTestname.gridx = 0;
        gbc_lblTestname.gridy = 1;
        pnlLeftScroll.add(lblTestname, gbc_lblTestname);

        txfTestname = new FilterJTextField();
        GridBagConstraints gbc_txfTestname = new GridBagConstraints();
        gbc_txfTestname.fill = GridBagConstraints.HORIZONTAL;
        gbc_txfTestname.insets = new Insets(0, 0 , 5, 0);
        gbc_txfTestname.gridx = 1;
        gbc_txfTestname.gridy = 1;
        gbc_txfTestname.gridwidth = 2;
        pnlLeftScroll.add(txfTestname, gbc_txfTestname);

        lblPrecondition = new JLabel(generalStrings.precondition());
        GridBagConstraints gbc_lblPrecondition = new GridBagConstraints();
        gbc_lblPrecondition.anchor = GridBagConstraints.WEST;
        gbc_lblPrecondition.insets = new Insets(0, 0, 5, 5);
        gbc_lblPrecondition.gridx = 0;
        gbc_lblPrecondition.gridy = 2;
        pnlLeftScroll.add(lblPrecondition, gbc_lblPrecondition);

        txaPrecondition = new JTextArea(5, 1);
        txaPrecondition.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txaPrecondition.setLineWrap(true);
        JScrollPane scrollPrecondition = new JScrollPane(txaPrecondition);
        GridBagConstraints gbc_scrollPrecondition = new GridBagConstraints();
        gbc_scrollPrecondition.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollPrecondition.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPrecondition.gridx = 1;
        gbc_scrollPrecondition.gridy = 2;
        gbc_scrollPrecondition.gridwidth = 2;
        pnlLeftScroll.add(scrollPrecondition, gbc_scrollPrecondition);

        lblDescription = new JLabel(generalStrings.description());
        GridBagConstraints gbc_lblDescription = new GridBagConstraints();
        gbc_lblDescription.anchor = GridBagConstraints.WEST;
        gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
        gbc_lblDescription.gridx = 0;
        gbc_lblDescription.gridy = 3;
        pnlLeftScroll.add(lblDescription, gbc_lblDescription);

        txaDescription = new JTextArea(5,1);
        txaDescription.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txaDescription.setLineWrap(true);
        JScrollPane scrollDescription = new JScrollPane(txaDescription);
        GridBagConstraints gbc_scrollDescription = new GridBagConstraints();
        gbc_scrollDescription.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollDescription.insets = new Insets(0, 0, 5, 0);
        gbc_scrollDescription.gridx = 1;
        gbc_scrollDescription.gridy = 3;
        gbc_scrollDescription.gridwidth = 2;
        pnlLeftScroll.add(scrollDescription, gbc_scrollDescription);

        lblExpectedResult = new JLabel(generalStrings.expectedResult());
        GridBagConstraints gbc_lblExpectedResult = new GridBagConstraints();
        gbc_lblExpectedResult.anchor = GridBagConstraints.WEST;
        gbc_lblExpectedResult.insets = new Insets(0, 0, 5, 5);
        gbc_lblExpectedResult.gridx = 0;
        gbc_lblExpectedResult.gridy = 4;
        pnlLeftScroll.add(lblExpectedResult, gbc_lblExpectedResult);

        txaExpectedResult = new JTextArea(5, 1);
        txaExpectedResult.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txaExpectedResult.setLineWrap(true);
        JScrollPane scrollExceptedResult = new JScrollPane(txaExpectedResult);
        GridBagConstraints gbc_scrollExceptedResult = new GridBagConstraints();
        gbc_scrollExceptedResult.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollExceptedResult.insets = new Insets(0, 0, 5, 0);
        gbc_scrollExceptedResult.gridx = 1;
        gbc_scrollExceptedResult.gridy = 4;
        gbc_scrollExceptedResult.gridwidth = 2;
        pnlLeftScroll.add(scrollExceptedResult, gbc_scrollExceptedResult);

        //empty Label for align the settings on top left
        GridBagConstraints gbc_empty = new GridBagConstraints();
        gbc_empty.weightx = 1.0;
        gbc_empty.weighty = 1.0;
        gbc_empty.gridx = 0;
        gbc_empty.gridy = 5;
        pnlLeftScroll.add(new JLabel(""), gbc_empty);



        //left panel with buttons on bottom
        pnlLeftBottom = new JPanel();
        pnlLeftBottom.setLayout(new GridLayout(1,2));
        pnlLeft.add(pnlLeftBottom, BorderLayout.SOUTH);

        btnSave = new JButton(buttonStrings.save(""));
        pnlLeftBottom.add(btnSave);

        btnCancel = new JButton(buttonStrings.cancel());
        pnlLeftBottom.add(btnCancel);

        //right panel
        pnlRight = new JPanel();
        pnlRight.setPreferredSize(new Dimension(400, 300));
        pnlRight.setLayout(new BorderLayout());
        pnlContent.add(pnlRight, BorderLayout.CENTER);

        tblTestExecutions = new JTable();
        tblTestExecutions.setModel(new DefaultTableModel(null, new String[]{
                "Tester", generalStrings.date(), generalStrings.remark(), generalStrings.result()}){

            //Table not editable
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

            Class[] columnTypes = new Class[]{
                    String.class, Timestamp.class, String.class, ImageIcon.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        tblTestExecutions.getColumnModel().getColumn(3).setMaxWidth(60);
        JScrollPane scrollTableTestcase = new JScrollPane(tblTestExecutions);
        pnlRight.add(scrollTableTestcase, BorderLayout.CENTER);

        btnExecuteTestcase = new JButton(wbsStrings.executeTest("Test"));
        pnlRight.add(btnExecuteTestcase, BorderLayout.SOUTH);


        this.pack();
        Controller.centerComponent(parent, this);
        this.setVisible(true);

        setValues();
        addBtnListener();
    }

    /**
     * Set Values for the testcases.
     */
    public void setValues(){

        txfWPId.setText(testCase.getWp_stringID());
        txfTestname.setText(testCase.getName());
        txaPrecondition.setText(testCase.getPrecondition());
        txaDescription.setText(testCase.getDescription());
        txaExpectedResult.setText(testCase.getExpectedResult());

        clearTable();
        for(TestExecution t : testCaseController.getTestExecutionsForTestCase(testCase)){
            ((DefaultTableModel)tblTestExecutions.getModel()).addRow(new Object[]{t.getEmployeeLogin(), t.getTime(),
                t.getRemark(), t.getStatus().equals("failed")? unsucessfulIcon: sucessfulIcon});
        }

    }

    /**
     * Add Listener
     */
    private void addBtnListener(){

        btnSave.addActionListener(testCtrl.getBtnSaveListener());
        btnCancel.addActionListener(testCtrl.getBtnCancelListener());
        btnExecuteTestcase.addActionListener(testCtrl.getBtnTestExecuteListener());
    }

    /**
     * Return the textarea for the description.
     * @return Description Textarea
     */
    public JTextArea getTxaDescription(){
        return txaDescription;
    }

    /**
     * Return the textarea for the expected result.
     * @return Expected Result Textarea
     */
    public JTextArea getTxaExpectedResult(){
        return txaExpectedResult;
    }

    /**
     * Return the textarea for the precondition.
     * @return Precondition Textarea
     */
    public JTextArea getTxaPrecondition(){
        return txaPrecondition;
    }

    /**
     * Return the testname.
     * @return Testname Textfield
     */
    public FilterJTextField getTxfTestname(){
        return txfTestname;
    }

    /**
     * Show message when description or expected isn´t set.
     */
    public void showMessage(String text){
        JOptionPane.showMessageDialog(this, text);
    }

    /**
     * Clear the testexecution table.
     */
    private void clearTable(){
        tblTestExecutions.setModel(new DefaultTableModel(null, new String[]{
                "Tester", generalStrings.date(), generalStrings.remark(), generalStrings.result()}){

            //Table not editable
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

            Class[] columnTypes = new Class[]{
                    String.class, Timestamp.class, String.class, ImageIcon.class
            };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
    }

}
