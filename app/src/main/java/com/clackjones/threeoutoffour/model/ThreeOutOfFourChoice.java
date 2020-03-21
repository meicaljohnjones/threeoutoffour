package com.clackjones.threeoutoffour.model;

/**
 * Bean class to hold a letter choice, including whether has
 * already been selected
 */
public class ThreeOutOfFourChoice implements java.io.Serializable {
    private String value;
    private boolean isAlreadySelected;

    public ThreeOutOfFourChoice() {

    }

    public ThreeOutOfFourChoice(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue() {
        this.value = value;
    }

    public boolean getIsAlreadySelected() {
        return isAlreadySelected;
    }

    public void setIsAlreadySelected(boolean alreadySelected) {
        isAlreadySelected = alreadySelected;
    }

    @Override
    public ThreeOutOfFourChoice clone() {
        ThreeOutOfFourChoice choiceClone = new ThreeOutOfFourChoice(this.getValue());
        choiceClone.value = this.value;

        return choiceClone;
    }

    public void select() {
        this.isAlreadySelected = true;
    }

    void reset() {
        isAlreadySelected = false;
    }
}
