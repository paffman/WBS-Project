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

import java.sql.Connection;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbcConnection.SQLExecuter;
import dbaccess.models.SemaphoreModel;

/**
 * The <code>MySQLSemaphoreModel</code> class implements the
 * <code>SemaphoreModel</code> and handles all the database access concerning
 * semaphores.
 */
public class MySQLSemaphoreModel implements SemaphoreModel {

    @Override
    public final boolean enterSemaphore(final String tag, final int id) {
        Connection connection = SQLExecuter.getConnection();
        boolean pFailed = false;
        PreparedStatement stm = null;
        String storedProcedure = "CALL semaphore_p(?,?)";
        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setString(1, tag);
            stm.setInt(2, id);
            stm.execute();
        } catch (MySQLIntegrityConstraintViolationException e) {
            pFailed = true;
        } catch (SQLException e) {
            System.out.println(e.getClass().getName());
            e.printStackTrace();
            pFailed = true;
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return !pFailed;
    }

    @Override
    public final boolean enterSemaphore(final String tag, final int id,
            final boolean force) {
        if (!force) {
            return enterSemaphore(tag, id);
        }

        Connection connection = SQLExecuter.getConnection();
        boolean pFailed = false;
        boolean entryExists = false;
        int empId = 0;

        PreparedStatement getEmpId = null;
        String storedProcedure = "CALL semaphore_select(?)";
        try {
            getEmpId = connection.prepareStatement(storedProcedure);
            getEmpId.setString(1, tag);
            ResultSet sqlResult = getEmpId.executeQuery();
            entryExists = sqlResult.next();
            if (entryExists) {
                empId = sqlResult.getInt("fid_emp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            pFailed = true;
        } finally {
            try {
                if (getEmpId != null) {
                    getEmpId.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pFailed) {
            return false;
        }

        if (entryExists) {
            PreparedStatement leaveStmt = null;
            storedProcedure = "CALL semaphore_v(?,?)";
            try {
                leaveStmt = connection.prepareStatement(storedProcedure);
                leaveStmt.setString(1, tag);
                leaveStmt.setInt(2, empId);
                leaveStmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                pFailed = true;
            } finally {
                try {
                    if (leaveStmt != null) {
                        leaveStmt.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pFailed) {
                return false;
            }
        }

        PreparedStatement enterStmt = null;
        storedProcedure = "CALL semaphore_p(?,?)";
        try {
            enterStmt = connection.prepareStatement(storedProcedure);
            enterStmt.setString(1, tag);
            enterStmt.setInt(2, id);
            enterStmt.execute();
        } catch (MySQLIntegrityConstraintViolationException e) {
            pFailed = true;
        } catch (SQLException e) {
            e.printStackTrace();
            pFailed = true;
        } finally {
            try {
                if (enterStmt != null) {
                    enterStmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return !pFailed;
    }

    @Override
    public final void leaveSemaphore(final String tag, final int id) {
        Connection connection = SQLExecuter.getConnection();
        PreparedStatement leaveStmt = null;
        String storedProcedure = "CALL semaphore_v(?,?)";
        try {
            leaveStmt = connection.prepareStatement(storedProcedure);
            leaveStmt.setString(1, tag);
            leaveStmt.setInt(2, id);
            leaveStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (leaveStmt != null) {
                    leaveStmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
