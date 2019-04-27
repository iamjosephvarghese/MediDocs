package org.blockchain.medical.finalandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button logOut;
    Button viewRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logOut = findViewById(R.id.logOut);
        viewRecord = findViewById(R.id.viewRecord);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("loggedin", false);
                editor.remove("name");
                editor.commit();

                Intent loginIntent = new Intent(Home.this,Login.class);
                startActivity(loginIntent);
                finish();


            }
        });

        viewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent viewIntent = new Intent(Home.this,ViewRecord.class);
                startActivity(viewIntent);
                finish();
            }
        });
    }
}