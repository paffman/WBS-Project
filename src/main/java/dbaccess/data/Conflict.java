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
 * A simple container class representing a Conflict. It mirrors all the
 * conflict fields in the conflicts table.
 */
public class Conflict {
    
    /** baseline id. */
    private int id;
    
    /** First workpackage involved in the conflict. */
    private int fid_wp;
    
    /** Second workpackage involved in the conflict. Not mandatory. */
    private int fid_wp_affected;
    
    /** Employee that caused the conflict.', */
    private int fid_emp;
    
    /** Reason for the conflict. */
    private int reason;
    
    /** Date when the conflict first appeared. */
    private Date occurence_date;
    
    /**
     * Creates a <code>Conflict</code> based on a <code>ResultSet</code>.
     *
     * @param resSet The result set containing the data
     * @return A <code>Conflict</code> object
     */
    public static final Conflict fromResultSet(ResultSet resSet){
        Conflict cf=new Conflict();

        try {
            cf.setId(resSet.getInt("id"));
            cf.setFid_wp(resSet.getInt("fid_wp"));
            cf.setFid_wp_affected(resSet.getInt("fid_wp_affected"));
            cf.setFid_emp(resSet.getInt("fid_emp"));
            cf.setReason(resSet.getInt("reason"));
            cf.setOccurence_date(resSet.getDate("occurence_date"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return cf;
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
     * @param id The baselines id
     */
    private void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the if from the workpackage.
     *
     * @return The workpackages id
     */
    public int getFid_wp() {
        return fid_wp;
    }

    /**
     * Sets the id from the referenced work package.
     *
     * @param fid_wp The referenced work packages id
     */
    public void setFid_wp(int fid_wp) {
        this.fid_wp = fid_wp;
    }

    /**
     * Returns the affected work package.
     *
     * @return The affected work package
     */
    public int getFid_wp_affected() {
        return fid_wp_affected;
    }

    /**
     * Sets the id from the affected work package.
     *
     * @param fid_wp_affected The affected work packages id.
     */
    public void setFid_wp_affected(int fid_wp_affected) {
        this.fid_wp_affected = fid_wp_affected;
    }

    /**
     * Returns the id from the referenced employee.
     *
     * @return The employees id
     */
    public int getFid_emp() {
        return fid_emp;
    }

    /**
     * Sets the id from the referenced employee.
     *
     * @param fid_emp The employees id
     */
    public void setFid_emp(int fid_emp) {
        this.fid_emp = fid_emp;
    }

    /**
     * Returns the reason id of the conflict.
     *
     * @return The reasons id
     */
    public int getReason() {
        return reason;
    }

    /**
     * Sets the reason of the conflict.
     *
     * @param reason The reasons id
     */
    public void setReason(int reason) {
        this.reason = reason;
    }

    /**
     * Returns the occurence date.
     *
     * @return The occurence date
     */
    public Date getOccurence_date() {
        return occurence_date;
    }

    /**
     * Sets the occurenced date of the conflict.
     *
     * @param occurence_date The occurence date
     */
    public void setOccurence_date(Date occurence_date) {
        this.occurence_date = occurence_date;
    }

    /**
     * Convert the conflict to a String
     *
     * @return the conflict as String
     */
    public String toString(){
        return id+" "+fid_wp+" "+fid_wp_affected+" "+fid_emp+" "+reason+" "+occurence_date;
    }    
}
