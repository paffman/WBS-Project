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
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple container class representing a Workpackge. It mirrors all the
 * database fields in the workpackage table.
 */
public class Workpackage {

    /** Work package id. */
    private int id;

    /** A string representation of the id. */
    private String stringID;

    /** The corresponding project id. */
    private int projectID;

    /** id of the responsible employee. */
    private int employeeID;

    /** id of the parent work package. */
    private int parentID;

    /** The position in the parent hierarchy. */
    private int positionID;

    /** The name of the work package. */
    private String name;

    /** The description of the work package. */
    private String description;

    /** Budget at Completion (time). */
    private double bac;

    /** Actual Costs (time). */
    private double ac;

    /** Earned Value. */
    private double ev;

    /** Estimation to Completion (time). */
    private double etc;

    /** Estimation at Completion (time). */
    private double eac;

    /** Cost Performance Index. */
    private double cpi;

    /** BAC costs. */
    private double bacCosts;

    /** AC costs. */
    private double acCosts;

    /** ETC costs. */
    private double etcCosts;

    /** Daily rate. */
    private double dailyRate;

    /** The release date of the work package. */
    private Date releaseDate;

    /** Weather or not this work package is top level (if it has children). */
    private boolean topLevel;

    /** Weather of not this work package is inactive. */
    private boolean inactive;

    /** Calculated start date. */
    private Date startDateCalc;

    /** Calculated end date. */
    private Date endDateCalc;

    /** Wished start date. */
    private Date startDateWish;

    /**
     * Creates a <code>Workpackage</code> based on a <code>ResultSet</code>.
     * 
     * @param resSet
     *            The result set containing the data
     * @return A <code>Workpackage</code> object
     */
    public static final Workpackage fromResultSet(final ResultSet resSet) {
        Workpackage wp = new Workpackage();

        try {
            wp.setId(resSet.getInt("id"));
            wp.setStringID(resSet.getString("string_id"));
            wp.setProjectID(resSet.getInt("fid_project"));
            wp.setEmployeeID(resSet.getInt("fid_resp_emp"));
            wp.setParentID(resSet.getInt("fid_parent"));
            wp.setPositionID(resSet.getInt("parent_order_id"));
            wp.setName(resSet.getString("name"));
            wp.setDescription(resSet.getString("description"));
            wp.setBac(resSet.getDouble("bac"));
            wp.setAc(resSet.getDouble("ac"));
            wp.setEv(resSet.getDouble("ev"));
            wp.setEtc(resSet.getDouble("etc"));
            wp.setEac(resSet.getDouble("eac"));
            wp.setCpi(resSet.getDouble("cpi"));
            wp.setBacCosts(resSet.getDouble("bac_costs"));
            wp.setAcCosts(resSet.getDouble("ac_costs"));
            wp.setEtcCosts(resSet.getDouble("etc_costs"));
            wp.setDailyRate(resSet.getDouble("wp_daily_rate"));
            wp.setReleaseDate(resSet.getDate("release_date"));
            wp.setTopLevel(resSet.getBoolean("is_toplevel_wp"));
            wp.setInactive(resSet.getBoolean("is_inactive"));
            wp.setStartDateCalc(resSet.getDate("start_date_calc"));
            wp.setStartDateWish(resSet.getDate("start_date_wish"));
            wp.setEndDateCalc(resSet.getDate("end_date_calc"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wp;
    }

    /**
     * Returns the ID.
     * 
     * @return The ID
     */
    public final int getId() {
        return id;
    }

    /**
     * Sets the ID.
     * 
     * @param id
     *            The new ID
     */
    private final void setId(final int id) {
        this.id = id;
    }

    /**
     * Returns the string ID.
     * 
     * @return The string representation of the ID
     */
    public final String getStringID() {
        return stringID;
    }

    /**
     * Sets the string representation of the id.
     * 
     * @param stringID
     *            A string ID. (example: "1.1.0.0")
     */
    public final void setStringID(final String stringID) {
        this.stringID = stringID;
    }

    /**
     * Returns the project id corresponding to this work package.
     * 
     * @return The project id
     */
    public final int getProjectID() {
        return projectID;
    }

    /**
     * Sets the project id corresponding to this work package.
     * 
     * @param projectID
     *            The project id
     */
    public final void setProjectID(final int projectID) {
        this.projectID = projectID;
    }

    /**
     * Returns the id of the responsible employee.
     * 
     * @return The id of the responsible employee
     */
    public final int getEmployeeID() {
        return employeeID;
    }

    /**
     * Sets the id of the responsible employee.
     * 
     * @param employeeID
     *            The id of the responsible employee
     */
    public final void setEmployeeID(final int employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Returns the id of the parent work package.
     * 
     * @return ID of the parent work package
     */
    public final int getParentID() {
        return parentID;
    }

    /**
     * Sets the id of the parent work package.
     * 
     * @param parentID
     *            The id of the parent work package.
     */
    public final void setParentID(final int parentID) {
        this.parentID = parentID;
    }

    /**
     * Returns the position of this work package in the parent hierarchy.
     * 
     * @return The position in the parent hierarchy
     */
    public final int getPositionID() {
        return positionID;
    }

    /**
     * Sets the position in the parent hierarchy.
     * 
     * @param positionID
     *            The new position in the parent hierarchy
     */
    public final void setPositionID(final int positionID) {
        this.positionID = positionID;
    }

    /**
     * Retursn the name of the work package.
     * 
     * @return The name of the work package.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name of the work package.
     * 
     * @param name
     *            The name of the work package
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the work package description.
     * 
     * @return The work package description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the work package description.
     * 
     * @param description
     *            The work package description
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the Budget at Completion (measured in time).
     * 
     * @return Budget at Completion
     */
    public final double getBac() {
        return bac;
    }

    /**
     * Sets the Budget at Completion (measured in time).
     * 
     * @param bac
     *            Budget at Completion
     */
    public final void setBac(final double bac) {
        this.bac = bac;
    }

    /**
     * Returns the Actual Cost (measured in time).
     * 
     * @return Actual Cost
     */
    public final double getAc() {
        return ac;
    }

    /**
     * Sets the Actual Cost (measured in time).
     * 
     * @param ac
     *            Actual Cost
     */
    public final void setAc(final double ac) {
        this.ac = ac;
    }

    /**
     * Returns the Earned Value.
     * 
     * @return Earned Value
     */
    public final double getEv() {
        return ev;
    }

    /**
     * Sets the Earned Value.
     * 
     * @param ev
     *            Earned Value
     */
    public final void setEv(final double ev) {
        this.ev = ev;
    }

    /**
     * Returns the Estimation to Completion (measured in time).
     * 
     * @return Estimation to Completion
     */
    public final double getEtc() {
        return etc;
    }

    /**
     * Sets the Estimation to Completion (measured in time).
     * 
     * @param etc
     *            Estimation to Completion
     */
    public final void setEtc(final double etc) {
        this.etc = etc;
    }

    /**
     * Returns the Estimation at Completion (measured in time).
     * 
     * @return Estimation at Completion
     */
    public final double getEac() {
        return eac;
    }

    /**
     * Sets the Estimation at Completion (measured in time).
     * 
     * @param eac
     *            Estimation at Completion
     */
    public final void setEac(final double eac) {
        this.eac = eac;
    }

    /**
     * Returns the Cost Performance Index.
     * 
     * @return Cost Performance Index
     */
    public final double getCpi() {
        return cpi;
    }

    /**
     * Sets the Cost Performance Index.
     * 
     * @param cpi
     *            Cost Performance Index
     */
    public final void setCpi(final double cpi) {
        this.cpi = cpi;
    }

    /**
     * Returns the Budget at Completion (measured in money).
     * 
     * @return Budget at Completion (money)
     */
    public final double getBacCosts() {
        return bacCosts;
    }

    /**
     * Sets the Budget at Completion (measured in money).
     * 
     * @param bacCosts
     *            Budget at Completion
     */
    public final void setBacCosts(final double bacCosts) {
        this.bacCosts = bacCosts;
    }

    /**
     * Returns the Actual Costs (measured in money).
     * 
     * @return Actual Costs in money
     */
    public final double getAcCosts() {
        return acCosts;
    }

    /**
     * Sets the Actual Costs (measured in money).
     * 
     * @param acCosts
     *            Actual Cost in money
     */
    public final void setAcCosts(final double acCosts) {
        this.acCosts = acCosts;
    }

    /**
     * Return the Estimation to Completion (measured in money)
     * 
     * @return Estimation to Completion in money
     */
    public final double getEtcCosts() {
        return etcCosts;
    }

    /**
     * Sets the Estimation to Completion (measured in money).
     * 
     * @param etcCosts
     *            Estimation to Completion in money
     */
    public final void setEtcCosts(final double etcCosts) {
        this.etcCosts = etcCosts;
    }

    /**
     * Return the daily rate of this work package.
     * 
     * @return The daily rate
     */
    public final double getDailyRate() {
        return dailyRate;
    }

    /**
     * Sets the daily Rate of this work package.
     * 
     * @param dailyRate
     *            The daily rate
     */
    public final void setDailyRate(final double dailyRate) {
        this.dailyRate = dailyRate;
    }

    /**
     * Returns the release date.
     * 
     * @return The release date
     */
    public final Date getReleaseDate() {
        if (releaseDate == null) {
            // TODO: check if it is alright to just initialize it
            releaseDate = new Date();
        }
        return releaseDate;
    }

    /**
     * Sets the release date.
     * 
     * @param releaseDate
     *            The release date
     */
    public final void setReleaseDate(final Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Returns weather or not this work package is a top level work package.
     * Work packages with children are top level.
     * 
     * @return <code>true</code> if this work package is top level,
     *         <code>false</code> otherwise
     */
    public final boolean isTopLevel() {
        return topLevel;
    }

    /**
     * Sets weather or not this work package should be top level.
     * 
     * @param topLevel
     *            If the work package should be top level
     */
    public final void setTopLevel(final boolean topLevel) {
        this.topLevel = topLevel;
    }

    /**
     * Returns weather or not this work package is inactive.
     * 
     * @return If this work package is inactive
     */
    public final boolean isInactive() {
        return inactive;
    }

    /**
     * Sets weather or not this work package should be inactive.
     * 
     * @param inactive
     *            If this work package should be inactive
     */
    public final void setInactive(final boolean inactive) {
        this.inactive = inactive;
    }

    /**
     * Returns the calculated start date.
     * 
     * @return The calculated start date
     */
    public final Date getStartDateCalc() {
        return startDateCalc;
    }

    /**
     * Sets the calculated start date.
     * 
     * @param startDateCalc
     *            The calculated start date
     */
    public final void setStartDateCalc(Date startDateCalc) {
        this.startDateCalc = startDateCalc;
    }

    /**
     * Returns the calculated end date.
     * 
     * @return The calculated end date
     */
    public final Date getEndDateCalc() {
        return endDateCalc;
    }

    /**
     * Sets the calculated end date.
     * 
     * @param endDateCalc
     *            The calculated end date
     */
    public final void setEndDateCalc(final Date endDateCalc) {
        this.endDateCalc = endDateCalc;
    }

    /**
     * Returns the wished start date.
     * 
     * @return The wished start date
     */
    public final Date getStartDateWish() {
        return startDateWish;
    }

    /**
     * Sets the wished start date.
     * 
     * @param startDateWish
     *            The wished start date
     */
    public final void setStartDateWish(final Date startDateWish) {
        this.startDateWish = startDateWish;
    }

    /**
     * WBS 2.0 - Constructor
     * 
     * @param lvl1ID
     * @param lvl2ID
     * @param lvl3ID
     * @param lvlxID
     * @param name
     * @param beschreibung
     * @param cpi
     * @param Bac
     * @param ac
     * @param ev
     * @param etc
     * @param wptagessatz
     * @param eac
     * @param bac_kosten
     * @param ac_kosten
     * @param etc_kosten
     * @param release
     * @param istOAP
     * @param istInaktiv
     * @param fid_Leiter
     * @param zustaendige
     * @param sv
     * @param spi
     * @param pv
     * @param startDateCalc
     * @param startDateHope
     * @param endDateCalc
     */
    public Workpackage(String stringId, String name, String description,
            Double cpi, Double bac, double ac, double ev, double etc,
            double dailyrate, double eac, double bacCosts, double acCosts,
            double etcCosts, Date releaseDate, boolean isTopLevel,
            boolean isInactive, int fidPL, ArrayList<String> responsible,
            Date startDateCalc, Date startDateWish, Date endDateCalc) {

        this.stringID = stringId;
        this.name = name;
        this.description = description;
        this.cpi = cpi;
        this.bac = bac;
        this.ac = ac;
        this.ev = ev;
        this.etc = etc;
        this.dailyRate = dailyrate;
        this.eac = eac;
        this.bacCosts = bacCosts;
        this.acCosts = acCosts;
        this.etcCosts = etcCosts;
        this.releaseDate = releaseDate;
        this.topLevel = isTopLevel;
        this.inactive = isInactive;
        this.employeeID = fidPL;
        this.startDateCalc = startDateCalc;
        this.startDateWish = startDateWish;
        this.endDateCalc = endDateCalc;
    }

    /**
     * Default Constructor.
     */
    public Workpackage() {
    };

    /**
     * This Method returns the Name of an Workpackage.
     * 
     * @return String mit der ID und dem Namen des Arbeitspakets
     */
    public final String toString() {
        return stringID + " " + name;
    }

}
