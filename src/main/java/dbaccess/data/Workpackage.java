/*
 * The WBS-­Tool is a project managment tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-­Bingen
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

package dbaccess.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a work package.
 */
public class Workpackage {

    /** Work package id. */
    private int id;

    /** A string representation of the id. */
    private String stringID;

    /** The corresponding project id. */
    private int projectID;

    /** id of the responsible employee. */
    private int employeeID;

    /** id of the parent work package. */
    private int parentID;

    /** The position in the parent hierarchy. */
    private int positionID;

    /** The name of the work package. */
    private String name;

    /** The description of the work package. */
    private String description;

    /**
     * Creates a <code>Workpackage</code> based on a <code>ResultSet</code>.
     *
     * @param resSet The result set containing the data
     * @return A <code>Workpackage</code> object
     */
    public static final Workpackage fromResultSet(final ResultSet resSet) {
        Workpackage wp = new Workpackage();

        try {
            wp.setId(resSet.getInt("id"));
            wp.setStringID(resSet.getString("string_id"));
            wp.setProjectID(resSet.getInt("fid_project"));
            wp.setEmployeeID(resSet.getInt("fid_resp_emp"));
            wp.setParentID(resSet.getInt("fid_parent"));
            wp.setPositionID(resSet.getInt("parent_order_id"));
            wp.setName(resSet.getString("name"));
            wp.setDescription(resSet.getString("description"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wp;
    }

    /**
     * Returns the ID.
     *
     * @return The ID
     */
    public final int getId() {
        return id;
    }

    /**
     * Sets the ID.
     *
     * @param id The new ID
     */
    public final void setId(final int id) {
        this.id = id;
    }

    /**
     * Returns the string ID.
     *
     * @return The string representation of the ID
     */
    public final String getStringID() {
        return stringID;
    }

    /**
     * Sets the string representation of the id.
     *
     * @param stringID A string ID. (example: "1.1.0.0")
     */
    public final void setStringID(final String stringID) {
        this.stringID = stringID;
    }

    /**
     * Returns the project id corresponding to this work package.
     *
     * @return The project id
     */
    public final int getProjectID() {
        return projectID;
    }

    /**
     * Sets the project id corresponding to this work package.
     *
     * @param projectID The project id
     */
    public final void setProjectID(final int projectID) {
        this.projectID = projectID;
    }

    /**
     * Returns the id of the responsible employee.
     *
     * @return The id of the responsible employee
     */
    public final int getEmployeeID() {
        return employeeID;
    }

    /**
     * Sets the id of the responsible employee.
     * @param employeeID The id of the responsible employee
     */
    public final void setEmployeeID(final int employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Returns the id of the parent work package.
     *
     * @return ID of the parent work package
     */
    public final int getParentID() {
        return parentID;
    }

    /**
     * Sets the id of the parent work package.
     * @param parentID The id of the parent work package.
     */
    public final void setParentID(final int parentID) {
        this.parentID = parentID;
    }

    /**
     * Returns the position of this work package in the parent hierarchy.
     *
     * @return The position in the parent hierarchy
     */
    public final int getPositionID() {
        return positionID;
    }

    /**
     * Sets the position in the parent hierarchy.
     *
     * @param positionID The new position in the parent hierarchy
     */
    public final void setPositionID(final int positionID) {
        this.positionID = positionID;
    }

    /**
     * Retursn the name of the work package.
     *
     * @return The name of the work package.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name of the work package.
     *
     * @param name The name of the work package
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the work package description.
     *
     * @return The work package description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the work package description.
     *
     * @param description The work package description
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

}
