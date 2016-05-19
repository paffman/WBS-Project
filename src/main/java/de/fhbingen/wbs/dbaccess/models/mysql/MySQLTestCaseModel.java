package de.fhbingen.wbs.dbaccess.models.mysql;

import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.dbaccess.data.Workpackage;
import de.fhbingen.wbs.dbaccess.models.TestCaseModel;
import de.fhbingen.wbs.jdbcConnection.SQLExecuter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL implementation for the TestCaseModel
 */
public class MySQLTestCaseModel implements TestCaseModel {



    @Override
    public final boolean addNewTestCase(TestCase testcase){

        final Connection connection = SQLExecuter.getConnection();
        final int paramCount = 5;
        PreparedStatement stm = null;
        boolean success = false;

        String storedProcedure = "CALL test_case_new(";

        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }

        storedProcedure += "?)";

        //System.out.println(storedProcedure);

        try {
            stm = connection.prepareStatement(storedProcedure);

            stm.setInt(1, testcase.getWorkpackageID());
            stm.setString(2, testcase.getName());
            stm.setString(3, testcase.getDescription());
            stm.setString(4, testcase.getPrecondition());
            stm.setString(5, testcase.getExpectedResult());

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
    public List<TestCase> getAllTestCases(Workpackage wp) {
        final Connection connection = SQLExecuter.getConnection();
        List<TestCase> tcList = new ArrayList<TestCase>();
        int paramCount = 1;
        ResultSet sqlResult = null;
        PreparedStatement stm = null;

        String storedProcedure = "CALL test_case_select_by_wp(";

        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }

        storedProcedure += "?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, wp.getId());
            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                tcList.add(tcFromResultSet(sqlResult));
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

        return tcList;
    }


    @Override
    public boolean updateTestCase(TestCase testcase) {
        final Connection connection = SQLExecuter.getConnection();
        final int paramCount = 5;
        PreparedStatement stm = null;
        boolean success = false;

        String storedProcedure = "CALL test_case_update_by_id(";

        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }

        storedProcedure += "?)";

        //System.out.println(storedProcedure);

        try {
            stm = connection.prepareStatement(storedProcedure);

            stm.setInt(1, testcase.getWorkpackageID());
            stm.setString(2, testcase.getName());
            stm.setString(3, testcase.getDescription());
            stm.setString(4, testcase.getPrecondition());
            stm.setString(5, testcase.getExpectedResult());

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
     * Creates a <code>TestCase</code> based on a <code>ResultSet</code> freshly fetched from the DB.
     *
     * @param resSet
     *            The result set containing the data
     * @return A <code>TestCase</code> object
     */
    public static final TestCase tcFromResultSet(final ResultSet resSet) {
        TestCase tc = new TestCase();

        try {
            tc.setWorkpackageID(resSet.getInt("fid_wp"));
            tc.setDescription(resSet.getString("description"));
            tc.setExpectedResult(resSet.getString("expected_result"));
            tc.setName(resSet.getString("name"));
            tc.setPrecondition(resSet.getString("precondition"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tc;
    }


}
