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

package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

import dbaccess.data.Workpackage;
import dbaccess.models.WorkpackageModel;
import org.junit.*;
import sqlutils.TestData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class MySQLWorkpackageModelTest {

    private static Connection con;
    private WorkpackageModel wpModel;

    @BeforeClass
    public static final void setupTest() {
        final String url = "jdbc:mysql://localhost:3306/";
        final String dbName = "mbtest";
        final String driver = "com.mysql.jdbc.Driver";
        final String userName = "unittest";
        final String password = "junit411";

        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + dbName,
                    userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static final void closeDBConnection() {
        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public final void setup() {
        TestData.reloadData(con);
        wpModel = new MySQLWorkpackageModel(con);
    }

    @After
    public final void cleanup() {
        wpModel = null;
    }

    @Test
    public final void testGetWorkpackage() {
        List<Workpackage> wpList = wpModel.getWorkpackage();
        assertThat(wpList, notNullValue());

        assertThat("list should not be empty", wpList.size() > 0);
        assertThat("list should contain 8 WPs", wpList.size() == 8);

        assertThat(wpList.get(0).getName(), equalTo("Mauerbau"));
        assertThat(wpList.get(1).getName(), equalTo("Vorbereitung"));
        assertThat(wpList.get(2).getName(), equalTo("Inspektion der Baustelle"));
        assertThat(wpList.get(3).getName(), equalTo("Entwurfszeichnung"));
        assertThat(wpList.get(4).getName(), equalTo("Material Beschaffung"));
        assertThat(wpList.get(5).getName(), equalTo("Bauen"));
        assertThat(wpList.get(6).getName(), equalTo("Zement Mischen"));
        assertThat(wpList.get(7).getName(), equalTo("Stein auf Stein"));
    }

    @Test
    public final void testGetWorkpackageByStringID1() {
        final String projectID = "0.0.0.0";

        Workpackage projectWP = wpModel.getWorkpackage(projectID);

        assertThat(projectWP, notNullValue());

        // validate the db fields
        assertThat(projectWP.getId(), equalTo(1));
        assertThat(projectWP.getProjectID(), equalTo(1));
        assertThat(projectWP.getEmployeeID(), equalTo(2));
        assertThat(projectWP.getParentID(), equalTo(0));
        assertThat(projectWP.getPositionID(), equalTo(1));
        assertThat(projectWP.getName(), equalTo("Mauerbau"));
        assertThat(projectWP.getDescription(), equalTo("Projekt"));
    }

    @Test
    public final void testGetWorkpackageByStringID2() {
        String wpID = "1.0.0.0";
        Workpackage wp = wpModel.getWorkpackage(wpID);

        // validate the db fields
        assertThat(wp.getId(), equalTo(2));
        assertThat(wp.getProjectID(), equalTo(1));
        assertThat(wp.getEmployeeID(), equalTo(2));
        assertThat(wp.getParentID(), equalTo(1));
        assertThat(wp.getPositionID(), equalTo(1));
        assertThat(wp.getName(), equalTo("Vorbereitung"));
        assertThat(wp.getDescription(), equalTo("Vorbereitende Arbeiten"));
    }

}
