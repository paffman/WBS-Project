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
 * A simple container class representing a Analyse Data. It mirrors all the
 * database fields in the analyse_data table.
 */
public class AnalyseData {
    
    /** Analyse data id */
    private int id;
    
    /** Workpackage from which the data comes */
    private int fid_wp;
    
    /** Baseline for which the data is analysed */
    private int fid_baseline;
    
    /** Name of the Workpackage */
    private String name;
    
    /** BAC of the Workpackage */
    private double bac;
    
    /** AC of the Workpackage */
    private double ac;

    /** EV of the Workpackage */
    private double ev;
    
    /** ETC of the Workpackage */
    private double etc;
    
    /** EAC of the Workpackage */
    private double eac;
    
    /** CPI of the Workpackage */
    private double cpi;
    
    /** BAC costs of the Workpackage */
    private double bac_costs;
    
    /** AC costs of the Workpackage */
    private double ac_costs;
    
    /** ETC costs of the Workpackage */
    private double etc_costs;
    
    /** Value of the earned value analysis: Schedule Variance */
    private double sv;
    
    /** Value of the earned value analysis: Schedule Performance Index */
    private double spi;
    
    /** Value of the earned value analysis: Planned Value */
    private double pv;
    
 
    /**
     * Creates a <code>AnalyseData</code> based on a <code>ResultSet</code>.
     *
     * @param resSet The result set containing the data
     * @return A <code>AnalyseData</code> object
     */
    public static final AnalyseData fromResultSet(final ResultSet resSet){
        AnalyseData ad=new AnalyseData();
        
        try {
            ad.setId(resSet.getInt("id"));
            ad.setFid_wp(resSet.getInt("fid_wp"));
            ad.setFid_baseline(resSet.getInt("fid_baseline"));
            ad.setName(resSet.getString("name"));
            ad.setBac(resSet.getDouble("bac"));
            ad.setAc(resSet.getDouble("ac"));
            ad.setEv(resSet.getDouble("ev"));
            ad.setEtc(resSet.getDouble("etc"));
            ad.setEac(resSet.getDouble("eac"));
            ad.setCpi(resSet.getDouble("cpi"));
            ad.setBac_costs(resSet.getDouble("bac_costs"));
            ad.setAc_costs(resSet.getDouble("ac_costs"));
            ad.setEtc_costs(resSet.getDouble("etc_costs"));
            ad.setSv(resSet.getDouble("sv"));
            ad.setSpi(resSet.getDouble("spi"));
            ad.setPv(resSet.getDouble("pv"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ad;
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
     * Sets the ID.
     *
     * @param id The new ID
     */
    private void setId(int id) {
        this.id = id;
    }
    
    /**
     * Returns the workpackage id.
     *
     * @return The workpackage id
     */
    public int getFid_wp() {
        return fid_wp;
    }

    /**
     * Sets the workpackage id.
     *
     * @param fid_wp The workpackage id
     */
    public void setFid_wp(int fid_wp) {
        this.fid_wp = fid_wp;
    }

    /**
     * Returns the baseline id.
     *
     * @return The baseline id
     */
    public int getFid_baseline() {
        return fid_baseline;
    }

    /**
     * Sets the baseline id.
     *
     * @param fid_baseline The baseline id
     */
    public void setFid_baseline(int fid_baseline) {
        this.fid_baseline = fid_baseline;
    }

    /**
     * Returns the workpackage name.
     *
     * @return The workpackage name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the workpackage.
     *
     * @param name The name of the workpackage
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the bac of the workpackage.
     *
     * @return The bac
     */
    public double getBac() {
        return bac;
    }

    /**
     * Sets the bac of the workpackage.
     *
     * @param bac The bac of the workpackage
     */
    public void setBac(double bac) {
        this.bac = bac;
    }

    /**
     * Returns the ac of the workpackage.
     *
     * @return The ac
     */
    public double getAc() {
        return ac;
    }

    /**
     * Sets the ac of the workpackage.
     *
     * @param ac The ac of the workpackage
     */
    public void setAc(double ac) {
        this.ac = ac;
    }

    /**
     * Returns the ev of the workpackage.
     *
     * @return The ev
     */
    public double getEv() {
        return ev;
    }

    /**
     * Sets the ev of the workpackage.
     *
     * @param ev The ev of the workpackage
     */
    public void setEv(double ev) {
        this.ev = ev;
    }

    /**
     * Returns the etc of the workpackage.
     *
     * @return The etc
     */
    public double getEtc() {
        return etc;
    }

    /**
     * Sets the etc of the workpackage.
     *
     * @param etc The etc of the workpackage
     */
    public void setEtc(double etc) {
        this.etc = etc;
    }

    /**
     * Returns the eac of the workpackage.
     *
     * @return The eac
     */
    public double getEac() {
        return eac;
    }

    /**
     * Sets the eac of the workpackage.
     *
     * @param eac The eac of the workpackage
     */
    public void setEac(double eac) {
        this.eac = eac;
    }

    /**
     * Returns the cpi of the workpackage.
     *
     * @return The cpi
     */
    public double getCpi() {
        return cpi;
    }

    /**
     * Sets the cpi of the workpackage.
     *
     * @param cpi The cpi of the workpackage
     */
    public void setCpi(double cpi) {
        this.cpi = cpi;
    }
    
    /**
     * Returns the bac_costs of the workpackage.
     *
     * @return The bac_costs
     */
    public double getBac_costs() {
        return bac_costs;
    }

    /**
     * Sets the bac costs of the workpackage.
     *
     * @param bac_costs The bac costs of the workpackage
     */
    public void setBac_costs(double bac_costs) {
        this.bac_costs = bac_costs;
    }

    /**
     * Returns the ac_costs of the workpackage.
     *
     * @return The ac_costs
     */
    public double getAc_costs() {
        return ac_costs;
    }

    /**
     * Sets the ac costs of the workpackage.
     *
     * @param ac_costs The ac costs of the workpackage
     */
    public void setAc_costs(double ac_costs) {
        this.ac_costs = ac_costs;
    }

    /**
     * Returns the etc_costs of the workpackage.
     *
     * @return The etc_costs
     */
    public double getEtc_costs() {
        return etc_costs;
    }

    /**
     * Sets the etc costs of the workpackage.
     *
     * @param etc_costs The etc costs of the workpackage
     */
    public void setEtc_costs(double etc_costs) {
        this.etc_costs = etc_costs;
    }

    /**
     * Returns the schedule variance.
     *
     * @return The schedule variance
     */
    public double getSv() {
        return sv;
    }

    /**
     * Sets the value of the earned value analysis: Schedule Variance.
     *
     * @param sv The schedule variance
     */
    public void setSv(double sv) {
        this.sv = sv;
    }

    /**
     * Returns the schedule performance index.
     *
     * @return The schedule performance index
     */
    public double getSpi() {
        return spi;
    }

    /**
     * Sets the value of the earned value analysis: Schedule Performance Index.
     *
     * @param spi The schedule performance index
     */
    public void setSpi(double spi) {
        this.spi = spi;
    }

    /**
     * Returns the planned value.
     *
     * @return The planned value
     */
    public double getPv() {
        return pv;
    }

    /**
     * Sets the value of the earned value analysis: Planned Value.
     *
     * @param pv The planned value
     */
    public void setPv(double pv) {
        this.pv = pv;
    }

    /**
     * Convert the analyse data to a String
     *
     * @return the analyse data as String
     */
    public String toString(){
        return id+" "+fid_wp+" "+fid_baseline+" "+name+" "+bac+" "+ac+" "+ev+" "+etc+" "+eac+" "+cpi+" "+bac_costs+" "+ac_costs+" "+etc_costs+" "+sv+" "+spi+" "+pv;
    }
}
