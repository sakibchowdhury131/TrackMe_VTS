package com.example.trackme_nov_edition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class PassResetActivity extends AppCompatActivity {

    private Button resend, signIn;
    private FirebaseAuth mAuth;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset);


        // change the status bar icon colors
        boolean shouldChangeStatusBarTintToDark = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (shouldChangeStatusBarTintToDark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);
            }
        }


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.__regActivityToolbar);

        // back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton();     // this function will handle the event of backbutton press on the toolbar
            }
        });


        resend = findViewById(R.id.resendBtn);
        signIn = findViewById(R.id.signInBtn);
        mAuth = FirebaseAuth.getInstance();


        // get email address from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            //The key argument here must match that used in the other activity
        }


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendResetEmail();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignInActivity();
            }
        });
    }

    private void resendResetEmail(){
        mAuth.sendPasswordResetEmail(email);
        Toast.makeText(PassResetActivity.this, "Sending a new password reset email", Toast.LENGTH_SHORT).show();
    }


    private void gotoSignInActivity(){
        Intent intent = new Intent(PassResetActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void handleBackButton(){
        startActivity(new Intent(PassResetActivity.this, SignInActivity.class));
    }
}