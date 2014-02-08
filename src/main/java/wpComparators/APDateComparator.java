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

package wpComparators;

import globals.Workpackage;

import java.util.Comparator;

/**
 * This comparator sorts the work packages after the date of the start.
 */
public class APDateComparator implements Comparator<Workpackage> {
    /**
     * Compares to work packages after the date of the start.
     * @param ap1
     *            The first work package.
     * @param ap2
     *            The second work package which has to compare with the
     *            first one.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    @Override
    public final int compare(final Workpackage ap1, final Workpackage ap2) {

        Integer[] ap1IDs = ap1.getLvlIDs();
        Integer[] ap2IDs = ap2.getLvlIDs();

        int position = 0;
        int levels = ap1IDs.length;

        while (ap1IDs[position].equals(ap2IDs[position])) {
            position++;
        }

        if (position + 1 < levels && ap1IDs[position + 1] == 0
            && ap2IDs[position + 1] == 0) {
            return startDateCompare(ap1, ap2);
        } else {
            return 0;
        }

    }

    /**
     * Compares to work packages after the date of the start.
     * @param ap1
     *            The first work package.
     * @param ap2
     *            The second work package which has to compare with the
     *            first one.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    private int startDateCompare(final Workpackage ap1, final Workpackage ap2) {
        if (ap1.getStartDateCalc() == null) {
            return 0;
        } else if (ap2.getStartDateCalc() == null) {
            return 0;
        } else {
            if (ap1.getStartDateCalc().before(ap2.getStartDateCalc())) {
                return -1;
            } else if (ap1.getStartDateCalc().after(ap2.getStartDateCalc())) {
                return 1;
            } else {
                return 0;
            }
        }

    }

}
