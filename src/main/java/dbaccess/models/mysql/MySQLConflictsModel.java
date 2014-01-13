package dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dbaccess.data.Conflict;
import dbaccess.models.ConflictsModel;

public class MySQLConflictsModel implements ConflictsModel {

    private Connection connection;

    public MySQLConflictsModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewConflict(Conflict conflict) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("INSERT INTO conflicts VALUES (" + conflict.getId()
                    + "," + conflict.getFid_wp() + ","
                    + conflict.getFid_wp_affected() + ","
                    + conflict.getFid_emp() + "," + conflict.getReason() + ","
                    + ",'" + conflict.getOccurence_date() + "')");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<Conflict> getConflicts() {
        List<Conflict> conList = new ArrayList<Conflict>();
        try {
            ResultSet result = null;
            Conflict conflict = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("SELECT * FROM conflict");

            while (result.next()) {
                conflict = new Conflict(result.getInt(1), result.getInt(2),
                        result.getInt(3), result.getInt(4), result.getInt(5),
                        result.getString(6));
                conList.add(conflict);
            }

            return conList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteConflicts() {
        try {
            Statement stm = connection.createStatement();
            stm.execute("DELETE * FROM conflict");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteConflict(int id) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("DELETE * FROM conflict WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
