package com.example.trackme_nov_edition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView forgotPassword, incorrectmsg;
    private Button signInBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.__regActivityToolbar);

        // back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton();     // this function will handle the event of backbutton press on the toolbar
            }
        });

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signInEmail);
        password = findViewById(R.id.signInPass);
        forgotPassword = findViewById(R.id.forgotpass);
        incorrectmsg = findViewById(R.id.incorrectmsg);
        signInBtn = findViewById(R.id.signinbutton);
        progressBar = findViewById(R.id.signInProgress);


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                login();
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));

            }
        });




    }

    void handleBackButton(){
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
    }


    void login(){
        String _email = email.getText().toString();
        String _password = password.getText().toString();

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


        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    if (mAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(SignInActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                        sendUserToNextActivity();
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        incorrectmsg.setTextColor(Color.parseColor("#FF0000"));
                        incorrectmsg.setText("Please complete the email verification");
                        progressBar.setVisibility(View.INVISIBLE);
                    }


                } else {
                    incorrectmsg.setTextColor(Color.parseColor("#FF0000"));
                    incorrectmsg.setText("Incorrect Email or Password");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    void sendUserToNextActivity(){
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


}