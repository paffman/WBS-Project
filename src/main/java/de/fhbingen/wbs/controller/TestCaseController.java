package de.fhbingen.wbs.controller;

import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.TestExecution;
import de.fhbingen.wbs.dbaccess.models.mysql.MySQLTestCaseModel;
import de.fhbingen.wbs.dbaccess.models.mysql.MySQLTestExecutionModel;
import de.fhbingen.wbs.dbaccess.repositories.TestCaseExecutionRepository;
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
        return tc.getLatestExecution();
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
        MySQLTestCaseModel sqltcm = new MySQLTestCaseModel();
        return sqltcm.addNewTestCase(tc);
    }


    /**
     *
     * @param tc
     *          TestCase to update
     * @return
     *          success of this operation
     */

    public boolean updateTestCase(TestCase tc){
        MySQLTestCaseModel sqltcm = new MySQLTestCaseModel();
        return sqltcm.updateTestCase(tc);
    }



    /**
     *
     * @param te
     *          TestExecution to add
     * @return
     *          success of this operation
     */

    public boolean addTestExecution(TestExecution te){
        MySQLTestExecutionModel sqlexecm = new MySQLTestExecutionModel();
        return sqlexecm.addNewTestExecution(te);
    }



    /**
     *
     * @param te
     *          TestExecution to update
     * @return
     *          success of this operation
     */

    public boolean updateTestExecution(TestExecution te){
        MySQLTestExecutionModel sqlexecm = new MySQLTestExecutionModel();
        return sqlexecm.updateTestExecution(te);
    }

    /**
     * Get testcases for this workpackage.
     * @return List of testcases
     */
    public List<TestCase> getTestCases(){
        return this.workpackage.getTestCases();
    }


}
