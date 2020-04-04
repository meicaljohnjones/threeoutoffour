package com.clackjones.threeoutoffour.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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

    @Override
    public void buyCoins(Integer numberOfCoins) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ThreeOutOfFourGame.ROUND_NUMBER_INCREMENTED_EVENT)) {
            this.addCoins(5);
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    private void addCoins(Integer numCoins) {
        int oldCoinScore = this.coinScore;
        this.coinScore = this.coinScore + numCoins;
        this.propertyChangeSupport.firePropertyChange(CoinScoreKeeper.COIN_SCORE_CHANGED_EVENT, oldCoinScore, this.coinScore);
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
