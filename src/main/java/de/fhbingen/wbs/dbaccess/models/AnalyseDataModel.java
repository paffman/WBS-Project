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

package de.fhbingen.wbs.dbaccess.models;

import java.util.List;

import de.fhbingen.wbs.dbaccess.data.AnalyseData;

/** The interface for the analyse data model */
public interface AnalyseDataModel {

    /**
     * A method to add a new analyse data.
     * @param data The analyse data which is added to the project.
     */
    public void addNewAnalyseData(AnalyseData data);

     /**
     * A method to get the analyse data.
     * @param fid Workpackage from which the data comes.
     * @return Returns the data from selected workpackage.
     */
    public List<AnalyseData> getAnalyseData(int fid);

    /**
     * A method to get the analyse data from the baseline.
     * @param baseline Baseline for which the data is analysed.
     * @return Returns a list with the data from the selected baseline.
     */
    public List<AnalyseData> getAnalyseDataForBaseline(int baseline);

}
