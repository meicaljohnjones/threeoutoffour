package com.clackjones.threeoutoffour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clackjones.threeoutoffour.model.ThreeOutOfFourChoice;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;
import com.clackjones.threeoutoffour.model.ThreeOutOfFourGameLocator;

import com.google.android.flexbox.FlexboxLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class GameActivity extends AppCompatActivity implements PropertyChangeListener {
    private static final float OPAQUE = 1f;
    private static final float TRANSPARENT = 0f;

    private ThreeOutOfFourGame threeOutOfFourGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.threeOutOfFourGame = ThreeOutOfFourGameLocator.getInstance().threeOutOfFourGame();
        threeOutOfFourGame.initialize();
        threeOutOfFourGame.addPropertyChangeListener(this);

        populateUI();
    }

    private void populateUI() {
        updateLettersRemainingText();
        updateImages();
        updateGrid();
        updateProposedAnswer();
        updateRoundNumber();
    }

    private void updateRoundNumber() {
        int roundNumber = this.threeOutOfFourGame.getCurrentRoundNumber();
        String formattedRoundNumber = String.format("Round %2d", roundNumber);

        TextView roundNumberTV = (TextView) findViewById(R.id.textRound);
        roundNumberTV.setText(formattedRoundNumber);
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
        if (!evt.getPropertyName().equals(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT)) {
            populateUI();
        } else {
            this.visitWinScreen();
        }
    }

    private void visitWinScreen() {
        Intent intent = new Intent(this, WinActivity.class);
        startActivity(intent);
    }

}