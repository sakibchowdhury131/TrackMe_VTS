package com.example.trackme_nov_edition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {


    private EditText resetEmail;
    private Button resetBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


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


        resetEmail = findViewById(R.id.resetyourEmail);
        resetBtn = findViewById(R.id.sendResetEmailBtn);
        mAuth = FirebaseAuth.getInstance();


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
        Toast.makeText(ForgotPasswordActivity.this, "Sending Reset Link", Toast.LENGTH_SHORT).show();
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


        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(ForgotPasswordActivity.this, PassResetActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {

                }
            }
        });
    }


    private void handleBackButton(){
        startActivity(new Intent(ForgotPasswordActivity.this, SignInActivity.class));
    }
}