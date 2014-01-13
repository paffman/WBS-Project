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

public class AnalyseData {
    private int id;
    private int fid_wp;
    private int fid_baseline;
    private String name;
    private double bac;
    private double ac;
    private double ev;
    private double etc;
    private double eac;
    private double cpi;
    private double bac_costs;
    private double ac_costs;
    private double etc_costs;
    private int sv;
    private int spi;
    private int pv;
    
    public String toString(){
        return id+" "+fid_wp+" "+fid_baseline+" "+name+" "+bac+" "+ac+" "+ev+" "+etc+" "+eac+" "+cpi+" "+bac_costs+" "+ac_costs+" "+etc_costs+" "+sv+" "+spi+" "+pv;
    }

    public AnalyseData(int id, int fid_wp, int fid_baseline, String name,
            double bac, double ac, double ev, double etc, double eac,
            double cpi, double bac_costs, double ac_costs, double etc_costs, int sv, int spi, int pv) {
        this.id=id;
        this.fid_wp=fid_wp;
        this.fid_baseline=fid_baseline;
        this.name=name;
        this.bac=bac;
        this.ac=ac;
        this.ev=ev;
        this.etc=etc;
        this.eac=eac;
        this.cpi=cpi;
        this.bac_costs=bac_costs;
        this.ac_costs=ac_costs;
        this.etc_costs=etc_costs;
        this.sv=sv;
        this.spi=spi;
        this.pv=pv;
    }

    public int getSv() {
        return sv;
    }

    public int getSpi() {
        return spi;
    }

    public int getPv() {
        return pv;
    }

    public int getId() {
        return id;
    }

    public int getFid_wp() {
        return fid_wp;
    }

    public int getFid_baseline() {
        return fid_baseline;
    }

    public String getName() {
        return name;
    }

    public double getBac() {
        return bac;
    }

    public double getAc() {
        return ac;
    }

    public double getEv() {
        return ev;
    }

    public double getEtc() {
        return etc;
    }

    public double getEac() {
        return eac;
    }

    public double getCpi() {
        return cpi;
    }

    public double getBac_costs() {
        return bac_costs;
    }

    public double getAc_costs() {
        return ac_costs;
    }

    public double getEtc_costs() {
        return etc_costs;
    }
}
