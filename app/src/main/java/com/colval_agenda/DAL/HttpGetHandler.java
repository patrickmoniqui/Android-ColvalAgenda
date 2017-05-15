package com.colval_agenda.DAL;

import android.graphics.Color;

import com.colval_agenda.BLL.Event;
import com.colval_agenda.Utils.Utils;

import org.json.JSONArray;
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
import java.util.Calendar;
import java.util.List;

/**
 * Created by Shadow on 12/05/2017.
 */

public class HttpGetHandler {

    public List<Event> callGetWebService(String reqURL) {
        String response = null;
        HttpURLConnection connexion = null;
        List<Event> eventList = null;
        try {
            URL url = new URL(reqURL);
            connexion = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(connexion.getInputStream());
            response = convertStreamToString(is);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("events");
            eventList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                Event event = new Event();
                event.setTitle(c.getString("TITLE"));
                event.setDescription(c.getString("DESCRIPTION"));
                event.setLocation(c.getString("LOCATION"));
                event.setEventColor(Color.DKGRAY);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(c.getString("DATE")));
                calendar.add(Calendar.HOUR, Integer.parseInt(c.getString("START_TIME")));
                event.setStartDate(Utils.calendarToDate(calendar));
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, Integer.parseInt(c.getString("DATE")));
                calendar.add(Calendar.HOUR, Integer.parseInt(c.getString("END_TIME")));
                event.setFinishDate(Utils.calendarToDate(calendar));
                event.setAllDay(false);
                event.setReminder(true);
                event.setEditable(false);
                event.setUserId(Integer.parseInt(c.getString("USER")));
                eventList.add(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connexion.disconnect();
        }
        return eventList;
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
