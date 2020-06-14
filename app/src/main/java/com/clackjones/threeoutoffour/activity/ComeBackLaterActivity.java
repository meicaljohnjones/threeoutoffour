package com.clackjones.threeoutoffour.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.clackjones.threeoutoffour.R;
import com.clackjones.threeoutoffour.dialog.ResetGameDialogFragment;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGameLocator;
import com.clackjones.threeoutoffour.score.OfflineCoinScoreKeeper;
import com.clackjones.threeoutoffour.score.OfflineCoinScoreKeeperProvider;

public class ComeBackLaterActivity extends AppCompatActivity {

    ImageView bigboss;
    Animation smalltobig;
    private OfflineCoinScoreKeeper offlineCoinScoreKeeper;
    private ThreeOutOfFourGame threeOutOfFourGame;
    private ResetGameDialogFragment resetGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comebacklater);

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        bigboss = (ImageView) findViewById(R.id.bigboss);
        bigboss.startAnimation(smalltobig);

        offlineCoinScoreKeeper = OfflineCoinScoreKeeperProvider.getInstance().loadOrCreate(this.getApplicationContext());
        this.threeOutOfFourGame = ThreeOutOfFourGameLocator.getInstance(offlineCoinScoreKeeper, this.getApplicationContext()).threeOutOfFourGame();
        threeOutOfFourGame.initialize();

    }

    public void restartGame(View view) {
        resetGameFragment = new ResetGameDialogFragment();
        resetGameFragment.setGame(threeOutOfFourGame);
        resetGameFragment.show(getSupportFragmentManager(), "reset");
    }

    public void returnToHomeScreen(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}





