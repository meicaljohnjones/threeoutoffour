package com.clackjones.threeoutoffour.model;

import java.util.ArrayList;
import java.util.List;

public class GameState implements java.io.Serializable {
    private boolean isAwaitingNextRound;
    private String currentProposedAnswer;
    private int currentRoundNumber;
    private String currentAnswer;

    //4 images
    private byte[] currTopLeftImage;
    private byte[] currTopRightImage;
    private byte[] currBottomLeftImage;
    private byte[] currBottomRightImage;

    private List<ThreeOutOfFourChoice> currentChoices;

    public GameState() {
        isAwaitingNextRound = false;
        currentRoundNumber = 0;

        currentAnswer = "";
        currentProposedAnswer = "";

        currTopLeftImage = new byte[]{};
        currTopRightImage = new byte[]{};
        currBottomLeftImage = new byte[]{};
        currBottomRightImage = new byte[]{};

        currentChoices = new ArrayList<>();
    }

    public static GameState awaitingNewRounds(Integer currentRoundNumber) {
        GameState gameState = new GameState();
        gameState.currentRoundNumber = currentRoundNumber;
        gameState.isAwaitingNextRound = true;
        return gameState;
    }

    public boolean isAwaitingNextRound() {
        return isAwaitingNextRound;
    }

    public String getCurrentProposedAnswer() {
        return currentProposedAnswer;
    }

    public void setCurrentProposedAnswer(String currentProposedAnswer) {
        this.currentProposedAnswer = currentProposedAnswer;
    }

    public int getCurrentRoundNumber() {
        return currentRoundNumber;
    }

    public void setCurrentRoundNumber(int currentRoundNumber) {
        this.currentRoundNumber = currentRoundNumber;
    }

    public String getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    public byte[] getCurrTopLeftImage() {
        return currTopLeftImage;
    }

    public void setCurrTopLeftImage(byte[] currTopLeftImage) {
        this.currTopLeftImage = currTopLeftImage;
    }

    public byte[] getCurrTopRightImage() {
        return currTopRightImage;
    }

    public void setCurrTopRightImage(byte[] currTopRightImage) {
        this.currTopRightImage = currTopRightImage;
    }

    public byte[] getCurrBottomLeftImage() {
        return currBottomLeftImage;
    }

    public void setCurrBottomLeftImage(byte[] currBottomLeftImage) {
        this.currBottomLeftImage = currBottomLeftImage;
    }

    public byte[] getCurrBottomRightImage() {
        return currBottomRightImage;
    }

    public void setCurrBottomRightImage(byte[] currBottomRightImage) {
        this.currBottomRightImage = currBottomRightImage;
    }

    public List<ThreeOutOfFourChoice> getCurrentChoices() {
        return currentChoices;
    }

    public void setCurrentChoices(List<ThreeOutOfFourChoice> currentChoices) {
        this.currentChoices = currentChoices;
    }
}
