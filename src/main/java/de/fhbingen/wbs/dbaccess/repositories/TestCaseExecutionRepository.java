package de.fhbingen.wbs.dbaccess.repositories;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.TestExecution;
import de.fhbingen.wbs.dbaccess.repositories.core.ParentToElementMappedCache;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestCaseExecutionRepository {
    private static class TestCaseExecutionParentToElementMappedCache extends ParentToElementMappedCache<TestExecution> {
        @Override
        protected List<TestExecution> loadAllElements() {
            return DBModelManager.getTestExecutionModel().getAllTestExecutions();
        }

        @Override
        protected Integer getParentId(TestExecution element) {
            return element.getTestcaseID();
        }

        @Override
        protected Integer getId(TestExecution element) {
            return element.getId();
        }
    }

    private static TestCaseExecutionParentToElementMappedCache testCaseExecutionMap;

    private TestCaseExecutionRepository() {}

    public static List<TestExecution> getAllTestCaseExecutions(TestCase testCase) {
        return getTestCaseExecutionMap().getAllByParentElement(testCase.getId());
    }

    public static TestExecution getLatestTestExecution(TestCase testCase) {
        List<TestExecution> allTestCaseExecutions = getAllTestCaseExecutions(testCase);

        if (!allTestCaseExecutions.isEmpty()) {
            Collections.sort(allTestCaseExecutions, new Comparator<TestExecution>() {
                @Override
                public int compare(TestExecution o1, TestExecution o2) {
                    return o2.getTime().compareTo(o1.getTime());
                }
            });

            return allTestCaseExecutions.get(0);
        }

        return null;
    }

    public static TestCaseExecutionParentToElementMappedCache getTestCaseExecutionMap() {
        if (testCaseExecutionMap == null) {
            testCaseExecutionMap = new TestCaseExecutionParentToElementMappedCache();
        }

        return testCaseExecutionMap;
    }

    public static boolean updateTestCaseExecution(TestExecution testExecution) {
        testCaseExecutionMap.setElement(testExecution);
        return DBModelManager.getTestExecutionModel().updateTestExecution(testExecution);
    }

    public static void reloadCache() {
        testCaseExecutionMap.loadCache();
    }
}
