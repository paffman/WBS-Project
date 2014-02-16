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

/**
 * The class for users which are logged in.
 */
public class User extends Worker {

    /**
     * Constructor.
     * @param login
     *            The login of the user.
     * @param leiter
     *            True: If the user is a leader. False: Else.
     */
    public User(final String login, final boolean leiter) {
        super(login, leiter);
    }

    /**
     * Constructor.
     * @param login
     *            The login of the user.
     * @param id
     *            The id of the user.
     * @param name
     *            The name of the user.
     * @param vorname
     *            The first name of the user.
     * @param leiter
     *            True: If the user is a leader. False: Else.
     */
    public User(final String login, final int id, final String name,
        final String vorname, final boolean leiter) {
        super(login, id, name, vorname, (leiter) ? 1 : 0, 0);
    }
}
