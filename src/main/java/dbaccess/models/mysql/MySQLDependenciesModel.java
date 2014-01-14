package dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dbaccess.data.Dependency;
import dbaccess.models.DependenciesModel;

public class MySQLDependenciesModel implements DependenciesModel {

    private Connection connection;

    public MySQLDependenciesModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewDependency(Dependency dependency) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("INSERT INTO dependencies VALUES ("
                    + dependency.getFid_wp_predecessor() + ","
                    + dependency.getFid_wp_successor() + ")");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<Dependency> getDependency() {
        List<Dependency> depList = new ArrayList<Dependency>();
        try {
            ResultSet result = null;
            Dependency dependency = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("SELECT * FROM dependencies");

            while (result.next()) {
                dependency = new Dependency(result.getInt(1), result.getInt(2));
                depList.add(dependency);
            }

            return depList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteDependency(int predecessorWpID, int successorWpID) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("DELETE * FROM dependency WHERE fid_wp_predecessor = "
                    + predecessorWpID + "AND fid_wp_successor = "
                    + successorWpID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
