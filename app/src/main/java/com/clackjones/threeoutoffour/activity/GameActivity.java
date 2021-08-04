package com.clackjones.threeoutoffour.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PersistableBundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.clackjones.threeoutoffour.R;
import com.clackjones.threeoutoffour.dialog.HintDialogFragment;
import com.clackjones.threeoutoffour.dialog.ResetGameDialogFragment;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourChoice;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGameLocator;

import com.clackjones.threeoutoffour.score.OfflineCoinScoreKeeper;
import com.clackjones.threeoutoffour.score.OfflineCoinScoreKeeperProvider;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static com.clackjones.threeoutoffour.model.ThreeOutOfFourGame.CHOICE_MADE_EVENT;
import static com.clackjones.threeoutoffour.model.ThreeOutOfFourGame.HINT_LETTER_REVEALED_EVENT;
import static com.clackjones.threeoutoffour.model.ThreeOutOfFourGame.INCORRECT_PROPOSED_ANSWER_EVENT;
import static com.clackjones.threeoutoffour.model.ThreeOutOfFourGame.RESET_GAME_EVENT;
import static com.clackjones.threeoutoffour.model.ThreeOutOfFourGame.WON_ROUND_EVENT;

public class GameActivity extends AppCompatActivity implements PropertyChangeListener {
    private static final float OPAQUE = 1f;
    private static final float TRANSPARENT = 0f;

    private ThreeOutOfFourGame threeOutOfFourGame;
    private OfflineCoinScoreKeeper offlineCoinScoreKeeper;
    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        offlineCoinScoreKeeper = OfflineCoinScoreKeeperProvider.getInstance().loadOrCreate(this.getApplicationContext());
        this.threeOutOfFourGame = ThreeOutOfFourGameLocator.getInstance(offlineCoinScoreKeeper, this.getApplicationContext()).threeOutOfFourGame();
        threeOutOfFourGame.initialize();

        if (threeOutOfFourGame.isAwaitingNextRound()) {
            visitComeBackLaterScreen();
        }

        threeOutOfFourGame.clearPropertyChangeListeners();
        threeOutOfFourGame.addPropertyChangeListener(this);


        threeOutOfFourGame.addPropertyChangeListener(offlineCoinScoreKeeper);
        offlineCoinScoreKeeper.addPropertyChangeListener(this);

        populateUI();
        setSupportActionBar(myToolbar);


        rewardedAd = createAndLoadRewardedAd();

        // setup clear button
        TextView proposedAnswerTV = (TextView) findViewById(R.id.clearText);
        proposedAnswerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threeOutOfFourGame.clearChoices();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.reset_action:
            ResetGameDialogFragment fragment = new ResetGameDialogFragment();
            fragment.setGame(threeOutOfFourGame);
            fragment.show(getSupportFragmentManager(), "reset");
            return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        threeOutOfFourGame.saveGame();
        OfflineCoinScoreKeeperProvider.getInstance().saveCoinScoreKeeper(this.getApplicationContext(),
                this.offlineCoinScoreKeeper);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void populateUI() {
        updateLettersRemainingText();
        updateImages();
        updateGrid();
        updateProposedAnswer();
        updateRoundNumberAndCoins();
    }

    private void updateRoundNumberAndCoins() {
        int roundNumber = this.threeOutOfFourGame.getCurrentRoundNumber();
        String formattedRoundNumber = String.format("Round %2d", roundNumber);

        int coinScore = this.offlineCoinScoreKeeper.getCoinScore();
        String formattedCoinScore = String.format("Coins: %2d", coinScore);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle(formattedRoundNumber + " | " + formattedCoinScore);
    }

    private void updateLettersRemainingText() {
        int lettersRemaining = this.threeOutOfFourGame.getLettersRemaining();
        String formattedLettersRemaining = String.format("Letters (%d)", lettersRemaining);

        TextView lettersRemainingTV = (TextView) findViewById(R.id.textTitle);
        lettersRemainingTV.setText(formattedLettersRemaining);
    }

    private void updateImages() {
        //get all ImageViews
        ImageView topLeftImage = (ImageView) findViewById(R.id.topLeftImage);
        ImageView topRightImage = (ImageView) findViewById(R.id.topRightImage);
        ImageView bottomLeftImage = (ImageView) findViewById(R.id.bottomLeftImage);
        ImageView bottomRightImage = (ImageView) findViewById(R.id.bottomRightImage);

        //for each ImageView, set to each of 4
        topLeftImage.setImageBitmap(decodedStringToBitmap(this.threeOutOfFourGame.getCurrTopLeftImage()));
        topRightImage.setImageBitmap(decodedStringToBitmap(this.threeOutOfFourGame.getCurrTopRightImage()));
        bottomLeftImage.setImageBitmap(decodedStringToBitmap(this.threeOutOfFourGame.getCurrBottomLeftImage()));
        bottomRightImage.setImageBitmap(decodedStringToBitmap(this.threeOutOfFourGame.getCurrBottomRightImage()));
    }

    private Bitmap decodedStringToBitmap(byte[] decodedString) {
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void updateGrid() {
        List<ThreeOutOfFourChoice> choices = this.threeOutOfFourGame.getChoices();
        List<View> choicesGrid = Arrays.asList(new View[]{
                findViewById(R.id.choice00),
                findViewById(R.id.choice01),
                findViewById(R.id.choice02),
                findViewById(R.id.choice03),
                findViewById(R.id.choice04),
                findViewById(R.id.choice05),
                findViewById(R.id.choice06),
                findViewById(R.id.choice07),
                findViewById(R.id.choice08),
                findViewById(R.id.choice09)
        });


        for (int choiceIdx = 0; choiceIdx < choices.size(); ++choiceIdx) {
            ThreeOutOfFourChoice choice = choices.get(choiceIdx);
            TextView choiceNBtn = (TextView) choicesGrid.get(choiceIdx);

            setUpChoiceButton(choiceNBtn, choice);
        }
    }

    private void setUpChoiceButton(TextView choiceNBtn, final ThreeOutOfFourChoice choice) {
        choiceNBtn.setText(choice.getValue());
        boolean isVisible = !choice.getIsAlreadySelected();
        if (isVisible) {
            choiceNBtn.animate().alpha(OPAQUE).setDuration(300);
            choiceNBtn.setEnabled(true);
        } else {
            choiceNBtn.animate().alpha(TRANSPARENT).setDuration(300);
            choiceNBtn.setEnabled(false);
        }

        choiceNBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threeOutOfFourGame.makeChoice(choice);
            }
        });
    }

    private void updateProposedAnswer() {
        String proposedAnswer = this.threeOutOfFourGame.getProposedAnswer();

        setAnswer(proposedAnswer);
        // TODO also need to replace clear button
    }


    private void setAnswer(String proposedAnswer) {
        List<TextView> ansGrid = Arrays.asList(new TextView[]{
                findViewById(R.id.ans00),
                findViewById(R.id.ans01),
                findViewById(R.id.ans02),
                findViewById(R.id.ans03),
                findViewById(R.id.ans04),
                findViewById(R.id.ans05),
                findViewById(R.id.ans06),
                findViewById(R.id.ans07),
                findViewById(R.id.ans08),
                findViewById(R.id.ans09)
        });


        char[] chars = proposedAnswer.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            String currChar = String.valueOf(chars[i]);
            ansGrid.get(i).setText(currChar);
            ansGrid.get(i).animate().alpha(OPAQUE).setDuration(300);
        }

        // clear out all other letters
        for (int i = chars.length; i < 10; ++i) {
            ansGrid.get(i).setText("");
            ansGrid.get(i).animate().alpha(TRANSPARENT).setDuration(300);

        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String evtName = evt.getPropertyName();
        if (evtName.equals(WON_ROUND_EVENT)) {
            this.populateUI();
            Snackbar.make(findViewById(R.id.gameActivity), R.string.correct, Snackbar.LENGTH_LONG)
                    .setAnchorView(findViewById(R.id.choice00))
                    .setDuration(2000)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onShown(Snackbar sb) {
                            super.onShown(sb);
                            threeOutOfFourGame.incrementRound();
                            GameActivity.this.visitWinScreen();
                        }
                    })
                    .show();
        } else if (evtName.equals(RESET_GAME_EVENT)) {
            this.visitHomeScreen();
        }  else if (evtName.equals(ThreeOutOfFourGame.HINT_LETTER_REMOVED_EVENT)) {
            populateUI();
        }  else if (evtName.equals(HINT_LETTER_REVEALED_EVENT)) {
            populateUI();
        }  else if (evtName.equals(CHOICE_MADE_EVENT)) {
            populateUI();
        } else if (evtName.equals(INCORRECT_PROPOSED_ANSWER_EVENT)) {
            populateUI();
            Snackbar.make(findViewById(R.id.gameActivity), R.string.try_again, Snackbar.LENGTH_SHORT)
                    .setAnchorView(findViewById(R.id.choice00))
                    .show();
        }
    }

    private void visitHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void visitWinScreen() {
        System.out.println("GameActivity::visitWinScreen()");
        this.threeOutOfFourGame.addPropertyChangeListener(this);
        Intent intent = new Intent(this, WinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void visitComeBackLaterScreen() {
        Intent intent = new Intent(this, ComeBackLaterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void showHintsMenu(View view) {
        HintDialogFragment hintDialogFragment = new HintDialogFragment();
        hintDialogFragment.setGame(this.threeOutOfFourGame);
        hintDialogFragment.show(getSupportFragmentManager(), "HintDialogFragment");
    }

    public void showAd(View view) {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                }

                @Override
                public void onRewardedAdClosed() {
                    rewardedAd = createAndLoadRewardedAd();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    offlineCoinScoreKeeper.addCoins(ThreeOutOfFourGame.WATCHED_AD_COINS);
                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                }
            };
            rewardedAd.show(this, adCallback);
        } else {
            System.out.println("The rewarded ad wasn't loaded yet.");
        }
    }

    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917"); // TODO: this is just a test ad - replace with actual;
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }
}