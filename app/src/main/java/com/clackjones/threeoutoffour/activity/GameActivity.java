package com.clackjones.threeoutoffour.activity;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clackjones.threeoutoffour.R;
import com.clackjones.threeoutoffour.dialog.ResetGameDialogFragment;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourChoice;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGameLocator;

import com.clackjones.threeoutoffour.score.CoinScoreKeeper;
import com.clackjones.threeoutoffour.score.OfflineCoinScoreKeeper;
import com.clackjones.threeoutoffour.score.OfflineCoinScoreKeeperProvider;
import com.google.android.flexbox.FlexboxLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class GameActivity extends AppCompatActivity implements PropertyChangeListener {
    private static final float OPAQUE = 1f;
    private static final float TRANSPARENT = 0f;

    private ThreeOutOfFourGame threeOutOfFourGame;
    private OfflineCoinScoreKeeper offlineCoinScoreKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        this.threeOutOfFourGame = ThreeOutOfFourGameLocator.getInstance(this.getApplicationContext()).threeOutOfFourGame();
        threeOutOfFourGame.initialize();
        threeOutOfFourGame.addPropertyChangeListener(this);

        offlineCoinScoreKeeper = OfflineCoinScoreKeeperProvider.getInstance().loadOrCreate(this.getApplicationContext());

        threeOutOfFourGame.addPropertyChangeListener(offlineCoinScoreKeeper);
        offlineCoinScoreKeeper.addPropertyChangeListener(this);

        populateUI();
        setSupportActionBar(myToolbar);
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
        topLeftImage.setBackgroundResource(this.threeOutOfFourGame.getCurrTopLeftImage());
        topRightImage.setBackgroundResource(this.threeOutOfFourGame.getCurrTopRightImage());
        bottomLeftImage.setBackgroundResource(this.threeOutOfFourGame.getCurrBottomLeftImage());
        bottomRightImage.setBackgroundResource(this.threeOutOfFourGame.getCurrBottomRightImage());
    }

    private void updateGrid() {
        List<ThreeOutOfFourChoice> choices = this.threeOutOfFourGame.getChoices();
        FlexboxLayout choicesGrid = findViewById(R.id.choiceGrid);

        for (int choiceIdx = 0; choiceIdx < choices.size(); ++choiceIdx) {
            ThreeOutOfFourChoice choice = choices.get(choiceIdx);
            TextView choiceNBtn = (TextView) choicesGrid.getFlexItemAt(choiceIdx);

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
        TextView proposedAnswerTV = (TextView) findViewById(R.id.editText);

        proposedAnswerTV.setText(proposedAnswer);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String evtName = evt.getPropertyName();

        if (evtName.equals(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT)) {
            this.visitWinScreen();

        } else if (evtName.equals(ThreeOutOfFourGame.RESET_GAME_EVENT)) {
            this.visitHomeScreen();
        }  else if (evtName.equals(CoinScoreKeeper.COIN_SCORE_CHANGED_EVENT)) {
            OfflineCoinScoreKeeperProvider.getInstance().saveCoinScoreKeeper(this.getApplicationContext(),
                    this.offlineCoinScoreKeeper);
        }
        else {
            populateUI();
        }
    }

    private void visitHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void visitWinScreen() {
        Intent intent = new Intent(this, WinActivity.class);
        startActivity(intent);
    }

}