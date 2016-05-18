package de.fhbingen.wbs.controller;

import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.globals.Workpackage;

import java.util.List;


public class TestCaseController {
    private Workpackage workpackage;

    public TestCaseController(Workpackage workpackage) {
        this.workpackage = workpackage;
    }

    public List<TestCase> getAllTestCases() {
        return this.workpackage.getAllTestCases();
    }

    //TODO add methods for running tests (executions etc.)
}
