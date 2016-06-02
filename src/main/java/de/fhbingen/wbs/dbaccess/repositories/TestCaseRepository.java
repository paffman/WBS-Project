package de.fhbingen.wbs.dbaccess.repositories;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.Workpackage;
import de.fhbingen.wbs.dbaccess.repositories.core.ParentToElementMappedCache;

import java.util.List;

public class TestCaseRepository {
    private static class TestCaseParentToElementMappedCache extends ParentToElementMappedCache<TestCase> {
        @Override
        protected List<TestCase> loadAllElements() {
            return DBModelManager.getTestCaseModel().getAllTestCases();
        }

        @Override
        protected Integer getParentId(TestCase element) {
            return element.getWorkpackageID();
        }

        @Override
        protected Integer getId(TestCase element) {
            return element.getId();
        }
    }

    private static TestCaseParentToElementMappedCache workpackageTestCaseMap;

    private TestCaseRepository() {}

    public static List<TestCase> getAllTestCases(Workpackage workpackage) {
        return getWorkpackageTestCaseMap().getAllByParentElement(workpackage.getId());
    }

    public static TestCaseParentToElementMappedCache getWorkpackageTestCaseMap() {
        if (workpackageTestCaseMap == null) {
            workpackageTestCaseMap = new TestCaseParentToElementMappedCache();
        }

        return workpackageTestCaseMap;
    }

    public static void updateTestCase(TestCase testCase) {
        workpackageTestCaseMap.setElement(testCase);
        DBModelManager.getTestCaseModel().updateTestCase(testCase);
    }
}
