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

package interfaces;

import java.util.Date;
import java.util.List;

/* The interface for the workpackage model */
public interface WorkpackageModel {
    
    /*
     * A method to get all workpackages from the project.
     * @return Returns a list with all workpackages from the project.
     */
    public List<?> getWorkpackage();
    
    /*
     * TODO: was macht diese methode?
     * @param isToplevel Information if the wp has child workpackages and cannot have efforts.
     */
    public List<?> getWorkpackage(boolean isToplevel);
    
    /*
     * A method to get a single workpackage.
     * @param stringID The complete hierachical ID of a workpackage. Unique within a project.
     * @return Returns the selected workpackage.
     */
    public Workpackage getWorkpackage(String stringID);
    
    /*
     * A method to get all workpackages in a range.
     * @param from The start date for this workpackage.
     * @param to The end date for this workpackage
     */
    public List<?> getWorkpackagesInDateRange(Date from, Date to);
    
    /*
     * A method to update a workpackage.
     * @param wp The workpackage which has to be updated.
     */
    public void updateWorkpackage(Workpackage wp);
    
    /*
     * A method to delete a workpackage.
     * @param wpID The unique ID for a workpackage.
     */
    public void deleteWorkpackage(int wpID);
    
}
