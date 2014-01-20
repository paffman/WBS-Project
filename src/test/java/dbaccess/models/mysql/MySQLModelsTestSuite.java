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

import jdbcConnection.MySqlConnect;
import jdbcConnection.SQLExecuter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sqlutils.TestDBConnector;
import sqlutils.TestData;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MySQLWorkpackageModelTest.class,
        MySQLSemaphoreModelTest.class
})
public class MySQLModelsTestSuite {

    @BeforeClass
    public static final void setUp() throws Exception {
        MySqlConnect.setDbConncetion("localhost", "mbtest", "", "unittest",
                "junit411");
        TestData.reloadData(SQLExecuter.getConnection());
    }

    @AfterClass
    public static final void tearDown() throws Exception {
        SQLExecuter.closeConnection();
    }

}
