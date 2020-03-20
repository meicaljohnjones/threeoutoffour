package com.clackjones.threeoutoffour.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class InMemoryThreeOutOfFourGame implements ThreeOutOfFourGame {
    private final RoundProvider roundProvider;
    private String currentProposedAnswer;
    String answer;
    private List<ThreeOutOfFourChoice> currentChoices;
    private int currentRoundNumber;
    private PropertyChangeSupport propertyChangeSupport;
    private boolean isInitialized;

    //4 images
    private int currTopLeftImage;
    private int currTopRightImage;
    private int currBottomLeftImage;
    private int currBottomRightImage;

    public InMemoryThreeOutOfFourGame(RoundProvider roundProvider) {
        this.roundProvider = roundProvider;
        currentRoundNumber = 0;
        this.isInitialized = false;
        this.propertyChangeSupport = new PropertyChangeSupport(this);

        this.currTopLeftImage = -1;
        this.currTopRightImage = -1;
        this.currBottomLeftImage = -1;
        this.currBottomRightImage = -1;
    }

    @Override
    public void initialize() {
        if (!this.isInitialized) {
            incrementRound();
            this.isInitialized = true;
        }
    }

    @Override
    public List<ThreeOutOfFourChoice> getChoices() {
        return this.currentChoices;
    }

    @Override
    public int getLettersRemaining() {
        return this.answer.length() - this.countChoicesMade();
    }

    private int countChoicesMade() {
        int choicesMade = 0;
        for (ThreeOutOfFourChoice choice: this.currentChoices) {
            if (choice.isAlreadySelected()) choicesMade += 1;
        }

        return choicesMade;
    }

    @Override
    public int getCurrentRoundNumber() {
        return this.currentRoundNumber;
    }

    @Override
    public String getProposedAnswer() {
        return this.currentProposedAnswer;
    }

    @Override
    public int getCurrTopLeftImage() {
        return currTopLeftImage;
    }

    @Override
    public int getCurrTopRightImage() {
        return currTopRightImage;
    }

    @Override
    public int getCurrBottomLeftImage() {
        return currBottomLeftImage;
    }

    @Override
    public int getCurrBottomRightImage() {
        return currBottomRightImage;
    }

    @Override
    public void makeChoice(ThreeOutOfFourChoice choice) {
        if (choice.isAlreadySelected()) {
            return;
        }

        String oldProposedAnswer = this.currentProposedAnswer;
        int oldLettersRemainingVal = this.getLettersRemaining();
        int choiceIndex = currentChoices.indexOf(choice);
        ThreeOutOfFourChoice choiceOldValue = choice.clone();

        choice.select();
        this.currentProposedAnswer = this.currentProposedAnswer + choice.getValue();
        int newLettersRemainingVal = this.getLettersRemaining();

        this.propertyChangeSupport.fireIndexedPropertyChange(ThreeOutOfFourGame.CHOICE_MADE_EVENT, choiceIndex, choiceOldValue, choice);
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.LETTERS_REMAINING_DECREMENTED_EVENT, oldLettersRemainingVal, newLettersRemainingVal);
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.PROPOSED_ANSWER_CHANGED_EVENT, oldProposedAnswer, currentProposedAnswer);

        boolean isEnoughLettersToMakeGuess = this.getLettersRemaining() == 0;
        if (isEnoughLettersToMakeGuess) {
            if (!isProposedAnswerCorrect()) {
                resetChoices();
                resetProposedAnswer();
                this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.INCORRECT_PROPOSED_ANSWER_EVENT, 1,0);
            } else {
                incrementRound();
            }
        }
    }

    @Override
    public void makeChoice(int choiceIdx) {
        ThreeOutOfFourChoice choice = this.currentChoices.get(choiceIdx);

        this.makeChoice(choice);
    }

    private boolean isProposedAnswerCorrect() {
        return this.currentProposedAnswer.equals(this.answer);
    }

    private void resetChoices() {
        for (ThreeOutOfFourChoice choice : this.currentChoices) {
            choice.reset();
        }
    }

    private void resetProposedAnswer() {
        this.currentProposedAnswer = "";
    }

    private void incrementRound() {
        Round nextRound = roundProvider.getNextRound(this.currentRoundNumber);

        String[] choiceLetters = nextRound.getRandomLetters();
        this.currentChoices = new ArrayList<>(choiceLetters.length);
        for (String letter : choiceLetters) {
            this.currentChoices.add(new ThreeOutOfFourChoice(letter));
        }

        this.currentProposedAnswer = "";
        this.answer = nextRound.getAnswer();

        this.currBottomLeftImage = nextRound.getBottomLeftImage();
        this.currBottomRightImage = nextRound.getBottomRightImage();
        this.currTopLeftImage = nextRound.getTopLeftImage();
        this.currTopRightImage = nextRound.getTopRightImage();

        int oldRoundNumber = this.currentRoundNumber;
        this.currentRoundNumber = nextRound.getRoundNumber();
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, oldRoundNumber, this.currentRoundNumber);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyName, propertyChangeListener);
    }
}
