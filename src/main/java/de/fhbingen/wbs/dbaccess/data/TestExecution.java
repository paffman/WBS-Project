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

package de.fhbingen.wbs.dbaccess.data;

import java.sql.Timestamp;

/**
 * A simple container class representing a TestExecution. It mirrors all the
 * database fields in the TestExecutions table.
 */
public class TestExecution {

    /** unique id of this testexecution */
    private int id;

    /** foreign key of the employee who did this testexecution*/
    private int employeeID;

    /** foreign key of the related testcase*/
    private int testcaseID;

    /** a remark for this testexectuion*/
    private String remark;

    /** the status of this testexecution*/
    private String status;

    /** name of the testcase*/
    private Timestamp time;

    /**
     * Returns the id of this testexecution.
     *
     * @return the id of this testexecution
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of this testexecution.
     *
     * @param id id of this testexecution
     */
    private void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the id of the related employee.
     *
     * @return the related employeeID
     */
    public int getEmployeeID() {
        return employeeID;
    }

    /**
     * Sets the id of the related employee.
     *
     * @param employeeID id of the related employee
     */
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Returns the id of the related testcase.
     *
     * @return the id of the related testcase
     */
    public int getTestcaseID() {
        return testcaseID;
    }

    /**
     * Sets the id of the related testcase.
     *
     * @param testcaseID the id of the related testcase
     */
    public void setTestcaseID(int testcaseID) {
        this.testcaseID = testcaseID;
    }

    /**
     * Returns the remark of this testexecution.
     *
     * @return the remark of this testexecution
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets the remark of this testexecution.
     *
     * @param remark the remark of this testexecution
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Returns the status of this textexecution.
     *
     * @return the status of this testexecution
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of this testexecution.
     *
     * @param status the status of this testexecution
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the time where this testexecution was done.
     *
     * @return the time where this testexecution was done
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * Sets the time where this testexecution was done.
     *
     * @param time the time where this testexecution was done
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     *
     * @param id
     * @param employeeID
     * @param testcaseID
     * @param remark
     * @param status
     * @param time
     */
    public TestExecution(int id, int employeeID, int testcaseID, String remark, String status, Timestamp time) {
        this.id = id;
        this.employeeID = employeeID;
        this.testcaseID = testcaseID;
        this.remark = remark;
        this.status = status;
        this.time = time;
    }

    public TestExecution() {

    }
}
