package com.clackjones.threeoutoffour.model;

import java.util.Random;

public class Round {
    //4 images
    private int topLeftImage;
    private int topRightImage;
    private int bottomLeftImage;
    private int bottomRightImage;

    //solution
    private String answer;
    private String[] randomLetters;

    //runs when new round object created
    public Round(int topLeftImage, int topRightImage, int bottomLeftImage, int bottomRightImage,
                 String answer, String[] randomLetters) {
        this.topLeftImage = topLeftImage;
        this.topRightImage = topRightImage;
        this.bottomLeftImage = bottomLeftImage;
        this.bottomRightImage = bottomRightImage;
        this.answer = answer;
        this.randomLetters = randomLetters;
    }

    public int getTopLeftImage() {
        return topLeftImage;
    }

    public int getTopRightImage() {
        return topRightImage;
    }

    public int getBottomLeftImage() {
        return bottomLeftImage;
    }

    public int getBottomRightImage() {
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
}


