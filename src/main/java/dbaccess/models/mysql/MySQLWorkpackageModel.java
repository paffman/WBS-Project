package dbaccess.models.mysql;

import dbaccess.data.Workpackage;
import dbaccess.models.WorkpackageModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The <code>MySQLWorkpackageModel</code> class implements the
 * <code>WorkpackageModel</code> and handles all the database access
 * concerning work packages.
 */
public class MySQLWorkpackageModel implements WorkpackageModel {

    private Connection connection;

    public MySQLWorkpackageModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public final void addNewWorkpackage(final Workpackage workpackage) {

    }

    @Override
    public final List<Workpackage> getWorkpackage() {
        List<Workpackage> wpList = new ArrayList<Workpackage>();
        ResultSet sqlResult = null;

        try {
            Statement stm = connection.createStatement();
            //sqlResult = stm.executeQuery("SELECT * FROM employees");
            sqlResult = stm.executeQuery("CALL employees_select(NULL)");

            while (sqlResult.next()) {
                //String name = sqlResult.getString("name");
                wpList.add(new Workpackage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //CallableStatement cstmt = null;

        /*try {
            String SQL = "{call employees_select (?)}";
            cstmt = connection.prepareCall(SQL);
            cstmt.setBoolean(1, false);
            ResultSet res = CALL dependencies_select
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        return wpList;
    }

    @Override
    public final List<Workpackage> getWorkpackage(final boolean onlyLeaves) {
        return null;
    }

    @Override
    public final Workpackage getWorkpackage(final String stringID) {
        return null;
    }

    @Override
    public final List<Workpackage> getWorkpackagesInDateRange(final Date from,
                                                              final Date to) {
        return null;
    }

    @Override
    public final void updateWorkpackage(final Workpackage wp) {

    }

    @Override
    public final void deleteWorkpackage(final int wpID) {

    }

}
