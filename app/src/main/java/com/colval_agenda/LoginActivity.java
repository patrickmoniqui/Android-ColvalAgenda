package com.colval_agenda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.colval_agenda.BLL.Response.LoginResponse;
import com.colval_agenda.Utils.Utils;
import com.colval_agenda.DAL.LoginManager;
import com.github.tibolte.colvalcalendar.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    EditText _emailText,  _passwordText;
    Button _loginButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        int userId = Integer.parseInt(_emailText.getText().toString());
        String password = _passwordText.getText().toString();
        LoginManager lm = new LoginManager();

        // TODO: Implement your own authentication logic here.

        AsyncTask<Void, Void, LoginResponse> loginAsync = new LoginAsync(userId, password);
        loginAsync.execute();
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        //moveTaskToBack(true);
    }

    public void onLoginSuccess(LoginResponse response) {
        _loginButton.setEnabled(true);
        int userId = Integer.parseInt(_emailText.getText().toString());
        String password = _passwordText.getText().toString();

        Utils.RegisterGlobalUser(getBaseContext(), userId, password, response.getDisplayName());

        Intent intent = new Intent(this, MainActivity.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
                startActivity(intent);
            }
        }, 2000); // 2sec delay to show welcome <user>
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);

        progressDialog.dismiss();
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || email.length() != 7) {
            _emailText.setError("Entrer un No de NA valide (7 chiffres).");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Entrer un mot de passe.");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    class LoginAsync extends AsyncTask<Void, Void, LoginResponse>
    {
        private int userId;
        private String pwd;

        public LoginAsync(int _username, String _pwd)
        {
            userId = _username;
            pwd = _pwd;
        }


        @Override
        protected LoginResponse doInBackground(Void... params) {
            try {
                final String url = "http://colvalagenda.gear.host/api/login/authentification?username=" + userId + "&pwd=" + pwd;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                LoginResponse response = restTemplate.getForObject(url, LoginResponse.class);
                return response;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                LoginResponse response = new LoginResponse();
                response.setSuccess(false);
                return response;
            }
        }

        @Override
        protected void onPostExecute(LoginResponse response) {

            if(response.isSuccess())
            {
                progressDialog.setMessage("Welcome " + response.getDisplayName() + "!");

                onLoginSuccess(response);
            }
            else
            {
                onLoginFailed();
            }
        }
    }
}