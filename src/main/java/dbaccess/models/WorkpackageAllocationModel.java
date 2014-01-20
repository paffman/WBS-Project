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

package dbaccess.models;

import java.util.Date;
import java.util.List;

import dbaccess.data.WorkpackageAllocation;

//TODO: Andere Namen für Join-Methoden ausdenken
/**The interface for the workpackage allocation model*/
public interface WorkpackageAllocationModel {

    /**
     * A method to add a new workpackage allocation.
     * @param wpAllocation The allocation which is added.
     */
    public void addNewWorkpackageAllocation(WorkpackageAllocation wpAllocation);
    
    /**
     * A method to get all workpackage allocations.
     * @return Returns a list with all workpackage allocations of the project.
     */
    public List<?> getWorkpackageAllocation();
    
    /**
     * A method to get all workpackage allocations from a specific workpackage.
     * @param fidWP The id from the specific workpackage.
     * @return Returns a list with the work package allocations from the selected workpackage.
     */
    public List<?> getWorkpackageAllocation(int fidWP);
    
    /**
     * TODO: Was macht die methode genau? alle zuweisungen eines bestimmten arbeiters zurückgeben? dann müsste der parameter anders heisen?
     */
    public List<?> getWorkpackageAllocationEmployees(int fidWP);
    
    /**
     * TODO: was macht das? + anderen Namen ausdenken
     */
    public List<?> getWorkpackageAllocationJoinWP();
    
    /**
     * TODO: was macht das? + anderen Namen ausdenken
     */
    public List<?> getWorkpackageAllocationJoinWP(Date from, Date to);
    
    /**
     * A method to delete a workpackage allocation
     * @param employeeID The id from the employee.
     * @param workpackageID The id from the workpackage.
     */
    public void deleteWorkpackageAllocation(int employeeID, int workpackageID);
    
}
