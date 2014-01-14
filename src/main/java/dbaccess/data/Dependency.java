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

public class Dependency{
    private int fid_wp_predecessor;
    private int fid_wp_successor;
    
    public Dependency(int fid_wp_predecessor, int fid_wp_successor){
        this.fid_wp_predecessor=fid_wp_predecessor;
        this.fid_wp_successor=fid_wp_successor;
    }
    
    public String toString(){
        return fid_wp_predecessor+" "+fid_wp_successor;
    }

    public int getFid_wp_predecessor() {
        return fid_wp_predecessor;
    }

    public int getFid_wp_successor() {
        return fid_wp_successor;
    }
}
