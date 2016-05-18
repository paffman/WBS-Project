package de.fhbingen.wbs.dbaccess.models;

/**
 * interface for all models handling requests regarding TestExecutions
 */
public interface TestExecutionModel {

    /**
     * Adds a new testexecution to the project.
     *
     * @param testexec
     *            The Testexecution which is added to the project.
     * @param testcase
     *            The testcase this execution belongs to.
     * @param executor
     *            The Employee which executed this TestExecution.
     *
     * @return success of the action.
     */
    boolean addNewTestExecution(TestExecution testexec, TestCase testcase, Employee executor);



    /**
     * Gets all testexecutions for a specific testcase.
     *
     * @param testcase
     *            The testcase this execution belongs to.
     * @return Returns a list with all testexecutions from the project.
     */
    List<TestExecution> getExecutionsForTestCase(TestCase testcase);



    /**
     * Gets all testexecutions from the project.
     *
     * @return Returns a list with all testexecutions from the project.
     */
    List<TestExecution> getAllTestExecutions();



    /**
     * Gets a single testexecution.
     *
     * @param id
     *            The unique id of a testexecution.
     * @return Returns the selected testexecution.
     */
    TestExecution getTestExecution(int id);


    /**
     * Updates a testexecution.
     *
     * @param testexec
     *            The testexecution which has to be updated.
     * @return the success of the update.
     */
    boolean updateTestExecution(TestCase testexec);




}
