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


package dbaccess.models;

import java.util.List;

import dbaccess.data.Dependency;

/** The interface for the dependencies model */
public interface DependenciesModel {
	
    /**
     * A method to add a new dependency to the project.
     * @param dependency The dependency which is added to the project.
     */
    public void addNewDependency(Dependency dependency);
    
    /**
     * A method to get the dependencies.
     * @return Returns a list with all dependencies.
     */
	public List<Dependency> getDependency();

	/**
	 * A method to delete a dependency.
	 * @param predecessorWpID Workpackage which must be done before another workpackage.
	 * @param succesorWpID Workpackage which has a workpackage that must be done before it.
	 */
	public void deleteDependency(int predecessorWpID, int successorWpID);
	
}
