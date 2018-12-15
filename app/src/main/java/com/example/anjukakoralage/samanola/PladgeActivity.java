package com.example.anjukakoralage.samanola;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PladgeActivity extends AppCompatActivity {

    private Button btnPldge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pladge);

        btnPldge = (Button) findViewById(R.id.btnpladge);

        btnPldge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThankActivty.class);
                startActivity(intent);
            }
        });
    }
}
