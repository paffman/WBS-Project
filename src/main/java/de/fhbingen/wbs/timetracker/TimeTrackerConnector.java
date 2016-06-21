package de.fhbingen.wbs.timetracker;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.JSONObject;


/**
 * Connector to the application server.
 */
public class TimeTrackerConnector {

    /**
     * URL for the application server.
     */
    private String address;

    public TimeTrackerConnector(String address){
        this.address = "http://" + address;

    }

    /**
     * Check the connection to the application server.
     * @return connection successful or not
     */
    public boolean checkConnection(){
        try {
            URL url = new URL(this.address +"/api/");
        } catch(MalformedURLException e){
            return false;
        }
        return true;
    }


    /**
     * Create new user.
     * @param name
     * @param password
     * @return the http response code
     */
    public int createUser(String name, String password) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address + "/api/users/");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            JSONObject data = new JSONObject();
            data.put("username", name);
            data.put("password", password);

            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
            os.write(data.toString());
            os.flush();
            os.close();
            connection.disconnect();

            return connection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void createProject(){

    }

    public void updateUser(){

    }




}
