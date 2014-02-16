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

package de.fhbingen.wbs.dbaccess.models;

import java.util.List;

import de.fhbingen.wbs.dbaccess.data.Conflict;

/**The interface for the conflict model*/
public interface ConflictsModel {

    /**
     * A method to add a new conflict to the project.
     * @param conflict The conflict which is added to the project.
     * @return Success of insert action.
     */
    public boolean addNewConflict(Conflict conflict);

    /**
     * A method to get all conflicts.
     * @return Returns a list with all conflicts in the project.
     */
    public List<Conflict> getConflicts();

    /**
     * A method to delete all conflicts.
     */
    public void deleteConflicts();

    /**
     * A method to delete a specific conflict.
     * @param id The id from the conflict which is deleted from the project.
     */
    public void deleteConflict(int id);

}
