package com.clackjones.threeoutoffour.model;

import java.io.Serializable;
import java.util.Random;

public class Round implements Serializable {
    private int roundNumber;

    //4 images
    private byte[] topLeftImage;
    private byte[] topRightImage;
    private byte[] bottomLeftImage;
    private byte[] bottomRightImage;

    //solution
    private String answer;
    private String[] randomLetters;

    //runs when new round object created
    public Round(int roundNumber, byte[] topLeftImage, byte[] topRightImage, byte[] bottomLeftImage, byte[] bottomRightImage,
                 String answer, String[] randomLetters) {
        this.roundNumber = roundNumber;
        this.topLeftImage = topLeftImage;
        this.topRightImage = topRightImage;
        this.bottomLeftImage = bottomLeftImage;
        this.bottomRightImage = bottomRightImage;
        this.answer = answer;
        this.randomLetters = randomLetters;
    }

    public byte[] getTopLeftImage() {
        return topLeftImage;
    }

    public byte[] getTopRightImage() {
        return topRightImage;
    }

    public byte[] getBottomLeftImage() {
        return bottomLeftImage;
    }

    public byte[] getBottomRightImage() {
        return bottomRightImage;
    }

    public void shuffleRandomLetters() {
        randomLetters = shuffleArray(randomLetters);
    }

    private String[] shuffleArray(String[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

    public String[] getRandomLetters() {
        return randomLetters;
    }

    public boolean isCorrectAnswer(String proposedAnswer) {
        // return takes it back to da iscorrectanswer method dat called dit
        return proposedAnswer.equals(answer);
    }

    public boolean enoughLettersSelected(int numberOfLetters) {
        return numberOfLetters == answer.length();
    }

    public int getAnswerLength() {
        return answer.length();
    }

    public String getAnswer() {
        return this.answer;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}


