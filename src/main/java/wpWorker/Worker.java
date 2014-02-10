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

package wpWorker;

import dbaccess.DBModelManager;

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
     * @param login
     *            The login of the user.
     * @param id
     *            The id of the worker.
     * @param vorname
     *            The first name of the worker.
     * @param name
     *            The name of the user.
     * @param berechtigung
     *            The permission on the data base. 0: Worker, 1: Project
     *            leader.
     * @param tagessatz
     *            The daily rate of the user.
     */
    public Worker(final String login, final int id, final String vorname,
        final String name, final int berechtigung, final double tagessatz) {
        this(login, id, vorname, name, berechtigung == 1, tagessatz);
    }

    /**
     * Constructor: Creates a worker.
     * @param login
     *            The login of the user.
     * @param vorname
     *            The first name of the worker.
     * @param name
     *            The name of the user.
     * @param berechtigung
     *            The permission on the data base. 0: Worker, 1: Project
     *            leader.
     * @param tagessatz
     *            The daily rate of the user.
     */
    public Worker(final String login, final String vorname,
        final String name, final int berechtigung, final double tagessatz) {
        this(login, -1, vorname, name, berechtigung == 1, tagessatz);
    }

    /**
     * Constructor: Creates a worker.
     * @param login
     *            The login of the user.
     * @param id
     *            The id of the worker.
     * @param vorname
     *            The first name of the worker.
     * @param name
     *            The name of the user.
     * @param leiter
     *            True: If the worker is a leader. False: Else.
     * @param tagessatz
     *            The daily rate of the user.
     */
    public Worker(final String login, final int id, final String vorname,
        final String name, final boolean leiter, final double tagessatz) {

        this.login = login;
        this.id = id;
        this.vorname = vorname;
        this.name = name;
        this.tagessatz = tagessatz;
        this.leiter = leiter;
    }

    // public Mitarbeiter(String login, String vorname, String name,
    // boolean
    // leiter, String passwort, double tagessatz) {
    // this.login = login;
    // this.vorname = vorname;
    // this.name = name;
    // this.passwort = passwort;
    // this.tagessatz = tagessatz;
    // this.leiter = leiter;
    // if(leiter) {
    // this.berechtigung = 1;
    // } else {
    // this.berechtigung = 0;
    // }
    // }
    //

    /**
     * Constructor: Create a worker.
     * @param login
     *            The login of the user.
     */
    public Worker(final String login) {
        this.login = login;
        this.id = -1;
        this.vorname = "";
        this.name = "";
        this.leiter = false;
        this.tagessatz = 0;
    }

    /**
     * Constructor: Create a worker.
     * @param id
     *            The workers id.
     */
    public Worker(final int id) {
        this.login = DBModelManager.getEmployeesModel().getEmployee(id)
            .getLogin();
        this.id = id;
        this.vorname = "";
        this.name = "";
        this.leiter = false;
        this.tagessatz = 0;
    }

    /**
     * Constructor: Create a worker.
     * @param login
     *            The login of the user.
     * @param leiter
     *            True: The worker is a project leader. False: The worker
     *            isn't a leader.
     */
    public Worker(final String login, final boolean leiter) {
        this.login = login;
        this.leiter = leiter;
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
        return leiter ? 1 : 0;
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
     * @param name
     *            The users name.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the first name of the user.
     * @param vorname
     *            The users first name.
     */
    public final void setVorname(final String vorname) {
        this.vorname = vorname;
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
     * @param tagessatz
     *            The daily rate of the worker.
     */
    public final void setTagessatz(final double tagessatz) {
        this.tagessatz = tagessatz;
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
     * @param id
     *            the id to set
     */
    public final void setId(final int id) {
        this.id = id;
    }

}
