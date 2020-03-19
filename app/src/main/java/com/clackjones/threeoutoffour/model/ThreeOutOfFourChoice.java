package com.clackjones.threeoutoffour.model;

/**
 * Bean class to hold a letter choice, including whether has
 * already been selected
 */
public class ThreeOutOfFourChoice {
    private String value;
    private boolean isAlreadySelected;

    public ThreeOutOfFourChoice(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isAlreadySelected() {
        return isAlreadySelected;
    }

    public void select() {
        this.isAlreadySelected = true;
    }

    @Override
    public ThreeOutOfFourChoice clone() {
        ThreeOutOfFourChoice choiceClone = new ThreeOutOfFourChoice(this.getValue());
        choiceClone.value = this.value;

        return choiceClone;
    }

    void reset() {
        isAlreadySelected = false;
    }
}
