package com.example.trackme_nov_edition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText firstName, lastName, email, password, recheckPassword;
    private Button verifyEmail;
    private ProgressBar progressBar;

    // EditText firstName = findViewById(R.id.FirstName);
    // EditText lastName = findViewById(R.id.LastName);
    // EditText email = findViewById(R.id.signUpEmail);
    // EditText password = findViewById(R.id.userdefinedpass);
    // EditText recheckPassword = findViewById(R.id.userdefinedpassrecheck);
    // Button verifyEmail = findViewById(R.id.verifyEmail);
    // private ProgressBar progressBar = findViewById(R.id.registerprogress);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        mAuth = FirebaseAuth.getInstance();;
        firstName = findViewById(R.id.FirstName);
        lastName = findViewById(R.id.LastName);
        email = findViewById(R.id.signUpEmail);
        password = findViewById(R.id.userdefinedpass);
        recheckPassword = findViewById(R.id.userdefinedpassrecheck);
        verifyEmail = findViewById(R.id.verifyEmail);
        progressBar = findViewById(R.id.registrationProgress);
        // ...
        // Initialize Firebase Auth
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.__regActivityToolbar);





        // back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton();     // this function will handle the event of backbutton press on the toolbar
            }
        });


        // verify email button event handle
        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // when the login button is pressed, go to the register method
                register();
            }


            //register method
            private void register() {
                String _fname = firstName.getText().toString();
                String _lname = lastName.getText().toString();
                String _email = email.getText().toString();
                String _password = password.getText().toString();
                String _recheckPassword = recheckPassword.getText().toString();



                if (_fname.isEmpty()){
                    firstName.setError("Enter your first name");
                    firstName.requestFocus();
                    return;
                }



                if (_lname.isEmpty()){
                    lastName.setError("Enter your last name");
                    lastName.requestFocus();
                    return;
                }


                if (_email.isEmpty()){
                    email.setError("Enter your email address");
                    email.requestFocus();
                    return;
                }


                if (_password.isEmpty()){
                    password.setError("Enter your password");
                    password.requestFocus();
                    return;
                }

                if (_recheckPassword.isEmpty()){
                    recheckPassword.setError("Enter your first name");
                    recheckPassword.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()){    // if email format is not recognized
                    email.setError("Enter a Valid Email Address");
                    email.requestFocus();
                    return;
                }


                if (_password.length()<6 | _password.length()>32){
                    password.setError("Password must be at least 6 characters long");
                    password.requestFocus();
                    return;
                }



                if (!_password.equals(_recheckPassword)){
                    recheckPassword.setError("Password doesn't match");
                    recheckPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }


    private void handleBackButton() {           // go to main activity when backbutton in the toolbar is pressed
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
    }
}