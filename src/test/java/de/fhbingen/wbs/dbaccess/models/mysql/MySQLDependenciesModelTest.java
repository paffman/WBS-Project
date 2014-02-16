package de.fhbingen.wbs.dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import de.fhbingen.wbs.jdbcConnection.SQLExecuter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestData;
import de.fhbingen.wbs.dbaccess.data.Dependency;
import de.fhbingen.wbs.dbaccess.models.DependenciesModel;

public class MySQLDependenciesModelTest {
    private DependenciesModel dpModel;

    @Before
    public final void setup() {
        dpModel = new MySQLDependenciesModel();
    }

    @After
    public final void cleanup() {
        dpModel = null;
    }

    @Test
    public final void testAddNewDependency() {
        // Setup dependency
        Dependency dependency = new Dependency();
        dependency.setFid_wp_predecessor(7);
        dependency.setFid_wp_successor(8);

        dpModel.addNewDependency(dependency);
        List<Dependency> dpList = dpModel.getDependency();
        assertThat(dpList, notNullValue());
        assertThat(dpList.size(), equalTo(4));
        assertThat(dpList.get(0).getFid_wp_predecessor(), equalTo(3));
        assertThat(dpList.get(0).getFid_wp_successor(), equalTo(4));
        assertThat(dpList.get(1).getFid_wp_predecessor(), equalTo(2));
        assertThat(dpList.get(1).getFid_wp_successor(), equalTo(5));
        assertThat(dpList.get(2).getFid_wp_predecessor(), equalTo(5));
        assertThat(dpList.get(2).getFid_wp_successor(), equalTo(6));
        assertThat(dpList.get(3).getFid_wp_predecessor(), equalTo(7));
        assertThat(dpList.get(3).getFid_wp_successor(), equalTo(8));
        TestData.reloadData(SQLExecuter.getConnection());
    }

    @Test
    public final void testGetDependency() {
        List<Dependency> dpList = dpModel.getDependency();
        assertThat(dpList, notNullValue());
        assertThat(dpList.size(), equalTo(3));
        assertThat(dpList.get(0).getFid_wp_predecessor(), equalTo(3));
        assertThat(dpList.get(0).getFid_wp_successor(), equalTo(4));
        assertThat(dpList.get(1).getFid_wp_predecessor(), equalTo(2));
        assertThat(dpList.get(1).getFid_wp_successor(), equalTo(5));
        assertThat(dpList.get(2).getFid_wp_predecessor(), equalTo(5));
        assertThat(dpList.get(2).getFid_wp_successor(), equalTo(6));

    }

    @Test
    public final void testDeleteDependency() {
        dpModel.deleteDependency(5, 6);
        List<Dependency> dpList = dpModel.getDependency();
        assertThat(dpList, notNullValue());
        assertThat(dpList.size(), equalTo(2));
        assertThat(dpList.get(0).getFid_wp_predecessor(), equalTo(3));
        assertThat(dpList.get(0).getFid_wp_successor(), equalTo(4));
        assertThat(dpList.get(1).getFid_wp_predecessor(), equalTo(2));
        assertThat(dpList.get(1).getFid_wp_successor(), equalTo(5));
        TestData.reloadData(SQLExecuter.getConnection());
    }

}
