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

import de.fhbingen.wbs.dbaccess.data.WorkpackageAllocation;
import de.fhbingen.wbs.dbaccess.data.Workpackage;

/** The interface for the workpackage allocation model. */
public interface WorkpackageAllocationModel {

    /**
     * A method to add a new workpackage allocation.
     *
     * @param wpAllocation
     *            The allocation which is added.
     * @return success of the action.
     */
    boolean addNewWorkpackageAllocation(WorkpackageAllocation wpAllocation);

    /**
     * A method to get all workpackage allocations.
     *
     * @return Returns a list with all workpackage allocations of the project.
     */
    List<WorkpackageAllocation> getWorkpackageAllocation();

    /**
     * A method to get all workpackage allocations from a specific workpackage.
     *
     * @param fidWP
     *            The id from the specific workpackage.
     * @return Returns a list with the work package allocations from the
     *         selected workpackage.
     */
    List<WorkpackageAllocation> getWorkpackageAllocation(int fidWP);

    /**
     * A method to get all workpackages for a single employee.
     *
     * @param fidEmp
     *            Id of the employee.
     * @return Returns a List of all Workpackages allocated to the employee.
     */
    List<Workpackage> getWorkpackageAllocationJoinWP(int fidEmp);

    /**
     * A method to get all workpackages for a single employee within a certain
     * span of pointOfTime.
     *
     * @param fidEmp
     *            Id of the employee.
     * @param from
     *            first Date
     * @param to
     *            second Date
     * @return Returns a List of all Workpackages, within the given dates,
     *         allocated to the employee.
     */
    List<Workpackage> getWorkpackageAllocationJoinWP(int fidEmp, Date from,
            Date to);

    /**
     * A method to delete a workpackage allocation.
     *
     * @param employeeID
     *            The id from the employee.
     * @param workpackageID
     *            The id from the workpackage.
     * @return success of deletion.
     */
    boolean deleteWorkpackageAllocation(int employeeID, int workpackageID);

}
