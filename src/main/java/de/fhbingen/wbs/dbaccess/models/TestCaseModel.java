package de.fhbingen.wbs.dbaccess.models;

import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.Workpackage;

import java.util.List;

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
     * Gets all testcases belonging to a workpackage and all its childs.
     * @param wp
     *            The workpackage we want all testcases for.
     * @return Returns a list with all queried testcases.
     */
    List<TestCase> getAllTestCases(Workpackage wp);


/*  TODO: do we need this method?
    /**
     * Gets a single testcase.
     *
     * @param id
     *            The unique id of a testcase.
     * @return Returns the selected testcase.
     */
//    TestCase getTestCase(int id);


    /**
     * Updates a testcase.
     *
     * @param tc
     *            The testcase which has to be updated.
     * @return the success of the update.
     */
    boolean updateTestCase(TestCase tc);




}
