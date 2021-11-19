package com.example.trackme_nov_edition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Animation topAnimation, bottomAnimation;
    TextView tv1, tv2 ;
    CardView cardView1, cardView2;
    Button signInBtn, getStartedBtn;

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
}