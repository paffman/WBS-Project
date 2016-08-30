package de.fhbingen.wbs.controller;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.TestExecution;
import de.fhbingen.wbs.dbaccess.repositories.TestCaseExecutionRepository;
import de.fhbingen.wbs.dbaccess.repositories.TestCaseRepository;
import de.fhbingen.wbs.globals.Workpackage;

import java.util.List;


public class TestCaseController {

    private Workpackage workpackage;

    public TestCaseController(Workpackage workpackage) {
        this.workpackage = workpackage;
    }

    /**
     * @return
     *          List of all Testcases
     */
    public List<TestCase> getAllTestCases() {
        return this.workpackage.getAllTestCases();
    }

    /**
     * @param tc
     *          TestCase for which we want to fetch the latest execution
     * @return
     *          latest execution for this Testcase
     */
    public TestExecution getLatestExecutionForTestCase(TestCase tc){
        return TestCaseExecutionRepository.getLatestTestExecution(tc);
    }

    /**
     *
     * @param tc
     *          TestCase for wich we want to fetch the executions
     * @return
     *          List of executions for this Testcase
     */
    public List<TestExecution> getTestExecutionsForTestCase(TestCase tc){
        return TestCaseExecutionRepository.getAllTestCaseExecutions(tc);
    }

    /**
     *
     * @param tc
     *          TestCase (belonging to this objects wp) to add to db
     * @return
     *          success of this operation
     */
    public boolean addTestCase(TestCase tc){
        boolean result = DBModelManager.getTestCaseModel().addNewTestCase(tc);
        TestCaseRepository.reloadCache();

        return result;
    }

    /**
     *
     * @param tc
     *          TestCase to update
     * @return
     *          success of this operation
     */
    public boolean updateTestCase(TestCase tc){

        return TestCaseRepository.updateTestCase(tc);
    }

    /**
     *
     * @param te
     *          TestExecution to add
     * @return
     *          success of this operation
     */
    public boolean addTestExecution(TestExecution te){

        boolean result = DBModelManager.getTestExecutionModel().addNewTestExecution(te);
        TestCaseExecutionRepository.reloadCache();

        return result;
    }

    /**
     *
     * @param te
     *          TestExecution to update
     * @return
     *          success of this operation
     */
    public boolean updateTestExecution(TestExecution te){
        return TestCaseExecutionRepository.updateTestCaseExecution(te);
    }

    /**
     * Get testcases for this workpackage.
     * @return List of testcases
     */
    public List<TestCase> getTestCases(){
        return this.workpackage.getTestCases();
    }
}
