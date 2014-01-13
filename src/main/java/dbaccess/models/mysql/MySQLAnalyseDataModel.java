package dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dbaccess.data.AnalyseData;
import dbaccess.models.AnalyseDataModel;

public class MySQLAnalyseDataModel implements AnalyseDataModel {
    private Connection connection;

    public MySQLAnalyseDataModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewAnalyseData(AnalyseData data) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("INSERT INTO analyse_data VALUES (" + data.getId()
                    + "," + data.getFid_wp() + "," + data.getFid_baseline()
                    + ", '" + data.getName() + "'," + data.getBac() + ","
                    + data.getAc() + "," + data.getEv() + "," + data.getEtc()
                    + "," + data.getEac() + "," + data.getCpi() + ","
                    + data.getBac_costs() + "," + data.getAc_costs() + ","
                    + data.getEtc_costs() + "," + data.getSv() + ","
                    + data.getSpi() + "," + data.getPv() + ")");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public AnalyseData getAnalyseData(int fid) {
        AnalyseData aData = null;
        try {
            ResultSet result = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("SELECT * FROM analyse_data WHERE id = "
                    + fid);

            if (result.next()){
                aData = new AnalyseData(result.getInt(1), result.getInt(2),
                        result.getInt(3), result.getString(4),
                        result.getDouble(5), result.getDouble(6),
                        result.getDouble(7), result.getDouble(8),
                        result.getDouble(9), result.getDouble(10),
                        result.getDouble(11), result.getDouble(12),
                        result.getDouble(13), result.getInt(14),
                        result.getInt(15), result.getInt(16));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aData;
    }

    @Override
    public List<?> getAnalyseDataForBaseline(int baseline) {
        List<AnalyseData> adList = new ArrayList<AnalyseData>();
        try {
            ResultSet result = null;
            AnalyseData aData=null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("SELECT * FROM analyse_data WHERE fid_baseline = "
                    + baseline);

            while (result.next()){
                aData = new AnalyseData(result.getInt(1), result.getInt(2),
                        result.getInt(3), result.getString(4),
                        result.getDouble(5), result.getDouble(6),
                        result.getDouble(7), result.getDouble(8),
                        result.getDouble(9), result.getDouble(10),
                        result.getDouble(11), result.getDouble(12),
                        result.getDouble(13), result.getInt(14),
                        result.getInt(15), result.getInt(16));
                adList.add(aData);
            }
            
            return adList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
