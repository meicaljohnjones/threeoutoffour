package com.clackjones.threeoutoffour.model;

public class GameState implements java.io.Serializable {
    private String currentProposedAnswer;
    private int currentRoundNumber;
    private String currentAnswer;

    //4 images
    private int currTopLeftImage;
    private int currTopRightImage;
    private int currBottomLeftImage;
    private int currBottomRightImage;

    public GameState() {

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

    public int getCurrTopLeftImage() {
        return currTopLeftImage;
    }

    public void setCurrTopLeftImage(int currTopLeftImage) {
        this.currTopLeftImage = currTopLeftImage;
    }

    public int getCurrTopRightImage() {
        return currTopRightImage;
    }

    public void setCurrTopRightImage(int currTopRightImage) {
        this.currTopRightImage = currTopRightImage;
    }

    public int getCurrBottomLeftImage() {
        return currBottomLeftImage;
    }

    public void setCurrBottomLeftImage(int currBottomLeftImage) {
        this.currBottomLeftImage = currBottomLeftImage;
    }

    public int getCurrBottomRightImage() {
        return currBottomRightImage;
    }

    public void setCurrBottomRightImage(int currBottomRightImage) {
        this.currBottomRightImage = currBottomRightImage;
    }
}
