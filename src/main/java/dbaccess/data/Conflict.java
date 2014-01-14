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

import java.util.Date;

public class Conflict {
    private int id;
    private int fid_wp;
    private int fid_wp_affected;
    private int fid_emp;
    private int reason;
    private Date occurence_date;
    
    public Conflict(int id, int fid_wp, int fid_wp_affected, int fid_emp, int reason, Date occurence_date){
        this.id=id;
        this.fid_wp=fid_wp;
        this.fid_wp_affected=fid_wp_affected;
        this.fid_emp=fid_emp;
        this.reason=reason;
        this.occurence_date=occurence_date;
    }
    
    public String toString(){
        return id+" "+fid_wp+" "+fid_wp_affected+" "+fid_emp+" "+reason+" "+occurence_date;
    }

    public int getId() {
        return id;
    }

    public int getFid_wp() {
        return fid_wp;
    }

    public int getFid_wp_affected() {
        return fid_wp_affected;
    }

    public int getFid_emp() {
        return fid_emp;
    }

    public int getReason() {
        return reason;
    }

    public Date getOccurence_date() {
        return occurence_date;
    }
    
    
}
