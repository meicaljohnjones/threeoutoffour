package com.clackjones.threeoutoffour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WinActivity extends AppCompatActivity {

    ImageView bigboss;
    Animation smalltobig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        bigboss = (ImageView) findViewById(R.id.bigboss);
        bigboss.startAnimation(smalltobig);
    }

    public void continueWithGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void returnToHomeScreen(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}





