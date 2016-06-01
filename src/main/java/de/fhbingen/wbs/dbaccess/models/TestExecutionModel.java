package de.fhbingen.wbs.dbaccess.models;

import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.TestExecution;

import java.util.List;

/**
 * interface for all models handling requests regarding TestExecutions
 */
public interface TestExecutionModel {

    /**
     * Adds a new testexecution to the project.
     *
     * @param testexec
     *            The Testexecution which is added to the project.
     *
     * @return success of the action.
     */
    boolean addNewTestExecution(TestExecution testexec);

    /**
     * Gets all testexecutions for a specific testcase.
     *
     * @param testcase
     *            The testcase this execution belongs to.
     * @return Returns a list with all testexecutions from the project.
     */
    List<TestExecution> getExecutionsForTestCase(TestCase testcase);

    /**
     * Gets the latest testexecution.
     *
     * @param testcase
     *            The testcase the latest execution belongs to.
     * @return Returns the selected testexecution.
     */
    TestExecution getLastExecution(TestCase testcase);

    /**
     * Updates a testexecution.
     *
     * @param testexec
     *            The testexecution which has to be updated.
     * @return the success of the update.
     */
    boolean updateTestExecution(TestExecution testexec);
}
