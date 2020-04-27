package com.clackjones.threeoutoffour.model;

import android.content.Context;

import com.clackjones.threeoutoffour.score.CoinScoreKeeper;
import com.clackjones.threeoutoffour.state.GameStateProvider;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventFiringThreeOutOfFourGame implements ThreeOutOfFourGame {
    final GameStateProvider gameStateProvider;
    final CoinScoreKeeper coinScoreKeeper;

    private final RoundProvider roundProvider;
    private GameState gameState;
    private PropertyChangeSupport propertyChangeSupport;
    private boolean isInitialized;

    private Context context;

    public EventFiringThreeOutOfFourGame(RoundProvider roundProvider, GameStateProvider gameStateProvider, CoinScoreKeeper coinScoreKeeper, Context context) {
        this.gameStateProvider = gameStateProvider;
        this.roundProvider = roundProvider;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.isInitialized = false;
        this.context = context;
        this.coinScoreKeeper = coinScoreKeeper;
    }

    @Override
    public void initialize() {
        if (!this.isInitialized) {
            this.gameState = gameStateProvider.loadGameStateOrCreateNew(this.context);
            boolean isNewGame = this.gameState.getCurrentRoundNumber() == 0;

            if (isNewGame) {
                incrementRound();
                this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, 0, 1);
            }
            this.isInitialized = true;
        }
    }

    @Override
    public List<ThreeOutOfFourChoice> getChoices() {
        return this.gameState.getCurrentChoices();
    }

    @Override
    public int getLettersRemaining() {
        return this.gameState.getCurrentAnswer().length() - this.gameState.getCurrentProposedAnswer().length();
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
                this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, this.gameState.getCurrentRoundNumber() - 1, this.gameState.getCurrentRoundNumber());
            }
        }

        saveGame();
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
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        boolean isListenerAlreadyRegistered = Arrays.asList(this.propertyChangeSupport.getPropertyChangeListeners()).contains(propertyChangeListener);
        if (!isListenerAlreadyRegistered) {
            this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
        }
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
        boolean isListenerAlreadyRegistered = Arrays.asList(this.propertyChangeSupport.getPropertyChangeListeners(propertyName)).contains(propertyChangeListener);
        if (!isListenerAlreadyRegistered) {
            this.propertyChangeSupport.addPropertyChangeListener(propertyName, propertyChangeListener);
        }
    }

    @Override
    public void saveGame() {
        this.gameStateProvider.saveGameState(this.gameState, this.context);
    }

    @Override
    public void restartGame() {
        this.gameStateProvider.deleteGame(context);
        this.gameState = new GameState();
        incrementRound();
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.RESET_GAME_EVENT, 0, 1);
    }

    @Override
    public void performRemoveALetterHint() throws InsufficientCoinScoreException {
        throwExceptionIfInsufficientFunds(ThreeOutOfFourGame.HINT_LETTER_REMOVED_COINS_REQUIRED);

        for (ThreeOutOfFourChoice choice : getChoices()) {
            boolean isLetterInAnswer = this.gameState.getCurrentAnswer().contains(choice.getValue());
            boolean isValidCandidateToRemove = !choice.getIsAlreadySelected() && !isLetterInAnswer;

            if (isValidCandidateToRemove) {
                choice.setIsAlreadySelected(true);
                this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.HINT_LETTER_REMOVED_EVENT, 0, 1);
                return;
            }
        }
    }

    @Override
    public void performRevealALetterHint() throws InsufficientCoinScoreException {
        throwExceptionIfInsufficientFunds(ThreeOutOfFourGame.HINT_LETTER_REVEALED_COINS_REQUIRED);

        clearIncorrectLetters();

        int indexOfNextLetterToReveal = this.gameState.getCurrentProposedAnswer().length();
        Character nextCorrectChar = this.gameState.getCurrentAnswer().toCharArray()[indexOfNextLetterToReveal];
        makeChoiceFirstMatching(nextCorrectChar.toString());
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.HINT_LETTER_REVEALED_EVENT, 0, 1);
    }

    private void makeChoiceFirstMatching(String character) {
        for (ThreeOutOfFourChoice choice: this.getChoices()) {
            boolean isCorrectLetter = choice.getValue().equals(character);
            if (!choice.getIsAlreadySelected() && isCorrectLetter) {
                makeChoice(choice);
                return;
            }
        }
    }

    private void clearIncorrectLetters() {
        char[] proposedChars = this.gameState.getCurrentProposedAnswer().toCharArray();
        char[] currentAnswerChars = this.gameState.getCurrentAnswer().toCharArray();

        int indexStartCorrection = proposedChars.length;
        for (int i = 0; i < proposedChars.length; ++i) {
            String proposedChar = String.valueOf(proposedChars[i]);
            String currentAnswerChar = String.valueOf(currentAnswerChars[i]);
            if (!proposedChar.equals(currentAnswerChar)) {
                indexStartCorrection = i;
                break;
            }
        }

        // undo incorrect choices
        for (int i = indexStartCorrection; i < proposedChars.length; ++i) {
            String proposedChar = String.valueOf(proposedChars[i]);
            undoChoice(proposedChar);
        }

        String proposedAnswerWithIncorrectRemoved = this.gameState.getCurrentProposedAnswer().substring(0, indexStartCorrection);
        this.gameState.setCurrentProposedAnswer(proposedAnswerWithIncorrectRemoved);
    }

    private void undoChoice(String proposedChar) {
        for (ThreeOutOfFourChoice choice: this.getChoices()) {
            boolean lettersMatch = choice.getValue().equals(proposedChar);
            if (choice.getIsAlreadySelected() && lettersMatch) {
                choice.reset();
                return;
            }
        }
    }

    @Override
    public void performSkipRoundHint() throws InsufficientCoinScoreException {
        throwExceptionIfInsufficientFunds(ThreeOutOfFourGame.HINT_SKIP_ROUND_COINS_REQUIRED);

        incrementRound();
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT, 0, 1);
        this.propertyChangeSupport.firePropertyChange(ThreeOutOfFourGame.HINT_SKIP_ROUND_EVENT, 0, 1);
    }

    private void throwExceptionIfInsufficientFunds(int requiredCoinCount) throws InsufficientCoinScoreException {
        if (this.coinScoreKeeper.getCoinScore() < requiredCoinCount) {
            throw new InsufficientCoinScoreException();
        }
    }

}
