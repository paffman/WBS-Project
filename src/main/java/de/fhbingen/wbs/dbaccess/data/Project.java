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

public class Project {

    /** The project id. */
    private final int id;

    /** The id of the project leader. */
    private final int projectLeader;

    /** The name of the project */
    private final String name;

    /** The depth of the work package hierarchy (ex '1.1.0.0' has 4 levels) */
    private final int levels;

    public Project(int aID, int aLeader, String aName, int levelAmount) {
        this.id = aID;
        this.projectLeader = aLeader;
        this.name = aName;
        this.levels = levelAmount;
    }

    public Project(Project otherProject) {
        this.id = otherProject.id;
        this.projectLeader = otherProject.projectLeader;
        this.name = otherProject.name;
        this.levels = otherProject.levels;
    }

    /**
     * Gets the Project id.
     * @return The Project id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the project leader id.
     * @return The id of the Project leader
     */
    public int getProjectLeader() {
        return projectLeader;
    }

    /**
     * Gets the name of the project.
     * @return The project name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the depth of the work package hierarchy (ex '1.1.0.0' has 4 levels).
     * @return The depth of the work package hierarchy.
     */
    public int getLevels() {
        return levels;
    }

}
