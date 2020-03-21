package com.clackjones.threeoutoffour.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class InMemoryThreeOutOfFourGame implements ThreeOutOfFourGame {
    private final RoundProvider roundProvider;
    private final GameState gameState; // Todo: Replace with action to get from GameStatePersister (load/save)
    private PropertyChangeSupport propertyChangeSupport;
    private boolean isInitialized;

    public InMemoryThreeOutOfFourGame(RoundProvider roundProvider) {
        this.gameState = new GameState(); // Todo: Replace with action to get from GameStatePersister (load/save)

        this.roundProvider = roundProvider;
        this.isInitialized = false;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
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
        return this.gameState.getCurrentChoices();
    }

    @Override
    public int getLettersRemaining() {
        return this.gameState.getCurrentAnswer().length() - this.countChoicesMade();
    }

    private int countChoicesMade() {
        int choicesMade = 0;
        for (ThreeOutOfFourChoice choice: this.gameState.getCurrentChoices()) {
            if (choice.getIsAlreadySelected()) choicesMade += 1;
        }

        return choicesMade;
    }

    @Override
    public int getCurrentRoundNumber() {
        return this.gameState.getCurrentRoundNumber();
    }

    @Override
    public String getProposedAnswer() {
        return this.gameState.getCurrentProposedAnswer();
    }

    @Override
    public int getCurrTopLeftImage() {
        return this.gameState.getCurrTopLeftImage();
    }

    @Override
    public int getCurrTopRightImage() {
        return this.gameState.getCurrTopRightImage();
    }

    @Override
    public int getCurrBottomLeftImage() {
        return this.gameState.getCurrBottomLeftImage();
    }

    @Override
    public int getCurrBottomRightImage() {
        return this.gameState.getCurrBottomRightImage();
    }

    @Override
    public void makeChoice(ThreeOutOfFourChoice choice) {
        if (choice.getIsAlreadySelected()) {
            return;
        }

        String oldProposedAnswer = this.gameState.getCurrentProposedAnswer();
        int oldLettersRemainingVal = this.getLettersRemaining();
        int choiceIndex = this.gameState.getCurrentChoices().indexOf(choice);
        ThreeOutOfFourChoice choiceOldValue = choice.clone();

        choice.select();
        this.gameState.setCurrentProposedAnswer(oldProposedAnswer + choice.getValue());
        int newLettersRemainingVal = this.getLettersRemaining();

        this.propertyChangeSupport.fireIndexedPropertyChange(ThreeOutOfFourGame.CHOICE_MADE_EVENT, choiceIndex, choiceOldValue, choice);
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.LETTERS_REMAINING_DECREMENTED_EVENT, oldLettersRemainingVal, newLettersRemainingVal);
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.PROPOSED_ANSWER_CHANGED_EVENT, oldProposedAnswer, this.gameState.getCurrentProposedAnswer());

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
        ThreeOutOfFourChoice choice = this.gameState.getCurrentChoices().get(choiceIdx);

        this.makeChoice(choice);
    }

    private boolean isProposedAnswerCorrect() {
        return this.gameState.getCurrentProposedAnswer().equals(this.gameState.getCurrentAnswer());
    }

    private void resetChoices() {
        for (ThreeOutOfFourChoice choice : this.gameState.getCurrentChoices()) {
            choice.reset();
        }
    }

    private void resetProposedAnswer() {
        this.gameState.setCurrentProposedAnswer("");
    }

    private void incrementRound() {
        Round nextRound = roundProvider.getNextRound(this.getCurrentRoundNumber());

        String[] choiceLetters = nextRound.getRandomLetters();
        List<ThreeOutOfFourChoice> choices = new ArrayList<>(choiceLetters.length);
        for (String letter : choiceLetters) {
            choices.add(new ThreeOutOfFourChoice(letter));
        }
        this.gameState.setCurrentChoices(choices);

        resetProposedAnswer();
        this.gameState.setCurrentAnswer(nextRound.getAnswer());

        this.gameState.setCurrBottomLeftImage(nextRound.getBottomLeftImage());
        this.gameState.setCurrBottomRightImage(nextRound.getBottomRightImage());
        this.gameState.setCurrTopLeftImage(nextRound.getTopLeftImage());
        this.gameState.setCurrTopRightImage(nextRound.getTopRightImage());

        int oldRoundNumber = this.gameState.getCurrentRoundNumber();
        this.gameState.setCurrentRoundNumber(nextRound.getRoundNumber());
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, oldRoundNumber, this.gameState.getCurrentRoundNumber());
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
