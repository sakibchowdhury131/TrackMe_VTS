package com.example.trackme_nov_edition;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
            // when the login button is pressed, go to the register method
                // and hide the keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

                mAuth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            mUser = mAuth.getCurrentUser();
                            String userID = mUser.getUid().toString();
                            DatabaseReference myRef = database.getReference("userDB/"+userID);
                            myRef.child("FirstName").setValue(_fname);
                            myRef.child("LastName").setValue(_lname);
                            myRef.child("Email").setValue(_email);
                            myRef.child("Password").setValue(_password);


                            mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegistrationActivity.this, "A verification email has been sent to your email", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        sendUserToNextActivity();

                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Email verification is pending", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                        }  else {
                            Toast.makeText(RegistrationActivity.this, "Email is already in use", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    private void sendUserToNextActivity() {
                        Intent intent = new Intent(RegistrationActivity.this, VerifyActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        });
    }


    private void handleBackButton() {           // go to main activity when backbutton in the toolbar is pressed
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
    }
}