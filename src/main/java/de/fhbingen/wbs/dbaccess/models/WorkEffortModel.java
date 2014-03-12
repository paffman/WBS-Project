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

import java.util.List;

import de.fhbingen.wbs.dbaccess.data.WorkEffort;

/** The interface for the work effort model. */
public interface WorkEffortModel {

    /**
     * A method to add a new work effort to the project.
     *
     * @param effort
     *            The effort which is added.
     */
    boolean addNewWorkEffort(WorkEffort effort);

    /**
     * A mehtod to get the work effort.
     *
     * @return Returns a list with the work effort from the project.
     */
    List<WorkEffort> getWorkEffort();

    /**
     * A method to get the work effort from a specific baseline.
     *
     * @param wpId
     *            The id from the specific workpackage.
     * @return Returns a list with the work effort from the specific baseline.
     */
    List<WorkEffort> getWorkEffort(int wpId);

    /**
     * A method to get the work effort from a specific baseline.
     *
     * @param wpId
     *            The id from the specific workpackage.
     * @return Returns the sum of efforts on the workpackage.
     */
    double getWorkEffortSum(int wpId);

}
