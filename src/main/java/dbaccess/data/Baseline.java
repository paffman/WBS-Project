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

public class Baseline {

    private int id;
    private int fid_project;
    private Date bl_date; 
    private String description;
    
    public Baseline(int id, int fid_project, Date bl_date, String description){
        this.id=id;
        this.fid_project=fid_project;
        this.bl_date=bl_date;
        this.description=description;
    }
    
    public String toString(){
        return id+" "+"fid_project"+" "+bl_date+" "+description;
    }
    
    public int getId() {
        return id;
    }

    public int getFid_project() {
        return fid_project;
    }

    public Date getBl_date() {
        return bl_date;
    }

    public String getDescription() {
        return description;
    } 
    
}
