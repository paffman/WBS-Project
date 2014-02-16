/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis Copyright (C) 2013 FH-Bingen This
 * program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY;; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.wpWorker;

import de.fhbingen.wbs.dbaccess.DBModelManager;

/**
 * Represents a worker.
 */
public class Worker {

    /** The login of the user. */
    private String login;

    /** The first name of the user. */
    private int id;

    /** The name of the user. */
    private String vorname;

    /** The permission of the user. */
    private String name;

    /** The daily rate of the user. */
    private double tagessatz;

    /** Defines if the user is a leader or not. */
    private boolean leiter;

    /**
     * Constructor: Creates a worker.
     * @param workerLogin
     *            The login of the user.
     * @param workerId
     *            The id of the worker.
     * @param firstName
     *            The first name of the worker.
     * @param surname
     *            The name of the user.
     * @param berechtigung
     *            The permission on the data base. 0: Worker, 1: Project
     *            leader.
     * @param dailyRate
     *            The daily rate of the user.
     */
    public Worker(final String workerLogin, final int workerId,
                  final String firstName, final String surname,
                  final int berechtigung, final double dailyRate) {
        this(workerLogin, workerId, firstName, surname, berechtigung == 1,
                dailyRate);
    }

    /**
     * Constructor: Creates a worker.
     * @param workerLogin
     *            The login of the user.
     * @param firstName
     *            The first name of the worker.
     * @param surname
     *            The name of the user.
     * @param berechtigung
     *            The permission on the data base. 0: Worker, 1: Project
     *            leader.
     * @param dailyRate
     *            The daily rate of the user.
     */
    public Worker(final String workerLogin, final String firstName,
        final String surname, final int berechtigung, final double dailyRate) {
        this(workerLogin, -1, firstName, surname, berechtigung == 1, dailyRate);
    }

    /**
     * Constructor: Creates a worker.
     * @param workerLogin
     *            The login of the user.
     * @param workerId
     *            The id of the worker.
     * @param firstName
     *            The first name of the worker.
     * @param surname
     *            The name of the user.
     * @param isProjectManager
     *            True: If the worker is a leader. False: Else.
     * @param dailyRate
     *            The daily rate of the user.
     */
    public Worker(final String workerLogin, final int workerId,
                  final String firstName, final String surname,
                  final boolean isProjectManager, final double dailyRate) {

        this.login = workerLogin;
        this.id = workerId;
        this.vorname = firstName;
        this.name = surname;
        this.tagessatz = dailyRate;
        this.leiter = isProjectManager;
    }

    /**
     * Constructor: Create a worker.
     * @param workerLogin
     *            The login of the user.
     */
    public Worker(final String workerLogin) {
        this.login = workerLogin;
        this.id = -1;
        this.vorname = "";
        this.name = "";
        this.leiter = false;
        this.tagessatz = 0;
    }

    /**
     * Constructor: Create a worker.
     * @param workerId
     *            The workers id.
     */
    public Worker(final int workerId) {
        this.login = DBModelManager.getEmployeesModel().getEmployee(workerId)
            .getLogin();
        this.id = workerId;
        this.vorname = "";
        this.name = "";
        this.leiter = false;
        this.tagessatz = 0;
    }

    /**
     * Constructor: Create a worker.
     * @param workerLogin
     *            The login of the user.
     * @param isProjectManager
     *            True: The worker is a project leader. False: The worker
     *            isn't a leader.
     */
    public Worker(final String workerLogin, final boolean isProjectManager) {
        this.login = workerLogin;
        this.leiter = isProjectManager;
        this.name = "";
        this.vorname = "";
    }

    /**
     * @return The login name.
     */
    public final String getLogin() {
        return login;
    }

    /**
     * @return The name of the worker.
     */
    public final String getName() {
        return name;
    }

    /**
     * @return The first name of the worker.
     */
    public final String getVorname() {
        return vorname;
    }

    /**
     * @return The permission of the worker. 0: worker. 1: leader.
     */
    public final int getBerechtigung() {
        if (leiter) {
            return 1;
        }
        return 0;
    }

    /**
     * @return True: If the worker is a leader. False: The worker isn't a
     *         leader.
     */
    public final boolean getProjLeiter() {
        return leiter;
    }

    /**
     * @return The daily rate of the worker.
     */
    public final Double getTagessatz() {
        return tagessatz;
    }

    /**
     * @return The worker shown as String.
     * @see {@link Object#toString()}
     */
    public final String toString() {
        if (login.equals("")) {
            return "";
        } else if (vorname.equals("") && name.equals("")) {
            return login;
        } else if (!vorname.equals("") && name.equals("")) {
            return login + " | " + name;
        } else {
            return login + " | " + vorname + " " + name;
        }
    }

    /**
     * Sets the name of the user.
     * @param newSurname
     *            The users name.
     */
    public final void setName(final String newSurname) {
        this.name = newSurname;
    }

    /**
     * Sets the first name of the user.
     * @param newFirstName
     *            The users first name.
     */
    public final void setVorname(final String newFirstName) {
        this.vorname = newFirstName;
    }

    /**
     * Sets the permission of the worker.
     * @param rights
     *            1: Leader permissions. 0: worker permissions.
     */
    public final void setBerechtigung(final int rights) {
        this.leiter = (rights == 1);
    }

    /**
     * Sets the daily rate of the worker.
     * @param newDailyRate
     *            The daily rate of the worker.
     */
    public final void setTagessatz(final double newDailyRate) {
        this.tagessatz = newDailyRate;
    }

    /**
     * @return True: If the workers are equal. False: If the workers aren't
     *         equal.
     * @param o
     *            The worker which has to checked with the actual worker.
     */
    public final boolean equals(final Object o) {
        if (o instanceof Worker) {
            Worker other = (Worker) o;
            return other.getLogin().equals(this.getLogin());
        } else {
            return false;
        }
    }

    /**
     * Calculate the hash code of the worker.
     * @return The hash code.
     */
    public final int hashCode() {
        return this.getLogin().hashCode();
    }

    /**
     * @return the id
     */
    public final int getId() {
        return id;
    }

    /**
     * @param newId
     *            the id to set
     */
    public final void setId(final int newId) {
        this.id = newId;
    }

}
