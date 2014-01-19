package dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbcConnection.SQLExecuter;
import dbaccess.models.SemaphoreModel;

public class MySQLSemaphoreModel implements SemaphoreModel {

	@Override
	public boolean enterSemaphore(String tag, int id) {
		Connection connection = SQLExecuter.getConnection();
		boolean pFailed = false;
		PreparedStatement stm = null;
		String storedProcedure = "CALL semaphore_p(?,?)";
		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setString(1, tag);
			stm.setInt(2, id);
			stm.execute();
			// } catch (){
			// pFailed = true;
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
	public boolean enterSemaphore(String tag, int id, boolean force) {
		if ( !force ){
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
				empId = sqlResult.getInt("emp_id");
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
		storedProcedure = "CALL semaphore_p(?)";
		try {
			enterStmt = connection.prepareStatement(storedProcedure);
			enterStmt.setString(1, tag);
			enterStmt.setInt(2, id);
			enterStmt.execute();
			// } catch (){
			// pFailed = true;
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
	public void leaveSemaphore(String tag, int id) {
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
