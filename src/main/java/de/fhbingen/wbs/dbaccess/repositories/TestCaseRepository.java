package de.fhbingen.wbs.dbaccess.repositories;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.Workpackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jdepoix on 01.06.16.
 */
public class TestCaseRepository {
    private static Map<Integer, Map<Integer, TestCase>> workpackageTestCaseMap = getWorkpackageTestCasesMap();

    private TestCaseRepository() {}

    private static Map<Integer, Map<Integer, TestCase>> getWorkpackageTestCasesMap() {
        Map<Integer, Map<Integer, TestCase>> newWorkpackageTestCaseMap = new HashMap<>();
        Map<Integer, TestCase> currentTestCaseMap = null;

        for (TestCase testCase : DBModelManager.getTestCaseModel().getAllTestCases()) {
            if ((currentTestCaseMap = newWorkpackageTestCaseMap.get(testCase.getWorkpackageID())) == null) {
                currentTestCaseMap = new HashMap<>();
                currentTestCaseMap.put(testCase.getId(), testCase);
                newWorkpackageTestCaseMap.put(testCase.getWorkpackageID(), currentTestCaseMap);
            } else {
                currentTestCaseMap.put(testCase.getId(), testCase);
            }
        }

        return newWorkpackageTestCaseMap;
    }

    public static List<TestCase> getAllTestCases(Workpackage workpackage) {
        return new ArrayList<>(workpackageTestCaseMap.get(workpackage.getId()).values());
    }

    public static void reloadAll() {
        TestCaseRepository.workpackageTestCaseMap = TestCaseRepository.getWorkpackageTestCasesMap();
    }
}
