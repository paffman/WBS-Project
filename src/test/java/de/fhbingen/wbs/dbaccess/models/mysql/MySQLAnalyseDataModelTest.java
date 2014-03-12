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
import de.fhbingen.wbs.dbaccess.data.AnalyseData;
import de.fhbingen.wbs.dbaccess.models.AnalyseDataModel;

public class MySQLAnalyseDataModelTest {
    private AnalyseDataModel adModel;

    @Before
    public final void setup() {
        adModel=new MySQLAnalyseDataModel();
    }

    @After
    public final void cleanup() {
        adModel = null;
    }


    @Test
    public final void testAddNewAnalyseData() {

        //Setup analyse data
        AnalyseData ad=new AnalyseData();
        ad.setFid_wp(2);
        ad.setFid_baseline(1);
        ad.setName("TestData");
        ad.setBac(3.1);
        ad.setAc(3.2);
        ad.setEv(3.3);
        ad.setEtc(3.4);
        ad.setEac(3.5);
        ad.setCpi(3.6);
        ad.setBac_costs(3.7);
        ad.setAc_costs(3.8);
        ad.setEtc_costs(3.9);
        ad.setSv(3);
        ad.setSpi(4);
        ad.setPv(5);

        adModel.addNewAnalyseData(ad);
        List<AnalyseData> aDatas = adModel.getAnalyseData(2);
        assertThat(aDatas.size(), equalTo(1));
        AnalyseData aData = aDatas.get(0);
        assertThat(aData, notNullValue());
        assertThat(aData.getFid_wp(), equalTo(2));
        assertThat(aData.getFid_baseline(), equalTo(1));
        assertThat(aData.getName(), equalTo("TestData"));
        assertThat(aData.getBac(), equalTo(3.1));
        assertThat(aData.getAc(), equalTo(3.2));
        assertThat(aData.getEv(), equalTo(3.3));
        assertThat(aData.getEtc(), equalTo(3.4));
        assertThat(aData.getEac(), equalTo(3.5));
        assertThat(aData.getCpi(), equalTo(3.6));
        assertThat(aData.getBac_costs(), equalTo(3.7));
        assertThat(aData.getAc_costs(), equalTo(3.8));
        assertThat(aData.getEtc_costs(), equalTo(3.9));
        assertThat(aData.getSv(), equalTo(3.0));
        assertThat(aData.getSpi(), equalTo(4.0));
        assertThat(aData.getPv(), equalTo(5.0));

        TestData.reloadData(SQLExecuter.getConnection());
    }

    @Test
    public final void testGetAnalyseData() {

        List<AnalyseData> aDatas = adModel.getAnalyseData(1);
        assertThat(aDatas.size(), equalTo(1));
        AnalyseData aData = aDatas.get(0);

        assertThat(aData, notNullValue());
        assertThat(aData, notNullValue());
        assertThat(aData.getFid_wp(), equalTo(1));
        assertThat(aData.getFid_baseline(), equalTo(1));
        assertThat(aData.getName(), equalTo("TestanalyseData"));
        assertThat(aData.getBac(), equalTo(2.1));
        assertThat(aData.getAc(), equalTo(2.2));
        assertThat(aData.getEv(), equalTo(2.3));
        assertThat(aData.getEtc(), equalTo(2.4));
        assertThat(aData.getEac(), equalTo(2.5));
        assertThat(aData.getCpi(), equalTo(2.6));
        assertThat(aData.getBac_costs(), equalTo(2.7));
        assertThat(aData.getAc_costs(), equalTo(2.8));
        assertThat(aData.getEtc_costs(), equalTo(2.9));
        assertThat(aData.getSv(), equalTo(3.0));
        assertThat(aData.getSpi(), equalTo(4.0));
        assertThat(aData.getPv(), equalTo(5.0));

    }

    @Test
    public final void testGetAnalyseDataForBaseline() {

        List<AnalyseData> adList = adModel.getAnalyseDataForBaseline(1);

        assertThat(adList, notNullValue());
        assertThat("list should not be empty", adList.size() > 0);
        assertThat(adList.get(0).getName(), equalTo("TestanalyseData"));
    }

}
