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

package dbaccess.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * A simple container class representing a planned value. It mirrors all the
 * database fields in the planned_value table.
 */
public class PlannedValue {

    /** Unique id of a planned value. */
    private int id;

    /** Foreign id of a workpackage. */
    private int fid_wp;

    /** Date for planned value. */
    private Date pv_date;

    /** The planned value. */
    private double pv;

    /**
     * @return the fid_wp
     */
    public final int getFid_wp() {
        return fid_wp;
    }

    /**
     * @param fid_wp
     *            the fid_wp to set
     */
    public final void setFid_wp(final int fid_wp) {
        this.fid_wp = fid_wp;
    }

    /**
     * @return the pv_date
     */
    public final Date getPv_date() {
        return pv_date;
    }

    /**
     * @param pv_date
     *            the pv_date to set
     */
    public final void setPv_date(final Date pv_date) {
        this.pv_date = pv_date;
    }

    /**
     * @return the pv
     */
    public final double getPv() {
        return pv;
    }

    /**
     * @param pv
     *            the pv to set
     */
    public final void setPv(final double pv) {
        this.pv = pv;
    }

    /**
     * @return the id
     */
    public final int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    private final void setId(final int id) {
        this.id = id;
    }

    /**
     * Creates a <code>PlannedValue</code> based on a <code>ResultSet</code>.
     * 
     * @param resSet
     *            The result set containing the data
     * @return A <code>PlannedValue</code> object
     */
    public static final PlannedValue fromResultSet(final ResultSet resSet) {
        PlannedValue pv = new PlannedValue();

        try {
            pv.setId(resSet.getInt("id"));
            pv.setFid_wp(resSet.getInt("fid_wp"));
            pv.setPv_date(resSet.getDate("pv_date"));
            pv.setPv(resSet.getDouble("pv"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pv;
    }

}
