package de.fhbingen.wbs.dbaccess.models;

/**
 * interface for all models handling requests regarding TestCases
 */
public interface TestCaseModel {

    /**
     * Adds a new testcase to the project.
     *
     * @param testcase
     *            The Testcase which is added to the project.
     * @return success of the action.
     */
    boolean addNewTestCase(TestCase testcase);



    /**
     * Gets all testcases from the project.
     *
     * @return Returns a list with all testcases from the project.
     */
    List<TestCase> getAllTestCases();



    /**
     * Gets a single testcase.
     *
     * @param id
     *            The unique id of a testcase.
     * @return Returns the selected testcase.
     */
    TestCase getTestCase(int id);


    /**
     * Updates a testcase.
     *
     * @param tc
     *            The testcase which has to be updated.
     * @return the success of the update.
     */
    boolean updateTestCase(TestCase tc);




}
