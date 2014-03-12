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

package de.fhbingen.wbs.dbaccess.models;

/** The interface for the semaphore model. */
public interface SemaphoreModel {

    /**
     * A method to enter a semaphore.
     *
     * @param tag
     *            The tag of the semaphore.
     * @param id
     *            The id of the entering user.
     * @return Returns if the semaphore could be entered.
     */
    boolean enterSemaphore(String tag, int id);

    /**
     * Enter a semaphore.
     *
     * @param tag
     *            The tag of the semaphore.
     * @param id
     *            The id of the entering user.
     * @param force
     *            When true the semaphore is forcefully entered.
     * @return Returns if the semaphore could be entered.
     */
    boolean enterSemaphore(String tag, int id, boolean force);

    /**
     * Leave a semaphore.
     *
     * @param tag
     *            The tag of the semaphore to leave
     * @param id
     *            The user id of the user who leaves
     */
    void leaveSemaphore(String tag, int id);
}
