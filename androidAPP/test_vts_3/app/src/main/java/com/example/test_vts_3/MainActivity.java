package com.example.test_vts_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        signInBtn = findViewById(R.id.startSignInID);
        getStartedBtn = findViewById(R.id.getStartedID);
        tv1 = findViewById(R.id.animationTV1);
        tv2 = findViewById(R.id.animationTV2);
        cardView1 = findViewById(R.id.animationcard1);
        cardView2 = findViewById(R.id.animationcard2);
        tv1.setAnimation(topAnimation);
        tv2.setAnimation(topAnimation);
        cardView1.setAnimation(bottomAnimation);
        cardView2.setAnimation(bottomAnimation);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Signing In", Toast.LENGTH_SHORT).show();
            }
        });


        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PhoneRegActivity.class));
            }
        });

    }
}