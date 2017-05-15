package com.colval_agenda.DAL;

import android.os.AsyncTask;

import com.colval_agenda.BLL.Event;
import com.colval_agenda.DrawableCalendarEvent;
import com.colval_agenda.Utils.Preferences;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 17-05-08.
 */

public class LoginManager {


    String param;

    public static boolean ValidateUser(int id, String pass)
    {
        /*
        boolean validateLogin = true;
        param = "ID=" + id + "&";
        param += "PASSWORD=" + pass;
        new LoginASyncTask().execute();
        return validateLogin;
         */
        return true;
    }

    class LoginASyncTask extends AsyncTask <Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            HttpManageHandler handler = new HttpManageHandler();
            handler.callWebService(Preferences.URL_CHECK_LOGIN, param);
            return null;
        }
    }
}
