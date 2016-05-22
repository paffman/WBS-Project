package de.fhbingen.wbs.dbaccess.models.mysql;

import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.TestExecution;
import de.fhbingen.wbs.dbaccess.models.TestExecutionModel;
import de.fhbingen.wbs.jdbcConnection.SQLExecuter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by morit on 18.05.2016.
 */
public class MySQLTestExecutionModel implements TestExecutionModel {


    @Override
    public boolean addNewTestExecution(TestExecution testexec) {
        final Connection connection = SQLExecuter.getConnection();
        final int paramCount = 5;
        PreparedStatement stm = null;
        boolean success = false;

        String storedProcedure = "CALL test_execution_new(";

        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }

        storedProcedure += "?)";

        //System.out.println(storedProcedure);

        try {
            stm = connection.prepareStatement(storedProcedure);

            stm.setInt(1, testexec.getTestcaseID());
            stm.setInt(2, testexec.getEmployeeID());
            stm.setString(3, testexec.getRemark());
            stm.setTimestamp(4, testexec.getTime());
            stm.setString(5, testexec.getStatus());

            stm.execute();
            success = true;

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
        return success;
    }

    @Override
    public List<TestExecution> getExecutionsForTestCase(TestCase testcase) {

        final Connection connection = SQLExecuter.getConnection();
        List<TestExecution> teList = new ArrayList<TestExecution>();
        int paramCount = 1;
        ResultSet sqlResult = null;
        PreparedStatement stm = null;

        String storedProcedure = "CALL test_execution_select_by_test_case(";

        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }

        storedProcedure += "?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, testcase.getId());
            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                teList.add(teFromResultSet(sqlResult));
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

        return teList;


    }




    @Override
    public TestExecution getLastExecution(TestCase testcase) {
        List<TestExecution> execList = getExecutionsForTestCase(testcase);
        TestExecution latestExec = null;
        if(execList.size()>0){
            latestExec = execList.get(execList.size() - 1);
        }

        return latestExec;
    }






    @Override
    public boolean updateTestExecution(TestExecution testexec) {


        final Connection connection = SQLExecuter.getConnection();
        final int paramCount = 5;
        PreparedStatement stm = null;
        boolean success = false;

        String storedProcedure = "CALL test_execution_update_by_id(";

        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }

        storedProcedure += "?)";

        //System.out.println(storedProcedure);

        try {
            stm = connection.prepareStatement(storedProcedure);

            stm.setInt(1, testexec.getTestcaseID());
            stm.setInt(2, testexec.getEmployeeID());
            stm.setString(3, testexec.getRemark());
            stm.setTimestamp(4, testexec.getTime());
            stm.setString(5, testexec.getStatus());

            stm.execute();
            success = true;

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
        return success;

    }




    /**
     * Creates a <code>TestExecution</code> based on a <code>ResultSet</code> freshly fetched from the DB.
     *
     * @param resSet
     *            The result set containing the data
     * @return A <code>TestExecution</code> object
     */
    public static final TestExecution teFromResultSet(final ResultSet resSet) {
        TestExecution te = new TestExecution();

        try {
            te.setId(resSet.getInt("id"));
            te.setTestcaseID(resSet.getInt("fid_tc"));
            te.setEmployeeID(resSet.getInt("fid_emp"));
            te.setRemark(resSet.getString("remark"));
            te.setStatus(resSet.getString("status")); // should return an enum
            te.setTime(resSet.getTimestamp("timestamp"));
            te.setEmployeeLogin(resSet.getString("login"));


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return te;
    }




}
