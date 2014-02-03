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
import java.util.Date;

/**
 * A simple container class representing a Baseline. It mirrors all the baseline
 * fields in the baseline table.
 */
public class Baseline {

    /** baseline id. */
    private int id;

    /** Reference on the project, to which the baseline belongs. */
    private int fid_project;

    /** Date when Baseline was created. */
    private Date bl_date;

    /** Description of the baseline. */
    private String description;

    /**
     * Creates a <code>Baseline</code> based on a <code>ResultSet</code>.
     * 
     * @param resSet
     *            The result set containing the data
     * @return A <code>Baseline</code> object
     */
    public static final Baseline fromResultSet(ResultSet resSet) {
        Baseline bl = new Baseline();

        try {
            bl.setId(resSet.getInt("id"));
            bl.setFid_project(resSet.getInt("fid_project"));
            bl.setBl_date(resSet.getDate("bl_date"));
            bl.setDescription(resSet.getString("description"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bl;
    }

    /**
     * Returns the ID.
     * 
     * @return The ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the baseline.
     * 
     * @param id
     *            The id of the baseline
     */
    private void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the project id.
     * 
     * @return The project id
     */
    public int getFid_project() {
        return fid_project;
    }

    /**
     * Sets the reference to a project.
     * 
     * @param fid_project
     *            The fid from the project
     */
    public void setFid_project(int fid_project) {
        this.fid_project = fid_project;
    }

    /**
     * Returns the date when the database was created.
     * 
     * @return The date when the database was created
     */
    public Date getBl_date() {
        return bl_date;
    }

    /**
     * Sets the date of the baseline.
     * 
     * @param bl_date
     *            The baselines date
     */
    public void setBl_date(Date bl_date) {
        this.bl_date = bl_date;
    }

    /**
     * Returns description of the baseline
     * 
     * @return The baselines description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the baseline.
     * 
     * @param description
     *            The description of the baseline
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Convert the baseline to a String
     * 
     * @return the baseline as String
     */
    public String toString() {
        return id + " " + "fid_project" + " " + bl_date + " " + description;
    }
}
