package com.example.trackme_nov_edition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Animation topAnimation, bottomAnimation;
    TextView tv1, tv2 ;
    CardView cardView1, cardView2;
    Button signInBtn, getStartedBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            updateUI();
        } else {
            // No user is signed in
            Log.d("firebaseLog", "no user found");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        signInBtn = findViewById(R.id.__signInMainActivity);
        getStartedBtn = findViewById(R.id.__getstartedBtn);
        tv1 = findViewById(R.id.__TopTextMainActivity);
        tv2 = findViewById(R.id.__BottomTextMainActivity);
        cardView1 = findViewById(R.id.__getstartedCardView);
        cardView2 = findViewById(R.id.__signInCardView);
        tv1.setAnimation(topAnimation);
        tv2.setAnimation(topAnimation);
        cardView1.setAnimation(bottomAnimation);
        cardView2.setAnimation(bottomAnimation);



        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "Signing In", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        });


        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "Getting Started", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });
    }


    private void updateUI(){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}