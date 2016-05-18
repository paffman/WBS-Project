package de.fhbingen.wbs.dbaccess.models.mysql;

import de.fhbingen.wbs.dbaccess.models.TestCaseModel;

/**
 * MySQL implementation for the TestCaseModel
 */
public class MySQLTestCaseModel implements TestCaseModel {
    //TODO implement methods



    @Override
    public final boolean addNewTestCase(TestCase testcase){

        final Connection connection = SQLExecuter.getConnection();
        final int paramCount = 5;
        PreparedStatement stm = null;
        boolean success = false;

        String storedProcedure = "CALL testcase_new(";

        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }

        storedProcedure += "?)";

        //System.out.println(storedProcedure);

        try {
            stm = connection.prepareStatement(storedProcedure);

            stm.setInt(1, testcase.getWorkpackageID());
            stm.setString(2, testcase.getPrecondition());
            stm.setString(3, testcase.getDescription());
            stm.setString(4, testcase.getExpectedResult());
            stm.setString(5, testcase.getName());

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





}
