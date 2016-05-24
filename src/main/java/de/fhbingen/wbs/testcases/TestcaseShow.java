package de.fhbingen.wbs.testcases;

import de.fhbingen.wbs.controller.TestCaseController;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.wpShow.WPShow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Show GUI for Testcase.
 */
public class TestcaseShow {

    /**
     * Testcase to show.
     */
    private TestCase testCase;

    /**
     * Controller for the testcase.
     */
    private TestCaseController testCaseController;

    /**
     * TestcaseshowGUI Frame.
     */
    private TestcaseShowGUI testcaseShowGUI;

    /**
     * General strings.
     */
    private General generalStrings;

    /**
     * WPShow controller.
     */
    private WPShow wpShow;

    /**
     *
     * @param testCase
     * @param testcasecontroller
     * @param parent
     */
    public TestcaseShow(TestCase testCase, TestCaseController testcasecontroller, JFrame parent, WPShow wpShow){

        this.testCase = testCase;
        this.testCaseController = testcasecontroller;
        this.wpShow = wpShow;
        this.generalStrings = LocalizedStrings.getGeneralStrings();

        this.testcaseShowGUI = new TestcaseShowGUI(generalStrings.testcase(), this, parent, testCase, testcasecontroller);
    }

    /**
     * Returns the Listener for the save button.
     * @return the action listener
     */
    protected final ActionListener getBtnSaveListener(){
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(getValuesSet()) {
                    save();
                }
            }
        };
    }

    /**
     * Returns the Listener for the cancel button.
     * @return the action listener
     */
    protected final ActionListener getBtnCancelListener(){
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                testcaseShowGUI.setVisible(false);
                testcaseShowGUI.dispose();
            }
        };
    }

    /**
     * Returns the listener for the testcase execution button.
     * @return the action listener.
     */
    protected final ActionListener getBtnTestExecuteListener(){
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(getValuesSet()) {
                    save();
                    new TestcaseExecutionShow(testCaseController, testCase, testcaseShowGUI, false, wpShow);
                }
            }
        };
    }

    /**
     * Proof all required values.
     * @return
     */
    private boolean getValuesSet(){
        if(testcaseShowGUI.getTxfTestname().getText().trim().equals("")){
            testcaseShowGUI.showMessage(generalStrings.testnameMessage());
            return false;
        }
        else {
            if (testcaseShowGUI.getTxaDescription().getText().trim().equals("") ||
                    testcaseShowGUI.getTxaExpectedResult().getText().trim().equals("")) {
                testcaseShowGUI.showMessage(generalStrings.descriptionExpectedResultMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Save the values for the testcase.
     */
    private void save(){
        testCase.setName(testcaseShowGUI.getTxfTestname().getText().trim());
        testCase.setPrecondition(testcaseShowGUI.getTxaPrecondition().getText().trim());
        testCase.setDescription(testcaseShowGUI.getTxaDescription().getText().trim());
        testCase.setExpectedResult(testcaseShowGUI.getTxaExpectedResult().getText().trim());
        testCaseController.updateTestCase(testCase);
    }



}
