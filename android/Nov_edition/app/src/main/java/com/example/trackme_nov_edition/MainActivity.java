package com.example.trackme_nov_edition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
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
                Toast.makeText(MainActivity.this, "Signing In", Toast.LENGTH_SHORT).show();
            }
        });


        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Getting Started", Toast.LENGTH_SHORT).show();
            }
        });
    }
}