package de.fhbingen.wbs.timetracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;


/**
 * Connector to the application server.
 */
public class TimeTrackerConnector {

    /**
     * URL for the application server.
     */
    private String address;

    /**
     * Token for the login user.
     */
    private String token;

    public TimeTrackerConnector(String address){
        this.address = "http://" + address;

    }

    /**
     * Check the connection to the application server.
     * @return connection successful or not
     */
    public boolean checkConnection(){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(this.address +"/api/");
            connection = (HttpURLConnection) url.openConnection();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_BAD_GATEWAY)
                return false;
        } catch(Exception e){
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

            return connection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * It doesn't actually create a new project, it is just needed to update the backend, due to crappy legacy code.
     */
    public int createProject(){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address + "/api/projects/");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.addRequestProperty("Authorization", "Token " + token);

            return connection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            connection.disconnect();
        }

    }

    public void updateUser(){

    }

    public int loginUser(String name, String password){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address + "/api/login/");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");

            JSONObject data = new JSONObject();
            data.put("username", name);
            data.put("password", password);

            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
            os.write(data.toString());
            os.flush();
            os.close();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_BAD_REQUEST) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), Charset.forName("UTF-8")));
                String jsonData = "";
                String s;
                while ((s = br.readLine()) != null) {
                    jsonData += s;
                }
                br.close();
                JSONParser jsParser = new JSONParser();
                JSONObject jsObject = (JSONObject) jsParser.parse(jsonData);
                this.token = jsObject.get("token").toString();
            }


            return connection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            connection.disconnect();
        }
    }

    public int addUserToProject(int dbID, int userID){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address + "/api/users/" + userID + "/projects/");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.addRequestProperty("Authorization", "Token " + token);

            JSONObject data = new JSONObject();
            data.put("project", "/api/projects/" + dbID + "/");

            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
            os.write(data.toString());
            os.flush();
            os.close();

            System.out.println("Test");

            return connection.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            connection.disconnect();
        }
    }






}
