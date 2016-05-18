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

/**
 * A simple container class representing a TestCase. It mirrors all the
 * database fields in the TestCases table.
 */
public class TestCase {
    

    /** unique id of the TestCase */
    private int id;

    /** foreign key of the Workpackage*/
    private int workpackageID;

    /** a textual description of the precondition for this testcase*/
    private String precondition;

    /** describes and specifies the testcase*/
    private String description;

    /** the result which is expected from this testcase*/
    private String expectedResult;

    /** name of the testcase*/
    private String name;

    /**
     * Returns the id of this testcase.
     *
     * @return the id of this testcase
     */
    public int getId() {return id;}

    /**
     * Sets the id of this testcase.
     *
     * @param id the id of this testcase.
     */
    private void setId(int id) { this.id=id;}

    /**
     * Returns the id of the related workpackage.
     *
     * @return the id of the related workpackage
     */
    public int getWorkpackageID() { return this.workpackageID;}

    /**
     * Sets the id of the related workpackage.
     *
     * @param workpackageID the id of the related workpackage
     */
    public void setWorkpackageID(int workpackageID) {this.workpackageID = workpackageID; }

    /**
     * Returns the precondition of this testcase.
     *
     * @return the precondition of this testcase
     */
    public String getPrecondition() {
        return precondition;
    }

    /**
     * Sets the precondition of this testcase.
     *
     * @param precondition the precondition of this testcase
     */
    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    /**
     * Returns the description of this testcase.
     *
     * @return the description of this testcase.
     */
    public String getDescription() { return this.description; }

    /**
     * Sets the description of this testcase.
     *
     * @param description the description of this testcase
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns the expected result of this testcase.
     *
     * @return the expected result
     */
    public String getExpectedResult() {
        return expectedResult;
    }

    /**
     * Sets the expected result of this testcase.
     *
     * @param expectedResult the expected result of this testcase
     */
    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    /**
     * Returns the name of this testcase.
     *
     * @return the name of this testcase
     */
    public String getName() {return this.name;}

    /**
     * Sets the name of this testcase.
     *
     * @param name the name of this testcase
     */
    public void setName(String name) { this.name = name; }

}
