package com.clackjones.threeoutoffour.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.clackjones.threeoutoffour.R;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGameLocator;
import com.clackjones.threeoutoffour.score.OfflineCoinScoreKeeper;
import com.clackjones.threeoutoffour.score.OfflineCoinScoreKeeperProvider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class WinActivity extends AppCompatActivity {
    static final int SHOW_INTERSTITIAL_AD_INTERVAL = 2;

    ImageView bigboss;
    Animation smalltobig;
    private ThreeOutOfFourGame threeOutOfFourGame;
    private InterstitialAd interstitialAd;
    private IntentAdListener intentAdListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        this.intentAdListener = new IntentAdListener();

        OfflineCoinScoreKeeper offlineCoinScoreKeeper = OfflineCoinScoreKeeperProvider.getInstance().loadOrCreate(this.getApplicationContext());
        this.threeOutOfFourGame = ThreeOutOfFourGameLocator.getInstance(offlineCoinScoreKeeper, this.getApplicationContext()).threeOutOfFourGame();
        this.threeOutOfFourGame.initialize();

        if (isTimeToShowInterstitial()) {
            interstitialAd = new InterstitialAd(this);
            interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/8691691433"); // TODO: this is just a test ad - replace with actual;
            interstitialAd.loadAd(new AdRequest.Builder().build());
            interstitialAd.setAdListener(intentAdListener);
        }


        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        bigboss = (ImageView) findViewById(R.id.bigboss);
        bigboss.startAnimation(smalltobig);
    }

    public void continueWithGame(View view) {
        performIntentWithOrWithoutAd(GameActivity.class);
    }


    public void returnToHomeScreen(View view) {
        performIntentWithOrWithoutAd(MainActivity.class);
    }

    private void performIntentWithOrWithoutAd(Class destinationActivity) {
        Runnable r = () -> {
            Intent intent = new Intent(WinActivity.this, destinationActivity);
            startActivity(intent);
        };

        if (isTimeToShowInterstitial()) {
            intentAdListener.setDoOnCloseAd(r);
            interstitialAd.show();
        } else {
            r.run();
        }
    }

    private boolean isTimeToShowInterstitial() {
        return (this.threeOutOfFourGame.getCurrentRoundNumber() - 1) % SHOW_INTERSTITIAL_AD_INTERVAL == 0;
    }
}


class IntentAdListener extends AdListener {
    private Runnable doOnCloseAd;

    public void setDoOnCloseAd(Runnable doOnCloseAd) {
        this.doOnCloseAd = doOnCloseAd;
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();
        doOnCloseAd.run();
    }
}





