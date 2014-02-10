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
 * A simple container class representing a Dependency. It mirrors all the
 * dependencies fields in the dependencies table.
 */
public class Dependency{
    
    /** Work package which must be done before another work package. */
    private int fid_wp_predecessor;
    
    /** Work package which has a work package that must be done before it. */
    private int fid_wp_successor;
    
    /**
     * Creates a <code>Dependency</code> based on a <code>ResultSet</code>.
     *
     * @param resSet The result set containing the data
     * @return A <code>Dependency</code> object
     */
    public static final Dependency fromResultSet(ResultSet resSet){
        Dependency dp=new Dependency();

        try {
            dp.setFid_wp_predecessor(resSet.getInt("fid_wp_predecessor"));
            dp.setFid_wp_successor(resSet.getInt("fid_wp_successor"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dp;
    }
    
    /**
     * Returns the work package which has to be done before.
     *
     * @return The work packages id
     */
    public int getFid_wp_predecessor() {
        return fid_wp_predecessor;
    }

    /**
     * Sets work package which has to be done before.
     *
     * @param fid_wp_predeccessor The id of the work package
     */
    public void setFid_wp_predecessor(int fid_wp_predecessor) {
        this.fid_wp_predecessor = fid_wp_predecessor;
    }

    /**
     * Returns the work package which has to be done after.
     *
     * @return The workpackages id
     */
    public int getFid_wp_successor() {
        return fid_wp_successor;
    }

    /**
     * Sets work package which has to be done after.
     *
     * @param fid_wp_successor The id of the work package
     */
    public void setFid_wp_successor(int fid_wp_successor) {
        this.fid_wp_successor = fid_wp_successor;
    }

    /**
     * Convert the dependencies to a String
     *
     * @return the dependencies as String
     */
    public String toString(){
        return fid_wp_predecessor+" "+fid_wp_successor;
    }
}
