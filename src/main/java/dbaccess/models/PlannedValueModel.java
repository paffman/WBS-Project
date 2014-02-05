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

package dbaccess.models;

import java.util.Date;
import java.util.List;

import dbaccess.data.PlannedValue;

/** The interface for the planned value model. */
public interface PlannedValueModel {

    /**
     * A method to add a new planned value to the project.
     * 
     * @param pValue
     *            The planned value which is added to the project.
     * @return success of action.
     */
    boolean addNewPlannedValue(PlannedValue pValue);

    /**
     * A method to get the planned value in a specific period.
     * 
     * @param from
     *            The start date of the period.
     * @param to
     *            The end date of the period.
     * @return Returns a list with the planned values in the period.
     */
    List<PlannedValue> getPlannedValue(Date from, Date to);

    /**
     * A method to get the planned value in a specific period from a specific
     * workpackage.
     * 
     * @param from
     *            The start date of the period.
     * @param to
     *            The end date of the period.
     * @param wpID
     *            The id of the workpackage.
     * @return Returns a list with the planned values from the selected
     *         workpackage in the period.
     */
    List<PlannedValue> getPlannedValue(Date from, Date to, int wpID);

    /**
     * A method to get the planned value from a specific workpackage on a
     * specific date.
     * 
     * @param aDate
     *            The specific date.
     * @param wpID
     *            The id from the workpackage.
     * @return Returns a list with the planned value from the selected
     *         workpackage and date.
     */
    double getPlannedValue(Date aDate, int wpID);

    /**
     * A method to get the planned value from a specific workpackage on a
     * specific date.
     * 
     * @param aDate
     *            The specific date.
     * @param wpID
     *            The id from the workpackage.
     * @param nextLowerDate
     *            selects date with < instead of = and returns the value of the
     *            first value found, orderd by DESC
     * @return Returns a list with the planned value from the selected
     *         workpackage and date.
     */
    double getPlannedValue(Date aDate, int wpID, boolean nextLowerDate);

    /**
     * A method to update a planned value.
     * 
     * @param aDate
     *            The specific date.
     * @param wpID
     *            The id of the workpackage from which the planned value has to
     *            be updated.
     * @param newValue
     *            The new value for the planned value.
     * @return success of action.
     */
    boolean updatePlannedValue(Date aDate, int wpID, double newValue);

    /**
     * A method to delete all planed values.
     * 
     * @return success of action.
     */
    boolean deletePlannedValue();

    /**
     * A method to delete a specific planned value
     * 
     * @param wpID
     *            The id of the workpackage from where the planned value is
     *            deleted.
     * @return success of action.
     */
    boolean deletePlannedValue(int wpID);

    /**
     * A method to delete a planned value on a specific date and a specific
     * workpackage.
     * 
     * @param aDate
     *            The specific date.
     * @param wpID
     *            The id of the workpackage.
     * @return success of action.
     */
    boolean deletePlannedValue(Date aDate, int wpID);

}
