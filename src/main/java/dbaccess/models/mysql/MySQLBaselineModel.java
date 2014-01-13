package dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dbaccess.data.Baseline;
import dbaccess.models.BaselineModel;

public class MySQLBaselineModel implements BaselineModel {

    private Connection connection;

    public MySQLBaselineModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewBaseline(Baseline line) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("INSERT INTO baseline VALUES (" + line.getId() + ","
                    + line.getFid_project() + ",'" + line.getBl_date() + "','"
                    + line.getDescription() + "')");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<Baseline> getBaseline() {
        List<Baseline> blList = new ArrayList<Baseline>();
        try {
            ResultSet result = null;
            Baseline baseline=null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("SELECT * FROM baseline");

            while (result.next()){
                baseline = new Baseline(result.getInt(1), result.getInt(2),
                        result.getString(3), result.getString(4));
                blList.add(baseline);
            }
            
            return blList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Baseline getBaseline(int baselineID) {
        Baseline baseline = null;
        try {
            ResultSet result = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("SELECT * FROM baseline WHERE id = "
                    + baselineID);

            if (result.next()){
                baseline = new Baseline(result.getInt(1), result.getInt(2),
                        result.getString(3), result.getString(4));
            }
            
            return baseline;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
