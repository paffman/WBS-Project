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

package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import dbaccess.models.SemaphoreModel;

import org.junit.*;

public class MySQLSemaphoreModelTest {

    private SemaphoreModel semaphoreModel;

    @Before
    public final void setup() {
        semaphoreModel = new MySQLSemaphoreModel();
    }

    @After
    public final void cleanup() {
        semaphoreModel = null;
    }

    @Test
    public final void testSemaphore() {
        boolean entered = semaphoreModel.enterSemaphore("pl", 1);
        assertThat(entered, equalTo(true));

        entered = semaphoreModel.enterSemaphore("pl", 1);
        assertThat(entered, equalTo(false));

        entered = semaphoreModel.enterSemaphore("pl2", 1);
        assertThat(entered, equalTo(true));

        entered = semaphoreModel.enterSemaphore("pl2", 1);
        assertThat(entered, equalTo(false));

        semaphoreModel.leaveSemaphore("pl2", 1);

        entered = semaphoreModel.enterSemaphore("pl2", 1);
        assertThat(entered, equalTo(true));

        entered = semaphoreModel.enterSemaphore("pl", 2);
        assertThat(entered, equalTo(false));

        entered = semaphoreModel.enterSemaphore("pl", 2, true);
        assertThat(entered, equalTo(true));

        entered = semaphoreModel.enterSemaphore("pl", 1, true);
        assertThat(entered, equalTo(true));

        entered = semaphoreModel.enterSemaphore("pl", 2, false);
        assertThat(entered, equalTo(false));

        semaphoreModel.leaveSemaphore("pl", 1);

        entered = semaphoreModel.enterSemaphore("pl", 2, false);
        assertThat(entered, equalTo(true));
    }

}
