package com.example.trackme_nov_edition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.__regActivityToolbar);





        // back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton();     // this function will handle the event of backbutton press on the toolbar
            }
        });
    }

    private void handleBackButton() {
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
    }
}