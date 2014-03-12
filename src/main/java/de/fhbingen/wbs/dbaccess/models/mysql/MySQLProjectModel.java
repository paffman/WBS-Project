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

package de.fhbingen.wbs.dbaccess.models.mysql;

import de.fhbingen.wbs.dbaccess.data.Project;
import de.fhbingen.wbs.dbaccess.models.ProjectModel;
import de.fhbingen.wbs.jdbcConnection.SQLExecuter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLProjectModel implements ProjectModel {

    @Override
    public void addNewProject(final Project project) {
        final Connection connection = SQLExecuter.getConnection();
        PreparedStatement stm = null;
        final String storedProcedure = "CALL project_new(?, ?, ?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, project.getProjectLeader());
            stm.setString(2, project.getName());
            stm.setInt(3, project.getLevels());
            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Project> getProject() {
        List<Project> projectsList = new ArrayList<Project>();
        final Connection connection = SQLExecuter.getConnection();
        Statement stm = null;
        ResultSet sqlResult = null;
        final String sqlString = "CALL project_select()";

        try {
            stm = connection.createStatement();
            sqlResult = stm.executeQuery(sqlString);

            while (sqlResult.next()) {
                Project tmpProject = new Project(sqlResult.getInt("id"),
                        sqlResult.getInt("fid_pl"),
                        sqlResult.getString("name"),
                        sqlResult.getInt("levels"));

                projectsList.add(tmpProject);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlResult != null) {
                    sqlResult.close();
                }
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return projectsList;
    }

}
