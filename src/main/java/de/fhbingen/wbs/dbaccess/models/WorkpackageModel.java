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

package de.fhbingen.wbs.dbaccess.models;

import java.util.Date;
import java.util.List;

import de.fhbingen.wbs.dbaccess.data.Workpackage;

/**
 * Representing the DB-Interface to a work package.
 */
public interface WorkpackageModel {

    /**
     * Adds a new workpackage to the project.
     *
     * @param workpackage
     *            The workpackage which is added to the project.
     * @return success of the action.
     */
    boolean addNewWorkpackage(Workpackage workpackage);

    /**
     * Gets all workpackages from the project.
     *
     * @return Returns a list with all workpackages from the project.
     */
    List<Workpackage> getWorkpackage();

    /**
     * Gets specific workpackages from the project.
     *
     * @param onlyLeaves
     *            Information if the method should return all workpackages or
     *            only the leaves.
     * @return If this parameter is true, the method returns a list with only
     *         the leaves from the project. Otherwise the method returns all
     *         workpackages.
     */
    List<Workpackage> getWorkpackage(boolean onlyLeaves);

    /**
     * Gets a single workpackage.
     *
     * @param stringID
     *            The complete hierachical ID of a workpackage. Unique within a
     *            project.
     * @return Returns the selected workpackage.
     */
    Workpackage getWorkpackage(String stringID);

    /**
     * Gets a single workpackage.
     *
     * @param id
     *            The unique id of a workpackage.
     * @return Returns the selected workpackage.
     */
    Workpackage getWorkpackage(int id);

    /**
     * Gets all workpackages in a range.
     *
     * @param from
     *            The start date for this workpackage
     * @param to
     *            The end date for this workpackage
     * @return A list of work packages
     */
    List<Workpackage> getWorkpackagesInDateRange(Date from, Date to);

    /**
     * Updates a workpackage.
     *
     * @param wp
     *            The workpackage which has to be updated.
     * @return the success of the update.
     */
    boolean updateWorkpackage(Workpackage wp);

    /**
     * Deletes a workpackage.
     *
     * @param stringID
     *            The complete hierachical ID of a workpackage. Unique within a
     *            project.
     * @return success of the deletion
     */
    boolean deleteWorkpackage(String stringID);

    /**
     * Deletes a workpackage.
     *
     * @param id
     *            Unique id of the workpackage.
     * @return success of the deletion
     */
    boolean deleteWorkpackage(int id);

    /**
     * updates the stringId of the given workpackage
     *
     * @param wp workpackage to update
     * @return
     */
    boolean updateStringId(Workpackage wp);
}
