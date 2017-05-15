package com.colval_agenda.DAL;

import com.colval_agenda.BLL.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shadow on 12/05/2017.
 */

public class HttpManageHandler {

    public boolean callWebService(String reqURL, String params) {
        HttpURLConnection connexion = null;
        String response = null;
        boolean login = true;

        try {
            reqURL += "?" + params;
            URL url = new URL(reqURL);
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestMethod("GET");
            BufferedReader in = new BufferedReader((new InputStreamReader(connexion.getInputStream())));
            InputStream is = new BufferedInputStream(connexion.getInputStream());
            response = convertStreamToString(is);
            JSONObject jsonObject = new JSONObject(response);
            login = jsonObject.getBoolean("validation");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connexion.disconnect();
        }
        return login;
    }

    private String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while((line=reader.readLine()) != null){
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  sb.toString();
    }
}


  /* List<Event> events = new ArrayList<>();
    public boolean callLoginService(String reqURL, String params) {
        HttpURLConnection connexion = null;

        try {
            reqURL += "?" + params;
            URL url = new URL(reqURL);
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestMethod("GET");
            BufferedReader in = new BufferedReader((new InputStreamReader(connexion.getInputStream())));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connexion.disconnect();
        }
        return true;
    }

    public List<Event> callClassService(String reqURL, String params) {
        HttpURLConnection connexion = null;

        try {
            reqURL += "?" + params;
            URL url = new URL(reqURL);
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestMethod("GET");
            BufferedReader in = new BufferedReader((new InputStreamReader(connexion.getInputStream())));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connexion.disconnect();
        }
        return events;
    }
    */