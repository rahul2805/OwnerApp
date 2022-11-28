package com.abc.templateownerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abc.templateownerapp.Model.User;
import com.abc.templateownerapp.utils.NetworkResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private String username, psswd;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new VerifyTokenTask().execute();
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.login);
        progressBar = findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ((EditText)findViewById(R.id.username)).getText().toString();
                psswd = ((EditText)findViewById(R.id.password)).getText().toString();
                if (username.length() == 0 || psswd.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    new LoginTask().execute();
                }
            }
        });
    }
    private class LoginTask extends AsyncTask<Void, Void, NetworkResponse> {

        @Override
        protected NetworkResponse doInBackground(Void... voids) {
            try {
                return Network.Login(username, psswd);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NetworkResponse response) {
            progressBar.setVisibility(View.GONE);
            if (response != null) {
                if (response.getResponseCode() == 200) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.getResponseString());
                        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        String name = jsonObject.getJSONObject("data").getString("name");
                        String email = jsonObject.getJSONObject("data").getString("email");
                        String id = jsonObject.getJSONObject("data").getString("id");
                        String token = jsonObject.getString("token");
                        User.getUserInstance(username, psswd, name, email, id, token);
                        jsonObject.getJSONObject("data").put("username", username);
                        jsonObject.getJSONObject("data").put("psswd", psswd);

                        Network.addUserDataToSharedPrefs(LoginActivity.this, jsonObject.getJSONObject("data").toString());
                        Network.updateTokenInSharedPref(LoginActivity.this, jsonObject.getString("token"));
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class VerifyTokenTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences preferences = LoginActivity.this.getSharedPreferences(Network.SHARE_PREF_NAME, LoginActivity.this.MODE_PRIVATE);

            if (preferences.contains("userData")) {
                try {
                    String token = preferences.getString("token", "");
                    NetworkResponse tokenVerifyResponse = Network.verifyToken(token);
                    JSONObject jsonObject = new JSONObject(preferences.getString("userData", ""));
                    if (tokenVerifyResponse.getResponseCode() == 200) {
//                    User.getUserInstance(username, psswd, role, name, email, id, token);

                        User.getUserInstance(jsonObject.getString("username"),
                                jsonObject.getString("psswd"),
                                jsonObject.getString("name"),
                                jsonObject.getString("email"),
                                jsonObject.getString("id"),
                                token);

                        return true;
                    } else {
                        NetworkResponse response =  Network.Login(jsonObject.getString("username"), jsonObject.getString("psswd"));
                        if (response.getResponseCode() == 200) {
                            JSONObject object = null;
                            try {
                                object = new JSONObject(response.getResponseString());
                                String t = object.getString("token");
                                User.getUserInstance(jsonObject.getString("username"),
                                        jsonObject.getString("psswd"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("id"),
                                        t);
                                Network.updateTokenInSharedPref(LoginActivity.this, t);
                                return true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return  false;
        }

        @Override
        protected void onPostExecute(Boolean resBool) {
            if (resBool) {
                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
