package com.clackjones.threeoutoffour.model;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface ThreeOutOfFourGame {
    final String INCORRECT_PROPOSED_ANSWER_EVENT = "INCORRECT_PROPOSED_ANSWER_EVENT";
    final String CHOICE_MADE_EVENT = "CHOICE_MADE_EVENT";
    final String PROPOSED_ANSWER_CHANGED_EVENT = "PROPOSED_ANSWER_MADE_EVENT";
    final String ROUND_NUMBER_INCREMENTED_EVENT = "ROUND_NUMBER_INCREMENTED_EVENT";
    final String RESET_GAME_EVENT = "RESET_GAME_EVENT";
    final String LETTERS_REMAINING_DECREMENTED_EVENT = "LETTERS_REMAINING_DECREMENTED_EVENT";
    final String HINT_LETTER_REMOVED_EVENT = "HINT_LETTER_REMOVED_EVENT";


    /**
     * Required setup
     */
    void initialize();

    /**
     * @return an ordered list of all choices and their current state (active/inactive)
     */
    List<ThreeOutOfFourChoice> getChoices();

    /**
     * @return letters remaining for proposed answer
     */
    int getLettersRemaining();

    /**
     * @return the current round number
     */
    int getCurrentRoundNumber();

    /**
     * @return the state of the current proposed answer
     */
    String getProposedAnswer();

    /**
     * @return id of the current top left image
     */
    public int getCurrTopLeftImage();

    /**
     * @return id of the current top right image
     */
    public int getCurrTopRightImage();

    /**
     * @return id of the current bottom left image
     */
    public int getCurrBottomLeftImage();

    /**
     * @return id of the current bottom right image
     */
    public int getCurrBottomRightImage();

    /**
     * The letter chosen to guess the words matching all
     * three out of four images. Sets this choice as selected.
     *
     * When a choice is made, we also figure out whether the game was won or lost.
     * If it was won, we increment the round.
     * If it was lost, we load new random choices and reset the current proposed answer
     *
     * @param choice represents letter chosen
     */
    void makeChoice(ThreeOutOfFourChoice choice);

    /**
     * same as makeChoice but uses index instead
     * @param choiceIndex
     */
    void makeChoice(int choiceIndex);


    /**
     * Fire events to  all PropertyChangeListeners when choice made, round won etc.
     * @param propertyChangeListener
     */
    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    /**
     * Fire specific events to  PropertyChangeListeners when choice made,
     * if the current round was won, if the current choice was false
     * @param propertyName
     * @param propertyChangeListener
     */
    void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

    void saveGame();

    void restartGame();

    void performRemoveALetterHint();
}
