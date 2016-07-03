package de.fhbingen.wbs.timetracker;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.json.JSONException;
import org.json.JSONObject;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
        this.address = new URL(new URL(address), "api/");
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
     * Make a [PATCH]
     * @param url
     * @param data
     * @return response Code
     * @throws UnirestException
     */
    public int patch(String url, Map<String, Object> data) throws UnirestException {
        Map<String, String> header = new HashMap<>();
        header.put("accept", "application/json");
        header.put("Authorization", "Token " + token);

        HttpRequestWithBody h = Unirest.patch(getAPIURL(url));
        h.headers(header);
        h.fields(data);

        return h.asJson().getCode();
    }


    /**
     * Make a [POST]
     * @param url
     * @param data
     * @param authentication
     * @return resposne Code
     * @throws UnirestException
     */
    public int post(String url, Map<String, Object> data, boolean authentication) throws UnirestException{
        Map<String, String> header = new HashMap<>();
        header.put("accept", "application/json");

        if (authentication) {
            header.put("Authorization", "Token " + token);
        }

        HttpRequestWithBody h = Unirest.post(getAPIURL(url));
        h.headers(header);
        h.fields(data);

        if (url.equals("login/")) {
            try {
                JSONObject j = h.asJson().getBody().getObject();
                this.token = j.get("token").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return h.asString().getCode();
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token){
        this.token = token;
    }

    /**
     * generates a String URL for the given API endpoint
     * @param path to the requested endpoint. Beginning or trailing slashes are not needed, but won't break anything.
     * @return String path to the requested endpoint
     * @throws UnirestException thrown if URL is malformed
     */
    public String getAPIURL(String path) throws UnirestException {
        try {
            return new URL(address, path).toString();
        } catch (MalformedURLException e) {
            throw new UnirestException(e);
        }
    }
}
