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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import dbaccess.data.Project;
import dbaccess.models.ProjectModel;
import jdbcConnection.SQLExecuter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sqlutils.TestData;

import java.util.List;

public class MySQLProjectModelTest {

    private ProjectModel projectModel;

    @Before
    public void setUp() {
        projectModel = new MySQLProjectModel();
    }

    @After
    public void tearDown() {
        projectModel = null;
    }

    @Test
    public void testAddNewProject() throws Exception {
        final Project projectToAdd = new Project(-1, 1, "test project", 3);
        projectModel.addNewProject(projectToAdd);

        List<Project> projectList = projectModel.getProject();
        assertThat(projectList, notNullValue());
        assertThat(projectList.size(), equalTo(2));
        assertThat(projectList.get(1).getId(), equalTo(2));
        assertThat(projectList.get(1).getProjectLeader(), equalTo(1));
        assertThat(projectList.get(1).getName(), equalTo("test project"));
        assertThat(projectList.get(1).getLevels(), equalTo(3));

        TestData.reloadData(SQLExecuter.getConnection());
    }

    @Test
    public void testGetProject() throws Exception {
        List<Project> wpList = projectModel.getProject();

        assertThat(wpList, notNullValue());
        assertThat(wpList.size(), equalTo(1));
        assertThat(wpList.get(0).getId(), equalTo(1));
        assertThat(wpList.get(0).getProjectLeader(), equalTo(1));
        assertThat(wpList.get(0).getName(), equalTo("Mauerbau"));
        assertThat(wpList.get(0).getLevels(), equalTo(4));
    }

}
