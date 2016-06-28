package de.fhbingen.wbs.timetracker;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Connector to the application server.
 */
public class TimeTrackerConnector {

    /**
     * URL for the application server.
     */
    private URL address;

    /**
     * Token for the login user.
     */
    private String token;

    public TimeTrackerConnector(String address) throws MalformedURLException {
        this.address = new URL(address + "/api/");
    }

    /**
     * Check the connection to the application server.
     *
     * @return connection successful or not
     */
    public boolean checkConnection() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) address.openConnection();
            int response = connection.getResponseCode();
            connection.disconnect();
            if (response == HttpURLConnection.HTTP_BAD_GATEWAY)
                return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    /**
     * Create new user.
     *
     * @param name
     * @param password
     * @return the http response code
     */
    public int createUser(String name, String password) throws IOException {
        URL url = new URL(address, "users/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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


    }

    /**
     * It doesn't actually create a new project, it is just needed to update the backend, due to crappy legacy code.
     */
    public int createProject() throws IOException {
        URL url = new URL(address, "projects/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/json");
        connection.addRequestProperty("Authorization", "Token " + token);

        connection.disconnect();

        return connection.getResponseCode();


    }

    /**
     * Update or reset password.
     * @param userID
     * @param newPW
     * @throws UnirestException
     */
    public void updateUser(int userID, String newPW) throws UnirestException {
        HttpResponse<JsonNode> jsonResponse = Unirest.patch(address + "users/" + userID + "/")
                .header("Conten-type", "application/json")
                .header("Authorization", "Token " + token)
                .field("password", newPW)
                .asJson();
    }

    /**
     * Login user and get an token for authentication.
     * @param name
     * @param password
     * @return
     * @throws IOException
     */
    public int loginUser(String name, String password) throws IOException {
        URL url = new URL(address, "login/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

        if (connection.getResponseCode() != HttpURLConnection.HTTP_BAD_REQUEST) {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), Charset.forName("UTF-8")));
            String jsonData = "";
            String s;
            while ((s = br.readLine()) != null) {
                jsonData += s;
            }
            br.close();

            try {
                JSONParser jsParser = new JSONParser();
                JSONObject jsObject = (JSONObject) jsParser.parse(jsonData);
                this.token = jsObject.get("token").toString();
            } catch(ParseException e){
                e.printStackTrace();
            }
        }

        connection.disconnect();

        return connection.getResponseCode();


    }

    /**
     * Align an project to user.
     * @param dbID
     * @param userID
     * @return
     * @throws IOException
     */
    public int addUserToProject(int dbID, int userID) throws IOException{
        URL url = new URL(address, "users/" + userID + "/projects/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

        connection.disconnect();

        return connection.getResponseCode();

    }


}
