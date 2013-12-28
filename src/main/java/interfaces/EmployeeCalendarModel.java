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

package interfaces;

import java.util.Date;
import java.util.List;

public interface EmployeeCalendarModel {

    public List<?> getEmployeeCalendar();
    
    public EmployeeCalendar getEmployeeCalendar(int id);
    
    public List<?> getEmployeeCalendarForFID(int fid);
    
    public List<?> getEmployeeCalendarInDateRange(Date from, Date to);
    
    public List<?> getEmployeeCalendarInDateRange(Date from, Date to, boolean mode2);

    public void deleteEmployeeCalendar(int id);
    
}
