package com.example.trackme_nov_edition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPasswordActivity extends AppCompatActivity {


    private EditText resetEmail;
    private Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);



        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.__regActivityToolbar);

        // back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton();     // this function will handle the event of backbutton press on the toolbar
            }
        });


        resetEmail = findViewById(R.id.resetyourEmail);
        resetBtn = findViewById(R.id.sendResetEmailBtn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                forgotPassProcedure();
            }
        });



    }

    private void forgotPassProcedure(){
        String email = resetEmail.getText().toString();

        if (email.isEmpty()){
            resetEmail.setError("Enter your email address");
            resetEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){    // if email format is not recognized
            resetEmail.setError("Enter a Valid Email Address");
            resetEmail.requestFocus();
            return;
        }
    }


    private void handleBackButton(){
        startActivity(new Intent(ForgotPasswordActivity.this, SignInActivity.class));
    }
}