/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-Bingen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.dbaccess;

import de.fhbingen.wbs.dbaccess.data.TestExecution;
import de.fhbingen.wbs.dbaccess.models.*;
import de.fhbingen.wbs.dbaccess.models.mysql.*;

/**
 * This class manages all different models.
 */
public class DBModelManager {

    private DBModelManager() {
    };

    private static AnalyseDataModel analyseDataModel =
            new MySQLAnalyseDataModel();
    private static BaselineModel baselineModel = new MySQLBaselineModel();
    private static ConflictsModel conflictsModel = new MySQLConflictsModel();
    private static DependenciesModel dependenciesModel =
            new MySQLDependenciesModel();
    private static EmployeeCalendarModel employeeCalendarModel =
            new MySQLEmployeeCalendarModel();
    private static EmployeesModel employeesModel = new MySQLEmployeesModel();
    private static HolidaysCalendarModel holidaysCalendarModel =
            new MySQLHolidaysCalendarModel();
    private static PlannedValueModel plannedValueModel =
            new MySQLPlannedValueModel();
    private static ProjectModel projectModel = new MySQLProjectModel();
    private static SemaphoreModel semaphorenModel = new MySQLSemaphoreModel();
    private static WorkEffortModel workEffortModel = new MySQLWorkEffortModel();
    private static WorkpackageAllocationModel workpackageAllocationModel =
            new MySQLWorkpackageAllocationModel();
    private static WorkpackageModel workpackageModel =
            new MySQLWorkpackageModel();
    private static TestCaseModel testCaseModel = new MySQLTestCaseModel();
    private static TestExecutionModel testExecutionModel = new MySQLTestExecutionModel();

    /**
     * This method provide the analyse data model.
     *
     * @return Returns the actual analyse data model.
     */
    public static AnalyseDataModel getAnalyseDataModel() {
        return analyseDataModel;
    }

    /**
     * This method provide the baseline model.
     *
     * @return Returns the actual baseline model.
     */
    public static BaselineModel getBaselineModel() {
        return baselineModel;
    }

    /**
     * This method provide the conflicts model.
     *
     * @return Returns the actual conflicts model.
     */
    public static ConflictsModel getConflictsModel() {
        return conflictsModel;
    }

    /**
     * This method provide the dependencies model.
     *
     * @return Returns the actual dependencies model.
     */
    public static DependenciesModel getDependenciesModel() {
        return dependenciesModel;
    }

    /**
     * This method provide the employee calendar model.
     *
     * @return Returns the actual employee calendar model.
     */
    public static EmployeeCalendarModel getEmployeeCalendarModel() {
        return employeeCalendarModel;
    }

    /**
     * This method provide the employees model.
     *
     * @return Returns the actual employees model.
     */
    public static EmployeesModel getEmployeesModel() {
        return employeesModel;
    }

    /**
     * This method provide the holiday calendar model.
     *
     * @return Returns the actual holiday calendar model.
     */
    public static HolidaysCalendarModel getHolidaysCalendarModel() {
        return holidaysCalendarModel;
    }

    /**
     * This method provide the planned value model.
     *
     * @return Returns the actual planned value model.
     */
    public static PlannedValueModel getPlannedValueModel() {
        return plannedValueModel;
    }

    /**
     * This method provide the project model.
     *
     * @return Returns the actual project model.
     */
    public static ProjectModel getProjectModel() {
        return projectModel;
    }

    /**
     * This method provide the semaphore model.
     *
     * @return Returns the actual semaphore data model.
     */
    public static SemaphoreModel getSemaphoreModel() {
        return semaphorenModel;
    }

    /**
     * This method provide the work effort model.
     *
     * @return Returns the actual work effort model.
     */
    public static WorkEffortModel getWorkEffortModel() {
        return workEffortModel;
    }

    /**
     * This method provide the workpackage allocation model.
     *
     * @return Returns the actual workpackage allocation model.
     */
    public static WorkpackageAllocationModel getWorkpackageAllocationModel() {
        return workpackageAllocationModel;
    }

    /**
     * This method provide the workpackage model.
     *
     * @return Returns the actual workpackage data model.
     */
    public static WorkpackageModel getWorkpackageModel() {
        return workpackageModel;
    }

    public static TestCaseModel getTestCaseModel() {
        return testCaseModel;
    }

    public static TestExecutionModel getTestExecutionModel() {
        return testExecutionModel;
    }
}
