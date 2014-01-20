package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dbaccess.data.AnalyseData;
import dbaccess.models.AnalyseDataModel;

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
        ad.setId(1);
        ad.setFid_wp(1);
        ad.setFid_baseline(1);
        ad.setName("TestData");
        ad.setBac(2.1);
        ad.setAc(2.2);
        ad.setEv(2.3);
        ad.setEtc(2.4);
        ad.setEac(2.5);
        ad.setCpi(2.6);
        ad.setBac_costs(2.7);
        ad.setAc_costs(2.8);
        ad.setEtc_costs(2.9);
        ad.setSv(3);
        ad.setSpi(4);
        ad.setPv(5);
        
        /*adModel.addNewAnalyseData(ad);
        AnalyseData aData = adModel.getAnalyseData(1);
        assertThat(aData, notNullValue());
        assertThat(aData.getId(), equalTo(1));
        assertThat(aData.getFid_wp(), equalTo(1));
        assertThat(aData.getFid_baseline(), equalTo(1));
        assertThat(aData.getName(), equalTo("TestData"));
        assertThat(aData.getBac(), equalTo(2.1));
        assertThat(aData.getAc(), equalTo(2.2));
        assertThat(aData.getEv(), equalTo(2.3));
        assertThat(aData.getEtc(), equalTo(2.4));
        assertThat(aData.getEac(), equalTo(2.5));
        assertThat(aData.getCpi(), equalTo(2.6));
        assertThat(aData.getBac_costs(), equalTo(2.7));
        assertThat(aData.getAc_costs(), equalTo(2.8));
        assertThat(aData.getEtc_costs(), equalTo(2.9));
        assertThat(aData.getSv(), equalTo(3));
        assertThat(aData.getSpi(), equalTo(4));
        assertThat(aData.getPv(), equalTo(5));*/
    }
    
    //TODO: Tests müssen noch implementiert werden
    @Test
    public final void testGetAnalyseData() {

        AnalyseData aData = adModel.getAnalyseData(1);
        assertThat(aData, notNullValue());

        //assertThat("list should not be empty", wpList.size() > 0);
        //assertThat("list should have 8 work packages", wpList.size() > 3);

    }
    
    @Test
    public final void testGetAnalyseDataForBaseline() {

        System.out.println(adModel.getAnalyseDataForBaseline(1));
        AnalyseData aData = adModel.getAnalyseData(1);
        assertThat(aData, notNullValue());

        //assertThat("list should not be empty", wpList.size() > 0);
        //assertThat("list should have 8 work packages", wpList.size() > 3);

    }

}
