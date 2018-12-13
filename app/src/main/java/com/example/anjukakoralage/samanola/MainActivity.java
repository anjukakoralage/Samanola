package com.example.anjukakoralage.samanola;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnSinhala, btnTamil, btnEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        btnSinhala = (Button) findViewById(R.id.btnSinhala);
        btnTamil = (Button) findViewById(R.id.btnTamil);
        btnEnglish = (Button) findViewById(R.id.btnEnglish);


        btnSinhala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setLocale("si");
                // recreate();
                Intent intent = new Intent(getApplicationContext(), InformationGetActivity.class);
                startActivity(intent);
            }
        });

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
                //recreate();
                Intent intent = new Intent(getApplicationContext(), InformationGetActivity.class);
                startActivity(intent);
            }
        });

        btnTamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ta");
                //recreate();
                Intent intent = new Intent(getApplicationContext(), InformationGetActivity.class);
                startActivity(intent);
            }
        });

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
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }
}
