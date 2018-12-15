package com.example.anjukakoralage.samanola;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class InformationGetActivity extends AppCompatActivity {

    private Button btnSubmite;
    private EditText etName, etMobile, etEmail;
    private String Name, Mobile, Email;
    private ProgressDialog pDialog;
    private JSONObject saveObject;
    private static final String TAG = InformationGetActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_information_get);


        etName = (EditText) findViewById(R.id.etName);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etEmail = (EditText) findViewById(R.id.etEmail);


        btnSubmite = (Button) findViewById(R.id.btnSubmite);
        btnSubmite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name = etName.getText().toString();
                Mobile = etMobile.getText().toString();
                Email = etEmail.getText().toString();

                if (Name.equalsIgnoreCase("")) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please enter you name", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    saveObject = new JSONObject();
                   showPDialog();
                    try {
                        saveObject.put("name", Name);
                        saveObject.put("phoneNo", Mobile);
                        saveObject.put("email", Email);
                        saveObject.put("image", "testImageString");

                    } catch (JSONException e) {
                        hidePDialog();
                    }
                    SaveAPI();
                }
            }
        });
    }

    private void SaveAPI() {

        String URLHead = getResources().getString(R.string.url_head_string);
        String saveURL = URLHead + "api/profile";

        JsonObjectRequest SaveDetails = new JsonObjectRequest(Request.Method.POST, saveURL, saveObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                hidePDialog();
                try {
                    if (!response.getString("data").isEmpty()) {

                        Snackbar.make(findViewById(android.R.id.content), ApplicationConstants.SAVED_SUCCESS, Snackbar.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(),PladgeActivity.class);
                        startActivity(intent);

                    } else {
                        hidePDialog();
                        Snackbar.make(findViewById(android.R.id.content), ApplicationConstants.SAVE_FAILED, Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    hidePDialog();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), ApplicationConstants.ERROR_MSG_GENERAL, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hidePDialog();
                if (volleyError instanceof NetworkError) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), ApplicationConstants.ERROR_MSG_NETWORK, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (volleyError instanceof TimeoutError) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), ApplicationConstants.ERROR_MSG_NETWORK_TIMEOUT, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), ApplicationConstants.ERROR_MSG_GENERAL, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        SaveDetails.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(SaveDetails);
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }


    //get language from preference
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    /*public void checkNetwork(){
        if (haveNetwork()){

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Device is online", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        else if (!haveNetwork()){

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Device is offline", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }*/

    private boolean haveNetwork() {

        boolean have_WIFI = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_WIFI = true;

            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    have_MobileData = true;

        }
        return have_MobileData || have_WIFI;

    }

    private void showPDialog() {
        pDialog = new ProgressDialog(InformationGetActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.show();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
