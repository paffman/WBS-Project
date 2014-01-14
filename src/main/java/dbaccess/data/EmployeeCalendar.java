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

public class EmployeeCalendar {
    private int id;
    private int fid_emp;
    private String begin_time;
    private String end_time;
    private String description;
    private boolean availability;
    private boolean full_time;

    public EmployeeCalendar(int id, int fid_emp, String begin_time,
            String end_time, String description, boolean availability, boolean full_time) {
        this.id=id;
        this.fid_emp=fid_emp;
        this.begin_time=begin_time;
        this.end_time=end_time;
        this.description=description;
        this.availability=availability;
        this.full_time=full_time;
    }
    
    public String toString(){
        return id+" "+fid_emp+" "+begin_time+" "+end_time+" "+description+" "+availability+" "+full_time;
    }

    public int getId() {
        return id;
    }

    public int getFid_emp() {
        return fid_emp;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailability() {
        return availability;
    }

    public boolean isFull_time() {
        return full_time;
    }    
}
