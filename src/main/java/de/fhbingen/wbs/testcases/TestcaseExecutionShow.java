package de.fhbingen.wbs.testcases;

import de.fhbingen.wbs.controller.TestCaseController;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.TestExecution;
import de.fhbingen.wbs.jdbcConnection.MySqlConnect;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import de.fhbingen.wbs.wpShow.WPShow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Show GUI for testcase execution.
 */
public class TestcaseExecutionShow {

    /**
     * Controller for the testcase.
     */
    private TestCaseController testCaseController;

    /**
     * TestCase
     */
    private TestCase testCase;

    /**
     * General strings.
     */
    private Wbs wbsStrings;

    /**
     * Testcase execution GUI.
     */
    private TestcaseExecutionShowGUI testcaseExecutionShowGUI;

    private JFrame parent;

    /**
     * Value for the two different options to execute an testcase or testcases.
     * True -> guide the user throw the testcases
     * False -> user only execute one testcase
     */
    private boolean allTestcases;

    /**
     * WPShow controller.
     */
    private WPShow wpShow;

    /**
     *
     * @param testCaseController
     * @param testcase
     */
    public TestcaseExecutionShow(TestCaseController testCaseController, TestCase testcase, final JFrame parent,
                                 boolean allTestcases, WPShow wpShow){

        this.testCaseController = testCaseController;
        this.testCase = testcase;
        this.allTestcases = allTestcases;
        this.parent = parent;
        this.wpShow = wpShow;
        wbsStrings = LocalizedStrings.getWbs();

        this.testcaseExecutionShowGUI = new TestcaseExecutionShowGUI(wbsStrings.executeTest("Test"), this, parent, testCaseController, testcase);





    }

    protected final ActionListener getBtnSuccessfulListener(){
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TestExecution testExecution = new TestExecution(testCase.getId(),
                        DBModelManager.getEmployeesModel().getEmployee(MySqlConnect.getLogin().substring(5)).getId(),
                        testcaseExecutionShowGUI.getTxaRemark().getText(),
                        new Timestamp(new Date().getTime()), "succeeded");
                testCaseController.addTestExecution(testExecution);
                wpShow.getWPShowGUI().setTestcases(testCaseController);
                testcaseExecutionShowGUI.setVisible(false);
                testcaseExecutionShowGUI.dispose();
                if(!allTestcases) {
                    ((TestcaseShowGUI) parent).setValues();
                }
                else{
                    wpShow.next();
                }
            }
        };
    }

    protected final ActionListener getBtnUnsuccessfulListener(){
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TestExecution testExecution = new TestExecution(testCase.getId(),
                        DBModelManager.getEmployeesModel().getEmployee(MySqlConnect.getLogin().substring(5)).getId(),
                        testcaseExecutionShowGUI.getTxaRemark().getText(),
                        new Timestamp(new Date().getTime()), "failed");
                testCaseController.addTestExecution(testExecution);
                wpShow.getWPShowGUI().setTestcases(testCaseController);
                testcaseExecutionShowGUI.setVisible(false);
                testcaseExecutionShowGUI.dispose();
                if(!allTestcases) {
                    ((TestcaseShowGUI) parent).setValues();
                }
                else{
                    wpShow.next();
                }
            }
        };
    }
}
