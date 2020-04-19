package com.clackjones.threeoutoffour.score;

import com.clackjones.threeoutoffour.model.ThreeOutOfFourGame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

public class OfflineCoinScoreKeeper implements CoinScoreKeeper {
    private int coinScore;
    private PropertyChangeSupport propertyChangeSupport;

    public OfflineCoinScoreKeeper() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.coinScore = 0;
    }

    @Override
    public Integer getCoinScore() {
        return this.coinScore;
    }

    /**
     * To be used for serialization purposes only
     */
    public void setCoinScore(Integer score) {
        this.coinScore = score;
        this.recitfyCoinScore();
    }

    @Override
    public void buyCoins(Integer numberOfCoins) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT)) {
            this.addCoins(5);
        } else if (evt.getPropertyName().equals(ThreeOutOfFourGame.HINT_LETTER_REMOVED_EVENT)) {
            this.removeCoins(10);
        }
    }

    private void addCoins(Integer numCoins) {
        int oldCoinScore = this.coinScore;
        this.coinScore = this.coinScore + numCoins;
        this.recitfyCoinScore();

        this.propertyChangeSupport.firePropertyChange(CoinScoreKeeper.COIN_SCORE_CHANGED_EVENT, oldCoinScore, this.coinScore);
    }

    private void removeCoins(Integer numCoins) {
        this.addCoins(-numCoins);
    }

    private void recitfyCoinScore() {
        if (this.coinScore < 0) {
            this.coinScore = 0;
        }
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
}
